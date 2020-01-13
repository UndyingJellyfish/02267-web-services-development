import adaptors.RemoteBankAdaptor;
import dtu.ws.fastmoney.BankServiceException_Exception;
import main.InMemoryDatastore;
import main.UserService;
import models.Customer;
import models.Merchant;
import org.junit.After;
import org.junit.Before;

import java.math.BigDecimal;

import static org.junit.Assert.fail;

public class RemoteBankAdaptorTest {

    private RemoteBankAdaptor bank;
    private InMemoryDatastore datastore;
    private UserService userService;
    private Customer customer;
    private Merchant merchant;

    @Before
    public void Setup() {
        bank = new RemoteBankAdaptor();
        datastore = new InMemoryDatastore();
        userService = new UserService(datastore);
        customer = new Customer("Bob");
        merchant = new Merchant("Alice's Flowers");
        try {
            String customerAccountId =  bank.addAccount(customer, new BigDecimal("100.0"));
            customer.setBankAccountId(customerAccountId);
            String merchantAccountId = bank.addAccount(merchant, new BigDecimal("100.0"));
            customer.setBankAccountId(merchantAccountId);
        } catch (BankServiceException_Exception e) {
            fail();
        }
        userService.addAccount(merchant);
        userService.addAccount(customer);
    }

    @After
    public void teardown() {
        try {
            bank.retireAccount(customer);
            bank.retireAccount(merchant);
        } catch (BankServiceException_Exception e) {
            fail();
        }
    }


}
