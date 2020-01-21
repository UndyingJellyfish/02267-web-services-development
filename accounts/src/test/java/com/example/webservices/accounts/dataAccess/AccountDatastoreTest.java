package com.example.webservices.accounts.dataAccess;

import com.example.webservices.accounts.interfaces.IAccountDatastore;
import com.example.webservices.accounts.models.Account;
import com.example.webservices.accounts.models.Customer;
import com.example.webservices.accounts.models.Merchant;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountDatastoreTest {
    @Autowired
    private JpaAccountDatastore jpaAccountDatastore;
    private IAccountDatastore store;
    private Customer customer = new Customer("name","123");
    private Merchant merchant = new Merchant("name","123");


    /**
     * @author s164434
     * @throws DuplicateEntryException
     */
    @Before
    public void setup() throws DuplicateEntryException {
        this.store = jpaAccountDatastore;
        store.addAccount(customer);
        store.addAccount(merchant);
    }

    /**
     * @author s164434
     */
    @Test
    public void getCustomer() {
        try {
            Account cust = store.getCustomer(customer.getAccountId());
            assertEquals(customer,cust);
        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    /**
     * @author s164434
     */
    @Test
    public void getCustomerException() {
        try {
            store.getCustomer(UUID.randomUUID());
            fail();
        } catch (EntryNotFoundException ignored) {
        }
    }

    /**
     * @author s164434
     */
    @Test
    public void getMerchant() {
        try {
            Account merch = store.getMerchant(merchant.getAccountId());
            assertEquals(merchant,merch);
        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    /**
     * @author s164434
     */
    @Test
    public void getMerchantException() {
        try {
            store.getMerchant(UUID.randomUUID());
            fail();
        } catch (EntryNotFoundException ignored) {
        }
    }

    /**
     * @author s164424
     */
    @Test
    public void getAccount() {
        try {
            Account merch = store.getAccount(merchant.getAccountId());
            assertEquals(merchant, merch);
        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    /**
     * @author s164424
     */
    @Test
    public void getAccountException() {
        try {
            store.getAccount(UUID.randomUUID());
            fail();
        } catch (EntryNotFoundException ignored) {
        }
    }

    /**
     * @author s164424
     * @throws EntryNotFoundException
     */
    @Test
    public void deleteAccount() throws EntryNotFoundException {
        Account acc = store.getAccount(customer.getAccountId());
        assertNotNull(acc);
        store.deleteAccount(acc.getAccountId());
        try{
            store.getAccount(acc.getAccountId());
            fail();
        }catch(EntryNotFoundException ignored){        }


    }

    /**
     * @author s164424
     * @throws DuplicateEntryException
     * @throws EntryNotFoundException
     */
    @Test
    public void addAccount() throws DuplicateEntryException, EntryNotFoundException {
        Account acc = new Customer("new","321");
        try{
            store.getAccount(acc.getAccountId());
            fail();
        }catch(EntryNotFoundException ignored){}

        store.addAccount(acc);

        Account acc2 = store.getAccount(acc.getAccountId());
        assertEquals(acc,acc2);

    }
}