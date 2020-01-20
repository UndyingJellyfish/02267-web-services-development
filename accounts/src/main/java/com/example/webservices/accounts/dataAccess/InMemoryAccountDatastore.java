package com.example.webservices.accounts.dataAccess;
import com.example.webservices.accounts.interfaces.IAccountDatastore;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import org.springframework.stereotype.Service;
import com.example.webservices.accounts.models.*;

import java.util.*;

//@Service
public class InMemoryAccountDatastore implements IAccountDatastore {
    private List<Account> accounts = new ArrayList<>();


    @Override
    public Customer getCustomer(UUID customerId) throws EntryNotFoundException {
        return (Customer)accounts.stream().filter(a -> a instanceof Customer && a.getAccountId().equals(customerId)).findFirst().orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public Merchant getMerchant(UUID merchantId) throws EntryNotFoundException {
        return (Merchant) accounts.stream().filter(a -> a instanceof Merchant && a.getAccountId().equals(merchantId)).findFirst().orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public Account getAccount(UUID accountId) throws EntryNotFoundException {
        return accounts.stream().filter(a -> a.getAccountId().equals(accountId)).findFirst().orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public void deleteAccount(UUID accountId) throws EntryNotFoundException {
            Account acc = this.getAccount(accountId);
            this.accounts.remove(acc);

    }

    @Override
    public Account saveAccount(Account account) {
        return account;
    }

    @Override
    public <T extends Account> T addAccount(T account) throws DuplicateEntryException {
        if(accounts.stream().anyMatch(a -> a.getAccountId().equals(account.getAccountId())
                || (account instanceof Customer && a instanceof Customer)
                && ((Customer) a).getCpr().equals(((Customer) account).getCpr()))){
            throw new DuplicateEntryException();
        }
        accounts.add(account);
        return account;
    }


}
