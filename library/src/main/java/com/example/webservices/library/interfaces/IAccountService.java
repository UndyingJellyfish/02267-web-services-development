package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;

import java.util.UUID;

public interface IAccountService {
    AccountDto getCustomer(UUID customerId) throws EntryNotFoundException;

    AccountDto getAccount(UUID id) throws EntryNotFoundException;

    AccountDto getMerchant(UUID merchantId)throws EntryNotFoundException;
}
