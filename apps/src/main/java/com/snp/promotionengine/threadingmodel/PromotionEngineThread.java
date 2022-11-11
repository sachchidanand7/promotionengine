package com.snp.promotionengine.threadingmodel;

import com.snp.promotionengine.container.OrderInfoHolder;
import com.snp.promotionengine.promotiontype.DefaultPromotionType;
import com.snp.promotionengine.promotiontype.PromotionType;
import com.snp.promotionengine.promotiontype.PromotionTypeBuilder;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class PromotionEngineThread implements Runnable {
    private final ConcurrentHashMap<Long, OrderInfoHolder> orderInfoHolderMap;
    private final AtomicBoolean runFlag;
    private final ArrayBlockingQueue<OrderInfoHolder> queue;
    private final PromotionTypeBuilder promotionTypeBuilder;
    private final Thread thread;
    private final DefaultPromotionType defaultPromotionType;

    public PromotionEngineThread(ArrayBlockingQueue<OrderInfoHolder> queue, ConcurrentHashMap<Long,
                                 OrderInfoHolder> orderInfoHolderMap,
                                 PromotionTypeBuilder promotionTypeBuilder) {
        this.queue = queue;
        runFlag = new AtomicBoolean(false);
        this.orderInfoHolderMap = orderInfoHolderMap;
        this.promotionTypeBuilder = promotionTypeBuilder;
        defaultPromotionType = new DefaultPromotionType();
        thread = new Thread(this);
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
        return totalOrderedPrice;
    }
}
