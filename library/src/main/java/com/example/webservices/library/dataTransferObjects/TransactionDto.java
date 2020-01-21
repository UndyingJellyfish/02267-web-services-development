package com.example.webservices.library.dataTransferObjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * @author s164395
 * provides a representation of a transaction object
 */
public class TransactionDto implements Serializable {
    private UUID transactionId;

    private UUID tokenId;
    private UUID creditorId;
    private UUID debtorId;
    private BigDecimal amount;
    private String description;
    private boolean isRefund;
    private Date transactionDate;

    private TransactionDto(){}


    public TransactionDto(UUID tokenId, UUID creditorId, UUID debtorId, BigDecimal amount, String description, Date transactionDate) {
        this(null, tokenId, creditorId, debtorId,amount,description,false,transactionDate);
    }

    public TransactionDto(UUID tokenId, UUID creditorId, UUID debtorId, BigDecimal amount, String description, boolean isRefund, Date transactionDate) {
        this(null, tokenId, creditorId, debtorId,amount,description,isRefund,transactionDate);
    }

    public TransactionDto(UUID transactionId, UUID tokenId, UUID creditorId, UUID debtorId, BigDecimal amount, String description, boolean isRefund, Date transactionDate) {
        this.transactionId = transactionId;
        this.tokenId = tokenId;
        this.creditorId = creditorId;
        this.debtorId = debtorId;
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
        return creditorId;
    }

    public void setCreditorId(UUID creditorId) {
        this.creditorId = creditorId;
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

    public void setDebtorId(UUID debtorId) {
        this.debtorId = debtorId;
    }

    public UUID getDebtorId() {
        return debtorId;
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
