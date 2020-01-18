package com.example.webservices.tokens.services;

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

import java.util.UUID;

@Service
public class AccountMQService extends RabbitMQBaseClass implements IAccountService {

    public AccountMQService(RabbitTemplate rabbitTemplate, @Qualifier("accounts") DirectExchange exchange) {
        super(rabbitTemplate, exchange);
    }

    @Override
    public AccountDto addCustomer(SignupDto signupDto) throws DuplicateEntryException {
        ResponseObject response = fromJson(send(QUEUE_CUSTOMER_SIGNUP, signupDto), ResponseObject.class);
        if(response.getStatusCode() == HttpStatus.OK){
            return fromJson(response.getBody(), AccountDto.class);
        }
        throw new DuplicateEntryException();
    }

    @Override
    public AccountDto addMerchant(SignupDto signupDto) throws DuplicateEntryException {
        ResponseObject response = fromJson(send(QUEUE_MERCHANT_SIGNUP, signupDto), ResponseObject.class);
        if(response.getStatusCode() == HttpStatus.OK){
            return fromJson(response.getBody(), AccountDto.class);
        }
        throw new DuplicateEntryException();
    }

    @Override
    public void changeName(ChangeNameDto changeNameDto) throws EntryNotFoundException {
        ResponseObject response = fromJson(send(QUEUE_ACCOUNT_CHANGENAME, changeNameDto), ResponseObject.class);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new EntryNotFoundException();
        }
    }

    @Override
    public void delete(UUID accountId) throws EntryNotFoundException {
        ResponseObject response = fromJson(send(QUEUE_ACCOUNT_DELETE, accountId), ResponseObject.class);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new EntryNotFoundException();
        }
    }

    @Override
    public AccountDto getCustomer(UUID customerId) throws EntryNotFoundException {
        return getAccount(customerId);
    }

    @Override
    public AccountDto getAccount(UUID id) throws EntryNotFoundException {
        ResponseObject response = fromJson(send(QUEUE_ACCOUNT_GET, id), ResponseObject.class);
        if(response.getStatusCode() == HttpStatus.OK){
            return fromJson(response.getBody(), AccountDto.class);
        }
        throw new EntryNotFoundException();
    }

    @Override
    public AccountDto getMerchant(UUID merchantId) throws EntryNotFoundException {
        return getAccount(merchantId);
    }
}