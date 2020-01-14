package main.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Transaction {

    private Date transactionDate;
    private Account creditor; // To
    private Account debtor; // From
    private BigDecimal amount;
    private Token token;
    private boolean isRefund = false;
    private UUID transactionId;

    private Transaction(){
        this.transactionId = UUID.randomUUID();
    }

    public Transaction(Account creditor, Account debtor, BigDecimal amount, Token token, Date transactionDate){
        this();
        this.creditor = creditor;
        this.debtor = debtor;
        this.amount = amount;
        this.token = token;
        this.transactionDate = transactionDate;
    }

    public Transaction(Account creditor, Account debtor, BigDecimal amount, Token token){
        this(creditor,debtor,amount,token, new Date());
    }

    public Transaction(Account creditor, Account debtor, BigDecimal amount, Token token, boolean isRefund){
        // A refund always happens today, right now
        this(creditor, debtor, amount, token, new Date());
        this.isRefund = isRefund;
    }

    /*No setters, values should be set in constructor*/
    public Date getTransactionDate() {
        return transactionDate;
    }

    public Account getCreditor() {
        return creditor;
    }



    public Account getDebtor() {
        return debtor;
    }


    public BigDecimal getAmount() {
        return amount;
    }


    public Token getToken() {
        return token;
    }

    public boolean isRefund() { return isRefund; }

    public UUID getTransactionId() {
        return this.transactionId;
    }

    public void anonymize() {
        this.debtor = null;
    }
}
