package com.example.webservices.transactions;

import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.transactions.dataAccess.InMemoryTransactionDatastore;
import com.example.webservices.transactions.interfaces.ITransactionDatastore;
import com.example.webservices.transactions.models.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class TransactionDatastoreTest {

    private ITransactionDatastore store;
    private Transaction transaction;

    @Before
    public void setup(){
            this.store = new InMemoryTransactionDatastore();
            this.transaction = new Transaction(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal(10), UUID.randomUUID(), "description");
            this.store.addTransaction(transaction);
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


    //@Test
    //public void addTransaction() {
    //    //Tested in setup and getTransaction
    //}

    @Test
    public void getTransactions() {
        Transaction trans = new Transaction(transaction.getDebtorId(),
                transaction.getCreditorId(),
                transaction.getAmount(),
                UUID.randomUUID(),
                "description2");

        store.addTransaction(trans);
        List<Transaction> transactionsCred = store.getTransactions(transaction.getCreditorId());
        List<Transaction> transactionsDeb = store.getTransactions(transaction.getCreditorId());
        assertEquals(transactionsCred,transactionsDeb);
        assertEquals(2,transactionsCred.size());

    }
}
