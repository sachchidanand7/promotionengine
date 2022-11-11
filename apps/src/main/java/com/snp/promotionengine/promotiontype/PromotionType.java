package com.snp.promotionengine.promotiontype;

import com.snp.promotionengine.container.OrderInfoHolder;

public interface PromotionType {

    void init(PromotionParameters promotionParameters);
    void loadPromotionType();
    double execute(OrderInfoHolder orderInfoHolder);
}
