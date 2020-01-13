package main.controllers;

import dtu.ws.fastmoney.BankServiceException_Exception;
import main.exceptions.TokenException;
import main.services.PaymentService;
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

    public static class TransactionDto {
        public TransactionDto(){}
        private UUID tokenId;
        private UUID merchantId;
        private BigDecimal amount;
        private String description;

        public UUID getTokenId() {
            return tokenId;
        }

        public void setTokenId(UUID tokenId) {
            this.tokenId = tokenId;
        }

        public UUID getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(UUID merchantId) {
            this.merchantId = merchantId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void TransferMoney(@RequestBody TransactionDto transaction){
        try {
            paymentService.transfer(transaction.tokenId, transaction.merchantId, transaction.amount, transaction.description);
        } catch (TokenException | BankServiceException_Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
