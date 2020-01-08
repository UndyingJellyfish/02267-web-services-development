import main.ITokenManager;
import main.TokenManager;
import models.Customer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TokenManagerTest {

    private ITokenManager tokenManager;
    private Customer bob = new Customer("Bob");

    @Before
    public void Setup(){
        tokenManager = new TokenManager();
    }

    @Test
    public void RequestSingleToken(){
        tokenManager.RequestToken(bob);        ;
    }

    @Test
    public void RequestMultipleTokens(){

    }

    @Test
    public void RequestZeroTokens(){

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
