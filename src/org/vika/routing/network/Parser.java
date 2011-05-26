package org.vika.routing.network;

import com.sun.tools.javac.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author oleg
 */
public class Parser {
    public static Network parse(final File file) throws IOException {
        final FileInputStream stream = new FileInputStream(file);
        try {
            final Scanner scanner = new Scanner(stream);
            // Number of nodes in a network
            final int number = scanner.nextInt();
            final int edges = scanner.nextInt();
            final HashMap <Pair<Integer, Integer>, Channel> channels = new HashMap<Pair<Integer, Integer>, Channel>();
            for (int i = 0; i< edges;i++) {
                channels.put(new Pair<Integer, Integer>(scanner.nextInt(), scanner.nextInt()), Channel.parse(i, scanner));
            }
            final Node[] nodes = new Node[number];
            for (int i = 0; i < number; i++) {
                nodes[i] = new Node(nodes);
            }
            for (Map.Entry<Pair<Integer, Integer>, Channel> entry : channels.entrySet()) {
                final Pair<Integer, Integer> pair = entry.getKey();
                final Channel channel = entry.getValue();
                nodes[pair.fst].adjacentNodes.put(pair.snd, channel);
                nodes[pair.snd].adjacentNodes.put(pair.fst, channel);
            }
            return new Network(nodes, edges);
        } finally {
            stream.close();
        }
    }
}
