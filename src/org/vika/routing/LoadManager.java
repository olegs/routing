package org.vika.routing;

import org.vika.routing.network.jade.NodeAgent;

/**
 * @author oleg
 */
public class LoadManager {
    private final Load myLoad;

    public LoadManager(final Load load) {
        myLoad = load;
    }

    public Message messageReceived(final NodeAgent agent, final Message message) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // Ignore
        }
        return message;
    }

    public static Load generate(final int changes, final int nodes, final int edges,
                                final int nodeRange, final int edgeRange) {
        final int[][] nodesLoad = new int[nodes][changes];
        final int[][] edgesLoad = new int[edges][changes];
        for (int i=0;i<changes;i++){
            for (int j=0;j<nodes;j++){
                nodesLoad[j][i] = Math.round((float) Math.random() * nodeRange);
            }
            for (int j=0;j<edges;j++){
                edgesLoad[j][i] = Math.round((float) Math.random() * edgeRange);
            }
        }
        return new Load(changes, nodesLoad, edgesLoad);
    }

    public static class Load {
       final int changes;
       final int[][] nodesLoad;
       final int[][] edgesLoad;

        public Load(int changes, int[][] nodesLoad, int[][] edgesLoad) {
            this.changes = changes;
            this.nodesLoad = nodesLoad;
            this.edgesLoad = edgesLoad;
        }
    }
}
