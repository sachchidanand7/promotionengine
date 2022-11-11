package com.snp.promotionengine.container;

import java.util.Arrays;

public class OrderInfoHolder {
    private static int MAX_SKU_BUCKET_SIZE = 4096;
    private static int MAX_NUM_OF_SKU = 128;

    private final int[] orderInfo;
    private final byte[] itemList;
    private final int[] promtedInfo;
    private int skuUnitIdIndex = 0;
    private double totalPrice = 0;
    private double totalPriceAfterPrompt = 0;

    /**
     * Constructor of OrderInfoHolder.
     */
    public OrderInfoHolder() {
        orderInfo = new int[MAX_SKU_BUCKET_SIZE];
        promtedInfo = new int[MAX_SKU_BUCKET_SIZE];
        itemList = new byte[MAX_NUM_OF_SKU];
        Arrays.fill(orderInfo, 0);
        Arrays.fill(itemList, (byte)0);
    }


    /**
     * Add SKU quantity corresponding to SKU Id.
     * @param skuId
     * @param quantity
     */
    public void addSKUQuantity(byte skuId, int quantity) {

        if (skuId > MAX_SKU_BUCKET_SIZE) {
            throw new RuntimeException("IndexOverFlow while adding SKU quantity");
        }
        orderInfo[skuId] = quantity;
        itemList[skuUnitIdIndex] = skuId;
        ++skuUnitIdIndex;
    }

    /**
     * Add SKU quantity corresponding to SKU Id.
     * @param skuId
     * @param quantity
     */
    public void addPromotedSKUQuantity(byte skuId, int quantity) {

        if (skuId > MAX_SKU_BUCKET_SIZE) {
            throw new RuntimeException("IndexOverFlow while adding SKU quantity");
        }
        promtedInfo[skuId] = quantity;
    }


    /**
     * Get SKU promoted quantity corresponding to SKU Id.
     * @param skuId
     * @return
     */
    public int getPromotedSKUQuantity(byte skuId) {
        if (skuId < 0) {
            throw new RuntimeException("UnderFlow while getting SKU id");
        }

        if (skuId > MAX_NUM_OF_SKU) {
            throw new RuntimeException("Found invalid SKU index");
        }
        return promtedInfo[skuId];
    }



    /**
     * Get SKU quantity corresponding to SKU Id.
     * @param skuId
     * @return
     */
    public int getSKUQuantity(byte skuId) {
        if (skuId < 0) {
            throw new RuntimeException("UnderFlow while getting SKU id");
        }

        if (skuId > MAX_NUM_OF_SKU) {
            throw new RuntimeException("Found invalid SKU index");
        }
        return orderInfo[skuId];
    }


    /**
     * Get SKU Id.
     * @param indeId
     * @return
     */
    public byte getSKUId(int indeId) {
        if (indeId < 0) {
            throw new RuntimeException("UnderFlow while getting SKU quantity");
        }

        if (indeId > skuUnitIdIndex) {
            throw new RuntimeException("Found invalid SKU Id");
        }
        return itemList[indeId];
    }


    /**
     * Get the total number of SKU.
     * @return
     */
    public int getTotalNumOfSKU() {
        return skuUnitIdIndex;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalPriceAfterPrompt() {
        return totalPriceAfterPrompt;
    }

    public void setTotalPriceAfterPrompt(double totalPriceAfterPrompt) {
        this.totalPriceAfterPrompt = totalPriceAfterPrompt;
    }
}
