package com.example.springIntigration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.util.List;

@MessagingGateway(defaultRequestChannel = "inputChannel")
public interface OrderProcessingGateway {
    @Gateway(requestChannel = "inputChannel", replyChannel = "outputChannel")
    List<Order> processOrders(List<Order> orders);
}
