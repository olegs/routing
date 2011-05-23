package org.vika.routing.network.jade;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.lang.acl.ACLMessage;
import org.vika.routing.Message;
import org.vika.routing.TimeLogManager;
import org.vika.routing.TrafficManager;

import java.io.IOException;

/**
 * @author oleg
 */
public class TrafficAgent extends Agent {
    private final NodeAgent[] myAgents;
    private TrafficManager myTrafficManager;
    private final TimeLogManager myTimeManager;

    public TrafficAgent(final NodeAgent[] agents, final TrafficManager trafficManager, final TimeLogManager timeManager) {
        myAgents = agents;
        myTrafficManager = trafficManager;
        myTimeManager = timeManager;
    }

    /**
     * Send message to the agent with given id
     */
    public static void sendMessage(final Agent[] agents, final int receiver, final Message message){
        final AMSAgentDescription description = new AMSAgentDescription();
        final AID aid = agents[receiver].getAID();
        description.setName(aid);
        final AMSAgentDescription agent = description;
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(agent.getName());
        try {
            msg.setContentObject(message);
        } catch (IOException e) {
            // Ignore this
        }
        agents[receiver].send(msg);
    }

    public void setup() {
        System.out.println("Traffic manager starting traffic.");
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                while (!myTrafficManager.end()){
                    final int delay = (int) myTrafficManager.getDelay();
                    final int initialAgent = myTrafficManager.getInitialAgent();
                    final Message message = myTrafficManager.getMessage();
                    // Setup initial time
                    message.time = Math.round(myTimeManager.getCurrentTime());
                    myTimeManager.log("Initiated " + message + " to agent " + initialAgent);
                    sendMessage(myAgents, initialAgent, message);
                    myTrafficManager.nextMessage();
                    if (!myTrafficManager.end()){
                        myTimeManager.sleep(delay);
                    }
                }
                System.out.println("Traffic manager is done.");
                doDelete();
            }
        });
        // Start Time manager
        myTimeManager.start();
    }
}
