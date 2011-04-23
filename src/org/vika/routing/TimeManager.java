package org.vika.routing;

/**
 * @author oleg
 */
public class TimeManager {
    private final int myDurationTime;
    private final int myQuantumTime;
    private long myStartTime;

    public TimeManager(final int time, final int quantumTime) {
        myDurationTime = time;
        myQuantumTime = quantumTime;
    }

    public void start() {
        myStartTime = System.nanoTime();
    }

    public long getDurationTime() {
        return myDurationTime;
    }

    public int getQuantumTime() {
        return myQuantumTime;
    }

    /**
     * @return returns current time measured by quantum ranges
     */
    public int getTime() {
        return Math.round((float)((System.nanoTime() - myStartTime) / ((10e3) * myQuantumTime)));
    }

}
