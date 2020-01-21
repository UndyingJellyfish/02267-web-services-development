package com.example.webservices.library.dataTransferObjects;

import java.io.Serializable;

/**
 * @author s164398
 * contains the information required by services to sign a new user up for DTUPay
 */
public class SignupDto implements Serializable {
    private String name;
    private String identifier;
    private String bankAccountId;

    private SignupDto(){}

    public SignupDto(String name, String identifier, String bankAccountId){
        this.name = name;
        this.identifier = identifier;
        this.bankAccountId = bankAccountId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String accountId) {
        this.bankAccountId = accountId;
    }
}