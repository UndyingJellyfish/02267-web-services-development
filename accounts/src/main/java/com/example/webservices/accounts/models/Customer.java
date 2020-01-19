package com.example.webservices.accounts.models;

import com.example.webservices.library.dataTransferObjects.AccountType;

public class Customer extends Account {

    private String cpr;

    public Customer(String name, String cpr) {
        this(name, cpr, null);
    }

    public Customer(String name, String cpr, String bankAccountId){
        super(name);
        setCpr(cpr);
        setBankAccountId(bankAccountId);
    }

    public String getCpr() {
        return cpr;
    }

    private void setCpr(String cpr) {
        // TODO: Enforce stricter cpr format
        if (cpr == null || cpr.isEmpty()) {
        //if (cpr.length() == 10 || cpr.contains("-") && cpr.length() == 11) {
            throw new IllegalArgumentException("Provided cpr did not suffice cpr format ");
        }

        this.cpr = cpr;
    }

     @Override
     public String getIdentifier() {
         return this.getCpr();
     }

    @Override
    public AccountType getType() {
        return AccountType.CUSTOMER;
    }
}
