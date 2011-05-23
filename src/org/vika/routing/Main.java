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
import org.vika.routing.routing.DeikstraRoutingManager;
import org.vika.routing.routing.NeuroRoutingManager;
import org.vika.routing.routing.RoutingManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author oleg
 */
public class Main {

    private static final int TIME = 100; // Total number of time quantum
    private static final int QUANTUM_TIME=100; // (0.01 sec) This is a time quantum used for modelling
    private static final int MESSAGES = 10; // How many messages will generated in traffic and spread during TIME
    private static final int EXPERIMENT_COUNT = 2;
    private static final String PROJECT_HOME = "C:/work/routing/";

    public static void main(String[] args) throws IOException, ControllerException, InterruptedException {
        // Create empty profile
        final Properties props = new ExtendedProperties();
        props.setProperty(Profile.LOCAL_SERVICE_MANAGER, "true");
        props.setProperty(Profile.LOCAL_HOST, "127.0.0.1");
        // props.setProperty(Profile.GUI, "true");
        final Profile p = new ProfileImpl(props);
        // Start a new JADE runtime system
        final Runtime runtime = Runtime.instance();
        final AgentContainer container = runtime.createMainContainer(p);

        // Now we have successfully launched Agents platform
        final String fileName = PROJECT_HOME + "tests/org/vika/routing/network/network.txt";
        final Network network = Parser.parse(fileName);
        final Node[] nodes = network.nodes;
        final NodeAgent[] nodeAgents = new NodeAgent[nodes.length];

        final LoadManager loadManager = new LoadManager();

        // Time manager
        final String outputFileName = PROJECT_HOME + "tests/org/vika/routing/network/result.txt";
        final BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
        final TimeLogManager timeManager = new TimeLogManager(writer, TIME, QUANTUM_TIME);

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
//               container.createNewAgent("sniffer", "jade.tools.sniffer.Sniffer", new Object[]{builder.toString()});
//        sniffer.start();
        try {
            for (int i=0;i< EXPERIMENT_COUNT;i++){
                timeManager.printToWriter("Starting experiment #" + i);
                emulate(container, network, nodes, nodeAgents, loadManager, timeManager, trafficManager);
            }
            timeManager.printAllStatistics();

        } finally {
            writer.close();
        }
        // Nasty hack to shut down
        System.exit(0);
    }

    private static void emulate(final AgentContainer container,
                                final Network network,
                                final Node[] nodes,
                                final NodeAgent[] nodeAgents,
                                final LoadManager loadManager,
                                final TimeLogManager timeManager,
                                final TrafficManager trafficManager) throws StaleProxyException, InterruptedException {
        // Generate random system load and traffic
        final LoadManager.Load load = LoadManager.generate(TIME, network.edges);
        final List<TrafficManager.TrafficEvent> traffic = TrafficManager.generate(nodes.length, MESSAGES, TIME);
        trafficManager.setTraffic(traffic);
        loadManager.setLoad(load);

        // Create neuro routing manager
        final RoutingManager neuroRoutingManager = new NeuroRoutingManager(network, loadManager, timeManager, MESSAGES);
        NodeAgent.routingManager = neuroRoutingManager;
        timeManager.resetStatistics(MESSAGES);
        // Start traffic agent
        container.acceptNewAgent("NeuroTrafficAgent", new TrafficAgent(nodeAgents, trafficManager, timeManager)).start();

        // Spin lock while all the messages are not processed
        while (!neuroRoutingManager.areAllMessagesReceived()){
            System.out.println("Waiting for routing finished.\n" +
                    "Messages left: " + neuroRoutingManager.leftMessages());
            Thread.sleep(1000);
        }
        timeManager.log("Routing successfully finished");
        timeManager.printStatistics();
        timeManager.saveNeuroStatistics();

        // Create OSPF routing manager
        final RoutingManager deikstraRoutingManager = new DeikstraRoutingManager(network, loadManager, timeManager, MESSAGES);
        NodeAgent.routingManager = deikstraRoutingManager;
        timeManager.resetStatistics(MESSAGES);
        // Start traffic agent
        trafficManager.reset();
        container.acceptNewAgent("DeikstraTrafficAgent", new TrafficAgent(nodeAgents, trafficManager, timeManager)).start();
        // Spin lock while all the messages are not processed
        while (!deikstraRoutingManager.areAllMessagesReceived()){
            System.out.println("Waiting for routing finished.\n" +
                    "Messages left: " + deikstraRoutingManager.leftMessages());
			Thread.sleep(1000);
        }
        timeManager.log("Routing successfully finished");
        timeManager.printStatistics();
        timeManager.saveDeikstraStatistics();
    }
}
