package com.example.webservices.tokens.services;

import com.example.webservices.library.dataTransferObjects.*;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;

import java.security.spec.ECField;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountMQServiceTest {

    private final AccountMQService service;
    RabbitTemplate template = mock(RabbitTemplate.class);
    DirectExchange exchange = mock(DirectExchange.class);
    SignupDto customerSignupDto = new SignupDto("bob","123","bankid");
    AccountDto customerAccountDto =  new AccountDto(UUID.randomUUID(),"bob","123", AccountType.CUSTOMER);
    SignupDto merchantSignupDto = new SignupDto("bob","123","bankid");
    AccountDto merchantAccountDto =  new AccountDto(UUID.randomUUID(),"bob","123", AccountType.MERCHANT);

    public AccountMQServiceTest(){
        service = new AccountMQService(template, exchange);
    }

    @Before
    public void setup(){
            when(exchange.getName()).thenReturn("account");
    }
    private <T> void setup(HttpStatus status, T ret){
        when(template.convertSendAndReceive(anyString(), anyString(), any(Message.class))).
                thenReturn(new ResponseObject(status, ret));
    }
    private <T> void setupFail(T ret){
        setup(HttpStatus.BAD_REQUEST, ret);
    }
    private <T> void setupSuccess(T ret){
        setup(HttpStatus.OK, ret);
    }

    @Test
    public void addCustomer() {

        setupSuccess(customerAccountDto);
        try {
            AccountDto res = service.addCustomer(customerSignupDto);
            assertEquals(customerAccountDto.getAccountId(), res.getAccountId());
        } catch (DuplicateEntryException | JsonProcessingException e) {
            fail();
        }


    }
    @Test
    public void addCustomerException() {

        setupFail(null);

        try {
            service.addCustomer(customerSignupDto);
            fail();
        } catch (DuplicateEntryException e) {
            assertNotNull(e);
        } catch (JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void addMerchant() {

        setupSuccess(merchantAccountDto);
        try {
            AccountDto res = service.addMerchant(merchantSignupDto);
            assertEquals(merchantAccountDto.getAccountId(), res.getAccountId());
        } catch (DuplicateEntryException | JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void addMerchantException() {
        setupFail(null);
        try {
            service.addMerchant(merchantSignupDto);
            fail();
        } catch (DuplicateEntryException e) {
            assertNotNull(e);
        } catch (JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void changeName() {
       setupSuccess(null);
        try {
            service.changeName(new ChangeNameDto( UUID.randomUUID(),"yeet"));
        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    @Test
    public void changeNameException() {
        setupFail(null);
        try {
            service.changeName(new ChangeNameDto( UUID.randomUUID(),"yeet"));
            fail();
        } catch (EntryNotFoundException ignored) {
        }
    }

    @Test
    public void delete() {
        setupSuccess(null);
        try {
            service.delete( UUID.randomUUID());
        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    @Test
    public void deleteException() {
        setupFail(null);
        try {
            service.delete( UUID.randomUUID());
            fail();
        } catch (EntryNotFoundException ignored) {
        }
    }

    @Test
    public void getCustomer() {
        setupSuccess(customerAccountDto);
        try {
            AccountDto dto = service.getCustomer(customerAccountDto.getAccountId());
            assertEquals(customerAccountDto.getAccountId(), dto.getAccountId());
        } catch (EntryNotFoundException | JsonProcessingException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getCustomerException() {
        setupFail(customerAccountDto);
        try {
            service.getCustomer(customerAccountDto.getAccountId());
            fail();
        } catch (EntryNotFoundException ignored) {        } catch (JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void getAccount() {
        setupSuccess(customerAccountDto);
        try {
            AccountDto dto = service.getAccount(customerAccountDto.getAccountId());
            assertEquals(customerAccountDto.getAccountId(), dto.getAccountId());
        } catch (EntryNotFoundException | JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void getAccountException() {
        setupFail(customerAccountDto);
        try {
            service.getAccount(customerAccountDto.getAccountId());
            fail();
        } catch (EntryNotFoundException ignored) {        } catch (JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void getMerchant() {
        setupSuccess(merchantAccountDto);
        try {
            AccountDto dto = service.getMerchant(merchantAccountDto.getAccountId());
            assertEquals(merchantAccountDto.getAccountId(), dto.getAccountId());
        } catch (EntryNotFoundException | JsonProcessingException e) {
            fail();
        }
    }
    @Test
    public void getMerchantException() {
        setupFail(merchantAccountDto);
        try {
            service.getCustomer(merchantAccountDto.getAccountId());
            fail();
        } catch (EntryNotFoundException ignored) {        } catch (JsonProcessingException e) {
            fail();
        }
    }
}