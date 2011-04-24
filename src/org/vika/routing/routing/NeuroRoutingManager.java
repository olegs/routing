package org.vika.routing.routing;

import org.vika.routing.LoadManager;
import org.vika.routing.Message;
import org.vika.routing.network.Channel;
import org.vika.routing.network.Network;
import org.vika.routing.network.jade.NodeAgent;

import java.util.Map;

/**
 * @author oleg
 */
public class NeuroRoutingManager implements RoutingManager {
    private final Network myNetwork;
    private final LoadManager myLoadManager;

    public NeuroRoutingManager(final Network network, final LoadManager loadManager) {
        myNetwork = network;
        myLoadManager = loadManager;
    }

    public void route(final NodeAgent agent, final Message message) {
        System.out.println("Routing request from agent " + agent.getId() + " message: " + message);
        // Ask load manager to process message received,
        // loadManager can call block agent before responding
        myLoadManager.messageReceived(agent, message);

        final int agentId = agent.getId();
        if (agentId == message.receiver) {
            System.out.println(agentId + " successfully received message: " + message.message);
            return;
        }
        final Map<Integer, Channel> adjacentNodes = myNetwork.nodes[agentId].adjacentNodes;
        // Send message to the adjacent node it receiver is one of them
        if (adjacentNodes.containsKey(message.receiver)) {
            agent.sendMessage(message.receiver, message);
            return;
        }
        System.out.println("Not implemented yet. Cannot route.");
    }
}
