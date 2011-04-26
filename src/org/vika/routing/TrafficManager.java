package org.vika.routing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author oleg
 * @date 21.04.11
 */
public class TrafficManager {
    private List<TrafficEvent> myTraffic;
    private int myIndex;

    public void setTraffic(final List<TrafficEvent> traffic){
        myTraffic = traffic;
        System.out.println("Traffic loaded. Total events: " + traffic.size());
        reset();
    }

    public void reset(){
        myIndex = 0;
    }

    public static List<TrafficEvent> generate(final int nodes, final int messages, final int totalTime) {
        final Random r = new Random();
        final List<TrafficEvent> result = new ArrayList<TrafficEvent>();
        int timeConsumed = 0;
        final int singeDelayTreshold = totalTime / messages * 2;
        for (int i = 0; i < messages; i++) {
            final int randomStart = r.nextInt(nodes);
            int randomTarget;
            while ((randomTarget = r.nextInt(nodes)) == randomStart);
            final long randomDelay = i == messages - 1
                    ? totalTime - timeConsumed
                    : r.nextInt(Math.min(singeDelayTreshold, totalTime - timeConsumed));
            timeConsumed += randomDelay;
            result.add(new TrafficEvent(randomStart, randomDelay, new Message(i, randomTarget)));
        }
        return result;
    }

    public boolean end() {
        return myIndex >= myTraffic.size();
    }

    public long getDelay() {
        return myTraffic.get(myIndex).delay;
    }

    public int getInitialAgent() {
        return myTraffic.get(myIndex).initializer;
    }

    public Message getMessage() {
        return myTraffic.get(myIndex).message;
    }

    public void nextMessage() {
        myIndex++;
    }

    public static class TrafficEvent {
        public final int initializer;
        public final long delay;
        public final Message message;

        public TrafficEvent(final int initializer, final long delay, final Message message) {
            this.initializer = initializer;
            this.delay = delay;
            this.message = message;
        }

        @Override
        public String toString() {
            return message + ":" + delay;
        }
    }
}
