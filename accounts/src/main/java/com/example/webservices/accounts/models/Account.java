package com.example.webservices.accounts.models;


import com.example.webservices.library.dataTransferObjects.AccountType;
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import java.util.UUID;

public abstract class Account {

    private String name;
    private UUID accountId;
    private String bankAccountId;



    public Account(String name){
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Name must be not null");
        }
        this.name = name;
        this.accountId = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public abstract String getIdentifier();

    public abstract AccountType getType();
}
