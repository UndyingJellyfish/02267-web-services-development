package com.example.webservices.library.dataTransferObjects;

import java.util.UUID;

public class TokenDto {
    private UUID tokenId;
    private boolean isUsed;
    private UUID customerId;

    public TokenDto(UUID tokenId, boolean isUsed){
        this.tokenId = tokenId;
        this.isUsed = isUsed;
    }

    public TokenDto(UUID tokenId, boolean isUsed, UUID customerId){
        this.tokenId = tokenId;
        this.isUsed = isUsed;
        setCustomerId(customerId);
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public UUID getTokenId() {
        return tokenId;
    }

    public void setTokenId(UUID tokenId) {
        this.tokenId = tokenId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }
}