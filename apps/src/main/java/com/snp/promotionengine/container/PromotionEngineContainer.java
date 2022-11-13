package com.snp.promotionengine.container;

import com.snp.common.config.Configuration;
import com.snp.common.config.DefaultConfiguration;
import com.snp.promotionengine.priceloader.SKUPriceLoader;
import com.snp.promotionengine.pricepublisher.PricePublisher;
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

    private final ArrayBlockingQueue<OrderInfoHolder> queue;
    private final Configuration configuration;
    private final WorkerThread[] workerThreads;
    private final PromotionEngineThread promotionEngineThread;
    private final PromotionTypeBuilder promotionTypeBuilder;

    /**
     * Constructor of Promotion engine container class.
     * @param path
     */
    public PromotionEngineContainer(String path) {
        configuration = new DefaultConfiguration(path);
        queue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
        workerThreads = new WorkerThread[NUM_OF_WORKER_THREAD];
        for(int i = 0; i < NUM_OF_WORKER_THREAD; i++) {
            workerThreads[i] = new WorkerThread(queue);
        }
        promotionTypeBuilder = new PromotionTypeBuilder();
        promotionEngineThread = new PromotionEngineThread(queue, promotionTypeBuilder);

    }

    /**
     *
     * @param pricePublisher
     */
    public void register(PricePublisher pricePublisher) {
        promotionEngineThread.register(pricePublisher);
    }

    /**
     * Load configuration file and build all promotion type class.
     */
    @Override
    public void loader() {

        // Load configuration.
        configuration.load();

        // Load price for each SKU.
        SKUPriceLoader.instance().loadSKUPrice(configuration);

        // Load each promotion type by looping through entry in configuration.
        int totalNumOfEntries = configuration.getTotalEntriesOfRepeatedNodes("promotionType");
        int index = 0;
        while (index < totalNumOfEntries) {

            try {
                String promotionTypeName = configuration.getAttributeFromRepeatedNodes("promotionType", "name", index);
                String promotionTypeClassName = configuration.getAttributeFromRepeatedNodes("promotionType", "className", index);

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
                throw new RuntimeException("Loading of configuration is failed");
            }

            ++index;
        }
    }

    /**
     * Start all threads.
     */
    @Override
    public void start() {

        promotionEngineThread.start();

        for(int i = 0; i < NUM_OF_WORKER_THREAD; i++) {
            workerThreads[i].start();
        }
    }

    /**
     * Stop all the threads.
     */
    @Override
    public void stop() {

        for(int i = 0; i < NUM_OF_WORKER_THREAD; i++) {
            workerThreads[i].stop();
        }

        promotionEngineThread.stop();
    }
}
