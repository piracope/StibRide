package model;

import data.dto.Dto;
import data.dto.FavoriteDto;
import data.dto.StationsDto;
import data.exception.RepositoryException;
import data.repository.FavoriteRepository;
import data.repository.StationsRepository;
import data.repository.StopsRepository;
import util.Observable;
import util.Update;
import util.UpdateType;

import java.util.ArrayList;
import java.util.List;

/**
 * The application's model.
 * <p>
 * Its main function is to compute the best path between two stops in a pre-constructed graph of nodes.
 */
public class BestPathSearcher extends Observable {
    /**
     * The Graph of stops.
     */
    private final Graph G;
    /**
     * The current starting point. If it changes, the graph needs to be re-evaluated.
     */
    private Node workingSource = null;

    /**
     * Creates a new BestPathSearcher by creating and filling the graph.
     *
     * @throws RepositoryException if the database had a problem
     */
    public BestPathSearcher() throws RepositoryException {
        G = new Graph();
        fillGraph();
    }

    /**
     * Fills the graph with all stations and constructs its edges by looking at their adjacent stops.
     *
     * @throws RepositoryException if the database had a problem
     */
    private void fillGraph() throws RepositoryException {

        StopsRepository stopRep = new StopsRepository();
        StationsRepository statRep = new StationsRepository();

        var allStations = statRep.getAll();
        var allStops = stopRep.getAll();

        // we first fill the graph with all stations
        for (var stat : allStations) {
            G.addNode(new Node(stat.getName(), stat.getKey()));
        }

        // this will serve as our rupture marker
        int currLine = allStops.get(0).getKey().lineId();
        Node prec = null;

        for (var stop : allStops) {
            // start anew when we get to a new line
            if (stop.getKey().lineId() != currLine) {
                prec = null;
                currLine = stop.getKey().lineId();
            }

            int stationId = stop.getKey().stationId();
            Node toAdd = G.getNode(statRep.get(stationId).getName()); // we get the station
            toAdd.addLine(stop.getKey().lineId());

            if (prec != null) { // and we add ourselves to the precedent, the precedent to ourselves
                prec.addDestination(toAdd);
                toAdd.addDestination(prec);
            }

            prec = toAdd;
        }
    }

    /**
     * Resets the graph to its initial state.
     */
    private void resetGraph() {
        for (Node n : G.getNodes()) {
            n.reset();
        }
    }

    /**
     * Computes the best path between two stations.
     *
     * @param from the starting station
     * @param to   the final station
     * @return the list of Nodes that form the best path between from and to
     */
    public List<Node> getPath(String from, String to) {
        // we do a lookup by name
        Node start = G.getNode(from);
        Node dest = G.getNode(to);
        if (start == null || dest == null) return null; // null-safe (we just don't do anything)

        // if the working source changed, we need to re-compute Dijkstra
        if (start != workingSource) {
            resetGraph();
            Dijkstra.shortestPath(start);
            workingSource = start;
        }

        var ret = new ArrayList<>(dest.getShortestPath());
        ret.add(dest);

        notifyObservers(new Update(UpdateType.SEARCH_RESULT, ret));
        return ret;
    }

    public List<String> getStations() throws RepositoryException {
        return new StationsRepository().getAll().stream().map(StationsDto::getName).toList();
    }

    public List<String> getFavorite() throws RepositoryException {
        return new FavoriteRepository().getAll().stream().map(Dto::getKey).toList();
    }

    /**
     * Saves a given path with a certain name as a favorite trip.
     *
     * @param start       the starting station
     * @param destination the final station
     * @param name        the name given to that "trip"
     * @throws RepositoryException if the database had a problem
     */
    public void savePath(String start, String destination, String name) throws RepositoryException {
        Node startN = G.getNode(start);
        Node destN = G.getNode(destination);

        if (startN == null || destN == null || name == null) return; // don't do anything if the input is malformed

        FavoriteRepository saved = new FavoriteRepository();
        saved.add(new FavoriteDto(startN.getId(), destN.getId(), name));

        notifyObservers(new Update(UpdateType.NEW_FAVORITE, getFavorite()));
        // tell the presenter that hey we have a new fav to display
    }


    /**
     * Gets the starting and final stations of a given favorite trip.
     *
     * @param text the trip's name
     * @return [sourceName, destName] or null if the trip doesn't exist
     * @throws RepositoryException if the database had a problem
     */
    public String[] fetchSave(String text) throws RepositoryException {
        FavoriteDto save = new FavoriteRepository().get(text);
        if (save == null) return null;

        StationsRepository stations = new StationsRepository();
        String source = stations.get(save.getStartId()).getName();
        String dest = stations.get(save.getDestId()).getName();

        return new String[]{source, dest};
    }

    public void deleteFav(String fav) throws RepositoryException {
        new FavoriteRepository().remove(fav);
        notifyObservers(new Update(UpdateType.NEW_FAVORITE, getFavorite()));
    }
}
