package com.example.webservices.transactions.models;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="PayTransaction")
public class Transaction {
    @Id
    private UUID transactionId;

    private Date transactionDate;
    private UUID creditor; // To
    private UUID debtor; // From
    private BigDecimal amount;
    private UUID token;
    private boolean isRefund = false;
    private String description;

    public Transaction(){
        this.transactionId = UUID.randomUUID();
        this.isRefund = false;
    }

    public Transaction(UUID creditor, UUID debtor, BigDecimal amount, UUID token, String description, Date transactionDate) {
        this();
        this.creditor = creditor;
        this.debtor = debtor;
        this.amount = amount;
        this.token = token;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    public Transaction(UUID creditor, UUID debtor, BigDecimal amount, UUID token, String description) {
        this(creditor,debtor,amount,token, description, Calendar.getInstance().getTime());
    }

    public Transaction(UUID creditor, UUID debtor, BigDecimal amount, UUID token, String description, boolean isRefund) {
        this(creditor, debtor, amount, token, description, Calendar.getInstance().getTime());
        this.isRefund = isRefund;
    }
    public Transaction(UUID creditor, UUID debtor, BigDecimal amount, UUID token, String description, boolean isRefund, Date transactionDate) {
        this(creditor, debtor, amount, token, description, transactionDate);
        this.isRefund = isRefund;
    }


    /*No setters, values should be set in constructor*/
    public Date getTransactionDate() {
        return transactionDate;
    }

    public UUID getCreditorId() {
        return creditor;
    }



    public UUID getDebtorId() {
        return debtor;
    }


    public BigDecimal getAmount() {
        return amount;
    }


    public UUID getTokenId() {
        return token;
    }

    public boolean isRefund() { return isRefund; }

    public UUID getTransactionId() {
        return this.transactionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
