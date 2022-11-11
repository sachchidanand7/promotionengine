package com.snp.promotionengine.container;

public class OrderIdGenerator {
    private static long  counter;

    public static long getId() {
        ++counter;
        return System.nanoTime() + counter ;
    }


}
