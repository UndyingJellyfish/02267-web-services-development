package com.example.webservices.library;

import com.example.webservices.library.dataTransferObjects.*;
import com.example.webservices.library.services.AccountMQService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
    private <T> void setupFail(T ret, HttpStatus status){
        setup(status, ret);
    }
    private <T> void setupSuccess(T ret){
        setup(HttpStatus.OK, ret);
    }

    @Test
    public void addCustomer() {

        setupSuccess(customerAccountDto);
            AccountDto res = service.addCustomer(customerSignupDto);
            assertEquals(customerAccountDto.getAccountId(), res.getAccountId());


    }
    @Test
    public void addCustomerException() {

        setupFail(null, HttpStatus.CONFLICT);

        try {
            service.addCustomer(customerSignupDto);
            fail();
        }  catch (ResponseStatusException e) {
            assertEquals(HttpStatus.CONFLICT, e.getStatus());
        }
    }

    @Test
    public void addMerchant() {

        setupSuccess(merchantAccountDto);
        AccountDto res = service.addMerchant(merchantSignupDto);
        assertEquals(merchantAccountDto.getAccountId(), res.getAccountId());

    }

    @Test
    public void addMerchantException() {
        setupFail(null, HttpStatus.CONFLICT);
        try {
            service.addMerchant(merchantSignupDto);
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.CONFLICT, e.getStatus());
        }
    }

    @Test
    public void changeName() {
        setupSuccess(null);
        service.changeName(new ChangeNameDto( UUID.randomUUID(),"yeet"));
     }

    @Test
    public void changeNameException() {
        setupFail(null, HttpStatus.NOT_FOUND);
        try {
            service.changeName(new ChangeNameDto( UUID.randomUUID(),"yeet"));
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    public void delete() {
        setupSuccess(null);
        try {
            service.delete( UUID.randomUUID());
        } catch (ResponseStatusException e) {
            fail();
        }
    }

    @Test
    public void deleteException() {
        setupFail(null, HttpStatus.NOT_FOUND);
        try {
            service.delete( UUID.randomUUID());
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    public void getCustomer() {
        setupSuccess(customerAccountDto);
        AccountDto dto = service.getCustomer(customerAccountDto.getAccountId());
        assertEquals(customerAccountDto.getAccountId(), dto.getAccountId());

    }

    @Test
    public void getCustomerException() {
        setupFail("exception", HttpStatus.NOT_FOUND);
        try {
            service.getCustomer(customerAccountDto.getAccountId());
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    public void getAccount() {
        setupSuccess(customerAccountDto);
        AccountDto dto = service.getAccount(customerAccountDto.getAccountId());
        assertEquals(customerAccountDto.getAccountId(), dto.getAccountId());

    }

    @Test
    public void getAccountException() {
        setupFail("exception", HttpStatus.NOT_FOUND);
        try {
            service.getAccount(customerAccountDto.getAccountId());
            fail();
        } catch (ResponseStatusException e) {
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    public void getMerchant() {
        setupSuccess(merchantAccountDto);

        AccountDto dto = service.getMerchant(merchantAccountDto.getAccountId());
        assertEquals(merchantAccountDto.getAccountId(), dto.getAccountId());

    }
    @Test
    public void getMerchantException() {
        setupFail("exception", HttpStatus.NOT_FOUND);
        try {
            service.getCustomer(merchantAccountDto.getAccountId());
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }
}