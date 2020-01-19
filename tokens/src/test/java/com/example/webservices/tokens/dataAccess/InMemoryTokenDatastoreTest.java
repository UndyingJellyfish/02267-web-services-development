package com.example.webservices.tokens.dataAccess;

import com.example.webservices.library.exceptions.TokenException;
import com.example.webservices.tokens.models.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

public class InMemoryTokenDatastoreTest {

    private InMemoryTokenDatastore datastore;
    public UUID customerId = UUID.randomUUID();

    @Before
    public void Setup(){
        datastore = new InMemoryTokenDatastore();
    }




    @Test
    public void getExistingToken(){
        List<Token> tokens = datastore.assignTokens(customerId,1);
        Token found;
        try {
            found = datastore.getToken(tokens.get(0).getTokenId());
        } catch (TokenException e) {
            Assert.fail();
            return;
        }
        assertEquals(found, found);

    }

    @Test
    public void getNonExistingToken(){
        //List<Token> tokens = datastore.assignTokens(customerId,1);
        try{
            Token found = datastore.getToken(UUID.randomUUID());
            Assert.fail();
        }catch(TokenException ignored){ }

    }

    @Test
    public void getTokens(){
        List<Token> tokens = datastore.assignTokens(customerId,3);
        List<Token> foundTokens = datastore.getTokens(customerId);
        Assert.assertThat(foundTokens, is(tokens));
    }





}

