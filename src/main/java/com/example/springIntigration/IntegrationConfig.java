package com.example.springIntigration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.MessageBuilder;

@Configuration
@EnableIntegration
public class IntegrationConfig {

    @Autowired
    @Qualifier("integrationConfig.inputChannel")
    private AbstractMessageChannel inputChannel;

    @Autowired
    @Qualifier("integrationConfig.outputChannel")
    private PollableChannel outputChannel;


    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel outputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "inputChannel", outputChannel = "outputChannel")
    public MessageHandler serviceActivator() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) {
                String payload = (String) message.getPayload();
                payload = payload.toUpperCase();
                Message<String> response = MessageBuilder.withPayload(payload).build();
                outputChannel().send(response);
            }
        };
    }

}
