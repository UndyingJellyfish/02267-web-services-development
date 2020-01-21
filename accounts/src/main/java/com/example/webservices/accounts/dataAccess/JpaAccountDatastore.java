package com.example.webservices.accounts.dataAccess;

import com.example.webservices.accounts.interfaces.IAccountDatastore;
import com.example.webservices.accounts.models.Account;
import com.example.webservices.accounts.models.Customer;
import com.example.webservices.accounts.models.Merchant;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

public class JpaAccountDatastore implements IAccountDatastore {

    private final AccountRepository<Account> accountRepository;
    private final AccountRepository<Customer> customerRepository;
    private final AccountRepository<Merchant> merchantRepository;

    public JpaAccountDatastore(AccountRepository<Account> accountRepository, AccountRepository<Customer> customerRepository, AccountRepository<Merchant> merchantRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
    }

    /**
     * @author s164407
     * @param repository
     * @param id
     * @param <T>
     * @return
     * @throws EntryNotFoundException
     */
    private <T extends Account> T getAccount(AccountRepository<T> repository, UUID id) throws EntryNotFoundException {
        return repository.findById(id).orElseThrow(EntryNotFoundException::new);
    }

    /**
     * @author s164407
     * @param id
     * @return
     */
    private boolean exists(UUID id) {
        return accountRepository.findById(id).isPresent();
    }

    /**
     * @author s164407
     * @param customerId
     * @return
     * @throws EntryNotFoundException
     */
    @Override
    public Customer getCustomer(UUID customerId) throws EntryNotFoundException {
        return getAccount(customerRepository, customerId);
    }

    /**
     * @author s164407
     * @param account
     * @param <T>
     * @return
     * @throws DuplicateEntryException
     */
    @Override
    public <T extends Account> T addAccount(T account) throws DuplicateEntryException {
        if (exists(account.getAccountId())) {
            throw new DuplicateEntryException();
        }

        return accountRepository.save(account);
    }

    /**
     * @author s164407
     * @param merchantId
     * @return
     * @throws EntryNotFoundException
     */
    @Override
    public Merchant getMerchant(UUID merchantId) throws EntryNotFoundException {
        return getAccount(merchantRepository, merchantId);
    }

    /**
     * @author s164407
     * @param accountId
     * @return
     * @throws EntryNotFoundException
     */
    @Override
    public Account getAccount(UUID accountId) throws EntryNotFoundException {
        return getAccount(accountRepository, accountId);
    }

    /**
     * @author s164407
     * @param accountId
     * @throws EntryNotFoundException
     */
    @Override
    public void deleteAccount(UUID accountId) throws EntryNotFoundException {
        if(!exists(accountId)){
            throw new EntryNotFoundException();
        }
        accountRepository.deleteById(accountId);
    }

    /**
     * @author s164407
     * @param account
     * @return
     */
    @Override
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }
}
