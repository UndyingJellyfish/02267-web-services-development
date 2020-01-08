import main.*;
import models.Customer;
import models.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


public class TokenManagerTest {

    private ITokenManager tokenManager;
    private Customer bob = new Customer("Bob");

    @Before
    public void Setup(){
        tokenManager = new TokenManager(new InMemoryDatastore());
    }

    @Test
    public void RequestTokens(){
        List<Token> beforeTokens = tokenManager.GetTokens(bob);
        tokenManager.RequestTokens(bob,2);
        List<Token> afterTokens = tokenManager.GetTokens(bob);
        assertEquals(0, beforeTokens.size());
        assertEquals(2, afterTokens.size());
    }

    @Test
    public void RequestNoTokens(){
        List<Token> beforeTokens = tokenManager.GetTokens(bob);
        try{
            tokenManager.RequestTokens(bob,0);
        }catch(IllegalArgumentException e){        }
        List<Token> afterTokens = tokenManager.GetTokens(bob);
        assertEquals(beforeTokens.size(), afterTokens.size());
    }

    @Test
    public void RequestTooManyTokens(){
        List<Token> beforeTokens = tokenManager.GetTokens(bob);
        try{
            tokenManager.RequestTokens(bob,6);
        }catch(IllegalArgumentException e){        }
        List<Token> afterTokens = tokenManager.GetTokens(bob);
        assertEquals(beforeTokens.size(), afterTokens.size());
    }

    @Test
    public void RequestTokensWhenOverLimit(){
        tokenManager.RequestTokens(bob,2); // Any qty over 1
        List<Token> beforeTokens = tokenManager.GetTokens(bob);
        try{
            tokenManager.RequestTokens(bob,1);
        }catch(IllegalArgumentException e){        }
        List<Token> afterTokens = tokenManager.GetTokens(bob);
        assertEquals(beforeTokens.size(), afterTokens.size());
    }


}