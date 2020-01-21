package com.example.webservices.accounts.models;

import com.example.webservices.library.dataTransferObjects.AccountType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Merchant")
public class Merchant extends Account {

    private String cvr;

    public Merchant() {
        super();
    }

    public Merchant(String name, String cvr) {
        this(name, cvr, null);
    }

    public Merchant(String name, String cvr, String bankAccountId) {
        super(name);
        setCvr(cvr);
        setBankAccountId(bankAccountId);
    }

    public String getCvr() {
        return cvr;
    }

    private void setCvr(String cvr) {
        if (cvr == null || cvr.isEmpty()) {
            throw new IllegalArgumentException("provided cvr did not suffice cvr format ");
        }
        this.cvr = cvr;
    }

    @Override
    public String getIdentifier() {
        return this.getCvr();
    }

    @Override
    public AccountType getType() {
        return AccountType.MERCHANT;
    }
}
