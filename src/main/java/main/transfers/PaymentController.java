package main.transfers;

import dtu.ws.fastmoney.BankServiceException_Exception;
import main.exceptions.TokenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.UUID;

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
        } catch (TokenException | IllegalArgumentException | BankServiceException_Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    public void RefundTransaction(@RequestBody TransactionDto transaction){
        try {
            paymentService.refund(transaction.getCustomerId(), transaction.getMerchantId(), transaction.getTokenId());
        } catch (TokenException |IllegalArgumentException| BankServiceException_Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }



}
