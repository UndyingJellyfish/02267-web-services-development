package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.ChangeNameDto;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.UUID;

public interface IAccountService {
    /**
     * @param signupDto contains the information required to sign up a new customer
     * @return the newly registered account as a customer
     * @throws DuplicateEntryException
     */
    AccountDto addCustomer(SignupDto signupDto) throws DuplicateEntryException;

    /**
     * @param signupDto contains the information required to sign up a new customer
     * @return the newly registered account as a merchant
     * @throws DuplicateEntryException thrown when the user cannot be created due to a duplicate
     */
    AccountDto addMerchant(SignupDto signupDto) throws DuplicateEntryException;

    /**
     * @param changeNameDto contains the information required to change the name of a user
     * @throws EntryNotFoundException thrown when the specified account does not exist
     */
    void changeName(ChangeNameDto changeNameDto) throws EntryNotFoundException;

    /**
     * @param accountId id of the account to delete
     * @throws EntryNotFoundException thrown when the specified account does not exist
     */
    void delete(UUID accountId) throws EntryNotFoundException;

    /**
     * @param customerId id of the customer to search for
     * @return gets the requested account as a customer
     * @throws EntryNotFoundException thrown when the specified account does not exist
     */
    AccountDto getCustomer(UUID customerId) throws EntryNotFoundException;

    /**
     * @param id id of the account to search for
     * @return gets the requested account
     * @throws EntryNotFoundException thrown when the specified account does not exist
     */
    AccountDto getAccount(UUID id) throws EntryNotFoundException;

    /**
     * @param merchantId id of the merchant to search for
     * @return gets the requested account as a merchant
     * @throws EntryNotFoundException thrown when the specified account does not exist
     */
    AccountDto getMerchant(UUID merchantId) throws EntryNotFoundException;

}
