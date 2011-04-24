package org.vika.routing;

import java.io.Serializable;

/**
 * @author oleg
 */
public class Message implements Serializable {
    public final String message;
    public final int receiver;
    public int time = -1;

    public Message(final int r, final String m) {
        receiver = r;
        message = m;
    }


    @Override
    public String toString() {
        return message + "@" + time + "[" + receiver + "]";
    }
}
