package com.example.webservices.transactions.controller;

import com.example.webservices.library.RabbitHelper;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.ITransactionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/** @author s164434 */
@Service
public class TransactionMQController extends RabbitHelper {

    private final ITransactionService transactionService;

    public TransactionMQController(ITransactionService transactionService){
        this.transactionService = transactionService;
    }

    @RabbitListener(queues = QUEUE_TRANSACTION_ADD)
    public ResponseObject addTransaction(TransactionDto transactionDto) {

        try{
            UUID transactionId = this.transactionService.addTransaction(transactionDto);
            return success(transactionId);
        }
        catch (DuplicateEntryException e){
            return failure(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e){
            return error(e);
        }
    }

    @RabbitListener(queues = QUEUE_TRANSACTION_REFUND)
    public ResponseObject refund(UUID transactionId) {
        try{
            UUID newTransaction = this.transactionService.refundTransaction(transactionId);
            return success(newTransaction);
        } catch (EntryNotFoundException e) {
            return failure(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DuplicateEntryException e) {
            return failure(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e){
            return error(e);
        }
    }

    @RabbitListener(queues = QUEUE_TRANSACTION_GET)
    public ResponseObject getTransaction(UUID transactionId) {
        try{
            List<TransactionDto> result = this.transactionService.getTransactions(transactionId);
            return success(result);
        } catch (Exception e){
            return error(e);
        }
    }
}
