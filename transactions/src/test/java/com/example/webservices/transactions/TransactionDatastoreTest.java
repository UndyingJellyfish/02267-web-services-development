package com.example.webservices.transactions;

import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.ITransactionService;
import com.example.webservices.transactions.dataAccess.JpaTransactionDatastore;
import com.example.webservices.transactions.interfaces.ITransactionDatastore;
import com.example.webservices.transactions.models.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/** @author s164410 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionDatastoreTest {

    @Autowired
    private ITransactionDatastore transactionDatastore;

    private ITransactionDatastore store;
    private Transaction transaction;

    @Before
    public void setup(){
        this.store = transactionDatastore;
        this.transaction = new Transaction(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal(10), UUID.randomUUID(), "description");
        try {
            this.store.addTransaction(transaction);
        } catch (DuplicateEntryException e) {
            fail();
        }
    }



    @Test
    public void getTransaction() {
        try {
            Transaction trans = store.getTransaction(transaction.getTransactionId());
            assertNotNull(trans);
            assertEquals(transaction.getTokenId(), trans.getTokenId());
            assertEquals(transaction.getAmount(), trans.getAmount());
        } catch (EntryNotFoundException e) {
            fail();
        }

    }

    @Test
    public void getTransactionException() {
        try {
            store.getTransaction(UUID.randomUUID());
            fail();
        } catch (EntryNotFoundException ignored) {        }

    }


    @Test
    public void getTransactions() {
        Transaction trans = new Transaction(transaction.getDebtorId(),
                transaction.getCreditorId(),
                transaction.getAmount(),
                UUID.randomUUID(),
                "description2");

        try {
            store.addTransaction(trans);
        } catch (DuplicateEntryException e) {
            fail();
        }
        List<Transaction> transactionsCred = store.getTransactions(transaction.getCreditorId());
        List<Transaction> transactionsDeb = store.getTransactions(transaction.getDebtorId());
        assertEquals(transactionsCred, transactionsDeb);
        assertEquals(2,transactionsCred.size());

    }
}
