package com.example.webservices.library.dataTransferObjects;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author s164395
 * provides a representation of an account object
 */
public class AccountDto implements Serializable {
    private UUID accountId;
    private String name;
    private String bankAccountId;
    private String identifier;
    private AccountType type;

    private AccountDto(){}

    public AccountDto(UUID accountId, String name, String identifier, AccountType type) {
        this(accountId, name, "", identifier, type);
    }

    public AccountDto(UUID accountId, String name, String bankAccountId, String identifier, AccountType type) {
        this.accountId = accountId;
        this.name = name;
        this.bankAccountId = bankAccountId;
        this.identifier = identifier;
        this.type = type;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
