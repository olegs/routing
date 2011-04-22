package org.vika.routing.network;

import junit.framework.TestCase;
import org.vika.routing.TrafficManager;

import java.util.List;

/**
 * @author oleg
 */
public class TrafficManagerTest extends TestCase {
    public void testGenerate() {
        final List<TrafficManager.TrafficEvent> traffic = TrafficManager.generate(10, 10, 100);
        assertEquals(10, traffic.size());
        int time = 0;
        for (TrafficManager.TrafficEvent trafficEvent : traffic) {
            time += trafficEvent.delay;
        }
        assertEquals(100, time);
    }
}
