package com.example.webservices.library.dataTransferObjects;

import java.io.Serializable;
import java.util.UUID;

/**
 * contains the information required by services to initialize a name change for a user
 */
public class ChangeNameDto implements Serializable {

    private ChangeNameDto(){}

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
