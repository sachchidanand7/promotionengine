package com.snp.promotionengine.pricepublisher;

public interface PricePublisher {

    void publishOrderPrice(long orderId, double totalPrice);
}
