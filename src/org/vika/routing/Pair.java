package org.vika.routing;

/**
 * @author oleg
 */
public class Pair<A, B> {
    public final A fst;
    public final B snd;


    public Pair(final A fst, final B snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public A getFirst(){
        return fst;
    }

    public B getSecond() {
        return snd;
    }
}
