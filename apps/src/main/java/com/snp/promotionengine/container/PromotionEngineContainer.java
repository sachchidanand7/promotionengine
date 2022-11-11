package com.snp.promotionengine.container;

import com.snp.common.config.Configuration;
import com.snp.common.config.DefaultConfiguration;
import com.snp.promotionengine.priceloader.SKUPriceLoader;
import com.snp.promotionengine.promotiontype.PromotionParameters;
import com.snp.promotionengine.promotiontype.PromotionType;
import com.snp.promotionengine.promotiontype.PromotionTypeBuilder;
import com.snp.promotionengine.threadingmodel.PromotionEngineThread;
import com.snp.promotionengine.threadingmodel.WorkerThread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class PromotionEngineContainer implements Container {
    private static int MAX_QUEUE_SIZE = 4096;
    private static int NUM_OF_WORKER_THREAD = 1;

    //NOTE : - It will be better to use persistent LVCache instead of concurrentHashmap.
    private final ConcurrentHashMap<Long, OrderInfoHolder> orderInfoTable;
    private final ArrayBlockingQueue<OrderInfoHolder> queue;
    private final Configuration configuration;
    private final WorkerThread[] workerThreads;
    private final PromotionEngineThread promotionEngineThread;
    private final PromotionTypeBuilder promotionTypeBuilder;
    private final SKUPriceLoader skuPriceLoader;

    public PromotionEngineContainer(String path) {
        configuration = new DefaultConfiguration(path);
        orderInfoTable = new ConcurrentHashMap();
        queue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
        workerThreads = new WorkerThread[NUM_OF_WORKER_THREAD];
        for(int i = 0; i < NUM_OF_WORKER_THREAD; i++) {
            workerThreads[i] = new WorkerThread(queue, orderInfoTable);
        }
        promotionTypeBuilder = new PromotionTypeBuilder();
        promotionEngineThread = new PromotionEngineThread(queue, orderInfoTable, promotionTypeBuilder);
        skuPriceLoader = new SKUPriceLoader();

    }

    @Override
    public void loader() {

        configuration.load();

        skuPriceLoader.loadSKUPrice(configuration);

        int totalNumOfEntries = configuration.getTotalEntriesOfRepeatedNodes("promotionType");
        int index = 0;
        while (index < totalNumOfEntries) {
            String promotionTypeName = configuration.getAttributeFromRepeatedNodes("promotionType", "name", index);
            String promotionTypeClassName = configuration.getAttributeFromRepeatedNodes("promotionType", "className", index);
            try {
                Class promotionTypeClass = Class.forName(promotionTypeClassName);
                PromotionType promotionType = (PromotionType) promotionTypeClass.newInstance();
                String price = configuration.getValueFromRepeatedNodes("promotionType", "price", index);
                double priceValue = price != null && !price.isEmpty() ? Double.parseDouble(price) : 0.0;
                String promotedPrice = configuration.getValueFromRepeatedNodes("promotionType", "strategyPrice", index);
                double promotedPriceValue = promotedPrice != null && !promotedPrice.isEmpty() ? Double.parseDouble(promotedPrice) : 0.0;
                PromotionParameters promotionParameters = new PromotionParameters(priceValue, promotedPriceValue);
                promotionType.init(promotionParameters);
                promotionTypeBuilder.add(promotionType);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ++index;
        }
    }

    @Override
    public void start() {

        promotionEngineThread.start();

        for(int i = 0; i < NUM_OF_WORKER_THREAD; i++) {
            workerThreads[i].start();
        }
    }

    @Override
    public void stop() {

        promotionEngineThread.stop();

        for(int i = 0; i < NUM_OF_WORKER_THREAD; i++) {
            workerThreads[i].stop();
        }
    }
}
