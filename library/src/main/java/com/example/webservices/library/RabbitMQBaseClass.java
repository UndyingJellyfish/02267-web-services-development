package com.example.webservices.library;

import com.example.webservices.library.dataTransferObjects.ResponseObject;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;


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
        return send(queue, toJson(message), ResponseObject.class);
    }
    protected <T, T2> T2 send(String queue, T message, Class<T2> type){
        return (T2) rabbitTemplate.convertSendAndReceive(exchange.getName(), queue, MessageBuilder.withPayload(message).build());
        //return fromJson(send(queue, toJson(message)), type);
    }

}
