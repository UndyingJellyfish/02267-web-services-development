package com.example.webservices.application.transfers;

import com.example.webservices.application.models.Account;
import com.example.webservices.application.models.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionDto {
    private UUID transactionId;

    //public TransactionDto(){}
    private UUID tokenId;
    private UUID merchantId;
    private UUID customerId;
    private BigDecimal amount;
    private String description;

    private TransactionDto() {
        transactionId = UUID.randomUUID();
    }

    public static TransactionDto Create() {
        return new TransactionDto();
    }

    public TransactionDto(Transaction transaction) {
        transactionId = transaction.getTransactionId();
        tokenId = transaction.getToken().getTokenId();
        merchantId = transaction.getCreditor().getAccountId();
        Account debtor = transaction.getDebtor();
        if (debtor != null) {
            customerId = debtor.getAccountId();
        }
        amount = transaction.getAmount();
        description = null; // TODO: Why the fuck is there not a description in the transaction object but there is in the DTO???
        // TODO: Where the FUCK is transaction date???
    }

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

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }
}
