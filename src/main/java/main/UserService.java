package main;

import interfaces.IAccountDatastore;
import models.Account;
import models.Customer;
import models.Merchant;

public class UserService {
    private IAccountDatastore accountDatastore;

    public UserService(IAccountDatastore accountDatastore) {
        this.accountDatastore = accountDatastore;
    }

    public <T extends Account> T addAccount(T account){
        return this.accountDatastore.addAccount(account);
    }


}
