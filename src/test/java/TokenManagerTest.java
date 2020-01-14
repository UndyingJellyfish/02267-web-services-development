import main.exceptions.DuplicateEntryException;
import main.models.Account;
import main.tokens.ITokenManager;
import main.exceptions.InvalidTokenException;
import main.exceptions.TokenException;
import main.dataAccess.InMemoryDatastore;
import main.tokens.TokenManager;
import main.models.Customer;
import main.models.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;


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
            fail();
        }
    }

    @Test
    public void RequestTokens(){
        List<Token> beforeTokens = tokenManager.GetTokens(custId);
        tokenManager.RequestTokens(custId,2);
        List<Token> afterTokens = tokenManager.GetTokens(custId);
        assertEquals(0, beforeTokens.size());
        assertEquals(2, afterTokens.size());
    }

    @Test
    public void RequestNoTokens(){
        List<Token> beforeTokens = tokenManager.GetTokens(custId);
        try{
            tokenManager.RequestTokens(custId,0);
        }catch(IllegalArgumentException e){        }
        List<Token> afterTokens = tokenManager.GetTokens(custId);
        assertEquals(beforeTokens.size(), afterTokens.size());
    }

    @Test
    public void RequestTooManyTokens(){
        List<Token> beforeTokens = tokenManager.GetTokens(custId);
        try{
            tokenManager.RequestTokens(custId,6);
        }catch(IllegalArgumentException e){        }
        List<Token> afterTokens = tokenManager.GetTokens(custId);
        assertEquals(beforeTokens.size(), afterTokens.size());
    }

    @Test
    public void RequestTokensWhenOverLimit(){
        tokenManager.RequestTokens(custId,2); // Any qty over 1
        List<Token> beforeTokens = tokenManager.GetTokens(custId);
        try{
            tokenManager.RequestTokens(custId,1);
            fail();
        }catch(IllegalArgumentException e){        }
        List<Token> afterTokens = tokenManager.GetTokens(custId);
        assertEquals(beforeTokens.size(), afterTokens.size());
    }

    @Test
    public void UseExistingToken() {
        List<Token> beforeTokens = tokenManager.RequestTokens(custId, 1);
        try{
            tokenManager.UseToken(beforeTokens.get(0).getTokenId());
        } catch (TokenException e){
            fail();
        }
        List<Token> afterTokens = tokenManager.GetTokens(custId);
        assertEquals(1, afterTokens.size());
        boolean isUsed = afterTokens.get(0).isUsed();
        assertTrue(isUsed);
    }

    @Test
    public void UseNotExistingToken() {
        UUID id = UUID.randomUUID();
        try{
            tokenManager.UseToken(id);
            fail();
        } catch (TokenException e){
            assertTrue(e instanceof  InvalidTokenException);
        }
    }


}
