package model;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Dijkstra {
    public static void shortestPath(Node source) {
        source.setDistance(0); // we begin at this node

        Set<Node> settledNodes = new HashSet<>(); // treated nodes
        Set<Node> unsettledNodes = new HashSet<>(); // untreated

        unsettledNodes.add(source);

        while (!unsettledNodes.isEmpty()) {
            Node curr = unsettledNodes.stream().min(Comparator.comparingInt(Node::getDistance)).get();
            unsettledNodes.remove(curr); // and we'll treat it
            // TODO : replace with PriorityQueue

            for(Node neighbor : curr.getAdjacentNodes()) { // go check its neighbors
                if(!settledNodes.contains(neighbor)) {
                    computeMinDist(neighbor, curr); // set their min distance to source
                    unsettledNodes.add(neighbor); // and add them for later
                }
            }

            settledNodes.add(curr); // now we can go check the others
        }
    }


    private static void computeMinDist(Node to, Node from) {
        int dist = from.getDistance();
        // FIXME : oopsie i use a magic number! should i make it implementation dependent i.e. check the weight ?
        if(dist + 1 < to.getDistance()) { // if the new distance is closer
            to.setDistance(dist + 1); // well that's the distance now

            var newPath = new LinkedList<>(from.getShortestPath());
            newPath.add(from);
            to.setShortestPath(newPath);

            /*
            if going from "from" to "to" is closer than the current distance, then the new best path is the
            path it took to get to "from"
             */
        }
    }
}
