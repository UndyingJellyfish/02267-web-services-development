package com.example.webservices.application.dataAccess;

import com.example.webservices.application.exceptions.DuplicateEntryException;
import com.example.webservices.application.exceptions.EntryNotFoundException;
import com.example.webservices.library.models.Account;
import com.example.webservices.library.models.Customer;
import com.example.webservices.library.models.Merchant;

import java.util.UUID;

public interface IAccountDatastore {
    Customer getCustomer(UUID customerId) throws EntryNotFoundException;
    <T extends Account> T addAccount(T account) throws DuplicateEntryException;

    Merchant getMerchant(UUID merchantId) throws EntryNotFoundException;

    Account getAccount(UUID accountId) throws EntryNotFoundException;
}
