package com.example.webservices.accounts.models;


import com.example.webservices.library.dataTransferObjects.AccountType;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Account")
public abstract class Account {

    @Id
    private UUID accountId;
    private String name;
    private String bankAccountId;


    public Account(){
        this.accountId = UUID.randomUUID();
    }
    public Account(String name){
        this();
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Name must be not null");
        }
        this.name = name;
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Account)) {
            return super.equals(obj);
        }
        return Objects.equals(((Account) obj).accountId, accountId);
    }
}
