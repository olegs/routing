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
        myStartTime = System.currentTimeMillis();
        System.out.println("Time manager started with quantum time " + myQuantumTime + "ms and total time " + myTotalTime);
    }

    /**
     * @return returns current time measured by quantum ranges
     */
    public int getCurrentTime() {
        return Math.round(((System.currentTimeMillis() - myStartTime) / myQuantumTime));
    }

    public void log(final String message) {
        System.out.println("[" + getCurrentTime() + "]" + message);
    }

    public void sleep(final int delay) {
        final int startTime = getCurrentTime();
        // Wait for the delay number of quantum time
        try {
            Thread.sleep(delay * myQuantumTime);
        } catch (InterruptedException e) {
            // Ignore, we should never face with
        }
        final int realDelay = getCurrentTime() - startTime - delay;
        assert -1 <= realDelay&& realDelay <= 1 : "Failed to wait for " + delay + " difference: " + realDelay;
    }
}
