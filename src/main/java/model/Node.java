package model;

import java.util.*;

public class Node {
    private final String name;

    private List<Node> shortestPath;
    private int distance;
    private final Set<Node> adjacentNodes;

    public Node(String name) {
        this.name = name;
        shortestPath = new LinkedList<>();
        distance = Integer.MAX_VALUE;
        adjacentNodes = new HashSet<>();
    }

    public void addDestination(Node destination) {
        adjacentNodes.add(destination);
    }

    public String getName() {
        return name;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
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

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
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
}
