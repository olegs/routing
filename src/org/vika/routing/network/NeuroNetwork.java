package org.vika.routing.network;


import org.vika.routing.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * @author oleg
 */
public class NeuroNetwork extends Network {
    public NeuroNode[] neuroNodes;

    public NeuroNetwork(Network n) {
        super(n.nodes, n.edges);
        neuroNodes = buildWValues(nodes);
    }

    private static NeuroNode[] buildWValues(final Node[] nodes) {
        // Here we run Floyd algorithm to get all the distances between any pair of vertexes in graph
        final Integer[][] distances = new Integer[nodes.length][nodes.length];
        // Initialization
        for (int i = 0; i < nodes.length; i++) {
            final Map<Integer, Channel> adjacentNodes = nodes[i].adjacentNodes;
            for (int j = 0; j < nodes.length; j++) {
                distances[i][j] = adjacentNodes.containsKey(j) ? adjacentNodes.get(j).time : Integer.MAX_VALUE;
            }
        }
        // n^3 actions
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes.length; j++) {
                for (int k = 0; k < nodes.length; k++) {
                    if (i != j && i != k && j != k &&
                            distances[i][j] != Integer.MAX_VALUE && distances[j][k] != Integer.MAX_VALUE) {
                        final int alternativePath = distances[i][j] + distances[j][k];
                        if (distances[i][k] > alternativePath) {
                            distances[i][k] = alternativePath;
                        }
                    }
                }
            }
        }
        // Well, now we have all the minimal path lengths between any pair of nodes if it exists
        final NeuroNode[] neuroNodes = new NeuroNode[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            final NeuroNode neuroNode = new NeuroNode(i);
            neuroNodes[i] = neuroNode;
            neuroNode.id = i;

            neuroNode.wValues = new HashMap<Pair<Integer, Integer>, Float>();
            for (Integer number : nodes[i].adjacentNodes.keySet()) {
                for (int j = 0; j < nodes.length; j++) {
                    if (i == j || number == i || number == j){
                        continue;
                    }
                    final float w = (float)distances[i][j] /
                            (float)(nodes[i].adjacentNodes.get(number).time + distances[number][j]);
                    neuroNode.wValues.put(new Pair<Integer, Integer>(number, j), w);
                }
            }
        }
        return neuroNodes;
    }
}
