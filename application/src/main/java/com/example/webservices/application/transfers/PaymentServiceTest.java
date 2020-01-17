package com.example.webservices.application.junit;

import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.interfaces.IBank;
import com.example.webservices.application.dataAccess.InMemoryDatastore;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.TokenQuantityException;
import com.example.webservices.payments.transfers.PaymentService;
import com.example.webservices.application.tokens.TokenManager;
import com.example.webservices.application.accounts.Customer;
import com.example.webservices.application.accounts.Merchant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.mock;

public class PaymentServiceTest {

    private PaymentService service;
    private Merchant merchant;
    private UUID tokenId;
    private IBank bank = mock(IBank.class);

    @Before
    public void setup(){
        Customer customer = new Customer("yoink", "12345678");
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
            tokenId = tokenManager.RequestToken(customer);
        } catch (EntryNotFoundException | TokenQuantityException e) {
            Assert.fail();
        }
    }

    private Exception exception;
    @Test
    public void negativeTransferAmount(){

        try{
            service.transfer(tokenId, merchant.getAccountId(), new BigDecimal(-23),"");
            Assert.fail();
        }
        catch(Exception e){
            exception = e;
        }
        Assert.assertNotNull(exception);
    }
}
