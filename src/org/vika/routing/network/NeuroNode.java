package org.vika.routing.network;

import com.sun.tools.javac.util.Pair;

import java.util.Map;

/**
 * @author oleg
 */
public class NeuroNode {
    public int id;
    public Map<Pair<Integer,Integer>, Float> wValues;

    public NeuroNode(final int node) {
        id = node;
    }

}