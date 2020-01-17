package com.example.webservices.transactions.services;

import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.interfaces.ITransactionService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService extends RabbitMQBaseClass implements ITransactionService {

    public TransactionService(RabbitTemplate rabbitTemplate, DirectExchange exchange) {
        super(rabbitTemplate, exchange);
    }

    @Override
    public List<TransactionDto> getTransactions(UUID id) {
        return null;
    }

    @Override
    public void AddTransaction(TransactionDto transaction) {

    }

    @Override
    public TransactionDto GetTransactionByTokenId(UUID tokenId) {
        return null;
    }
}
