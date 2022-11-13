package com.snp.promotionengine.container;

public class OrderIdGenerator {

    /**
     * Get unique order id;
     * @return
     */
    public static long getId() {
        return System.nanoTime();
    }
}
