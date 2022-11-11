package com.snp.promotionengine.messaging;

public interface MessagingSyncTransport {
    void init();
    void register(MessageHandler messageHandler);
    void pollMessages();
    void stop();
}
