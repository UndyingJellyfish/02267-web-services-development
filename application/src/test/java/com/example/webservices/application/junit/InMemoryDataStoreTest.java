package com.example.webservices.application.junit;

import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.TokenException;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.application.dataAccess.InMemoryDatastore;
import com.example.webservices.application.accounts.Account;
import com.example.webservices.application.accounts.Customer;
import com.example.webservices.application.tokens.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class InMemoryDataStoreTest {

    private InMemoryDatastore datastore = new InMemoryDatastore();

    @Before
    public void Setup(){
        datastore = new InMemoryDatastore();
    }

    @Test
    public void addAccount(){
        Account acc = new Customer("bob","123");
        try {
            datastore.addAccount(acc);
        } catch (DuplicateEntryException e) {
            Assert.fail();
        }
    }

    @Test
    public void addAccountWithDuplicateUUID(){
        Account acc = new Customer("bob","123");
        try {
            datastore.addAccount(acc);
        } catch (DuplicateEntryException e) {
            Assert.fail();
        }
        try{
            datastore.addAccount(acc);
            Assert.fail();
        }
        catch( DuplicateEntryException e){        }
    }

    @Test
    public void addAccountWithDuplicateCpr(){
        Account acc = new Customer("alice","123");
        Account acc2 = new Customer("bob","123");

        try {
            datastore.addAccount(acc);
        } catch (DuplicateEntryException e) {
            Assert.fail();
        }

        try{
            datastore.addAccount(acc2);
            Assert.fail();
        }
        catch(DuplicateEntryException e){        }
    }

    @Test
    public void getExistingCustomer(){
        Account acc = new Customer("bob","123");
        try {
            datastore.addAccount(acc);
        } catch (DuplicateEntryException e) {
            Assert.fail();
        }
        Account found = null;
        try {
            found = datastore.getCustomer(acc.getAccountId());
        } catch (EntryNotFoundException e) {
            Assert.fail();
        }
        assertEquals(acc, found);

    }

    @Test
    public void getNonExistingCustomer(){
        try {
             datastore.getCustomer(UUID.randomUUID());
            Assert.fail();
        } catch (EntryNotFoundException e) {
            //success
        }
    }

    @Test
    public void getExistingToken(){

        Customer customer = new Customer("bob","123");
        Token token = new Token(customer);
        datastore.assignTokens(customer,new ArrayList<Token>(){{add(token);}});
        Token found;
        try {
            found = datastore.getToken(token.getTokenId());
        } catch (TokenException e) {
            Assert.fail();
            return;
        }
        assertEquals(found, token);

    }

    @Test
    public void getNonExistingToken(){
        Customer customer = new Customer("bob","123");
        datastore.assignTokens(customer,new ArrayList<Token>(){});
        try{
            Token found = datastore.getToken(UUID.randomUUID());
            Assert.fail();
        }catch(TokenException e){ }

    }

    @Test
    public void getTokens(){
        Customer customer = new Customer("bob","123");
        List<Token> tokens =  new ArrayList<Token>(){{add(new Token(customer)); add(new Token(customer));}};
        datastore.assignTokens(customer,tokens);
        List<Token> foundTokens = datastore.getTokens(customer.getAccountId());
        Assert.assertThat(foundTokens, is(tokens));
    }

    @Test
    public void AssignTokens(){
        // Tested in getTokens
    }


    @Test
    public void AssignDuplicateToken(){
        // Assign a token which is already assigned to the customer

        Customer customer = new Customer("bob","123");
        Token token = new Token(customer);
        List<Token> tokens =  new ArrayList<Token>(){{add(token);}};

        datastore.assignTokens(customer,tokens);

        try{
            datastore.assignTokens(customer,tokens);
            Assert.fail();

        }catch(IllegalArgumentException e){}

        List<Token> afterTokens = datastore.getTokens(customer.getAccountId());
        Assert.assertThat(afterTokens, is(tokens));
        Assert.assertEquals(1, afterTokens.size());
    }

    @Test
    public void AssignTokensWithDuplicates(){
        // assign a set of tokens which contains duplicate id's

        Customer customer = new Customer("bob","123");
        Token token = new Token(customer);

        List<Token> beforeTokens = datastore.getTokens(customer.getAccountId());
        List<Token> tokens =  new ArrayList<Token>(){{add(token); add(token);}};

        try{
            datastore.assignTokens(customer,tokens);
            Assert.fail();
        }catch(IllegalArgumentException e){}

        List<Token> afterTokens = datastore.getTokens(customer.getAccountId());

        Assert.assertThat(afterTokens, is(beforeTokens));
        Assert.assertEquals(0, afterTokens.size());
    }

    @Test
    public void GetAccount(){
        Customer customer = new Customer("Bob", "12345");
        try {
            datastore.addAccount(customer);
        } catch (DuplicateEntryException e) {
            Assert.fail();
        }

        Account account = null;
        try {
            account = datastore.getAccount(customer.getAccountId());
        } catch (EntryNotFoundException e) {
            Assert.fail();
        }

        assertEquals(customer.getAccountId(), account.getAccountId());
    }

}
