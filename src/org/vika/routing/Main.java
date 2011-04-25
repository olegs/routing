package org.vika.routing;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.tools.sniffer.Agent;
import jade.util.ExtendedProperties;
import jade.util.leap.ArrayList;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import org.vika.routing.network.Network;
import org.vika.routing.network.Node;
import org.vika.routing.network.Parser;
import org.vika.routing.network.jade.NodeAgent;
import org.vika.routing.network.jade.TrafficAgent;
import org.vika.routing.routing.NeuroRoutingManager;
import org.vika.routing.routing.RoutingManager;

import java.io.IOException;
import java.util.List;

/**
 * @author oleg
 */
public class Main {

    private static final int TIME = 100; // Total number of time quantum
    private static final int QUANTUM_TIME=100; // (0.1 sec) This is a time quantum used for modelling
    private static final int MESSAGES = 10; // How many messages will generated in traffic and spread during TIME
    private static final int NODE_LOAD_MAX = 10;
    private static final int EDGE_LOAD_MAX = 10;

    public static void main(String[] args) throws IOException, ControllerException, InterruptedException {
        // Create empty profile
        final Properties props = new ExtendedProperties();
        // props.setProperty(Profile.GUI, "true");
        final Profile p = new ProfileImpl(props);
        // Start a new JADE runtime system
        final AgentContainer container = Runtime.instance().createMainContainer(p);

        // Now we have successfully launched Agents platform
        final String fileName = "C:/work/routing/tests/org/vika/routing/network/network.txt";
        final Network network = Parser.parse(fileName);
        final Node[] nodes = network.nodes;
        final NodeAgent[] nodeAgents = new NodeAgent[nodes.length];

        final LoadManager loadManager = new LoadManager();

        // Time manager
        final TimeManager timeManager = new TimeManager(TIME, QUANTUM_TIME);

        // Initiate traffic agent
        final TrafficManager trafficManager = new TrafficManager();

        // Create JADE backend
        // Register all the agents according to the network, and create argument string for the snifferAgent
        final ArrayList nodeAgentsList = new ArrayList(nodeAgents.length);
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < nodes.length; i++) {
            final NodeAgent nodeAgent = new NodeAgent(i, nodeAgents, loadManager, timeManager);
            nodeAgents[i] = nodeAgent;
            final String name = "agent" + i;
            if  (builder.length() > 0){
                builder.append(";");
            }
            builder.append(name);
            nodeAgentsList.add(new Agent(name));
            container.acceptNewAgent(name, nodeAgent).start();
        }

        // SnifferAgent creating
//        final AgentController sniffer =
//                container.createNewAgent("sniffer", "jade.tools.sniffer.Sniffer", new Object[]{builder.toString()});
//        sniffer.start();
        emulate(container, network, nodes, nodeAgents, loadManager, timeManager, trafficManager);

        // Spin lock to kill the runtime on time limit
        while (timeManager.getCurrentTime() < TIME){
            Thread.sleep(1000);
        }
        Runtime.instance().shutDown();
    }

    private static void emulate(final AgentContainer container,
                                final Network network,
                                final Node[] nodes,
                                final NodeAgent[] nodeAgents,
                                final LoadManager loadManager,
                                final TimeManager timeManager,
                                final TrafficManager trafficManager) throws StaleProxyException {
        // Start traffic agent
        final TrafficAgent trafficAgent = new TrafficAgent(nodeAgents, trafficManager, timeManager);

        // Create routing manager
        final RoutingManager routingManager = new NeuroRoutingManager(network, loadManager, timeManager);
        NodeAgent.routingManager = routingManager;
        // Generate random system load and traffic
        final LoadManager.Load load =
                LoadManager.generate(TIME, nodes.length, network.edges, NODE_LOAD_MAX, EDGE_LOAD_MAX);
        final List<TrafficManager.TrafficEvent> traffic = TrafficManager.generate(nodes.length, MESSAGES, TIME);
        trafficManager.setTraffic(traffic);
        loadManager.setLoad(load);

        container.acceptNewAgent("trafficAgent", trafficAgent).start();
    }
}
