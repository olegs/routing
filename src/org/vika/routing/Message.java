package org.vika.routing;

import java.io.Serializable;

/**
 * @author oleg
 */
public class Message implements Serializable {
    public int id;
    public final int initiator;
    public final int receiver;
    public int time = -1;

    public Message(final int id, final int initiator, final int receiver) {
        this.id = id;
        this.initiator = initiator;
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return "message" + id + "@" + time + "[" + initiator + "->" + receiver + "]";
    }
}
