package com.example.springIntigration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class SpringIntegrationExampleTests {

    @Autowired
    private OrderProcessingGateway gateway;

    @Test
    public void testProcessOrders() {
        List<Order> orders = Arrays.asList(new Order("1"), new Order("2"), new Order("3"));
        List<Order> processedOrders = gateway.processOrders(orders);
        processedOrders.forEach(System.out::println);
    }
}