package com.example.webservices.payments.controller;

import com.example.webservices.library.RabbitHelper;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.*;
import com.example.webservices.library.interfaces.IPaymentService;
import jdk.nashorn.internal.parser.Token;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PaymentMQController extends RabbitHelper {

    private final IPaymentService paymentService;

    public PaymentMQController(IPaymentService paymentService){
        this.paymentService = paymentService;
    }

    @RabbitListener(queues = QUEUE_PAYMENT_TRANSFER)
    public ResponseObject transfer(TransactionDto jsonString){

        try {
            //TransactionDto transactionDto = fromJson(jsonString, TransactionDto.class);
            TransactionDto result = this.paymentService.transfer(jsonString);
            return success(result);
        } catch (EntryNotFoundException | InvalidTokenException e) {
            return failure(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch ( DuplicateEntryException e) {
            return failure(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (TokenException | InvalidTransferAmountException e) {
            return failure(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return failure(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RabbitListener(queues = QUEUE_PAYMENT_REFUND)
    public ResponseObject refund(TransactionDto transactionDto){
        try {
            this.paymentService.refund(transactionDto.getTransactionId());
            return success();
        } catch (EntryNotFoundException e) {
            return failure(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (DuplicateEntryException e) {
            return failure(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
