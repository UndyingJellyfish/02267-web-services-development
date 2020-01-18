package com.example.webservices.transactions.controller;

import com.example.webservices.library.RabbitHelper;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.interfaces.ITransactionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionMQController extends RabbitHelper {

    private final ITransactionService transactionService;

    public TransactionMQController(ITransactionService transactionService){

        this.transactionService = transactionService;
    }

    @RabbitListener(queues = QUEUE_TRANSACTION_ADD)
    public ResponseObject addTransaction(String jsonString){

        try{
            TransactionDto dto = fromJson(jsonString, TransactionDto.class);
            this.transactionService.AddTransaction(dto);
            return success();
        }
        catch (Exception e){
            return failure(e.getMessage());
        }
    }

    @RabbitListener(queues = QUEUE_TRANSACTION_GET)
    public ResponseObject getTransaction(String jsonString){
        try{
            UUID transactionId = fromJson(jsonString, UUID.class);
            List<TransactionDto> result = this.transactionService.getTransactions(transactionId);
            return success(result);
        }
        catch (Exception e){
            return failure(e.getMessage());
        }



    }


}
