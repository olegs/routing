package org.vika.routing;

/**
 * @author oleg
 */
public class TimeManager {
    private final int myTotalTime;
    private final int myQuantumTime;
    private long myStartTime;

    public TimeManager(final int time, final int quantumTime) {
        myTotalTime = time;
        myQuantumTime = quantumTime;
    }

    public void start() {
        myStartTime = System.nanoTime();
    }

    public long getTotalTime() {
        return myTotalTime;
    }

    public int getQuantumTime() {
        return myQuantumTime;
    }

    /**
     * @return returns current time measured by quantum ranges
     */
    public int getCurrentTime() {
        return Math.round((float)((System.nanoTime() - myStartTime) / ((10e6) * myQuantumTime)));
    }

}
