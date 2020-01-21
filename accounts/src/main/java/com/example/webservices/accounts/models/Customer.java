package com.example.webservices.accounts.models;

import com.example.webservices.library.dataTransferObjects.AccountType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Customer")
public class Customer extends Account {

    private String cpr;

    public Customer(){
        super();
    }
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
        if (cpr == null || cpr.isEmpty()) {
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
