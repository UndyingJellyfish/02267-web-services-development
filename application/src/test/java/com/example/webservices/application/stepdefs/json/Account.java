package com.example.webservices.application.stepdefs.json;

import java.math.BigDecimal;

public class Account{
    private BigDecimal balance;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}

