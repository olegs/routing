package org.vika.routing;

import org.vika.routing.network.Channel;
import org.vika.routing.network.Network;
import org.vika.routing.network.NodeAgent;

import java.util.Map;

/**
 * @author oleg
 */
public class RoutingManager {
    private final Network myNetwork;
    private final LoadManager myLoadManager;

    public RoutingManager(final Network network, final LoadManager loadManager) {
        myNetwork = network;
        myLoadManager = loadManager;
    }

    public void route(final NodeAgent agent, final Message message) {
        // Ask load manager to process message received
        myLoadManager.messageReceived(agent, message);

        final int agentId = agent.getId();
        if (agentId == message.receiver){
            System.out.println("Agent " + agentId + " successfully received message: " + message.message);
            return;
        }
        final Map<Integer,Channel> adjacentNodes = myNetwork.nodes[agentId].adjacentNodes;
        if (adjacentNodes.containsKey(message.receiver)){
            agent.sendMessage(message.receiver, message);
            return;
        }
        System.out.println("Not implemented yet. Cannot route.");
    }
}
