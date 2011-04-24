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
        System.out.println("Time manager started with quantum time " + myQuantumTime + "ms and total time " + myTotalTime);
    }

    public int getQuantumTime() {
        return myQuantumTime;
    }

    /**
     * @return returns current time measured by quantum ranges
     */
    public int getCurrentTime() {
        return (int) Math.round(((System.nanoTime() - myStartTime) / (10e6 * myQuantumTime)));
    }

    public void log(final String message) {
        System.out.println("[" + getCurrentTime() + "]" + message);
    }
}
