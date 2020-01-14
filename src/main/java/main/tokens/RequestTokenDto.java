package main.tokens;

import java.util.UUID;

public class RequestTokenDto {
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
