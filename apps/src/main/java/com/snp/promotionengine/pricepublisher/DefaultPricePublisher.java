package com.snp.promotionengine.pricepublisher;

public class DefaultPricePublisher implements PricePublisher {

    @Override
    public void publishOrderPrice(long orderId, double totalPrice) {
        System.out.println("Received orderId : " + orderId + " total price : " + totalPrice);
    }
}
