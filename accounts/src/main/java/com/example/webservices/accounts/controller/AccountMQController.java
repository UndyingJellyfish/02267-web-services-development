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
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

    /**
     * @author s164398
     * @param changeNameDto {@inheritDoc}
     * @return publishes a response for the requested name change
     */
    @RabbitListener(queues = QUEUE_ACCOUNT_CHANGENAME)
    public ResponseObject changeName(ChangeNameDto changeNameDto) {
        try {
            this.accountService.changeName(changeNameDto);
            return success();
        } catch (EntryNotFoundException e) {
            return failure(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return error(e);
        }

    }

    /**
     * @author s164398
     * @param accountId id of the account to delete
     * @return publishes a response for the requested account deletion
     */
    @RabbitListener(queues = QUEUE_ACCOUNT_DELETE)
    public ResponseObject deleteAccount(UUID accountId) {
        try {
            this.accountService.delete(accountId);
            return success();
        } catch (EntryNotFoundException e) {
            return failure(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return error(e);
        }
    }

    /**
     * @author s164398
     * @param accountId id of the account to get
     * @return publishes a response for the requested account
     */
    @RabbitListener(queues = QUEUE_ACCOUNT_GET)
    public ResponseObject getAccount(UUID accountId) {
        try {
            AccountDto account = this.accountService.getAccount(accountId);
            return success(account);
        } catch (EntryNotFoundException  e) {
            return failure(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return error(e);
        }
    }

    /**
     * @author s164398
     * @param signupDto {@inheritDoc}
     * @return publishes a response for the newly signed up customer
     */
    @RabbitListener(queues = QUEUE_CUSTOMER_SIGNUP)
    public ResponseObject customerSignup(SignupDto signupDto) {
        try {
            AccountDto account = this.accountService.addCustomer(signupDto);
            return success(account);
        } catch (DuplicateEntryException e) {
            return failure(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e){
            return error(e);
        }

    }

    /**
     * @author s164398
     * @param signupDto {@inheritDoc}
     * @return publishes a response for the newly signed up merchant
     */
    @RabbitListener(queues = QUEUE_MERCHANT_SIGNUP)
    public ResponseObject merchantSignup(SignupDto signupDto) {
        try {
            AccountDto account = this.accountService.addMerchant(signupDto);
            return success(account);
        } catch (DuplicateEntryException e) {
            return failure(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e){
            return error(e);
        }
    }
}
