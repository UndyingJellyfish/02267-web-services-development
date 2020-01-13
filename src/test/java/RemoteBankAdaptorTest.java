import adaptors.RemoteBankAdaptor;
import dtu.ws.fastmoney.BankServiceException_Exception;
import models.Customer;
import models.Merchant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class RemoteBankAdaptorTest {

    private static final BigDecimal startingBalance = new BigDecimal("100.");

    private RemoteBankAdaptor bank;
    private Customer customer;
    private Merchant merchant;

    @Before
    public void Setup() {
        bank = new RemoteBankAdaptor();
        customer = new Customer("Bob Bobby", "1234567002");
        merchant = new Merchant("Alice's Flowers", "12312312");
        try {
            String customerAccountId = bank.addAccount(customer, startingBalance);
            customer.setBankAccountId(customerAccountId);
            String merchantAccountId = bank.addAccount(merchant, startingBalance);
            merchant.setBankAccountId(merchantAccountId);
        } catch (BankServiceException_Exception | ClassNotFoundException e) {
            fail();
        }
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

    private void CheckBalance(Customer customer, BigDecimal customerBalance, Merchant merchant, BigDecimal merchantBalance) {
        try {
            BigDecimal currentBalance = bank.getBalance(customer);
            assertEquals(customerBalance, currentBalance);
            currentBalance = bank.getBalance(merchant);
            assertEquals(merchantBalance, currentBalance);
        } catch (BankServiceException_Exception e) {
            fail();
        }
    }

    @Test
    public void TestBalanceWasSetup() {
        CheckBalance(customer, startingBalance, merchant, startingBalance);
    }

    @Test
    public void TestTransfer() {
        BigDecimal amount = new BigDecimal("50.0");
        try {
            CheckBalance(customer, startingBalance, merchant, startingBalance);
            bank.transferMoney(customer, merchant, amount, "Test transfer");
            CheckBalance(customer, startingBalance.subtract(amount), merchant, startingBalance.add(amount));
        } catch (BankServiceException_Exception e) {
            fail();
        }
    }
}
