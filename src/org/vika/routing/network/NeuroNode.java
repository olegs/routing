package org.vika.routing.network;


import org.vika.routing.Pair;

import java.util.Map;

/**
 * @author oleg
 */
public class NeuroNode {
    public int id;
    // Here we store wValues. Key is pair (node to send, target node).
    public Map<Pair<Integer,Integer>, Float> wValues;

    public NeuroNode(final int node) {
        id = node;
    }

}