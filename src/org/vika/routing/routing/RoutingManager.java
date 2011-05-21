package org.vika.routing.routing;

import org.vika.routing.Message;
import org.vika.routing.network.jade.NodeAgent;

import java.util.Collection;

/**
 * @author oleg
 * @date 24.04.11
 */
public interface RoutingManager {
    public void route(final NodeAgent agent, final Message message);
    public Collection<Integer> leftMessages();
    public boolean areAllMessagesReceived();
}
