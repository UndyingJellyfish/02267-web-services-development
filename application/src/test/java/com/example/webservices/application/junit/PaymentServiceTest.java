package com.example.webservices.application.junit;

import com.example.webservices.application.exceptions.DuplicateEntryException;
import com.example.webservices.application.bank.IBank;
import com.example.webservices.application.dataAccess.InMemoryDatastore;
import com.example.webservices.application.exceptions.EntryNotFoundException;
import com.example.webservices.application.exceptions.TokenQuantityException;
import com.example.webservices.application.transfers.PaymentService;
import com.example.webservices.application.tokens.TokenManager;
import com.example.webservices.library.models.Customer;
import com.example.webservices.library.models.Merchant;
import com.example.webservices.library.models.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;

public class PaymentServiceTest {

    private PaymentService service;
    private Customer customer;
    private Merchant merchant;
    private Token token;
    private IBank  bank = mock(IBank.class);

    @Before
    public void setup(){
        customer = new Customer("yoink","12345678");
        merchant = new Merchant("doink", "12345678");
        InMemoryDatastore store = new InMemoryDatastore();
        TokenManager tokenManager = new TokenManager(store,store);
        service = new PaymentService(tokenManager, store, store, bank);
        try {
            store.addAccount(customer);
        } catch (DuplicateEntryException e) {
            Assert.fail();
        }
        try {
            store.addAccount(merchant);
        } catch (DuplicateEntryException e) {
            Assert.fail();
        }
        try {
            token = tokenManager.RequestToken(customer);
        } catch (EntryNotFoundException | TokenQuantityException e) {
            Assert.fail();
        }
    }

    private Exception excp;
    @Test
    public void negativeTransferAmount(){

        try{
            service.transfer(token.getTokenId(), merchant.getAccountId(), new BigDecimal(-23),"");
            Assert.fail();
        }
        catch(Exception e){
            excp = e;
        }
        Assert.assertNotNull(excp);
    }
}
