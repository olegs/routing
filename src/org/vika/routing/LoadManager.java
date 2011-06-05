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

    public static Load generate(final int changes, final int edges, final boolean loaded) {
        final Random r = new Random();
        final float[][] edgesLoad = new float[edges][changes];
        for (int i=0;i<changes;i++){
            for (int j=0;j<edges;j++){
                edgesLoad[j][i] = generateValue(r, loaded);
            }
        }
        return new Load(changes, edgesLoad);
    }

    // Here we emulate necessarily probabilities
    private static float generateValue(final Random r, final boolean loaded) {
        if (!loaded){
            final int i = r.nextInt(4);
            if (i==0){
                return 0;
            }
            if (i==1){
                return 0.1f;
            }
            if (i==2){
                return 0.2f;
            }
            return 0.5f;
        }
        final int i = r.nextInt(100);
        if (i < 15){
            return 0f;
        }
        if (i < 15 + 35){
            return 0.1f;
        }
        if (i < 15 + 35 + 35){
            return 0.2f;
        }
        return 0.5f;
    }

    public float getEdgeLoad(final int id, final int currentTime) {
        return myLoad.edgesLoad[id][currentTime % myLoad.changes];
    }

    public static class Load {
       final int changes;
       final float[][] edgesLoad;

        public Load(int changes, float[][] edgesLoad) {
            this.changes = changes;
            this.edgesLoad = edgesLoad;
        }
    }
}
