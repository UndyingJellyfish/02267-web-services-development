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
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
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
    public ResponseObject changeName(ChangeNameDto jsonString) {
        try {
            //ChangeNameDto changeNameDto = fromJson(jsonString, ChangeNameDto.class);
            this.accountService.changeName(jsonString);
            return success();

        } catch (EntryNotFoundException e) {
            return failure(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }
    @RabbitListener(queues = QUEUE_ACCOUNT_DELETE)
    public ResponseObject deleteAccount(UUID accountId) {
        try {
            this.accountService.delete(accountId);
            return success();

        } catch (EntryNotFoundException e) {
            return failure(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @RabbitListener(queues = QUEUE_ACCOUNT_GET)
    public ResponseObject getAccount(UUID accountId) {
        try {
            AccountDto account = this.accountService.getAccount(accountId);

            return success(account);

        } catch (EntryNotFoundException  e) {
            return failure(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @RabbitListener(queues = QUEUE_CUSTOMER_SIGNUP)
    public ResponseObject customerSignup(SignupDto jsonString) {
        try {
            //SignupDto signupDto = fromJson(jsonString, SignupDto.class);
            AccountDto account = this.accountService.addCustomer(jsonString);

            return success(account);

        } catch (Exception e) {
            return failure(e.getMessage());
        }

    }
    @RabbitListener(queues = QUEUE_MERCHANT_SIGNUP)
    public ResponseObject merchantSignup(SignupDto jsonString) {
        try {
            //SignupDto signupDto = fromJson(jsonString, SignupDto.class);
            AccountDto account = this.accountService.addMerchant(jsonString);

            return success(account);

        } catch (Exception e) {
            return failure(e.getMessage());
        }

    }

}
