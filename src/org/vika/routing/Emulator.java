package org.vika.routing;

import org.vika.routing.network.Network;
import org.vika.routing.network.NodeAgent;

/**
 * @author oleg
 * @date 18.04.11
 */
public class Emulator {
    private final Network myNetwork;
    private final NodeAgent[] myNodeAgents;
    private final RoutingManager myRoutingManager;
    private final LoadManager myLoadManager;

    public Emulator(final Network network, final NodeAgent[] nodeAgents,
                    final RoutingManager routingManager, final LoadManager loadManager) {
        myNetwork = network;
        myNodeAgents = nodeAgents;
        myRoutingManager = routingManager;
        myLoadManager = loadManager;
    }

    public void emulate() {
        for (int i=0;i<100;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Ignore
            }
            myNodeAgents[0].sendMessage(1, new Message(myNodeAgents.length - 1, "Test message"));
        }
    }
}
