package com.example.webservices.application.transfers;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.BankException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.InvalidTransferAmountException;
import com.example.webservices.library.exceptions.TokenException;
import com.example.webservices.library.interfaces.IPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        } catch (TokenException | IllegalArgumentException | EntryNotFoundException | BankException | InvalidTransferAmountException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    public void RefundTransaction(@RequestBody TransactionDto transaction){
        try {
            paymentService.refund(transaction);
        } catch (TokenException | IllegalArgumentException | EntryNotFoundException | BankException | InvalidTransferAmountException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }



}
