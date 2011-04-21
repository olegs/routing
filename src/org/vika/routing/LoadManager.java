package org.vika.routing;

import org.vika.routing.network.jade.NodeAgent;

/**
 * @author oleg
 */
public class LoadManager {
    public Message messageReceived(final NodeAgent agent, final Message message) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // Ignore
        }
        return message;
    }
}
