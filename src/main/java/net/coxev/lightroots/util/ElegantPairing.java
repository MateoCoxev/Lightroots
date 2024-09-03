package net.coxev.lightroots.util;

import java.math.BigInteger;

public class ElegantPairing {
    public static long pair(int a, int b) {
        return a >= b ? (long) Math.pow(a, 2) + a + b : a + (long) Math.pow(b, 2);
    }
    public static int[] unpair(long z) {
        var r = Math.floor(Math.sqrt(z));
        var p = Math.pow(r, 2);
        var m = z - p;

        var x = m < r ? z - p : r;
        var y = m < r ? r : z - p - r;

        return new int[]{(int) x, (int) y};
    }
}
