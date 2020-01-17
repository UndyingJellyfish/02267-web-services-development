package com.example.webservices.application.accounts;


import java.util.UUID;

abstract class Account {

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
}
