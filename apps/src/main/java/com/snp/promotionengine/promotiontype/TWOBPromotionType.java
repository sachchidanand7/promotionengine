package com.snp.promotionengine.promotiontype;

import com.snp.promotionengine.container.OrderInfoHolder;
import com.snp.promotionengine.priceloader.SKUPriceLoader;

public class TWOBPromotionType implements PromotionType {

    private static byte TWO_B_PROMOTION_TYPE_UNIT_ID = 'B';
    private PromotionParameters promotionParameters;

    public TWOBPromotionType() {

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
        int quantity = orderInfoHolder.getSKUQuantity(TWO_B_PROMOTION_TYPE_UNIT_ID) -
                orderInfoHolder.getPromotedSKUQuantity(TWO_B_PROMOTION_TYPE_UNIT_ID);
        double eachSKUBlPrice = SKUPriceLoader.instance().getSKUPrice(TWO_B_PROMOTION_TYPE_UNIT_ID);
        double totalPrice = quantity * eachSKUBlPrice;
        if (quantity >= 2) {

            int numOfQuantityToBePromoted = quantity / 2;
            int modular = quantity % 2;
            double afterPromotedPrice = numOfQuantityToBePromoted * promotionParameters.getPromotedPrice() + modular * eachSKUBlPrice;

            orderInfoHolder.addPromotedSKUQuantity(TWO_B_PROMOTION_TYPE_UNIT_ID, numOfQuantityToBePromoted * 2);
            return totalPrice - afterPromotedPrice;

        }
        return 0;

    }
}
