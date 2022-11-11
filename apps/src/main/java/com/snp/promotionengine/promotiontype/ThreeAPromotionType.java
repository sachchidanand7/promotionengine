package com.snp.promotionengine.promotiontype;

import com.snp.promotionengine.container.OrderInfoHolder;
import com.snp.promotionengine.priceloader.SKUPriceLoader;

public class ThreeAPromotionType implements PromotionType {

    private static byte THREE_A_PROMOTION_TYPE_UNIT_ID = 'A';
    private PromotionParameters promotionParameters;

    public ThreeAPromotionType() {

    }

    @Override
    public void loadPromotionType() {

    }

    @Override
    public void init(PromotionParameters promotionParameters) {
        this.promotionParameters = promotionParameters;
    }

    @Override
    public double execute(OrderInfoHolder orderInfoHolder) {

        int quantity = orderInfoHolder.getSKUQuantity(THREE_A_PROMOTION_TYPE_UNIT_ID) -
                orderInfoHolder.getPromotedSKUQuantity(THREE_A_PROMOTION_TYPE_UNIT_ID);
        double eachSKUAlPrice = SKUPriceLoader.getSKUPrice(THREE_A_PROMOTION_TYPE_UNIT_ID);
        double totalPrice = quantity * eachSKUAlPrice;
        if (quantity >= 3) {

            int numOfQuantityToBePromoted = quantity / 3;
            int modular = quantity % 3;
            double afterPromotedPrice = numOfQuantityToBePromoted * promotionParameters.getPromotedPrice() + modular * eachSKUAlPrice;
            orderInfoHolder.addPromotedSKUQuantity(THREE_A_PROMOTION_TYPE_UNIT_ID, numOfQuantityToBePromoted * 3);
           return totalPrice - afterPromotedPrice;

        }
        return 0;
    }
}
