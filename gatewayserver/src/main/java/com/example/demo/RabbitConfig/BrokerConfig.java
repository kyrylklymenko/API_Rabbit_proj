package com.example.demo.RabbitConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
public class BrokerConfig {

    //Гайд: https://habr.com/ru/post/262069/

    public static final String USERS_QUEUE_ID = "banking_user_queue";
    public static final String USERS_STRING_QUEUE_ID = "banking_user_queue";
    public static final String WORKERS_QUEUE_ID = "banking_worker_queue";
    public static final String USER_EXCHANGE = "banking_user_exchange";
    public static final String WORKER_EXCHANGE = "banking_worker_exchange";
    public static final String EXCHANGE = "banking_exchange";
    public static final String USERS_ROUTING_KEY = "banking_user_routingKey";
    public static final String WORKERS_ROUTING_KEY = "banking_worker_routingKey";


    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("localhost");
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public Queue userCreateQueue(){
        return new Queue(USERS_QUEUE_ID);
    }

    @Bean
    public Queue userStringQueue(){
        return new Queue(USERS_STRING_QUEUE_ID);
    }

    @Bean
    public Queue workerCreateQueue(){
        return new Queue(WORKERS_QUEUE_ID);
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public TopicExchange workerExchange() {
        return new TopicExchange(WORKER_EXCHANGE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding userCreateBinding() {
        return BindingBuilder.bind(userCreateQueue()).to(exchange()).with(USERS_ROUTING_KEY);
    }

    @Bean
    public Binding userStringBinding() {
        return BindingBuilder.bind(userStringQueue()).to(exchange()).with(USERS_ROUTING_KEY);
    }

    @Bean
    public Binding workerBinding() {
        return BindingBuilder.bind(workerCreateQueue()).to(exchange()).with(WORKERS_ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
