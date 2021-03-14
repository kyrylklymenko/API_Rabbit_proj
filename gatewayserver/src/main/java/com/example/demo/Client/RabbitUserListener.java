package com.example.demo.Client;

import com.example.demo.api.dto.userDTO.UserDataFormat;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static com.example.demo.RabbitConfig.BrokerConfig.USERS_QUEUE_ID;
import static com.example.demo.RabbitConfig.BrokerConfig.USERS_STRING_QUEUE_ID;

@Component
public class RabbitUserListener {

    @RabbitListener(queues = USERS_QUEUE_ID)
    public void consumeMessage(UserDataFormat message) {
        System.out.println("Message recieved from queue : " + message.getUserName());
    }

    @RabbitListener(queues = USERS_STRING_QUEUE_ID)
    public void consumeTestMessage(String message) {
        System.out.println("Message recieved from queue : " + message);
    }
}
