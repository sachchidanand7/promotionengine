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

    /**
     * Constructor of promotion engine thread.
     * @param queue
     * @param promotionTypeBuilder
     */
    public PromotionEngineThread(ArrayBlockingQueue<OrderInfoHolder> queue,
                                 PromotionTypeBuilder promotionTypeBuilder) {
        this.queue = queue;
        runFlag = new AtomicBoolean(false);
        this.promotionTypeBuilder = promotionTypeBuilder;
        defaultPromotionType = new DefaultPromotionType();
        thread = new Thread(this);
    }

    /**
     * Register price publisher to publish total calculated price.
     * @param pricePublisher
     */
    public void register(PricePublisher pricePublisher) {
        this.pricePublisher = pricePublisher;
    }

    /**
     * Start promotion engine thread.
     */
    public void start() {
        thread.start();
    }

    /**
     * This API is executed by thread.
     */
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

    /**
     * Stop the thread.
     */
    public void stop() {
        runFlag.set(false);
    }

    /**
     * Process each event.
     */
    protected void processEvent() {
        if (!queue.isEmpty()) {
            OrderInfoHolder orderInfoHolder = queue.peek();
            if (orderInfoHolder != null ) {
                processOrder(orderInfoHolder);
                queue.remove(orderInfoHolder);
            }
        }
    }

    /**
     * Process each order, apply all promotion type and calculate total price.
     * And then publish the total price to registered publisher.
     * @param orderInfoHolder
     * @return
     */
    protected double processOrder(OrderInfoHolder orderInfoHolder) {
        //Get the list of promotion types.
        List<PromotionType> promotionTypeList = promotionTypeBuilder.getAllPromotionTypes();
        double totalOrderedPrice = 0;

        // Get total price before applying all promotion types.
        double totalPrice = defaultPromotionType.execute(orderInfoHolder);

        // Calculate total price after applying all promotion types.
        double discountedPrice = 0;
        for(PromotionType promotionType : promotionTypeList) {
            discountedPrice += promotionType.execute(orderInfoHolder);
        }
        totalOrderedPrice = totalPrice - discountedPrice;

        // Publish price to registered publisher.
        if (pricePublisher != null) {
            pricePublisher.publishOrderPrice(orderInfoHolder.getOrderId(), totalOrderedPrice);
        }
        return totalOrderedPrice;
    }
}
