package org.vika.routing;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author oleg
 */
public class TimeLogManager {
    private final BufferedWriter myLogWriter;
    private final int myTotalTime;
    private final int myQuantumTime;
    private long myStartTime;
    public float[] deliveryTimes;
    private final ArrayList<float[]> neuroStatistics = new ArrayList<float[]>();
    private final ArrayList<float[]> deikstraStatistics = new ArrayList<float[]>();

    public TimeLogManager(final BufferedWriter logWriter, final int time, final int quantumTime) {
        myLogWriter = logWriter;
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
    public float getCurrentTime() {
        return ((System.currentTimeMillis() - myStartTime) / myQuantumTime);
    }

    public void log(final String message) {
        printToWriter("[" + getCurrentTime() + "]" + message);
    }

    public void printToWriter(final String output) {
        System.out.println(output);
        try {
            myLogWriter.write(output + "\n");
        } catch (IOException e) {
            System.err.println("Failed to write to log file");
        }
    }

    public void sleep(final float delay) {
        // Wait for the delay number of quantum time
        try {
            Thread.sleep(Math.round(delay * myQuantumTime));
        } catch (InterruptedException e) {
            // Ignore, we should never face with
        }
    }

    public void resetStatistics(final int messages){
        deliveryTimes = new float[messages];
    }

    public void messageReceived(final Message message) {
        final float deliveryTime = getCurrentTime() - message.time;
        log("Successfully received message: " + message + " in time " + deliveryTime);
        deliveryTimes[message.id] = deliveryTime;
    }

    public void printStatistics() {
        printToWriter(Arrays.toString(deliveryTimes));
    }

    public void saveNeuroStatistics() {
        neuroStatistics.add(deliveryTimes);
    }

    public void saveDeikstraStatistics() {
        deikstraStatistics.add(deliveryTimes);
    }

    public void printAllStatistics() {
        printToWriter("Neuro routing delivery times");
        for (float[] f : neuroStatistics) {
            printToWriter(Arrays.toString(f));
        }
        printToWriter("Deikstra routing delivery times");
        for (float[] f : deikstraStatistics) {
            printToWriter(Arrays.toString(f));
        }
    }
}
