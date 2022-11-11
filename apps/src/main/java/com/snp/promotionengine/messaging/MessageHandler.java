package com.snp.promotionengine.messaging;

import java.nio.ByteBuffer;

public interface MessageHandler {
    void handleMessage(ByteBuffer byteBuffer);
}
