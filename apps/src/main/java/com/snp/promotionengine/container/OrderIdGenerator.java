package com.snp.promotionengine.container;

public class OrderIdGenerator {

    public static long getId() {
        return System.nanoTime();
    }
}
