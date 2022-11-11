package com.snp.promotionengine.promotiontype;

import java.util.ArrayList;
import java.util.List;

public class PromotionTypeBuilder {
    List<PromotionType> promotionTypes;
    public PromotionTypeBuilder() {
        promotionTypes = new ArrayList<>();
    }

    public PromotionTypeBuilder init() {
        return this;
    }

    public void add(PromotionType promotionType) {
        promotionTypes.add(promotionType);
    }
    public PromotionTypeBuilder build() {
        return this;
    }

    public List<PromotionType> getAllPromotionTypes() {

        return promotionTypes;
    }

}
