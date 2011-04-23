package org.vika.routing.network.jade;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import org.vika.routing.Message;
import org.vika.routing.TimeManager;
import org.vika.routing.TrafficManager;

/**
 * @author oleg
 */
public class TrafficAgent extends Agent {
    private final NodeAgent[] myAgents;
    private TrafficManager myTrafficManager;
    private final TimeManager myTimeManager;

    public TrafficAgent(final NodeAgent[] agents, final TrafficManager trafficManager, final TimeManager timeManager) {
        myAgents = agents;
        myTrafficManager = trafficManager;
        myTimeManager = timeManager;
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
                    // Wait for the delay number of quantum time
                    block(delay * myTimeManager.getQuantumTime());
                    myTrafficManager.nextMessage();
                }
            }
        });
        // Start Time manager
        myTimeManager.start();
    }


}
