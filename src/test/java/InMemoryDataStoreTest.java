import main.dataAccess.IAccountDatastore;
import main.exceptions.EntryNotFoundException;
import main.exceptions.TokenException;
import main.exceptions.DuplicateEntryException;
import main.dataAccess.InMemoryDatastore;
import main.models.Account;
import main.models.Customer;
import main.models.Token;
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
            fail();
        }
    }

    @Test
    public void addAccountWithDuplicateUUID(){
        Account acc = new Customer("bob","123");
        try {
            datastore.addAccount(acc);
        } catch (DuplicateEntryException e) {
            fail();
        }
        try{
            datastore.addAccount(acc);
            fail();
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
            fail();
        }

        try{
            datastore.addAccount(acc2);
            fail();
        }
        catch(DuplicateEntryException e){        }
    }

    @Test
    public void getExistingCustomer(){
        Account acc = new Customer("bob","123");
        try {
            datastore.addAccount(acc);
        } catch (DuplicateEntryException e) {
            fail();
        }
        Account found = null;
        try {
            found = datastore.getCustomer(acc.getAccountId());
        } catch (EntryNotFoundException e) {
            fail();
        }
        assertEquals(acc, found);

    }

    @Test
    public void getNonExistingCustomer(){
        try {
             datastore.getCustomer(UUID.randomUUID());
            fail();
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
            fail();
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
            fail();
        }catch(TokenException e){ }

    }

    @Test
    public void getTokens(){
        Customer customer = new Customer("bob","123");
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

        Customer customer = new Customer("bob","123");
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

        Customer customer = new Customer("bob","123");
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

    @Test
    public void GetAccount(){
        Customer customer = new Customer("Bob", "12345");
        try {
            datastore.addAccount(customer);
        } catch (DuplicateEntryException e) {
            fail();
        }

        Account account = null;
        try {
            account = datastore.getAccount(customer.getAccountId());
        } catch (EntryNotFoundException e) {
            fail();
        }

        assertEquals(customer.getAccountId(), account.getAccountId());
    }

}
