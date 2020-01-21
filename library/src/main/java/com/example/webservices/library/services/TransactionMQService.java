package com.example.webservices.library.services;

import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.ITransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sun.security.util.PendingException;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionMQService extends RabbitMQBaseClass implements ITransactionService {

    public TransactionMQService(RabbitTemplate rabbitTemplate, @Qualifier("transactions") DirectExchange exchange) {
        super(rabbitTemplate, exchange);
    }

    /**
     * @author s164398
     * @param id the id of the account
     * @return returns null because it should not be public
     */
    @Override
    public List<TransactionDto> getTransactions(UUID id) {
        return null;
    }

    /**
     * @author s164398
     * @param transactionId the id of the token to refund
     * @return The id of the new transaction that refunds
     */
    @Override
    public UUID refundTransaction(UUID transactionId) {
        ResponseObject response = send(QUEUE_TRANSACTION_REFUND, transactionId);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
        return fromJson(response.getBody(), UUID.class);
    }

    /**
     * @author s164398
     * @param transaction persists a new transaction
     * @return The id of the new transaction
     */
    @Override
    public UUID addTransaction(TransactionDto transaction) {
        ResponseObject response = send(QUEUE_TRANSACTION_ADD, transaction);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
        return fromJson(response.getBody(), UUID.class);
    }

    /**
     * @author s164407
     * @param transactionId id of the transaction to fetch
     * @return representation of the requested transaction
     */
    @Override
    public TransactionDto getTransaction(UUID transactionId) {
        ResponseObject response = send(QUEUE_TRANSACTION_GET, transactionId);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
        return fromJson(response.getBody(), TransactionDto.class);
    }
}
