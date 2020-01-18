package com.example.webservices.payments.services;

import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.interfaces.ITransactionService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    public void AddTransaction(TransactionDto transaction) {
        ResponseObject response = send(QUEUE_TRANSACTION_ADD, transaction);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new RuntimeException();
        }
    }

    @Override
    public TransactionDto GetTransactionByTokenId(UUID tokenId) {
        ResponseObject response = send(QUEUE_TRANSACTION_GET, tokenId);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new RuntimeException();
        }
        return fromJson(response.getBody(), TransactionDto.class);
    }
}
