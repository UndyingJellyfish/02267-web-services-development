package com.example.webservices.application.accounts;

import com.example.webservices.application.exceptions.DuplicateEntryException;
import com.example.webservices.application.dataAccess.IAccountDatastore;
import com.example.webservices.application.exceptions.EntryNotFoundException;
import com.example.webservices.application.tokens.ITokenManager;
import com.example.webservices.application.models.Account;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class  AccountService {
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
}
