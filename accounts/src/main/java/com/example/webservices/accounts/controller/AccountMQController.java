package com.example.webservices.accounts.controller;

import com.example.webservices.accounts.interfaces.IAccountDatastore;
import com.example.webservices.accounts.models.Account;
import com.example.webservices.accounts.models.Customer;
import com.example.webservices.accounts.models.Merchant;
import com.example.webservices.library.RabbitHelper;
import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.*;
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


    @RabbitListener(queues = QUEUE_ACCOUNT_CHANGENAME)
    public String changeName(String jsonString){
        try {
            ChangeNameDto changeNameDto = fromJson(jsonString, ChangeNameDto.class);
            this.accountService.changeName(changeNameDto);
            return success();

        } catch (EntryNotFoundException e) {
            return failure(e.getMessage());
        }

    }
    @RabbitListener(queues = QUEUE_ACCOUNT_DELETE)
    public String deleteAccount(String jsonString){
        try {
            UUID accountId = fromJson(jsonString, UUID.class);
            this.accountService.delete(accountId);

            return success();

        } catch (EntryNotFoundException e) {
            return failure(e.getMessage());
        }

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
            SignupDto signupDto = fromJson(jsonString, SignupDto.class);
            AccountDto account = this.accountService.addCustomer(signupDto);

            return success(account);

        } catch (DuplicateEntryException e) {
            return failure(e.getMessage());
        }

    }
    @RabbitListener(queues = QUEUE_MERCHANT_SIGNUP)
    public String merchantSignup(String jsonString){
        try {
            SignupDto signupDto = fromJson(jsonString, SignupDto.class);
            AccountDto account = this.accountService.addMerchant(signupDto);

            return success(account);

        } catch (DuplicateEntryException e) {
            return failure(e.getMessage());
        }

    }

}
