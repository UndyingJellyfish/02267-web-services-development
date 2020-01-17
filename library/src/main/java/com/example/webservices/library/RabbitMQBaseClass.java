package com.example.webservices.library;

import gherkin.deps.com.google.gson.Gson;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.lang.reflect.Type;


public abstract class RabbitMQBaseClass extends RabbitHelper {

    protected final RabbitTemplate rabbitTemplate;
    protected final DirectExchange exchange;

    public RabbitMQBaseClass(RabbitTemplate rabbitTemplate, DirectExchange exchange) {
        super();
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    protected String send(String queue, String message){
        return (String)rabbitTemplate.convertSendAndReceive(exchange.getName(), queue, message);
    }
    protected <T> String send(String queue, T message){
        return send(queue, toJson(message));
    }

}
