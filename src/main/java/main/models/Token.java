package main.models;

import java.util.Date;
import java.util.UUID;

public class Token {
    private UUID tokenId;
    private boolean used;
    private Date useDate = null;
    private Customer customer;

    public Token(Customer customer){
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

    public Date getUseDate() {
        return useDate;
    }

    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }

    public Customer getCustomer() {
        return customer;
    }
}
