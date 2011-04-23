package org.vika.routing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author oleg
 * @date 21.04.11
 */
public class TrafficManager {
    private final List<TrafficEvent> myTraffic;
    private int myIndex = 0;

    public TrafficManager(final List<TrafficEvent> traffic) {
        myTraffic = traffic;
    }

    public static List<TrafficEvent> generate(final int nodes, final int messages, final int time) {
        final Random r = new Random();
        final List<TrafficEvent> result = new ArrayList<TrafficEvent>();
        int timeConsumed = 0;
        final int singeDelayTreshold = time / messages * 2;
        for (int i = 0; i < messages; i++) {
            final int randomStart = r.nextInt(nodes);
            final int randomTarget = r.nextInt(nodes);
            final String message = "message" + i;
            final long randomDelay = i == messages - 1
                    ? time - timeConsumed
                    : r.nextInt(Math.min(singeDelayTreshold, time - timeConsumed));
            timeConsumed += randomDelay;
            result.add(new TrafficEvent(randomStart, randomDelay, new Message(randomTarget, message)));
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
    }
}
