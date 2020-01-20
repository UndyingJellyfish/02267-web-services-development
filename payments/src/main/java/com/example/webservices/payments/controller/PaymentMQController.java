package com.example.webservices.payments.controller;

import com.example.webservices.library.RabbitHelper;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.BankException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.InvalidTransferAmountException;
import com.example.webservices.library.exceptions.TokenException;
import com.example.webservices.library.interfaces.IPaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentMQController extends RabbitHelper {

    private final IPaymentService paymentService;

    public PaymentMQController(IPaymentService paymentService){
        this.paymentService = paymentService;
    }

    @RabbitListener(queues = QUEUE_PAYMENT_TRANSFER)
    public ResponseObject transfer(TransactionDto jsonString) throws JsonProcessingException {

        try {
            //TransactionDto transactionDto = fromJson(jsonString, TransactionDto.class);
            TransactionDto result = this.paymentService.transfer(jsonString);
            return success(result);
        } catch (EntryNotFoundException | TokenException | BankException | InvalidTransferAmountException | JsonProcessingException e) {
            return failure(e.getMessage());
        }

    }
    @RabbitListener(queues = QUEUE_PAYMENT_REFUND)
    public ResponseObject refund(TransactionDto transactionDto) throws JsonProcessingException {

        try {
           //TransactionDto transactionDto = fromJson(jsonString, TransactionDto.class);
            this.paymentService.refund(transactionDto.getTransactionId());
            return success();
        } catch (EntryNotFoundException | JsonProcessingException e) {
            return failure(e.getMessage());
        }

    }
}
