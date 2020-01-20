package com.example.webservices.library;

import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.lang.reflect.ParameterizedType;


public abstract class RabbitMQBaseClass extends RabbitHelper {

    protected final RabbitTemplate rabbitTemplate;
    protected final DirectExchange exchange;

    public RabbitMQBaseClass(RabbitTemplate rabbitTemplate, DirectExchange exchange) {
        super();
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }



    /*private <T2> send(String queue, T message){
        return (String)rabbitTemplate.convertSendAndReceive(exchange.getName(), queue, message);
    }*/
    protected <T> ResponseObject send(String queue, T message){
        return send(queue, message, ResponseObject.class);
    }
    protected <T, T2> T2 send(String queue, T message, Class<T2> type){
        return (T2) rabbitTemplate.convertSendAndReceive(exchange.getName(),
                queue,
                MessageBuilder.withPayload(message).build()
                );
        //return fromJson(send(queue, toJson(message)), type);
    }

}
