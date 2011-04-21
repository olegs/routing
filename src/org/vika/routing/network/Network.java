package org.vika.routing.network;

/**
 * @author oleg
 */
public class Network {
    public Node[] nodes;
    public final int edges;

    public Network(final Node[] nodes, final int edges) {
        this.nodes = nodes;
        this.edges = edges;
    }
}
