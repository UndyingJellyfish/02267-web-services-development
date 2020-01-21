package com.example.webservices.library.services;

import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.ChangeNameDto;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IAccountService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AccountMQService extends RabbitMQBaseClass implements IAccountService {

    public AccountMQService(RabbitTemplate rabbitTemplate, @Qualifier("accounts") DirectExchange exchange) {
        super(rabbitTemplate, exchange);
    }

    /**
     * @author s164410
     * @param signupDto contains the information required to sign up a new customer
     * @return
     */
    @Override
    public AccountDto addCustomer(SignupDto signupDto) {
        ResponseObject response = send(QUEUE_CUSTOMER_SIGNUP, signupDto);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
        return fromJson(response.getBody(), AccountDto.class);

    }

    /**
     * @author s164410
     * @param signupDto contains the information required to sign up a new customer
     * @return
     */
    @Override
    public AccountDto addMerchant(SignupDto signupDto) {
        ResponseObject response = send(QUEUE_MERCHANT_SIGNUP, signupDto);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
        return fromJson(response.getBody(), AccountDto.class);

    }

    /**
     * @author s164410
     * @param changeNameDto contains the information required to change the name of a user
     */
    @Override
    public void changeName(ChangeNameDto changeNameDto) {
        ResponseObject response = send(QUEUE_ACCOUNT_CHANGENAME, changeNameDto);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
    }

    /**
     * @author s164410
     * @param accountId id of the account to delete
     */
    @Override
    public void delete(UUID accountId) {
        ResponseObject response = send(QUEUE_ACCOUNT_DELETE, accountId);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
    }

    /**
     * @author s164410
     * @param customerId id of the customer to search for
     * @return
     */
    @Override
    public AccountDto getCustomer(UUID customerId) {
        return getAccount(customerId);
    }

    /**
     * @author s164410
     * @param id id of the account to search for
     * @return
     */
    @Override
    public AccountDto getAccount(UUID id) {
        ResponseObject response = send(QUEUE_ACCOUNT_GET, id);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
        return fromJson(response.getBody(), AccountDto.class);

    }

    /**
     * @author s164410
     * @param merchantId id of the merchant to search for
     * @return
     */
    @Override
    public AccountDto getMerchant(UUID merchantId) {
        return getAccount(merchantId);
    }
}
