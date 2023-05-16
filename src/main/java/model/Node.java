package model;

import java.util.*;


public class Node {
    /**
     * The station's name.
     */
    private final String name;
    /**
     * The station's ID.
     */
    private final int id;
    /**
     * All lines that pass on this station.
     */
    private final List<Integer> lines;
    /**
     * The nodes adjacent to this one in the graph.
     */
    private final Set<Node> adjacentNodes;
    /**
     * The shortest path to get from a certain starting node to this one.
     */
    private List<Node> shortestPath;
    /**
     * The distance between this node and a certain starting node.
     */
    private int distance;

    public Node(String name, int id) {
        this.name = name;
        this.id = id;
        shortestPath = new LinkedList<>();
        distance = Integer.MAX_VALUE;
        adjacentNodes = new HashSet<>();
        lines = new ArrayList<>();
    }

    /**
     * Resets this node to its initial pre-Dijkstra state.
     */
    public void reset() {
        shortestPath = new LinkedList<>();
        distance = Integer.MAX_VALUE;
    }

    public void addDestination(Node destination) {
        adjacentNodes.add(destination);
    }

    public void addLine(int line) {
        lines.add(line);
    }

    public List<Integer> getLines() {
        return lines;
    } // marked unused but used by JavaFX controller

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Set<Node> getAdjacentNodes() {
        return adjacentNodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return name.equals(node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
