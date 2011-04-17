package org.vika.routing;

import org.vika.routing.network.Node;
import org.vika.routing.network.Parser;

import java.io.*;

/**
 * @author oleg
 */
public class Main {
    public static void main(String[] args) throws IOException {
        final String fileName = "network.txt";
        final Node[] nodes = Parser.parse(fileName);
    }

}
