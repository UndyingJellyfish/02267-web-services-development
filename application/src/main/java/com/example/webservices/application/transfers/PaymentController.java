package com.example.webservices.application.transfers;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.BankException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.InvalidTransferAmountException;
import com.example.webservices.library.exceptions.TokenException;
import com.example.webservices.library.interfaces.IPaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final IPaymentService paymentService;


    public PaymentController(IPaymentService paymentService){
        this.paymentService = paymentService;
    }



    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public void TransferMoney(@RequestBody TransactionDto transaction){
        try {
            paymentService.transfer(transaction);
        } catch (TokenException | IllegalArgumentException | EntryNotFoundException | BankException | InvalidTransferAmountException | JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    public void RefundTransaction(@RequestBody UUID transactionId){
        try {
            paymentService.refund(transactionId);
        } catch (EntryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }



}
