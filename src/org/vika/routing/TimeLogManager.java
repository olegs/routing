package org.vika.routing;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @author oleg
 */
public class TimeLogManager {
    private final int myTotalTime;
    private final int myQuantumTime;
    private long myStartTime;
    public int[] deliveryTimes;
    private BufferedWriter myLogWriter;

    public TimeLogManager(final int time, final int quantumTime) {
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
        final String output = "[" + getCurrentTime() + "]" + message;
        try {
            myLogWriter.write(output + "\n");
        } catch (IOException e) {
            System.err.println("Failed to write to log file");
        }
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
        assert -2 <= realDelay&& realDelay <= 2 : "Failed to wait for " + delay + " with difference: " + realDelay;
    }

    public void resetStatistics(final int messages){
        deliveryTimes = new int[messages];
    }

    public void messageReceived(final Message message) {
        final int deliveryTime = getCurrentTime() - message.time;
        log("Successfully received message: " + message + " in time " + deliveryTime);
        deliveryTimes[message.id] = deliveryTime;
    }

   public void setMyLogWriter(final BufferedWriter logWriter) {
        myLogWriter = logWriter;
    }

    public void printStatistics() {
        final StringBuilder builder = new StringBuilder("Deliver statistics:");
        for (int time : deliveryTimes) {
            builder.append(" " + time);
        }
        log(builder.toString());
    }
}
