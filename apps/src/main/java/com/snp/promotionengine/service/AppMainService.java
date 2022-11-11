package com.snp.promotionengine.service;

import com.snp.promotionengine.container.PromotionEngineContainer;
import com.snp.promotionengine.pricepublisher.DefaultPricePublisher;

import java.net.URL;

public class AppMainService {

    private PromotionEngineContainer promotionEngineContainer;

    /**
     * Constructor of this class.
     */
    public AppMainService() {

        try {

            // Load the configuration file path.
            ClassLoader classLoader = getClass().getClassLoader();
            URL url = classLoader.getResource("promotion-engine.xml");
            String configPath = url.getPath();

            // Create promotion engine container and register price publisher for each published order price.
            promotionEngineContainer = new PromotionEngineContainer(configPath);
            promotionEngineContainer.register(new DefaultPricePublisher());

            // Load promotion engine container.
            promotionEngineContainer.loader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Start app services.
     */
    public void start() {
        try {
            // Start promotion engine.
            promotionEngineContainer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] arg) {

        AppMainService appMainService = new AppMainService();

        appMainService.start();
    }
}
