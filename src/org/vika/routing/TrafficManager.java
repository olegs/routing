package org.vika.routing;

/**
 * @author oleg
 * @date 21.04.11
 */
public class TrafficManager {
    public boolean end() {
        return false;
    }

    public int getDelay() {
        return 0;
    }

    public int getInitialAgent() {
        return 0;
    }

    public Message getMessage() {
        return new Message(0, "Sample message");
    }

    public void nextMessage() {
        // Do nothing
    }
}
