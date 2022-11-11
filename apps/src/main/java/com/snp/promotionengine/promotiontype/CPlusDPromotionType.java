package com.snp.promotionengine.promotiontype;

import com.snp.promotionengine.container.OrderInfoHolder;
import com.snp.promotionengine.priceloader.SKUPriceLoader;

public class CPlusDPromotionType implements PromotionType {
    private PromotionParameters promotionParameters;
    private static byte SKU_UNIT_C = 'C';
    private static byte SKU_UNIT_D = 'D';

    public CPlusDPromotionType() {

    }

    @Override
    public void init(PromotionParameters promotionParameters) {
        this.promotionParameters = promotionParameters;
    }

    @Override
    public void loadPromotionType() {

    }

    @Override
    public double execute(OrderInfoHolder orderInfoHolder) {
        int quantityOfC = orderInfoHolder.getSKUQuantity(SKU_UNIT_C) -
                orderInfoHolder.getPromotedSKUQuantity(SKU_UNIT_C);

        int quantityOfD = orderInfoHolder.getSKUQuantity(SKU_UNIT_D) -
                orderInfoHolder.getPromotedSKUQuantity(SKU_UNIT_D);

        int quantity = Math.min(quantityOfC, quantityOfD);

        if (quantity >= 1) {
            double eachSKUBlPriceOf_C = SKUPriceLoader.instance().getSKUPrice(SKU_UNIT_C);
            double eachSKUBlPriceOf_D = SKUPriceLoader.instance().getSKUPrice(SKU_UNIT_D);
            double totalPrice = quantityOfC * eachSKUBlPriceOf_C + quantityOfD * eachSKUBlPriceOf_D;
            orderInfoHolder.addPromotedSKUQuantity(SKU_UNIT_C, quantity);
            orderInfoHolder.addPromotedSKUQuantity(SKU_UNIT_D, quantity);
            return totalPrice - ((quantity * promotionParameters.getPromotedPrice()) +
                                (((quantityOfC - quantity) * eachSKUBlPriceOf_C)  +
                                ((quantityOfD - quantity) * eachSKUBlPriceOf_D)) );

        }
        return 0;
    }
}
