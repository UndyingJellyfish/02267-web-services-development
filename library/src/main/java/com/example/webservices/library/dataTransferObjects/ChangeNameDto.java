package com.example.webservices.library.dataTransferObjects;

import java.io.Serializable;
import java.util.UUID;

public class ChangeNameDto implements Serializable {

    public ChangeNameDto(UUID accountId, String newName){

        this.accountId = accountId;
        this.newName = newName;
    }
    private UUID accountId;
    private String newName;

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }
}
