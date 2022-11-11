package com.snp.promotionengine.test;

import com.snp.promotionengine.container.OrderInfoHolder;
import com.snp.promotionengine.promotiontype.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.ByteBuffer;

@RunWith(JUnit4.class)
public class TestPromotionEngine {

    private  PromotionEngineThreadTest promotionEngineThreadTest;
    private static int MAX_CAPACITY = 1024;
    private ByteBuffer byteBuffer;

    @Before
    public void setup() throws Exception {

        PromotionTypeBuilder promotionTypeBuilder = new PromotionTypeBuilder();
        PromotionType promotionType = new ThreeAPromotionType();
        PromotionParameters promotionParameters = new PromotionParameters(50, 130);
        promotionType.init(promotionParameters);
        promotionTypeBuilder.add(promotionType);

        PromotionType twoBPromotionType = new TWOBPromotionType();
        PromotionParameters twoBpromotionParameters = new PromotionParameters(30, 45);
        twoBPromotionType.init(twoBpromotionParameters);
        promotionTypeBuilder.add(twoBPromotionType);

        PromotionType cPlusPromotionType = new CPlusDPromotionType();
        PromotionParameters cPlusDPomotionParameters = new PromotionParameters(0, 30);
        cPlusPromotionType.init(cPlusDPomotionParameters);
        promotionTypeBuilder.add(cPlusPromotionType);


        promotionEngineThreadTest = new PromotionEngineThreadTest(null, null, promotionTypeBuilder);
        byteBuffer = ByteBuffer.allocate(MAX_CAPACITY);
    }

    @Test
    public void testScenarioA() {
        OrderInfoHolder orderInfoHolder = new OrderInfoHolder();
        orderInfoHolder.addSKUQuantity((byte)'A', 1);
        orderInfoHolder.addSKUQuantity((byte)'B', 1);
        orderInfoHolder.addSKUQuantity((byte)'C', 1);
        double result = promotionEngineThreadTest.processOrder(orderInfoHolder);
        Assert.assertTrue(result == 100);
    }

    @Test
    public void testScenarioB() {
        OrderInfoHolder orderInfoHolder = new OrderInfoHolder();
        orderInfoHolder.addSKUQuantity((byte)'A', 5);
        orderInfoHolder.addSKUQuantity((byte)'B', 5);
        orderInfoHolder.addSKUQuantity((byte)'C', 1);
        double result = promotionEngineThreadTest.processOrder(orderInfoHolder);
        Assert.assertTrue(result == 370);
    }

    @Test
    public void testScenarioC() {
        OrderInfoHolder orderInfoHolder = new OrderInfoHolder();
        orderInfoHolder.addSKUQuantity((byte)'A', 3);
        orderInfoHolder.addSKUQuantity((byte)'B', 5);
        orderInfoHolder.addSKUQuantity((byte)'C', 1);
        orderInfoHolder.addSKUQuantity((byte)'D', 1);
        double result = promotionEngineThreadTest.processOrder(orderInfoHolder);
        Assert.assertTrue(result == 280);
    }
}
