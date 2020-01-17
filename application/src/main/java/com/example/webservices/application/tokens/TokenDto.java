package com.example.webservices.application.tokens;

import java.util.UUID;

public class TokenDto {
    private UUID tokenId;
    private boolean isUsed;

    public TokenDto(UUID tokenId, boolean isUsed){
        this.tokenId = tokenId;
        this.isUsed = isUsed;
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
}
