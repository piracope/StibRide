package model;

import data.config.ConfigManager;
import data.dto.Dto;
import data.dto.SavedDto;
import data.dto.StationsDto;
import data.exception.RepositoryException;
import data.repository.SavedRepository;
import data.repository.StationsRepository;
import data.repository.StopsRepository;
import presenter.Update;
import presenter.UpdateType;
import util.Observable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BestPathSearcher extends Observable {
    private final Graph G;
    private Node workingSource = null;

    public BestPathSearcher() throws RepositoryException {
        G = new Graph();
        fillGraph();
    }

    public static void main(String[] args) throws RepositoryException, IOException {
        ConfigManager.getInstance().load();
        var test = new BestPathSearcher();
        System.out.println(Arrays.deepToString(test.getPath("PORTE DE NAMUR", "GARE CENTRALE").toArray()));
        System.out.println(Arrays.deepToString(test.getPath("PORTE DE NAMUR", "ROI BAUDOUIN").toArray()));
    }

    private void fillGraph() throws RepositoryException {
        StopsRepository stopRep = new StopsRepository();
        StationsRepository statRep = new StationsRepository();
        var allStations = statRep.getAll();
        var allStops = stopRep.getAll();

        for (var stat : allStations) {
            G.addNode(new Node(stat.getName(), stat.getKey()));
        }

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

    private void resetGraph() {
        for (Node n : G.getNodes()) {
            n.reset();
        }
    }

    public List<Node> getPath(String from, String to) {
        Node start = G.getNode(from);
        Node dest = G.getNode(to);
        if (start == null || dest == null) return null;

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

    public List<String> getSaved() throws RepositoryException {
        return new SavedRepository().getAll().stream().map(Dto::getKey).toList();
    }

    public void savePath(String start, String destination, String name) throws RepositoryException {
        Node startN = G.getNode(start);
        Node destN = G.getNode(destination);

        if (startN == null || destN == null || name == null) return;

        SavedRepository saved = new SavedRepository();
        saved.add(new SavedDto(startN.getId(), destN.getId(), name));
        notifyObservers(new Update(UpdateType.SAVED, getSaved()));
    }

    public void fetchSave(String text) throws RepositoryException {
        SavedDto save = new SavedRepository().get(text);
        StationsRepository stations = new StationsRepository();
        String source = stations.get(save.getStartId()).getName();
        String dest = stations.get(save.getDestId()).getName();


        notifyObservers(new Update(UpdateType.SAVE_FETCH, new String[]{source, dest}));
    }
}
