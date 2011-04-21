package org.vika.routing;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import org.vika.routing.network.Network;
import org.vika.routing.network.Node;
import org.vika.routing.network.Parser;
import org.vika.routing.network.jade.NodeAgent;
import org.vika.routing.network.jade.TrafficAgent;

import java.io.IOException;
import java.util.List;

/**
 * @author oleg
 */
public class Main {
    public static void main(String[] args) throws IOException, ControllerException {
        // Create empty profile
        final Properties props = new ExtendedProperties();
        props.setProperty(Profile.GUI, "true");
        final Profile p = new ProfileImpl(props);
        // Start a new JADE runtime system
        final AgentContainer container = Runtime.instance().createMainContainer(p);

        // Now we have successfully launched Agents platform
        final String fileName = "C:/work/routing/tests/org/vika/routing/network/network.txt";
        final Network network = Parser.parse(fileName);
        final Node[] nodes = network.nodes;
        final NodeAgent[] nodeAgents = new NodeAgent[nodes.length];

        // Generate random system load
        final LoadManager.Load load = LoadManager.generate(1000, nodes.length, network.edges, 10, 10);
        final LoadManager loadManager = new LoadManager(load);


        // Create routing manager
        final RoutingManager routingManager = new RoutingManager(network, loadManager);

        // Initiate traffic agent
        final List<TrafficManager.TrafficEvent> traffic = TrafficManager.generate(nodes.length, 100, 1000000);
        final TrafficAgent trafficAgent = new TrafficAgent(nodeAgents, new TrafficManager(traffic));

        // Create JADE backend
        // Register all the agents according to the network
        for (int i = 0; i < nodes.length; i++) {
            final NodeAgent nodeAgent = new NodeAgent(i, nodeAgents, loadManager, routingManager);
            nodeAgents[i] = nodeAgent;
            // Register agents
            container.acceptNewAgent("agent" + i, nodeAgent).start();
        }
        container.acceptNewAgent("trafficAgent", trafficAgent).start();
    }

}
