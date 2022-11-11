package com.snp.promotionengine.promotiontype;

public class PromotionParameters {
    private double price = 0;
    private double promotedPrice = 0;

    public PromotionParameters(double price, double promotedPrice) {
        this.price = price;
        this.promotedPrice = promotedPrice;
    }

    public double getPrice() {
        return price;
    }

    public double getPromotedPrice() {
        return promotedPrice;
    }
}
