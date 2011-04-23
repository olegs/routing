package org.vika.routing.network.jade;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.vika.routing.LoadManager;
import org.vika.routing.Message;
import org.vika.routing.RoutingManager;

import java.io.IOException;

/**
 * @author oleg
 * Agent used in network, which can be used in JADE system
 */
public class NodeAgent extends Agent {
    public final LoadManager myLoadManager;
    public final RoutingManager myRoutingManager;
    private final NodeAgent[] myAgents;
    private final int myId;

    public NodeAgent(final int id,
                     final NodeAgent[] agents,
                     final LoadManager loadManager,
                     final RoutingManager routingManager) {
        myLoadManager = loadManager;
        myRoutingManager = routingManager;
        myAgents = agents;
        myId = id;
    }

    public void setup() {
        System.out.println("Agent " + getAID().getName() + " is ready.");
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
        System.out.println(" – " + getLocalName()
                + " received: "
                + msg.getContent());
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
    public void sendMessage(final int receiver, final Message message){
        final AMSAgentDescription agent = findAMSAgentDescription(receiver);
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(agent.getName());
        try {
            msg.setContentObject(message);
        } catch (IOException e) {
            // Ignore this
        }
        send(msg);
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