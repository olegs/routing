package org.vika.routing.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * @author TRSteep
 */
public class AgentB extends Agent {

    protected void setup() {
        System.out.println("Привет! агент " + getAID().getName() + " готов.");
        addBehaviour(new CyclicBehaviour(this) {

            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println(" – " +
                            myAgent.getLocalName() +
                            " received: "
                            + msg.getContent());
//Вывод на экран локального имени агента и полученного сообщения
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent("Pong");
//Содержимое сообщения
                    send(reply); //отправляем сообщения
                }
                block();
            }
        });
    }

}
