package org.vika.routing.network;

import java.util.Scanner;

/**
 * @author oleg
 */
public class Channel {
    public final int id;
    public float time;

    public Channel(final int id) {
        this.id = id;
    }

    public static Channel parse(final int id, final Scanner scanner) {
        final Channel channel = new Channel(id);
        channel.time = scanner.nextFloat();
        return channel;
    }
}
