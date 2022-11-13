package com.snp.promotionengine.messaging;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * This is dummy messaging transport, which generate hard coded messages.
 * Note:- Message format
 * <NumberOfSKU : 4bytes>
 * <SKU_ID : 1byte><quantity : 4bytes>
 *     ...
 * <SKU_ID : 1byte><quantity : 4bytes>
 */
public class DefaultMessagingTransport implements MessagingSyncTransport {

    private static int MAX_CAPACITY = 1024;
    private ArrayList<MessageHandler> messageHandlers;
    private int messageCounter = 0;
    private ByteBuffer byteBuffer;

    /**
     * Constructor of this class.
     */
    public DefaultMessagingTransport() {
        messageHandlers = new ArrayList<>();
        byteBuffer = ByteBuffer.allocate(MAX_CAPACITY);
    }

    /**
     * Initialize the messaging transport.
     */
    @Override
    public void init() {
    }

    /**
     * Register for callback.
     * @param messageHandler
     */
    @Override
    public void register(MessageHandler messageHandler) {
        messageHandlers.add(messageHandler);
    }

    /**
     * Poll the each message.
     */
    @Override
    public void pollMessages() {

        // Receive the message.
        ByteBuffer byteBuffer = receiveMessage();
        if (byteBuffer != null) {

            // Pass this message to each message handler.
            for (MessageHandler messageHandler : messageHandlers) {
                messageHandler.handleMessage(byteBuffer);
            }


        }
    }

    /**
     * Stop message transport.
     */
    @Override
    public void stop() {
    }


    /**
     * Receive the message from transport.
     * Note:- Creating here dummy three types of messages.
     * @return
     */
    private ByteBuffer receiveMessage() {
        ByteBuffer byteBuffer = null;
        switch (messageCounter) {
            case 0:
                byteBuffer = getMessageForScenarioA();
                break;
            case 1:
                byteBuffer = getMessageForScenarioB();
                break;
            case 2:
                byteBuffer = getMessageForScenarioC();
                break;
        }
        ++messageCounter;
        if (messageCounter == 3) {
            messageCounter = 0;
        }
        return byteBuffer;

    }

    /**
     * Creating dummy messages.
     * @return
     */
    private ByteBuffer getMessageForScenarioA() {
        byteBuffer.clear();
        byteBuffer.putInt(3);
        byteBuffer.put((byte)'A');
        byteBuffer.putInt(1);
        byteBuffer.put((byte)'B');
        byteBuffer.putInt(1);
        byteBuffer.put((byte)'C');
        byteBuffer.putInt(1);
        byteBuffer.flip();

       return byteBuffer;
    }

    /**
     * Creating dummy messages.
     * @return
     */
    private ByteBuffer getMessageForScenarioB() {
        byteBuffer.clear();
        byteBuffer.putInt(3);
        byteBuffer.put((byte)'A');
        byteBuffer.putInt(5);
        byteBuffer.put((byte)'B');
        byteBuffer.putInt(5);
        byteBuffer.put((byte)'C');
        byteBuffer.putInt(1);
        byteBuffer.flip();

        return byteBuffer;
    }

    /**
     * Creating dummy messages.
     * @return
     */
    private ByteBuffer getMessageForScenarioC() {
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

}
