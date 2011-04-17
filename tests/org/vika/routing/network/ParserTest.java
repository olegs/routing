package org.vika.routing.network;

import junit.framework.TestCase;

import java.io.IOException;

/**
 * @author oleg
 */
public class ParserTest extends TestCase {
    public void testParsing() throws IOException {
        final Node[] nodes = Parser.parse("C:/work/routing/tests/org/vika/routing/network/network.txt");
        assertEquals(2, nodes.length);
        assertEquals(0, nodes[0].id);
        assertEquals(1, nodes[0].adjacentNodes.size());
        assertEquals(1, nodes[1].adjacentNodes.size());
        assertEquals(10, nodes[0].adjacentNodes.get(1).size);
    }
}
