package com.snp.promotionengine.promotiontype;

import com.snp.promotionengine.container.OrderInfoHolder;
import com.snp.promotionengine.priceloader.SKUPriceLoader;

public class DefaultPromotionType implements PromotionType {
    private PromotionParameters promotionParameters;


    public DefaultPromotionType() {

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
        int  index = 0;
        double totalPrice = 0;
        while(index < orderInfoHolder.getTotalNumOfSKU()) {
            byte skuUnitId = orderInfoHolder.getSKUId(index);
            int quantity = orderInfoHolder.getSKUQuantity(skuUnitId);
            double price = SKUPriceLoader.instance().getSKUPrice(skuUnitId);
            ++index;
            totalPrice += quantity * price;
        }

        orderInfoHolder.setTotalPrice(totalPrice);

        return totalPrice;
    }
}
