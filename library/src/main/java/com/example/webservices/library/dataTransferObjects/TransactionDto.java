package com.example.webservices.library.dataTransferObjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class TransactionDto implements Serializable {
    private UUID transactionId;

    private UUID tokenId;
    private UUID creditor;
    private UUID debtor;
    private BigDecimal amount;
    private String description;
    private boolean isRefund;
    private Date transactionDate;

    public TransactionDto(UUID tokenId, UUID creditor, UUID debtor, BigDecimal amount, String description, Date transactionDate) {
        this(null, tokenId,creditor,debtor,amount,description,false,transactionDate);
    }

    public TransactionDto(UUID tokenId, UUID creditor, UUID debtor, BigDecimal amount, String description, boolean isRefund,Date transactionDate) {
        this(null, tokenId,creditor,debtor,amount,description,isRefund,transactionDate);
    }

    public TransactionDto(UUID transactionId, UUID tokenId, UUID creditor, UUID debtor, BigDecimal amount, String description, boolean isRefund, Date transactionDate) {
        this.transactionId = transactionId;
        this.tokenId = tokenId;
        this.creditor = creditor;
        this.debtor = debtor;
        this.amount = amount;
        this.description = description;
        this.isRefund = isRefund;
        this.transactionDate = transactionDate;
    }

    private TransactionDto(UUID uuid) {
        transactionId = uuid;
    }

    public static TransactionDto Create() {
        return new TransactionDto(UUID.randomUUID());
    }


    public UUID getTokenId() {
        return tokenId;
    }

    public void setTokenId(UUID tokenId) {
        this.tokenId = tokenId;
    }

    public UUID getCreditorId() {
        return creditor;
    }

    public void setCreditor(UUID creditor) {
        this.creditor = creditor;
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

    public void setDebtor(UUID debtor) {
        this.debtor = debtor;
    }

    public UUID getDebtorId() {
        return debtor;
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

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
