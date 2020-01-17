package com.example.webservices.library;

import gherkin.deps.com.google.gson.Gson;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.lang.reflect.Type;


public abstract class RabbitMQBaseClass {

    private Gson gson = new Gson();

    protected final RabbitTemplate rabbitTemplate;
    protected final DirectExchange exchange;

    public RabbitMQBaseClass(RabbitTemplate rabbitTemplate, DirectExchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    protected  <T> T fromJson(String response, Class<T> type){
        return gson.fromJson(response, type);
    }
    protected  <T> T fromJson(String response, Type type){
        return gson.fromJson(response, type);
    }

    protected String send(String queue, String message){
        return (String)rabbitTemplate.convertSendAndReceive(exchange.getName(), queue, message);
    }

}
