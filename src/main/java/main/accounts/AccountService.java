package main.accounts;

import main.exceptions.DuplicateEntryException;
import main.dataAccess.IAccountDatastore;
import main.models.Account;
import org.springframework.stereotype.Service;

@Service
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
