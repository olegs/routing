package org.vika.routing.routing;

import com.sun.tools.javac.util.Pair;
import org.vika.routing.LoadManager;
import org.vika.routing.Message;
import org.vika.routing.TimeManager;
import org.vika.routing.network.Channel;
import org.vika.routing.network.Network;
import org.vika.routing.network.NeuroNetwork;
import org.vika.routing.network.jade.NodeAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author oleg
 */
public class DeikstraRoutingManager implements RoutingManager {
    private static final float DEFAULT_NODE_ACTIVATION = 0.9f;
    private final Network myNetwork;
    private final LoadManager myLoadManager;
    private final NeuroNetwork myNeuroNetwork;
    private final TimeManager myTimeManager;
    private final int myRoutingEvents;
    private int myReceivedMessages;

    public DeikstraRoutingManager(final Network network, final LoadManager loadManager,
                               final TimeManager timeManager, final int routingEvents) {
        myRoutingEvents = routingEvents;
        myReceivedMessages = 0;
        System.out.println("Deikstra network based routing manager is used!");
        myNetwork = network;
        myNeuroNetwork = new NeuroNetwork(network);
        myLoadManager = loadManager;
        myTimeManager = timeManager;
    }

    public void route(final NodeAgent agent, final Message message) {
        myTimeManager.log("Request from " + agent.getId() + " to route " + message);
        final int currentTime = myTimeManager.getCurrentTime();
        final int agentId = agent.getId();
        if (agentId == message.receiver) {
            myTimeManager.log("Successfully received message: " + message + " in time " + (myTimeManager.getCurrentTime() - message.time));
            myReceivedMessages++;
            return;
        }
        final Map<Integer, Channel> adjacentNodes = myNetwork.nodes[agentId].adjacentNodes;
        // Send message to the adjacent node it receiver is one of them
        if (adjacentNodes.containsKey(message.receiver)) {
            // We should add non-blocking transmit message with given time
            final int channelTime = adjacentNodes.get(message.receiver).time;
            myTimeManager.log("Sending " +  message + " to " + message.receiver + " channel time " + channelTime);
            agent.sendMessageAfterDelay(message.receiver, message, channelTime);
            return;
        }
        final Map<Integer, Float> activationLevels = new HashMap<Integer, Float>();
        final Map<Pair<Integer,Integer>, Float> wValues = myNeuroNetwork.neuroNodes[agentId].wValues;
        for (Map.Entry<Integer, Channel> entry : adjacentNodes.entrySet()) {
            final int adjacentNodeId = entry.getKey();
            final Channel channel = entry.getValue();

            // Get wValue
            final Pair<Integer, Integer> key = new Pair<Integer, Integer>(adjacentNodeId, message.receiver);
            final Float wValue = wValues.get(key);
            // Update wValue
            final int channelLoad = myLoadManager.getEdgeLoad(channel.id, currentTime);
            wValues.put(key, wValue - channelLoad);

            final float hValue = DEFAULT_NODE_ACTIVATION + myLoadManager.getNodeLoad(adjacentNodeId, currentTime);
            activationLevels.put(adjacentNodeId, hValue - wValue);
        }
        // Once we are done with activation levels, we can choose maximum of them
        int maxId = -1;
        float maxActivationLevel = (float)-10e100;
        for (Map.Entry<Integer, Float> entry : activationLevels.entrySet()) {
            final Float value = entry.getValue();
            if (value > maxActivationLevel){
              maxActivationLevel = value;
              maxId = entry.getKey();
          }
        }
        // Ok we have maximum activation level id, send message there.
        final int channelTime = adjacentNodes.get(maxId).time;
        myTimeManager.log("Sending " +  message + " to " + maxId + " channel time " + channelTime);
        agent.sendMessageAfterDelay(maxId, message, channelTime);
    }

    public boolean areAllMessagesReceived() {
        return myReceivedMessages == myRoutingEvents;
    }
}

