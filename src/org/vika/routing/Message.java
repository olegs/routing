package org.vika.routing;

import java.io.Serializable;

/**
 * @author oleg
 */
public class Message implements Serializable {
    public String message;
    public int receiver;

    public Message(final int r, final String m) {
        receiver = r;
        message = m;
    }

    @Override
    public String toString() {
        return message + "[" + receiver + "]";
    }
}
