package main.services;

import main.interfaces.IAccountDatastore;
import models.Account;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private IAccountDatastore accountDatastore;

    public UserService(IAccountDatastore accountDatastore) {
        this.accountDatastore = accountDatastore;
    }

    public <T extends Account> T addAccount(T account){
        return this.accountDatastore.addAccount(account);
    }


}
