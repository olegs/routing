package org.vika.routing.routing;

import org.vika.routing.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author oleg
 */
public class AbstractRoutingManager {
    private List<Integer> myLeftMessages;

    public AbstractRoutingManager(final int totalMessages) {
        myLeftMessages = new ArrayList<Integer>(totalMessages);
        for (int i=0;i<totalMessages;i++){
            myLeftMessages.add(i);
        }
    }

    protected void messageReceived(final Message message) {
        myLeftMessages.remove((Integer)message.id);
    }

    public Collection<Integer> leftMessages() {
        return myLeftMessages;
    }

    public boolean areAllMessagesReceived() {
        return myLeftMessages.isEmpty();
    }
}

