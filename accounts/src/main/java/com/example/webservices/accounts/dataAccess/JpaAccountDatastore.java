package com.example.webservices.accounts.dataAccess;

import com.example.webservices.accounts.interfaces.AccountRepository;
import com.example.webservices.accounts.interfaces.IAccountDatastore;
import com.example.webservices.accounts.models.Account;
import com.example.webservices.accounts.models.Customer;
import com.example.webservices.accounts.models.Merchant;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JpaAccountDatastore implements IAccountDatastore {

    private final AccountRepository<Account> accountRepository;
    private final AccountRepository<Customer> customerRepository;
    private final AccountRepository<Merchant> merchantRepository;

    public JpaAccountDatastore(AccountRepository<Account> accountRepository, AccountRepository<Customer> customerRepository, AccountRepository<Merchant> merchantRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
    }

    private <T extends Account> T getAccount(AccountRepository<T> repository, UUID id) throws EntryNotFoundException {
        return repository.findById(id).orElseThrow(EntryNotFoundException::new);
    }

    private boolean exists(UUID id) {
        return accountRepository.findById(id).isPresent();
    }

    @Override
    public Customer getCustomer(UUID customerId) throws EntryNotFoundException {
        return getAccount(customerRepository, customerId);
    }

    @Override
    public <T extends Account> T addAccount(T account) throws DuplicateEntryException {
        if (exists(account.getAccountId())) {
            throw new DuplicateEntryException();
        }

        return accountRepository.save(account);
    }

    @Override
    public Merchant getMerchant(UUID merchantId) throws EntryNotFoundException {
        return getAccount(merchantRepository, merchantId);
    }

    @Override
    public Account getAccount(UUID accountId) throws EntryNotFoundException {
        return getAccount(accountRepository, accountId);
    }

    @Override
    public void deleteAccount(UUID accountId) throws EntryNotFoundException {
        if(!exists(accountId)){
            throw new EntryNotFoundException();
        }
        accountRepository.deleteById(accountId);
    }
}
