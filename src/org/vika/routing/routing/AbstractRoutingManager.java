package org.vika.routing.routing;

import org.vika.routing.Message;
import org.vika.routing.network.jade.NodeAgent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author oleg
 */
public abstract class AbstractRoutingManager implements RoutingManager {
    private List<Integer> myLeftMessages;
    protected final AtomicInteger myWaitCount = new AtomicInteger(0);

    public AbstractRoutingManager(final int totalMessages) {
        myLeftMessages = new ArrayList<Integer>(totalMessages);
        for (int i=0;i<totalMessages;i++){
            myLeftMessages.add(i);
        }
    }

    protected synchronized void messageReceived(final Message message) {
        myLeftMessages.remove((Integer)message.id);
    }

    public Collection<Integer> leftMessages() {
        return myLeftMessages;
    }

    public boolean areAllMessagesReceived() {
        return myLeftMessages.isEmpty();
    }

    public int getWaitTime() {
        return myWaitCount.get();
    }
}

