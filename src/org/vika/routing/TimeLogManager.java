package org.vika.routing;

import org.vika.routing.routing.RoutingManager;

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
    private final ArrayList<Integer> neuroWaitTimes = new ArrayList<Integer>();
    private final ArrayList<Integer> deikstraWaitTimes = new ArrayList<Integer>();
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
        return ((float)(System.currentTimeMillis() - myStartTime)) / myQuantumTime;
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

    public void printStatistics(final RoutingManager manager) {
        printToWriter(Arrays.toString(deliveryTimes));
        printToWriter("Wait time: " + manager.getWaitTime());
    }

    public void saveNeuroStatistics(final RoutingManager manager) {
        neuroStatistics.add(deliveryTimes);
        neuroWaitTimes.add(manager.getWaitTime());
    }

    public void saveDeikstraStatistics(final RoutingManager manager) {
        deikstraStatistics.add(deliveryTimes);
        deikstraWaitTimes.add(manager.getWaitTime());
    }

    public void printAllStatistics() {
        printToWriter("Neuro routing delivery times");
        for (int i=0;i<neuroStatistics.size();i++){
            printToWriter(Arrays.toString(neuroStatistics.get(i)));
            printToWriter("Wait time: " + neuroWaitTimes.get(i));
        }
        printToWriter("Deikstra routing delivery times");
        for (int i=0;i<deikstraStatistics.size();i++){
            printToWriter(Arrays.toString(deikstraStatistics.get(i)));
            printToWriter("Wait time: " + deikstraWaitTimes.get(i));
        }
    }
}
