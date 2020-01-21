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
     */
    @Before
    public void setup() {
        this.store = jpaAccountDatastore;
        try {
            store.addAccount(customer);
            store.addAccount(merchant);
        } catch (DuplicateEntryException e) {
            fail(e.getMessage());
        }
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
     */
    @Test
    public void deleteAccount() {
        Account acc = null;
        try {
            acc = store.getAccount(customer.getAccountId());
        } catch (EntryNotFoundException e) {
            fail(e.getMessage());
        }
        assertNotNull(acc);
        try {
            store.deleteAccount(acc.getAccountId());
        } catch (EntryNotFoundException e) {
            fail(e.getMessage());
        }
        try{
            store.getAccount(acc.getAccountId());
            fail();
        }catch(EntryNotFoundException ignored){
            // expected behaviour, it is deleted, cannot be fetched
        }
    }

    /**
     * @author s164424
     * @throws DuplicateEntryException when the account already exists
     */
    @Test
    public void addAccount() throws DuplicateEntryException {
        Account acc = new Customer("new","321");
        try{
            store.getAccount(acc.getAccountId());
            fail();
        } catch(EntryNotFoundException ignored){}

        store.addAccount(acc);

        Account acc2 = null;
        try {
            acc2 = store.getAccount(acc.getAccountId());
        } catch (EntryNotFoundException e) {
            fail(e.getMessage());
        }
        assertEquals(acc,acc2);
    }
}