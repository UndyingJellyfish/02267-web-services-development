package com.example.webservices.application.transfers;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.*;
import com.example.webservices.library.interfaces.IPaymentService;
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

    /**
     * @author s164410
     * */
    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public void TransferMoney(@RequestBody TransactionDto transaction){
        try {
            paymentService.transfer(transaction);
        } catch (ResponseStatusException e){
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * @author s164395
     * */
    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    public void RefundTransaction(@RequestBody UUID transactionId){
        try {
            paymentService.refund(transactionId);
        } catch (ResponseStatusException e){
            throw e;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
