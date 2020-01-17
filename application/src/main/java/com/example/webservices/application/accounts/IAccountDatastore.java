package com.example.webservices.application.accounts;
import com.example.webservices.application.accounts.Account;
import com.example.webservices.application.accounts.Customer;
import com.example.webservices.application.accounts.Merchant;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;

import java.util.UUID;

public interface IAccountDatastore {
    Customer getCustomer(UUID customerId) throws EntryNotFoundException;
    <T extends Account> T addAccount(T account) throws DuplicateEntryException;

    Merchant getMerchant(UUID merchantId) throws EntryNotFoundException;

    Account getAccount(UUID accountId) throws EntryNotFoundException;

    void deleteAccount(UUID accountId) throws EntryNotFoundException;
}
