package com.example.webservices.accounts.services;

import com.example.webservices.accounts.AccountsApplication;
import com.example.webservices.accounts.interfaces.IAccountDatastore;
import com.example.webservices.accounts.models.Account;
import com.example.webservices.accounts.models.Customer;
import com.example.webservices.accounts.models.Merchant;
import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.AccountType;
import com.example.webservices.library.dataTransferObjects.ChangeNameDto;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IAccountService;
import com.example.webservices.library.interfaces.ITokenManager;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class  AccountService implements IAccountService {
    private IAccountDatastore accountDatastore;
    private ITokenManager tokenManager;

    public AccountService(IAccountDatastore accountDatastore, ITokenManager tokenManager) {
        this.accountDatastore = accountDatastore;
        this.tokenManager = tokenManager;
    }

    /**
     * @author s164424
     * {@inheritDoc}
     */
    @Override
    public void changeName(ChangeNameDto changeNameDto) throws EntryNotFoundException {
        Account account = this.accountDatastore.getAccount(changeNameDto.getAccountId());
        account.setName(changeNameDto.getNewName());
        this.accountDatastore.saveAccount(account);
    }

    /**
     * @author s164424
     * {@inheritDoc}
     */
    private AccountDto addAccount(Account account) throws DuplicateEntryException {
        account = this.accountDatastore.addAccount(account);
        return new AccountDto(account.getAccountId(), account.getName(), account.getBankAccountId(), account.getIdentifier(), account.getType());
    }

    /**
     * @author s164424
     * {@inheritDoc}
     */
    @Override
    public AccountDto addCustomer(SignupDto signupDto) throws DuplicateEntryException {
        Account account = new Customer(signupDto.getName(), signupDto.getIdentifier(), signupDto.getBankAccountId());
        return addAccount(account);
    }

    /**
     * @author s164424
     * {@inheritDoc}
     */
    @Override
    public AccountDto addMerchant(SignupDto signupDto) throws DuplicateEntryException {
        Account account = new Merchant(signupDto.getName(), signupDto.getIdentifier(), signupDto.getBankAccountId());
        return addAccount(account);
    }


    /**
     * @author s164424
     * {@inheritDoc}
     */
    public void delete(UUID accountId) throws EntryNotFoundException {
        this.accountDatastore.deleteAccount(accountId);
        this.tokenManager.retireAll(accountId);
    }

    /**
     * @author s164424
     * {@inheritDoc}
     */
    @Override
    public AccountDto getCustomer(UUID customerId) throws EntryNotFoundException {
        return getAccount(customerId);
    }

    /**
     * @author s164424
     * {@inheritDoc}
     */
    @Override
    public AccountDto getAccount(UUID id) throws EntryNotFoundException {
        Account acc = accountDatastore.getAccount(id);
        AccountType type = acc instanceof Merchant ? AccountType.MERCHANT : acc instanceof Customer ? AccountType.CUSTOMER : AccountType.NONE;
        return new AccountDto(acc.getAccountId(), acc.getName(),acc.getBankAccountId(), acc.getIdentifier(), type);
    }

    /**
     * @author s164424
     * {@inheritDoc}
     */
    @Override
    public AccountDto getMerchant(UUID merchantId) throws EntryNotFoundException {
        return getAccount(merchantId);
    }
}
