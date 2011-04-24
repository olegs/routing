package org.vika.routing.network.jade;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.vika.routing.LoadManager;
import org.vika.routing.Message;
import org.vika.routing.TimeManager;
import org.vika.routing.routing.RoutingManager;

import java.io.IOException;

/**
 * @author oleg
 * Agent used in network, which can be used in JADE system
 */
public class NodeAgent extends Agent {
    public final LoadManager myLoadManager;
    public final RoutingManager myRoutingManager;
    private final TimeManager myTimeManager;
    private final NodeAgent[] myAgents;
    private final int myId;

    public NodeAgent(final int id,
                     final NodeAgent[] agents,
                     final LoadManager loadManager,
                     final RoutingManager routingManager,
                     final TimeManager timeManager) {
        myLoadManager = loadManager;
        myRoutingManager = routingManager;
        myTimeManager = timeManager;
        myAgents = agents;
        myId = id;
    }

    public void setup() {
        myTimeManager.log("Agent " + getAID().getName() + " is ready.");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    messageRecieved(msg);
                }
                block();
            }
        });
    }

    private void messageRecieved(final ACLMessage msg) {
        try {
            myTimeManager.log(getLocalName()
                    + " received: "
                    + msg.getContentObject());
        } catch (UnreadableException e) {
            System.err.println("Couldn't read the message content: " + msg);
        }
        Message result;
        try {
            result = (Message) msg.getContentObject();
        } catch (UnreadableException e) {
            result = null;
        }
        final Message message = result;
        myRoutingManager.route(this, message);
    }

    /**
     * Send message to the agent with given id
     */
    public void sendMessageAfterDelay(final int receiver, final Message message, final int delay){
        final AMSAgentDescription agent = findAMSAgentDescription(receiver);
        final ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(agent.getName());
        try {
            msg.setContentObject(message);
        } catch (IOException e) {
            // Ignore this
        }
        new Thread(new Runnable() {
            public void run() {
                myTimeManager.sleep(delay);
                send(msg);
            }
        }).run();
    }

    private AMSAgentDescription findAMSAgentDescription(final int id) {
        final AMSAgentDescription description = new AMSAgentDescription();
        final AID aid = myAgents[id].getAID();
        description.setName(aid);
        return description;
    }

    public int getId() {
        return myId;
    }
}
