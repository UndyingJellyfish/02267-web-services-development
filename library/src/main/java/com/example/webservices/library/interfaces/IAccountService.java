package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;

import java.util.UUID;

public interface IAccountService {
    AccountDto addCustomer(SignupDto signupDto) throws DuplicateEntryException;

    AccountDto addMerchant(SignupDto signupDto) throws DuplicateEntryException;

    void changeName(UUID accountId, String newName) throws EntryNotFoundException;

    void delete(UUID accountId) throws EntryNotFoundException;

    AccountDto getCustomer(UUID customerId) throws EntryNotFoundException;

    AccountDto getAccount(UUID id) throws EntryNotFoundException;

    AccountDto getMerchant(UUID merchantId) throws EntryNotFoundException;
}
