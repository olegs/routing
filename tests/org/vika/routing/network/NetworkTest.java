package org.vika.routing.network;

import com.sun.tools.javac.util.Pair;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.Map;

/**
 * @author oleg
 */
public class NetworkTest extends TestCase {
    public void testSimple() throws IOException {
        final Node[] nodes = Parser.parse("C:/work/routing/tests/org/vika/routing/network/neuronetwork.txt");
        final NeuroNetwork network = new NeuroNetwork(nodes);
        final StringBuilder builder = new StringBuilder();
        for (NeuroNode neuroNode : network.neuroNodes) {
            if (builder.length() > 0 && neuroNode.wValues.size() > 0){
                builder.append("\n");
            }
            for (Map.Entry<Pair<Integer, Integer>, Float> entry : neuroNode.wValues.entrySet()) {
                builder.append("(").append(entry.getKey().fst).append(",")
                        .append(entry.getKey().snd).append(",").append(entry.getValue()).append(")");
            }
        }
        assertEquals("(2,1,0.6666667)(1,2,0.36363637)(3,4,1.0)(2,4,0.375)(1,4,0.2)(1,3,0.14285715)(3,2,0.8)(3,1,0.6)(2,3,0.2857143)\n" +
                "(2,0,0.6666667)(0,2,0.5)(0,3,1.0)(2,4,1.0)(0,4,1.0)(2,3,1.0)\n" +
                "(3,0,0.8)(1,0,0.36363637)(0,1,0.5)(0,3,0.5)(3,4,1.0)(1,4,0.2857143)(0,4,0.5714286)(1,3,0.23076923)(3,1,0.45454547)\n" +
                "(4,0,0.5)(2,0,0.2857143)(4,1,0.8)(2,1,1.0)(0,1,1.0)(4,2,0.6)(0,2,0.5)(2,4,0.14285715)(0,4,0.2)\n" +
                "(3,0,1.0)(3,2,1.0)(3,1,1.0)", builder.toString());
    }
}
