package com.example.webservices.accounts.controller;

import com.example.webservices.accounts.interfaces.IAccountDatastore;
import com.example.webservices.accounts.models.Account;
import com.example.webservices.accounts.models.Customer;
import com.example.webservices.accounts.models.Merchant;
import com.example.webservices.library.RabbitHelper;
import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.AccountType;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

public class AccountMQController extends RabbitHelper {

    private static final String QUEUE_ACCOUNT_GET = "account.get";
    private static final String QUEUE_CUSTOMER_GET = "account.get.customer";
    private static final String QUEUE_MERCHANT_GET = "account.get.merchant";

    private final IAccountDatastore accountDatastore;

    public AccountMQController(IAccountDatastore accountDatastore) {
        super();
        this.accountDatastore = accountDatastore;
    }

    @RabbitListener(queues = QUEUE_ACCOUNT_GET)
    public String getAccount(String jsonString){
        UUID accountId = fromJson(jsonString, UUID.class);

        Account account;
        try {
            account = this.accountDatastore.getAccount(accountId);

            return success(createDto(account));

        } catch (EntryNotFoundException e) {
            return failure(e.getMessage());
        }

    }

   /* @RabbitListener(queues = QUEUE_CUSTOMER_GET)
    public String getCustomer(String jsonString){
        UUID accountId = fromJson(jsonString, UUID.class);

        Customer account;
        try {
            account = this.accountDatastore.getCustomer(accountId);
        } catch (EntryNotFoundException e) {
            return failure(e.getMessage());
        }

        return success(createDto(account));
    }
    @RabbitListener(queues = QUEUE_MERCHANT_GET)
    public String getMerchant(String jsonString){
        UUID accountId = fromJson(jsonString, UUID.class);

        Merchant account;
        try {
            account = this.accountDatastore.getMerchant(accountId);
        } catch (EntryNotFoundException e) {
            return failure(e.getMessage());
        }

        return success(createDto(account));
    }*/


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
