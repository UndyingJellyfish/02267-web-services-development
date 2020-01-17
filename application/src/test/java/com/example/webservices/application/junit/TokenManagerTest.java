package com.example.webservices.application.junit;

import com.example.webservices.application.exceptions.*;
import com.example.webservices.application.models.Account;
import com.example.webservices.application.tokens.ITokenManager;
import com.example.webservices.application.dataAccess.InMemoryDatastore;
import com.example.webservices.application.tokens.TokenManager;
import com.example.webservices.application.models.Customer;
import com.example.webservices.application.models.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;


public class TokenManagerTest {

    private ITokenManager tokenManager;
    private UUID custId;

    @Before
    public void Setup(){
        InMemoryDatastore store = new InMemoryDatastore();
        tokenManager = new TokenManager(store,store);
        Account cust = new Customer("Bob","123");
        custId = cust.getAccountId();
        try {
            store.addAccount(cust);
        } catch (DuplicateEntryException e) {
            Assert.fail();
        }
    }

    @Test
    public void RequestTokens(){

        List<Token> beforeTokens = null;
        try {
            beforeTokens = tokenManager.GetTokens(custId);
            tokenManager.RequestTokens(custId,2);
            List<Token> afterTokens = tokenManager.GetTokens(custId);
            Assert.assertEquals(0, beforeTokens.size());
            Assert.assertEquals(2, afterTokens.size());
        } catch (EntryNotFoundException | TokenQuantityException e) {
            Assert.fail();
        }

    }

    @Test
    public void RequestNoTokens(){
        List<Token> beforeTokens = null;
        try {
            beforeTokens = tokenManager.GetTokens(custId);
            try{
                tokenManager.RequestTokens(custId,0);
            }catch(TokenQuantityException ignored){        }
            List<Token> afterTokens = tokenManager.GetTokens(custId);
            Assert.assertEquals(beforeTokens.size(), afterTokens.size());
        } catch (EntryNotFoundException e) {
            Assert.fail();
        }

    }

    @Test
    public void RequestTooManyTokens(){
        List<Token> beforeTokens = null;
        try {
            beforeTokens = tokenManager.GetTokens(custId);
            try{
                tokenManager.RequestTokens(custId,6);
            }catch(TokenQuantityException ignored){        }
            List<Token> afterTokens = tokenManager.GetTokens(custId);
            Assert.assertEquals(beforeTokens.size(), afterTokens.size());
        } catch (EntryNotFoundException e) {
            Assert.fail();
        }

    }

    @Test
    public void RequestTokensWhenOverLimit(){
        try {
            tokenManager.RequestTokens(custId,2); // Any qty over 1
            List<Token> beforeTokens = tokenManager.GetTokens(custId);
            try{
                tokenManager.RequestTokens(custId,1);
                Assert.fail();
            }catch(TokenQuantityException ignored){        }
            List<Token> afterTokens = tokenManager.GetTokens(custId);
            Assert.assertEquals(beforeTokens.size(), afterTokens.size());
        } catch (EntryNotFoundException | TokenQuantityException e) {
            Assert.fail();
        }

    }

    @Test
    public void UseExistingToken() {
        List<Token> beforeTokens = null;
        try {
            beforeTokens = tokenManager.RequestTokens(custId, 1);
            try{
                tokenManager.UseToken(beforeTokens.get(0).getTokenId());
            } catch (TokenException e){
                Assert.fail();
            }
            List<Token> afterTokens = tokenManager.GetTokens(custId);
            Assert.assertEquals(1, afterTokens.size());
            boolean isUsed = afterTokens.get(0).isUsed();
            Assert.assertTrue(isUsed);
        } catch (EntryNotFoundException | TokenQuantityException e) {
            Assert.fail();
        }

    }

    @Test
    public void UseNotExistingToken() {
        UUID id = UUID.randomUUID();
        try{
            tokenManager.UseToken(id);
            Assert.fail();
        } catch (TokenException e){
            Assert.assertTrue(e instanceof  InvalidTokenException);
        }
    }


}