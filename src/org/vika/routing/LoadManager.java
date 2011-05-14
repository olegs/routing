package org.vika.routing;

import java.util.Random;

/**
 * @author oleg
 */
public class LoadManager {
    private Load myLoad;

    public void setLoad(final Load load) {
        System.out.println("Load loaded. Total changes: " + load.changes);
        myLoad = load;
    }

    public static Load generate(final int changes, final int edges) {
        final Random r = new Random();
        final int[][] edgesLoad = new int[edges][changes];
        for (int i=0;i<changes;i++){
            for (int j=0;j<edges;j++){
                edgesLoad[j][i] = r.nextInt(3);
            }
        }
        return new Load(changes, edgesLoad);
    }

    public float getEdgeLoad(final int id, final int currentTime) {
        final int value = myLoad.edgesLoad[id][currentTime % myLoad.changes];
        switch (value){
            case 0: return 0;
            case 1: return 0.1f;
            case 2: return 0.2f;
            case 3: return 0.5f;
        }
        throw new IllegalStateException("Wrong load value:" + value);
    }

    public static class Load {
       final int changes;
       final int[][] edgesLoad;

        public Load(int changes, int[][] edgesLoad) {
            this.changes = changes;
            this.edgesLoad = edgesLoad;
        }
    }
}
