package com.example.webservices.application.accounts;

public class SignupDto{
    private String name;
    private String cpr;
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
}