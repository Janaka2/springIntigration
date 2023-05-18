package com.example.springIntigration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.aggregator.DefaultAggregatingMessageGroupProcessor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.core.MessageSelector;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.List;

@SpringBootApplication
@EnableIntegration
public class SpringIntegrationExample {

    public static void main(String[] args) {
        SpringApplication.run(SpringIntegrationExample.class, args);
    }

    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel outputChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow processOrderFlow() {
        return IntegrationFlows
                .from("inputChannel")
                .transform(transform())
                .filter(filter())
                .route(router())
                .split(splitter())
                .handle(serviceActivator())
                .aggregate(aggregator())
                .channel("outputChannel")
                .get();
    }

    @Transformer
    public GenericTransformer<Order, Order> transform() {
        return order -> {
            System.out.println("Transforming order: " + order);
            order.setStatus("processing");
            return order;
        };
    }


    @ServiceActivator
    public MessageSelector filter() {
        return message -> {
            Order order = (Order) message.getPayload();
            System.out.println("Filtering order: " + order);
            return order.getStatus().equals("processing");
        };
    }

    @Bean
    public HeaderValueRouter router() {
        HeaderValueRouter router = new HeaderValueRouter("status");
        router.setChannelMapping("processing", "processingChannel");
        return router;
    }

    @Bean
    public MessageChannel processingChannel() {
        return new DirectChannel();
    }

    @Bean
    public AbstractMessageSplitter splitter() {
        return new AbstractMessageSplitter() {
            @Override
            protected Object splitMessage(Message<?> message) {
                System.out.println("Splitting message: " + message);
                return ((List<?>) message.getPayload()).toArray();
            }
        };
    }

    @Bean
    public MessageHandler serviceActivator() {
        return message -> {
            Order order = (Order) message.getPayload();
            System.out.println("Adding timestamp to order: " + order);
            order.setTimestamp(System.currentTimeMillis());
        };
    }

    @Bean
    public DefaultAggregatingMessageGroupProcessor aggregator() {
        return new DefaultAggregatingMessageGroupProcessor();
    }
}
