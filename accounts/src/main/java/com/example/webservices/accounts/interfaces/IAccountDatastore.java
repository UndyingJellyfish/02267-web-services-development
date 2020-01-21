package com.example.webservices.accounts.interfaces;
import com.example.webservices.accounts.models.Account;
import com.example.webservices.accounts.models.Customer;
import com.example.webservices.accounts.models.Merchant;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;

import java.util.UUID;

public interface IAccountDatastore {
    Customer getCustomer(UUID customerId) throws EntryNotFoundException;
    <T extends Account> T addAccount(T account) throws DuplicateEntryException;
    Merchant getMerchant(UUID merchantId) throws EntryNotFoundException;
    Account getAccount(UUID accountId) throws EntryNotFoundException;
    void deleteAccount(UUID accountId) throws EntryNotFoundException;
    Account saveAccount(Account account);
}
