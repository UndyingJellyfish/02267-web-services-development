package com.example.webservices.payments.services;

import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.ITransactionService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sun.security.util.PendingException;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionMQService extends RabbitMQBaseClass implements ITransactionService {

    public TransactionMQService(RabbitTemplate rabbitTemplate, @Qualifier("transactions") DirectExchange exchange) {
        super(rabbitTemplate, exchange);
    }

    @Override
    public List<TransactionDto> getTransactions(UUID id) {
        return null;
    }

    @Override
    public UUID refundTransaction(UUID tokenId) throws EntryNotFoundException {
        //TODO: Fix
        throw new PendingException();
    }

    @Override
    public UUID addTransaction(TransactionDto transaction) {
        ResponseObject response = send(QUEUE_TRANSACTION_ADD, transaction);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new RuntimeException();
        }
        return fromJson(response.getBody(), UUID.class);
    }

    @Override
    public TransactionDto getTransaction(UUID transactionId) {
        ResponseObject response = send(QUEUE_TRANSACTION_GET, transactionId);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new RuntimeException();
        }
        return fromJson(response.getBody(), TransactionDto.class);
    }
}
