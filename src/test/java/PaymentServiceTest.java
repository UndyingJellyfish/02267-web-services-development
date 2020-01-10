import adaptors.LocalBankAdaptor;
import main.InMemoryDatastore;
import main.PaymentService;
import main.TokenManager;
import models.Customer;
import models.Merchant;
import models.Token;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class PaymentServiceTest {

    private PaymentService service;
    private Customer customer;
    private Merchant merchant;
    private Token token;

    @Before
    public void setup(){
        customer = new Customer("yoink");
        merchant = new Merchant("doink");
        InMemoryDatastore store = new InMemoryDatastore();
        TokenManager tokenManager = new TokenManager(store);
        service = new PaymentService(tokenManager, store, store, new LocalBankAdaptor());
        store.addAccount(customer);
        store.addAccount(merchant);
        token = tokenManager.RequestToken(customer);
    }

    private Exception excp;
    @Test
    public void negativeTransferAmount(){

        try{
            service.transfer(token.getTokenId(), merchant.getAccountId(), new BigDecimal(-23),"");
            fail();
        }
        catch(Exception e){
            excp = e;
        }
        assertNotNull(excp);
    }
}
