package org.vika.routing.network;

import junit.framework.TestCase;
import org.vika.routing.TimeManager;

/**
 * @author oleg
 * @date 23.04.11
 */
public class TimeManagerTest extends TestCase {

    public void testTime() throws InterruptedException {
        final TimeManager timeManager = new TimeManager(100, 1000);
        timeManager.start();
        Thread.sleep(2200);
        assertEquals(220, timeManager.getCurrentTime());
    }
}
