import exceptions.InvalidTokenException;
import main.InMemoryDatastore;
import models.Account;
import models.Customer;
import models.Token;
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
        Account acc = new Customer("bob");
        datastore.addAccount(acc);
    }

    @Test
    public void addAccountWithDuplicateUUID(){
        Account acc = new Customer("bob");
        datastore.addAccount(acc);

        try{
            datastore.addAccount(acc);
            fail();
        }
        catch(IllegalArgumentException e){        }
    }

    @Test
    public void getExistingCustomer(){
        Account acc = new Customer("bob");
        datastore.addAccount(acc);
        Account found = datastore.getCustomer(acc.getAccountId());
        assertEquals(acc, found);

    }

    @Test
    public void getNonExistingCustomer(){
        Customer cust = datastore.getCustomer(UUID.randomUUID());
        assertNull(cust);
    }

    @Test
    public void getExistingToken(){

        Customer customer = new Customer("bob");
        Token token = new Token(customer);
        datastore.assignTokens(customer,new ArrayList<Token>(){{add(token);}});
        Token found = null;
        try {
            found = datastore.getToken(token.getTokenId());
        } catch (InvalidTokenException e) {
            fail();
        }
        assertEquals(found, token);

    }

    @Test
    public void getNonExistingToken(){
        Customer customer = new Customer("bob");
        datastore.assignTokens(customer,new ArrayList<Token>(){});
        try{
            Token found = datastore.getToken(UUID.randomUUID());
            fail();
        }catch(InvalidTokenException e){ }

    }

    @Test
    public void getTokens(){
        Customer customer = new Customer("bob");
        List<Token> tokens =  new ArrayList<Token>(){{add(new Token(customer)); add(new Token(customer));}};
        datastore.assignTokens(customer,tokens);
        List<Token> foundTokens = datastore.getTokens(customer);
        assertThat(foundTokens, is(tokens));
    }

    @Test
    public void AssignTokens(){
        // Tested in getTokens
    }


    @Test
    public void AssignDuplicateToken(){
        // Assign a token which is already assigned to the customer

        Customer customer = new Customer("bob");
        Token token = new Token(customer);
        List<Token> tokens =  new ArrayList<Token>(){{add(token);}};

        datastore.assignTokens(customer,tokens);

        try{
            datastore.assignTokens(customer,tokens);
            fail();

        }catch(IllegalArgumentException e){}

        List<Token> afterTokens = datastore.getTokens(customer);
        assertThat(afterTokens, is(tokens));
        assertEquals(1, afterTokens.size());
    }

    @Test
    public void AssignTokensWithDuplicates(){
        // assign a set of tokens which contains duplicate id's

        Customer customer = new Customer("bob");
        Token token = new Token(customer);

        List<Token> beforeTokens = datastore.getTokens(customer);
        List<Token> tokens =  new ArrayList<Token>(){{add(token); add(token);}};

        try{
            datastore.assignTokens(customer,tokens);
            fail();
        }catch(IllegalArgumentException e){}

        List<Token> afterTokens = datastore.getTokens(customer);

        assertThat(afterTokens, is(beforeTokens));
        assertEquals(0, afterTokens.size());
    }

}
