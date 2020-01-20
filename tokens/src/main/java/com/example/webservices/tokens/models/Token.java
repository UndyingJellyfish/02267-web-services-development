package com.example.webservices.tokens.models;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Token {
    @Id
    private UUID tokenId;
    private boolean used;
    private Date useDate = null;
    private UUID customer;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Token)) {
            return super.equals(obj);
        }
        return Objects.equals(((Token) obj).tokenId, tokenId);
    }

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
