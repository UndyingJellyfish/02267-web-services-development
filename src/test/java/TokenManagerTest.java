import main.ITokenManager;
import main.TokenManager;
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
        tokenManager = new TokenManager();
    }

    @Test
    public void RequestSingleToken(){

        List<Token> beforeTokens = tokenManager.GetTokens(bob);
        tokenManager.RequestToken(bob);
        List<Token> afterTokens = tokenManager.GetTokens(bob);
        assertEquals(0, beforeTokens.size());
        assertEquals(1, afterTokens.size());

    }

    @Test
    public void RequestMultipleTokens(){
        List<Token> beforeTokens = tokenManager.GetTokens(bob);
        tokenManager.RequestTokens(bob,2);
        List<Token> afterTokens = tokenManager.GetTokens(bob);
        assertEquals(0, beforeTokens.size());
        assertEquals(2, afterTokens.size());
    }

    @Test
    public void RequestZeroTokens(){
        List<Token> beforeTokens = tokenManager.GetTokens(bob);
        try{
            tokenManager.RequestTokens(bob,0);
        }catch(IllegalArgumentException e){        }
        List<Token> afterTokens = tokenManager.GetTokens(bob);
        assertEquals(beforeTokens.size(), afterTokens.size());
    }

    @Test
    public void RequestMoreThanFiveTokens(){

    }

    @Test
    public void RequestTokensWhenOverLimit(){

    }


    @Test
    public void trueTest(){
        assertTrue(true);
    }
}
