package com.example.webservices.application.accounts;

public class SignupDto{
    private String name;
    private String identifier;
    private String bankAccountId;
    //public SignupDto(){}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String accountId) {
        this.bankAccountId = accountId;
    }
}