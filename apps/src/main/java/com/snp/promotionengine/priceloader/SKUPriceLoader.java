package com.snp.promotionengine.priceloader;

import com.snp.common.config.Configuration;

import java.util.Arrays;
import java.util.HashMap;

public class SKUPriceLoader {
    private static int MAX_SKU_PRICE_BUCKET_SIZE = 1024;
    private static byte SKU_A = 'A';
    private static byte SKU_B = 'B';
    private static byte SKU_C = 'C';
    private static byte SKU_D = 'D';


    private static double[] priceHolder;

    public SKUPriceLoader() {
        priceHolder = new double[MAX_SKU_PRICE_BUCKET_SIZE];
        Arrays.fill(priceHolder, 0);
    }

    /**
     * To do.Load each price from config.
     * @param configuration
     */
    public void loadSKUPrice(Configuration configuration) {
        priceHolder[SKU_A] = 50;
        priceHolder[SKU_B] = 30;
        priceHolder[SKU_C] = 20;
        priceHolder[SKU_D] = 15;
    }

    public static double getSKUPrice(byte skuUnitId) {
        if (skuUnitId > priceHolder.length || skuUnitId < 0) {
            throw new RuntimeException("Found invalid SKU unit Id");
        }
        return priceHolder[skuUnitId];
    }


}
