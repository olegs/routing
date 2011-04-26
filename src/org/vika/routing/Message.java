package org.vika.routing;

import java.io.Serializable;

/**
 * @author oleg
 */
public class Message implements Serializable {
    public int id;
    public final int receiver;
    public int time = -1;

    public Message(final int i, final int r) {
        id = i;
        receiver = r;
    }

    @Override
    public String toString() {
        return "message" + id + "@" + time + "[" + receiver + "]";
    }
}
