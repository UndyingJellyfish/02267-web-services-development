package com.example.webservices.accounts.controller;

import com.example.webservices.accounts.interfaces.IAccountDatastore;
import com.example.webservices.accounts.models.Account;
import com.example.webservices.accounts.models.Customer;
import com.example.webservices.accounts.models.Merchant;
import com.example.webservices.library.RabbitHelper;
import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.AccountType;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IAccountService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountMQController extends RabbitHelper {


    private final IAccountService accountService;

    public AccountMQController(IAccountService accountService) {
        super();
        this.accountService = accountService;
    }

    @RabbitListener(queues = QUEUE_ACCOUNT_GET)
    public String getAccount(String jsonString){
        try {
            UUID accountId = fromJson(jsonString, UUID.class);
            AccountDto account = this.accountService.getAccount(accountId);

            return success(account);

        } catch (EntryNotFoundException e) {
            return failure(e.getMessage());
        }

    }

    @RabbitListener(queues = QUEUE_CUSTOMER_SIGNUP)
    public String customerSignup(String jsonString){
        try {
            SignupDto accountId = fromJson(jsonString, SignupDto.class);
            AccountDto account = this.accountService.addCustomer(accountId);

            return success(account);

        } catch (DuplicateEntryException e) {
            return failure(e.getMessage());
        }

    }


    private AccountDto createDto(Account account) {
        // TODO: Add AccountType to the Account Object, and make the Customer and Merchant Constructors set the field.
        if(account instanceof Customer){
            return new AccountDto(account.getAccountId(), account.getName(), account.getBankAccountId(), account.getIdentifier(), AccountType.CUSTOMER);
        }
        if(account instanceof Merchant){
            return new AccountDto(account.getAccountId(), account.getName(), account.getBankAccountId(), account.getIdentifier(), AccountType.MERCHANT);
        }
        throw new RuntimeException("This should not happen");
    }

}
