package org.vika.routing.network;

import java.util.HashMap;
import java.util.Map;

/**
 * @author oleg
 */
public class Node {
    static int numberOfNodes;
    static Node[] nodes;

    public Map<Integer, Channel> adjacentNodes = new HashMap<Integer, Channel>();
    public int id;

    public Node(final Node[] nodes) {
        // register my id;
        id = numberOfNodes++;
        Node.nodes = nodes;
    }
}
