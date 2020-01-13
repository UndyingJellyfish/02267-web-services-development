package main;

import exceptions.DuplicateEntryException;
import interfaces.IAccountDatastore;
import models.Account;
import models.Customer;
import models.Merchant;

public class AccountService {
    private IAccountDatastore accountDatastore;

    public AccountService(IAccountDatastore accountDatastore) {
        this.accountDatastore = accountDatastore;
    }

    public <T extends Account> T addAccount(T account) throws DuplicateEntryException
    {
        return this.accountDatastore.addAccount(account);
    }


}
