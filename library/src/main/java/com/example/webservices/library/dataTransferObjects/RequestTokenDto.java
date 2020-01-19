package com.example.webservices.library.dataTransferObjects;

import java.io.Serializable;
import java.util.UUID;

public class RequestTokenDto implements Serializable {
    private UUID customerId;
    private int amount;

    public RequestTokenDto(){}

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }
}
