package com.example.webservices.tokens.dataAccess;

import com.example.webservices.library.exceptions.TokenException;
import com.example.webservices.tokens.TokensApplication;
import com.example.webservices.tokens.interfaces.ITokenDatastore;
import com.example.webservices.tokens.models.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

/** @author s164395 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TokenDatastoreTest {


    private ITokenDatastore datastore;
    @Autowired
    private ITokenDatastore tokenDatastore;
    public UUID customerId = UUID.randomUUID();

    @Before
    public void Setup(){
        datastore = tokenDatastore;
    }


    @Test
    public void getExistingToken(){
        List<Token> tokens = datastore.generateAndAssignTokens(customerId,1);
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
        try{
            Token found = datastore.getToken(UUID.randomUUID());
            Assert.fail();
        }catch(TokenException ignored){ }

    }

    @Test
    public void getTokens(){
        List<Token> tokens = datastore.generateAndAssignTokens(customerId,3);
        List<Token> foundTokens = datastore.getTokens(customerId);
        Assert.assertThat(foundTokens, is(tokens));
    }





}

