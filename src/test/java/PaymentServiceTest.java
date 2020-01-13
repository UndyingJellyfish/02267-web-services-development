import exceptions.DuplicateEntryException;
import interfaces.IBank;
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
import static org.mockito.Mockito.mock;

public class PaymentServiceTest {

    private PaymentService service;
    private Customer customer;
    private Merchant merchant;
    private Token token;
    private IBank  bank = mock(IBank.class);

    @Before
    public void setup(){
        customer = new Customer("yoink","1");
        merchant = new Merchant("doink");
        InMemoryDatastore store = new InMemoryDatastore();
        TokenManager tokenManager = new TokenManager(store);
        service = new PaymentService(tokenManager, store, store, bank);
        try {
            store.addAccount(customer);
        } catch (DuplicateEntryException e) {
            fail();
        }
        try {
            store.addAccount(merchant);
        } catch (DuplicateEntryException e) {
            fail();
        }
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
