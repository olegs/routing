package org.vika.routing;

import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;
import org.vika.routing.network.jade.NodeAgent;

/**
 * @author oleg
 * @date 18.04.11
 */
public class Emulator {
    private final AgentContainer myContainer;
    private final NodeAgent[] myNodeAgents;
    private final TrafficManager myTrafficManager;

    public Emulator(final AgentContainer container,
                    final NodeAgent[] nodeAgents,
                    final TrafficManager trafficManager) {
        myContainer = container;
        myNodeAgents = nodeAgents;
        myTrafficManager = trafficManager;
    }

    public void emulate() throws StaleProxyException {

    }
}
