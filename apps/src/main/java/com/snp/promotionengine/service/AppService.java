package com.snp.promotionengine.service;

import com.snp.promotionengine.container.PromotionEngineContainer;

public class AppService {


    private PromotionEngineContainer promotionEngineContainer;

    public AppService(String path) {
        promotionEngineContainer = new PromotionEngineContainer(path);
        promotionEngineContainer.loader();
    }

    public void start() {
        promotionEngineContainer.start();
    }

    public static void main(String[] arg) {
        if (arg.length == 0) {
            System.out.println("Config file path is missing...");
        }
        String path = arg[0];

        AppService appService = new AppService(path);

        appService.start();
    }
}
