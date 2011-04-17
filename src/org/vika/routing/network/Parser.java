package org.vika.routing.network;

import com.sun.org.apache.xpath.internal.NodeSet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author oleg
 * @date 17.04.11
 */
public class Parser {
    public static Node[] parse(final String fileName) throws IOException {
        final FileInputStream stream = new FileInputStream(fileName);
        try {
            final Scanner scanner = new Scanner(stream);
            // Number of nodes in a network
            final int number = scanner.nextInt();
            final Node[] nodes = new Node[number];
            for (int i = 0; i < number; i++) {
                nodes[i] = new Node(nodes);
            }
            for (int i = 0; i < number; i++) {
                Node.parse(scanner, nodes, i);
            }
            // Sanity check that we've successfully reached the end of the file
            assert scanner.nextInt() == -1;
            return nodes;
        } finally {
            stream.close();
        }
    }
}
