package com.example.webservices.application.tokens;


import java.util.Date;
import java.util.UUID;

 class Token {
    private UUID tokenId;
    private boolean used;
    private Date useDate = null;
    private UUID customer;

    public Token() {}

    public Token(UUID customer){
        this.customer = customer;
        this.tokenId = UUID.randomUUID();
    }

    public UUID getTokenId() {
        return tokenId;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public UUID getCustomerId() {
        return customer;
    }
}
