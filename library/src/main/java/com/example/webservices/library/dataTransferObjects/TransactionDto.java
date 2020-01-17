package com.example.webservices.library.dataTransferObjects;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionDto {
    private UUID transactionId;

    private UUID tokenId;
    private UUID merchantId;
    private UUID customerId;
    private BigDecimal amount;
    private String description;
    private boolean isRefund;

    public TransactionDto(UUID tokenId, UUID merchantId, UUID customerId, BigDecimal amount, String description, boolean isRefund) {
        this.tokenId = tokenId;
        this.merchantId = merchantId;
        this.customerId = customerId;
        this.amount = amount;
        this.description = description;
        this.isRefund = isRefund;
    }

    public TransactionDto(UUID transactionId, UUID tokenId, UUID merchantId, UUID customerId, BigDecimal amount, String description, boolean isRefund) {
        this.transactionId = transactionId;
        this.tokenId = tokenId;
        this.merchantId = merchantId;
        this.customerId = customerId;
        this.amount = amount;
        this.description = description;
        this.isRefund = isRefund;
    }

    private TransactionDto(UUID uuid) {
        transactionId = uuid;
    }

    public static TransactionDto Create() {
        return new TransactionDto(UUID.randomUUID());
    }

/*
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
 */

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

    public boolean isRefund() {
        return isRefund;
    }

    public void setRefund(boolean refund) {
        isRefund = refund;
    }
}
