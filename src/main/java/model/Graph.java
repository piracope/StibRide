package model;

import java.util.HashSet;
import java.util.Set;

// i'm copying that from Baeldung
public class Graph {
    private final Set<Node> nodes;

    public Graph() {
        this.nodes = new HashSet<>();
    }

    public void addNode(Node n) {
        nodes.add(n);
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    /**
     * Returns the node which has a given station's name.
     *
     * @param name the station's name
     * @return the node of that station or null if it couldn't be found
     */
    public Node getNode(String name) {
        return nodes.stream().filter(n -> n.getName().equals(name)).findAny().orElse(null);
    }
}
