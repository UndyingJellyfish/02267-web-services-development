import main.exceptions.DuplicateEntryException;
import main.bank.IBank;
import main.dataAccess.InMemoryDatastore;
import main.transfers.PaymentService;
import main.tokens.TokenManager;
import main.models.Customer;
import main.models.Merchant;
import main.models.Token;
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
        customer = new Customer("yoink","12345678");
        merchant = new Merchant("doink", "12345678");
        InMemoryDatastore store = new InMemoryDatastore();
        TokenManager tokenManager = new TokenManager(store,store);
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
