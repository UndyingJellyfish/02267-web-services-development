package com.example.webservices.library.services;

import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.BankException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.InvalidTransferAmountException;
import com.example.webservices.library.exceptions.TokenException;
import com.example.webservices.library.interfaces.IPaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class PaymentMQService extends RabbitMQBaseClass implements IPaymentService {

    public PaymentMQService(RabbitTemplate rabbitTemplate, @Qualifier("payments") DirectExchange exchange) {
        super(rabbitTemplate, exchange);
    }

    /**
     * @author s164407
     * @param transactionDto data object used to transfer money from one account to another using a token
     * @return TransactionDto
     */
    @Override
    public TransactionDto transfer(TransactionDto transactionDto) {
        ResponseObject response = send(QUEUE_PAYMENT_TRANSFER, transactionDto);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
        return fromJson(response.getBody(), TransactionDto.class);
    }


    /**
     * @author s164407
     * @param transactionId the transaction to refund
     */
    @Override
    public void refund(UUID transactionId) {
        ResponseObject response = send(QUEUE_PAYMENT_REFUND, transactionId);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
    }
}
