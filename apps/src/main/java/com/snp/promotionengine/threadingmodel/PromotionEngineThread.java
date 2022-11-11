package com.snp.promotionengine.threadingmodel;

import com.snp.promotionengine.container.OrderInfoHolder;
import com.snp.promotionengine.pricepublisher.PricePublisher;
import com.snp.promotionengine.promotiontype.DefaultPromotionType;
import com.snp.promotionengine.promotiontype.PromotionType;
import com.snp.promotionengine.promotiontype.PromotionTypeBuilder;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class PromotionEngineThread implements Runnable {
    private final AtomicBoolean runFlag;
    private final ArrayBlockingQueue<OrderInfoHolder> queue;
    private final PromotionTypeBuilder promotionTypeBuilder;
    private final Thread thread;
    private final DefaultPromotionType defaultPromotionType;
    private PricePublisher pricePublisher;

    public PromotionEngineThread(ArrayBlockingQueue<OrderInfoHolder> queue,
                                 PromotionTypeBuilder promotionTypeBuilder) {
        this.queue = queue;
        runFlag = new AtomicBoolean(false);
        this.promotionTypeBuilder = promotionTypeBuilder;
        defaultPromotionType = new DefaultPromotionType();
        thread = new Thread(this);
    }

    public void register(PricePublisher pricePublisher) {
        this.pricePublisher = pricePublisher;
    }

    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        runFlag.set(true);
        while(runFlag.get()) {
            try {
                processEvent();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        runFlag.set(false);
    }

    protected void processEvent() {

        if (!queue.isEmpty()) {
            OrderInfoHolder orderInfoHolder = queue.peek();
            if (orderInfoHolder != null ) {
                processOrder(orderInfoHolder);
                queue.remove(orderInfoHolder);
            }
        }
    }


    protected double processOrder(OrderInfoHolder orderInfoHolder) {
        List<PromotionType> promotionTypeList = promotionTypeBuilder.getAllPromotionTypes();
        double totalOrderedPrice = 0;
        double totalPrice = defaultPromotionType.execute(orderInfoHolder);
        double discountedPrice = 0;
        for(PromotionType promotionType : promotionTypeList) {
            discountedPrice += promotionType.execute(orderInfoHolder);
        }

        totalOrderedPrice = totalPrice - discountedPrice;
        if (pricePublisher != null) {
            pricePublisher.publishOrderPrice(orderInfoHolder.getOrderId(), totalOrderedPrice);
        }
        return totalOrderedPrice;
    }
}
