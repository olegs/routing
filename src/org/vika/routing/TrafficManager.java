package org.vika.routing;

import java.util.ArrayList;
import java.util.List;

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

    public static List<TrafficEvent> generate(final int nodes, final int messages, final long time){
        final List<TrafficEvent>  result = new ArrayList<TrafficEvent>();
        final long timeConsumed = 0;
        for (int i=0;i<messages;i++) {
            final int randomStart = Math.round((float)Math.random() * nodes);
            final int randomTarget = Math.round((float)Math.random() * nodes);
            final String message = "Message["+ i +"]";
            final long randomDelay = i == messages - 1 ?
                    time - timeConsumed : Math.round((float)Math.random() * (time - timeConsumed));
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
