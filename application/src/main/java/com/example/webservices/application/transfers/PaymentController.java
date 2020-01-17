package com.example.webservices.application.transfers;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.BankException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.TokenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;


    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }



    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public void TransferMoney(@RequestBody TransactionDto transaction){
        try {
            paymentService.transfer(transaction.getTokenId(), transaction.getMerchantId(), transaction.getAmount(), transaction.getDescription());
        } catch (TokenException | IllegalArgumentException | EntryNotFoundException| BankException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    public void RefundTransaction(@RequestBody TransactionDto transaction){
        try {
            paymentService.refund(transaction.getCustomerId(), transaction.getMerchantId(), transaction.getTokenId());
        } catch (TokenException |IllegalArgumentException | EntryNotFoundException | BankException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }



}
