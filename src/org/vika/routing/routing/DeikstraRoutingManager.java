package org.vika.routing.routing;

import org.vika.routing.LoadManager;
import org.vika.routing.Message;
import org.vika.routing.TimeLogManager;
import org.vika.routing.network.Channel;
import org.vika.routing.network.Network;
import org.vika.routing.network.Node;
import org.vika.routing.network.jade.NodeAgent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * @author oleg
 */
public class DeikstraRoutingManager extends AbstractRoutingManager implements RoutingManager {
    private final Network myNetwork;
    private final LoadManager myLoadManager;
    private final TimeLogManager myTimeManager;

    private final Object myRoutingTableLock = new Object();
    private volatile int[][] myRoutingTable;


    /**
     * Calculate all the routing table
     * @param network
     * @return
     */
    private static int[][] calculateRoutingTable(final Network network, final boolean[] channelAvailability) {
        final int nodesNumber = network.nodes.length;
        final int[][] routingTable = new int[nodesNumber][nodesNumber];
        // Here we build open shortest path for all the nodes using Deikstra algorithm
        for (int nodeId=0;nodeId < nodesNumber;nodeId++){
            routingTable[nodeId][nodeId] = -1;
            // Fill distances
            final float[] distances = new float[nodesNumber];
            Arrays.fill(distances, Float.MAX_VALUE);
            distances[nodeId] = 0;

            // Fill settledNodes set
            final boolean[] settledNodes = new boolean[nodesNumber];
            Arrays.fill(settledNodes, false);

            // Here we are going to store reverse path to each node
            final int reversePath[] = new int[nodesNumber];
            Arrays.fill(reversePath, -1);

            final PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>(nodesNumber, new Comparator<Node>() {
                public int compare(final Node node1, final Node node2) {
                    return distances[node1.id] < distances[node2.id] ? -1 : distances[node1.id] == distances[node2.id] ? 0 : 1;
                }
            });
            // Start with target node
            priorityQueue.add(network.nodes[nodeId]);
            while (!priorityQueue.isEmpty()) {
                final Node minimumNode = priorityQueue.poll();
                relaxNode(network, minimumNode, priorityQueue, distances, settledNodes, reversePath, channelAvailability);
            }

            // Now we have all the distances and reverse paths, lets restore direct paths for routing
            for (int j = 0; j < nodesNumber;j++){
                if (j == nodeId){
                    routingTable[nodeId][j] = -1;
                } else {
                    int current = j;
                    // In case if everything is ok, we should build full path
                    while (true) {
                        // In this case we see that path doesn't exist at the moment
                        if (current == -1) {
                            break;
                        }
                        final int previous = reversePath[current];
                        if (previous == nodeId){
                            routingTable[nodeId][j] = current;
                            break;
                        }
                        current = previous;
                    }
                }
            }
        }
        return routingTable;
    }

    private static void relaxNode(final Network network,
                                  final Node node,
                                  final PriorityQueue<Node> priorityQueue,
                                  final float[] distances,
                                  final boolean[] settledNodes,
                                  final int[] reversePath,
                                  final boolean[] channelAvailability) {
        // Relax node
        settledNodes[node.id] = true;
        for (Map.Entry<Integer, Channel> entry : network.nodes[node.id].adjacentNodes.entrySet()) {
                final int neighbour = entry.getKey();
                // We relax only not settled nodes
                if (settledNodes[neighbour]){
                    continue;
                }

                final Channel channel = entry.getValue();
                // Check if channel is available
                if (!channelAvailability[channel.id]){
                    continue;
                }

            if (distances[neighbour] == -1 || distances[neighbour] > distances[node.id] + channel.time) {
                reversePath[neighbour] = node.id;
                distances[neighbour] = distances[node.id] + channel.time;
                priorityQueue.add(network.nodes[neighbour]);
            }
        }
    }


    public DeikstraRoutingManager(final Network network,
                                  final LoadManager loadManager,
                                  final TimeLogManager timeManager,
                                  final int totalMessages) {
        super(totalMessages);
        System.out.println("Deikstra network based routing manager is used!");
        myNetwork = network;
        myLoadManager = loadManager;
        myTimeManager = timeManager;
        synchronized (myRoutingTableLock) {
            myRoutingTable = calculateRoutingTable(myNetwork, getChannelAvailability(myNetwork, myLoadManager, myTimeManager));
        }
    }

    private static boolean[] getChannelAvailability(final Network network, final LoadManager loadManager, final TimeLogManager timeLogManager) {
        final float currentTime = timeLogManager.getCurrentTime();
        final boolean result[] = new boolean[network.edges];
        for (int i=0;i<network.edges;i++){
            result[i] = loadManager.getEdgeLoad(i, Math.round(currentTime)) < 0.5f;
        }
        return result;
    }

    public void route(final NodeAgent agent, final Message message) {
        myTimeManager.log("Request from " + agent.getId() + " to route " + message);
        final int agentId = agent.getId();
        if (agentId == message.receiver) {
            myTimeManager.messageReceived(message);
            messageReceived(message);
            return;
        }
        while (true) {
            // Route
            int next = -1;
            Channel channel = null;
            final float channelLoad;
            synchronized (myRoutingTableLock) {
                next = myRoutingTable[agentId][message.receiver];
                if (next != -1) {
                    final Map<Integer, Channel> adjacentNodes = myNetwork.nodes[agentId].adjacentNodes;
                    channel = adjacentNodes.get(next);
                    assert adjacentNodes.containsKey(next) : "Cannot find node " + next + " for agent " + agentId;
                    channelLoad = myLoadManager.getEdgeLoad(channel.id, Math.round(myTimeManager.getCurrentTime()));
                } else {
                    channelLoad = 0.5f;
                }
            }
            if (channelLoad < 0.5f){
                final float deliveryTime = channel.time + channelLoad;
                myTimeManager.log("Sending " +  message + " to " + next + " channel time " + deliveryTime);
                agent.sendMessageAfterDelay(next, message, deliveryTime);
                return;
            } else {
                myWaitCount.incrementAndGet();
                myTimeManager.log("Channel out of service reached, rebuilding routing table...");
                myTimeManager.sleep(1);
                synchronized (myRoutingTableLock) {
                    myRoutingTable = calculateRoutingTable(myNetwork, getChannelAvailability(myNetwork, myLoadManager, myTimeManager));
                }
            }
        }
    }
}

