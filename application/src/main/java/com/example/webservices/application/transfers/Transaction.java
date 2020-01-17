package com.example.webservices.application.transfers;


import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

 class Transaction {

    private Date transactionDate;
    private UUID creditor; // To
    private UUID debtor; // From
    private BigDecimal amount;
    private UUID token;
    private boolean isRefund = false;
    private UUID transaction;

    private Transaction(){
        this.transaction = UUID.randomUUID();
    }

    public Transaction(UUID creditor, UUID debtor, BigDecimal amount, UUID token, Date transactionDate){
        this();
        this.creditor = creditor;
        this.debtor = debtor;
        this.amount = amount;
        this.token = token;
        this.transactionDate = transactionDate;
    }

    public Transaction(UUID creditor, UUID debtor, BigDecimal amount, UUID token){
        this(creditor,debtor,amount,token, new Date());
    }

    public Transaction(UUID creditor, UUID debtor, BigDecimal amount, UUID token, boolean isRefund){
        // A refund always happens today, right now
        this(creditor, debtor, amount, token, new Date());
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

    public UUID getTransaction() {
        return this.transaction;
    }
}