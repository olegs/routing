package org.vika.routing.routing;

import org.vika.routing.Message;
import org.vika.routing.network.jade.NodeAgent;

/**
 * @author oleg
 * @date 24.04.11
 */
public interface RoutingManager {
    public void route(final NodeAgent agent, final Message message);
    public boolean areAllMessagesReceived();
}
