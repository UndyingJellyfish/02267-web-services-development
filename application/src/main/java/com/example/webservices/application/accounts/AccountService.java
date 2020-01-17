package com.example.webservices.application.accounts;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.AccountType;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IAccountService;
import com.example.webservices.library.interfaces.ITokenManager;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
class  AccountService implements IAccountService {
    private IAccountDatastore accountDatastore;
    private ITokenManager tokenManager;

    public AccountService(IAccountDatastore accountDatastore, ITokenManager tokenManager) {
        this.accountDatastore = accountDatastore;
        this.tokenManager = tokenManager;
    }


    @Override
    public AccountDto addCustomer(SignupDto signupDto) throws DuplicateEntryException {
        Customer customer = new Customer(signupDto.getName(),signupDto.getIdentifier(),signupDto.getBankAccountId());
        Account account = this.accountDatastore.addAccount(customer);
        AccountDto dto = new AccountDto(account.getAccountId(), account.getName(), account.getBankAccountId(), account.getIdentifier(), AccountType.CUSTOMER);
        return dto;
    }

    @Override
    public AccountDto addMerchant(SignupDto signupDto) throws DuplicateEntryException {
        Merchant merchant = new Merchant(signupDto.getName(),signupDto.getIdentifier(),signupDto.getBankAccountId());
        Account account = this.accountDatastore.addAccount(merchant);
        AccountDto dto = new AccountDto(account.getAccountId(), account.getName(), account.getBankAccountId(), account.getIdentifier(), AccountType.CUSTOMER);
        return dto;
    }

    @Override
    public void changeName(UUID accountId, String newName) throws EntryNotFoundException {
        this.accountDatastore.getAccount(accountId).setName(newName);
    }

    @Override
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
