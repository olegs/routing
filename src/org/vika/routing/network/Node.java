package org.vika.routing.network;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

    public static void parse(final Scanner scanner, final Node[] nodes, int number) {
        // We have a contract here that the information about channel is stored in the node with less id.
        final Node node = nodes[number];
        final int edges = scanner.nextInt();
        for (int i=0;i<edges;i++){
            final int neighbourId = scanner.nextInt();
            assert neighbourId > node.id : "Contract error, neighbour Id is not greater than id";
            // parse information about the channel
            final Channel channel = Channel.parse(scanner);
            node.adjacentNodes.put(neighbourId, channel);
            nodes[neighbourId].adjacentNodes.put(number, channel);
        }
    }
}
