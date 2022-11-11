package com.snp.promotionengine.messaging;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DefaultMessagingTransport implements MessagingSyncTransport {

    private static int MAX_CAPACITY = 1024;
    private ArrayList<MessageHandler> messageHandlers;
    private int messageCounter = 0;
    private ByteBuffer byteBuffer;

    public DefaultMessagingTransport() {
        messageHandlers = new ArrayList<>();
        byteBuffer = ByteBuffer.allocate(MAX_CAPACITY);
    }

    @Override
    public void init() {

    }

    @Override
    public void register(MessageHandler messageHandler) {
        messageHandlers.add(messageHandler);
    }

    @Override
    public void pollMessages() {
        ByteBuffer byteBuffer = receiveMessage();
        for (MessageHandler messageHandler : messageHandlers) {
            messageHandler.handleMessage(byteBuffer);
        }
    }



    private ByteBuffer receiveMessage() {
        return getMessageForScenarioA();
    }


//    private ByteBuffer getMessageForScenarioA() {
//        byteBuffer.clear();
//        byteBuffer.putInt(3);
//        byteBuffer.put((byte)'A');
//        byteBuffer.putInt(1);
//        byteBuffer.put((byte)'B');
//        byteBuffer.putInt(1);
//        byteBuffer.put((byte)'C');
//        byteBuffer.putInt(1);
//        byteBuffer.flip();
//
//       return byteBuffer;
//    }


//    private ByteBuffer getMessageForScenarioA() {
//        byteBuffer.clear();
//        byteBuffer.putInt(3);
//        byteBuffer.put((byte)'A');
//        byteBuffer.putInt(5);
//        byteBuffer.put((byte)'B');
//        byteBuffer.putInt(5);
//        byteBuffer.put((byte)'C');
//        byteBuffer.putInt(1);
//        byteBuffer.flip();
//
//        return byteBuffer;
//    }

    private ByteBuffer getMessageForScenarioA() {
        byteBuffer.clear();
        byteBuffer.putInt(4);
        byteBuffer.put((byte)'A');
        byteBuffer.putInt(3);
        byteBuffer.put((byte)'B');
        byteBuffer.putInt(5);
        byteBuffer.put((byte)'C');
        byteBuffer.putInt(1);
        byteBuffer.put((byte)'D');
        byteBuffer.putInt(1);
        byteBuffer.flip();

        return byteBuffer;
    }


    @Override
    public void stop() {

    }
}
