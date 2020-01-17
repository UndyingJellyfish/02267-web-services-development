package com.example.webservices.application.transfers;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.TokenQuantityException;
import com.example.webservices.library.interfaces.IAccountService;
import com.example.webservices.library.interfaces.IBank;
import com.example.webservices.library.interfaces.IPaymentService;
import com.example.webservices.library.interfaces.ITokenManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class PaymentServiceTest {

    private IPaymentService service;
    private AccountDto merchant;
    private AccountDto customer;
    private UUID tokenId;
    private IBank bank = mock(IBank.class);
    private IAccountService accountService = mock(IAccountService.class);
    private ITokenManager tokenManager = mock(ITokenManager.class);

    public PaymentServiceTest(IPaymentService paymentService){
        this.service = paymentService;
    }

    @Before
    public void setup(){
        SignupDto customer = new SignupDto("yoink", "12345678", UUID.randomUUID().toString());
        SignupDto merchant = new SignupDto("doink", "12345678", UUID.randomUUID().toString());
        try {
            this.customer = accountService.addCustomer(customer);
            this.merchant = accountService.addCustomer(merchant);
        } catch (DuplicateEntryException e) {
            fail();
        }
        try {
            this.tokenId = tokenManager.RequestToken(this.customer.getAccountId());
        } catch (EntryNotFoundException | TokenQuantityException e) {
            fail();
        }
    }

    private Exception exception;
    @Test
    public void negativeTransferAmount(){

        try{
            service.transfer(tokenId, merchant.getAccountId(), new BigDecimal(-23),"");
            fail();
        }
        catch(Exception e){
            exception = e;
        }
        Assert.assertNotNull(exception);
    }
}
