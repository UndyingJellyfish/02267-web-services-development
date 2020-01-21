package com.example.webservices.accounts.interfaces;
import com.example.webservices.accounts.models.Account;
import com.example.webservices.accounts.models.Customer;
import com.example.webservices.accounts.models.Merchant;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;

import java.util.UUID;

public interface IAccountDatastore {

    /**
     * @param customerId id of a the customer to fetch
     * @return the customer found in the database
     * @throws EntryNotFoundException thrown when the specified id does not match any database object
     */
    Customer getCustomer(UUID customerId) throws EntryNotFoundException;

    /**
     * @param account the account to add
     * @param <T> type of the account to add
     * @return the newly created account
     * @throws DuplicateEntryException thrown when the specified id already exists in the database
     */
    <T extends Account> T addAccount(T account) throws DuplicateEntryException;

    /**
     * @param merchantId id of a the merchant to fetch
     * @return the merchant found in the database
     * @throws EntryNotFoundException thrown when the specified id does not match any database object
     */
    Merchant getMerchant(UUID merchantId) throws EntryNotFoundException;

    /**
     * @param accountId id of a the account to fetch
     * @return the account found in the database
     * @throws EntryNotFoundException thrown when the specified id does not match any database object
     */
    Account getAccount(UUID accountId) throws EntryNotFoundException;

    /**
     * @param accountId id of the account to delete
     * @throws EntryNotFoundException thrown when the specified id does not match any database object
     */
    void deleteAccount(UUID accountId) throws EntryNotFoundException;

    /**
     * @param account the account to save in the database
     * @return the account saved in the database
     */
    Account saveAccount(Account account);
}
