package model;

import java.util.*;

public class Node {
    private final String name;

    private final List<Integer> lines;
    private final Set<Node> adjacentNodes;
    private List<Node> shortestPath;
    private int distance;

    public Node(String name) {
        this.name = name;
        shortestPath = new LinkedList<>();
        distance = Integer.MAX_VALUE;
        adjacentNodes = new HashSet<>();
        lines = new ArrayList<>();
    }

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
    }

    public String getName() {
        return name;
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
