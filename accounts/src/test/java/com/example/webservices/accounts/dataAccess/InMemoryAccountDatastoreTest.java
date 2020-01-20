package com.example.webservices.accounts.dataAccess;

import com.example.webservices.accounts.interfaces.IAccountDatastore;
import com.example.webservices.accounts.models.Account;
import com.example.webservices.accounts.models.Customer;
import com.example.webservices.accounts.models.Merchant;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import org.codehaus.groovy.runtime.MethodRankHelper;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.UUID;

import static org.junit.Assert.*;

public class InMemoryAccountDatastoreTest {
    private IAccountDatastore store = new InMemoryAccountDatastore();
    private Customer customer = new Customer("name","123");
    private Merchant merchant = new Merchant("name","123");

    @Before
    public void setup() throws DuplicateEntryException {
        store.addAccount(customer);
        store.addAccount(merchant);
    }

    @Test
    public void getCustomer() {
        try {
            Account cust = store.getCustomer(customer.getAccountId());
            assertEquals(customer,cust);
        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    @Test
    public void getCustomerException() {
        try {
            store.getCustomer(UUID.randomUUID());
            fail();
        } catch (EntryNotFoundException ignored) {
        }
    }

    @Test
    public void getMerchant() {
        try {
            Account merch = store.getMerchant(merchant.getAccountId());
            assertEquals(merchant,merch);
        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    @Test
    public void getMerchantException() {
        try {
            store.getMerchant(UUID.randomUUID());
            fail();
        } catch (EntryNotFoundException ignored) {
        }
    }

    @Test
    public void getAccount() {
        try {
            Account merch = store.getAccount(merchant.getAccountId());
            assertEquals(merchant, merch);
        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    @Test
    public void getAccountException() {
        try {
            store.getAccount(UUID.randomUUID());
            fail();
        } catch (EntryNotFoundException ignored) {
        }
    }

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