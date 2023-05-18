package com.example.springIntigration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IntegrationTest {

    @Autowired
    @Qualifier("inputChannel")
    private MessageChannel inputChannel;

    @Autowired
    @Qualifier("outputChannel")
    private PollableChannel outputChannel;

    @Test
    public void whenSendMessage_thenReceiveTransformedMessage() {
        Message<String> message = MessageBuilder.withPayload("Hello, Spring Integration!").build();
        inputChannel.send(message);

        Message<?> received = outputChannel.receive(1000);
        Assertions.assertEquals("HELLO, SPRING INTEGRATION!", received.getPayload());
    }

}