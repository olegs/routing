package org.vika.routing.network.jade;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import org.vika.routing.Message;
import org.vika.routing.TrafficManager;

/**
 * @author oleg
 */
public class TrafficAgent extends Agent {
    private final NodeAgent[] myAgents;
    private TrafficManager myTrafficManager;

    public TrafficAgent(final NodeAgent[] agents, final TrafficManager trafficManager) {
        myAgents = agents;
        myTrafficManager = trafficManager;
    }

    public void setup() {
        System.out.println("Agent " + getAID().getName() + " is ready.");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                while (!myTrafficManager.end()){
                    final int delay = (int) myTrafficManager.getDelay();
                    final int initialAgent = myTrafficManager.getInitialAgent();
                    final Message message = myTrafficManager.getMessage();
                    AgentsUtil.sendMessage(myAgents, initialAgent, message);
                    block(delay);
                    myTrafficManager.nextMessage();
                }
            }
        });
    }
}
