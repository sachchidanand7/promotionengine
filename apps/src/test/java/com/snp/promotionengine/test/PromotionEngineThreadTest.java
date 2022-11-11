package com.snp.promotionengine.test;

import com.snp.promotionengine.container.OrderInfoHolder;
import com.snp.promotionengine.priceloader.SKUPriceLoader;
import com.snp.promotionengine.promotiontype.PromotionTypeBuilder;
import com.snp.promotionengine.threadingmodel.PromotionEngineThread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class PromotionEngineThreadTest extends PromotionEngineThread {

    public PromotionEngineThreadTest(ArrayBlockingQueue<OrderInfoHolder> queue,
                                     ConcurrentHashMap<Long,
            OrderInfoHolder> orderInfoHolderMap,
                                     PromotionTypeBuilder promotionTypeBuilder) {
        super(queue, promotionTypeBuilder);
        SKUPriceLoader.instance().loadSKUPrice(null);
    }


    @Override
    protected double processOrder(OrderInfoHolder orderInfoHolder) {
        return super.processOrder(orderInfoHolder);
    }
}
