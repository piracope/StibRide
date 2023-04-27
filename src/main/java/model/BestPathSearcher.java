package model;

import data.config.ConfigManager;
import data.dto.StationsDto;
import data.exception.RepositoryException;
import data.repository.StationsRepository;
import data.repository.StopsRepository;
import util.Observable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BestPathSearcher extends Observable {
    private final Graph G;
    private Node workingSource = null;

    public BestPathSearcher() throws RepositoryException {
        G = new Graph();
        fillGraph();
    }

    private void fillGraph() throws RepositoryException {
        StopsRepository stopRep = new StopsRepository();
        StationsRepository statRep = new StationsRepository();
        var allStations = statRep.getAll();
        var allStops = stopRep.getAll();

        for(var stat : allStations) {
            G.addNode(new Node(stat.getName()));
        }

        int currLine = allStops.get(0).getKey().lineId();
        Node prec = null;


        for(var stop : allStops) {
            // start anew when we get to a new line
            if(stop.getKey().lineId() != currLine) {
                prec = null;
                currLine = stop.getKey().lineId();
            }

            int stationId = stop.getKey().stationId();
            Node toAdd = G.getNode(statRep.get(stationId).getName()); // we get the station

            if(prec != null) { // and we add ourselves to the precedent, the precedent to ourselves
                prec.addDestination(toAdd);
                toAdd.addDestination(prec);
            }

            prec = toAdd;
        }
    }

    public List<Node> getPath(String from, String to) {
        Node start = G.getNode(from);
        Node dest = G.getNode(to);
        if(start == null || dest == null) return null;

        if(start != workingSource) {
            Dijkstra.shortestPath(start);
            workingSource = start;
        }

        return dest.getShortestPath();
    }

    public List<String> getStations() throws RepositoryException {
        return new StationsRepository().getAll().stream().map(StationsDto::getName).toList();
    }

    public static void main(String[] args) throws RepositoryException, IOException {
        ConfigManager.getInstance().load();
        var test = new BestPathSearcher();
        System.out.println(Arrays.deepToString(test.getPath("PORTE DE NAMUR", "GARE CENTRALE").toArray()));
        System.out.println(Arrays.deepToString(test.getPath("PORTE DE NAMUR", "ROI BAUDOUIN").toArray()));
    }
}
