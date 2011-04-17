package org.vika.routing.network;

import java.util.Scanner;

/**
 * @author oleg
 */
public class Channel {
    public int size;

    public static Channel parse(final Scanner scanner) {
        // TODO: implement me later
        final Channel channel = new Channel();
        channel.size = scanner.nextInt();
        return channel;
    }
}
