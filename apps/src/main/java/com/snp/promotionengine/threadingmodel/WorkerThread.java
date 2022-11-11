package com.snp.promotionengine.threadingmodel;

import com.snp.promotionengine.container.OrderIdGenerator;
import com.snp.promotionengine.container.OrderInfoHolder;
import com.snp.promotionengine.messaging.DefaultMessagingTransport;
import com.snp.promotionengine.messaging.MessageHandler;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorkerThread implements Runnable, MessageHandler {
    private final AtomicBoolean runFlag;
    private final ArrayBlockingQueue<OrderInfoHolder> queue;
    private final Thread thread;
    private final DefaultMessagingTransport messagingTransport;


    public WorkerThread(ArrayBlockingQueue<OrderInfoHolder> queue) {
        this.queue = queue;
        runFlag = new AtomicBoolean(false);
        thread = new Thread(this);
        messagingTransport = new DefaultMessagingTransport();
    }

    public void start() {
        messagingTransport.register(this);
        thread.start();
    }

    @Override
    public void run() {
        runFlag.set(true);
        while(runFlag.get()) {
            try {
                receiveOrderRequest();

                // Can have here waiting strategy BUSYSPIN/BLOCK.
                Thread.sleep(1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        runFlag.set(false);
    }

    /**
     * Order request can be received from any kind of transport type { IPC/SOLACE/ZEROMQ/Aeron/Or any TCP based protocols}
     *  Using here
     */
    private void receiveOrderRequest() {
        messagingTransport.pollMessages();
    }

    /**
     * Handle received message from transport.
     * @param byteBuffer
     */
    @Override
    public void handleMessage(ByteBuffer byteBuffer) {
        int totalNumberOfItems = byteBuffer.getInt();
        int index = 0;
        long orderId = OrderIdGenerator.getId();
        OrderInfoHolder orderInfoHolder = new OrderInfoHolder();
        orderInfoHolder.setOrderId(orderId);
        while(index < totalNumberOfItems) {
            byte skuId = byteBuffer.get();
            int quantity = byteBuffer.getInt();
            // ****Creating here garbage, we should use Object pool over here.
            orderInfoHolder.addSKUQuantity(skuId, quantity);
            ++index;

        }

        boolean isOrderEnqued = false;
        while (!isOrderEnqued) {
            isOrderEnqued = queue.offer(orderInfoHolder);
        }

    }
}
