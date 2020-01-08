package models;

import java.util.Date;

public class Transaction {

    private Date transactionDate;
    private Account creditor; // To
    private Account debtor; // From
    private Double amount;
    private Token token;

    public Transaction(Account creditor, Account debtor, Double amount, Token token, Date transactionDate){
        this.creditor = creditor;
        this.debtor = debtor;
        this.amount = amount;
        this.token = token;
        this.transactionDate = transactionDate;
    }

    public Transaction(Account creditor, Account debtor, Double amount, Token token){
        Date transactionDate = new Date();
        this.creditor = creditor;
        this.debtor = debtor;
        this.amount = amount;
        this.token = token;
        this.transactionDate = transactionDate;

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


    public Double getAmount() {
        return amount;
    }


    public Token getToken() {
        return token;
    }

}
