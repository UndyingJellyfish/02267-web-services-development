package com.example.webservices.library.dataTransferObjects;

public class SignupDto{
    private String name;
    private String cpr;
    private String bankAccountId;
    //public SignupDto(){}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String accountId) {
        this.bankAccountId = accountId;
    }
}