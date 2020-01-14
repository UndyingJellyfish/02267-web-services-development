package com.example.webservices.application.accounts;

import com.example.webservices.application.exceptions.DuplicateEntryException;
import com.example.webservices.application.dataAccess.IAccountDatastore;
import com.example.webservices.library.models.Account;
import org.springframework.stereotype.Service;

@Service
public class  AccountService {
    private IAccountDatastore accountDatastore;

    public AccountService(IAccountDatastore accountDatastore) {
        this.accountDatastore = accountDatastore;
    }

    public <T extends Account> T addAccount(T account) throws DuplicateEntryException
    {
        return this.accountDatastore.addAccount(account);
    }


}
