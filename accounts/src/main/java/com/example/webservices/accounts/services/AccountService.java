package com.example.webservices.accounts.services;

import com.example.webservices.accounts.AccountsApplication;
import com.example.webservices.accounts.interfaces.IAccountDatastore;
import com.example.webservices.accounts.models.Account;
import com.example.webservices.accounts.models.Customer;
import com.example.webservices.accounts.models.Merchant;
import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.AccountType;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IAccountService;
import com.example.webservices.library.interfaces.ITokenManager;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class  AccountService implements IAccountService {
    private IAccountDatastore accountDatastore;
    private ITokenManager tokenManager;

    public AccountService(IAccountDatastore accountDatastore, ITokenManager tokenManager) {
        this.accountDatastore = accountDatastore;
        this.tokenManager = tokenManager;
    }

    public <T extends Account> T addAccount(T account) throws DuplicateEntryException
    {
        return this.accountDatastore.addAccount(account);
    }


    public void changeName(UUID accountId, String newName) throws EntryNotFoundException {
        this.accountDatastore.getAccount(accountId).setName(newName);
    }

    public void delete(UUID accountId) throws EntryNotFoundException {
        this.accountDatastore.deleteAccount(accountId);
        this.tokenManager.retireAll(accountId);
    }

    @Override
    public AccountDto getCustomer(UUID customerId) throws EntryNotFoundException {
        Customer cust = accountDatastore.getCustomer(customerId);
        return new AccountDto(cust.getAccountId(), cust.getName(), cust.getBankAccountId(), cust.getCpr(), AccountType.CUSTOMER);
    }

    @Override
    public AccountDto getAccount(UUID id) throws EntryNotFoundException {
        Account acc = accountDatastore.getAccount(id);
        return new AccountDto(acc.getAccountId(), acc.getName(),acc.getBankAccountId(), acc.getIdentifier(), AccountType.NONE);
    }

    @Override
    public AccountDto getMerchant(UUID merchantId) throws EntryNotFoundException {
        Merchant merch = accountDatastore.getMerchant(merchantId);
        return new AccountDto(merch.getAccountId(), merch.getName(), merch.getBankAccountId(), merch.getCvr(), AccountType.MERCHANT);
    }
}
