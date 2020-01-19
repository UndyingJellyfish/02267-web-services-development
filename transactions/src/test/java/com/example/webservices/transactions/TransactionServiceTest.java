package com.example.webservices.transactions;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.transactions.interfaces.ITransactionDatastore;
import com.example.webservices.transactions.models.Transaction;
import com.example.webservices.transactions.services.TransactionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;


public class TransactionServiceTest {

    private TransactionService transactionService;
    private ITransactionDatastore transactionDatastore = mock(ITransactionDatastore.class);
    private List<Transaction> transactions;
    private UUID creditorId = UUID.randomUUID();
    private UUID debtorId = UUID.randomUUID();
    private UUID tokenId = UUID.randomUUID();
    private BigDecimal amount = new BigDecimal(100);
    private String description = "Transaction";
    private Date date = new Date();
    private Transaction transaction;

    public TransactionServiceTest() {
        this.transactionService = new TransactionService(transactionDatastore);
    }

    @Before
    public void setUp() throws Exception {
        this.transaction = new Transaction(this.creditorId,
                this.debtorId,
                this.amount,
                this.tokenId,
                this.description,
                this.date);
        this.transactions = new ArrayList<Transaction>(){
            {add(transaction);}
        };
        when(transactionDatastore.getTransactions(creditorId)).thenReturn(transactions);
    }

    @After
    public void tearDown() {
        transactions = null;
        reset(this.transactionDatastore);
    }

    @Test
    public void getTransactions() {
        List<TransactionDto> transactionDtos = this.transactionService.getTransactions(creditorId);
        List<UUID> actual = transactionDtos.stream().map(TransactionDto::getTransactionId).collect(Collectors.toList());
        List<UUID> excepted = transactions.stream().map(Transaction::getTransactionId).collect(Collectors.toList());
        assertEquals(excepted, actual);
    }

    private boolean Match(TransactionDto transaction) {
        return transaction.getTokenId().equals(tokenId) &&
                transaction.getCreditorId().equals(creditorId) &&
                transaction.getDebtorId().equals(debtorId) &&
                transaction.getAmount().equals(amount) &&
                transaction.getDescription().equals(description) &&
                !transaction.isRefund();
    }

    private boolean Match(Transaction transaction) {
        return transaction.getTokenId().equals(tokenId) &&
                transaction.getCreditorId().equals(creditorId) &&
                transaction.getDebtorId().equals(debtorId) &&
                transaction.getAmount().equals(amount) &&
                transaction.getDescription().equals(description) &&
                !transaction.isRefund();
    }

    @Test
    public void addTransaction() {
       TransactionDto dto = new TransactionDto(
                tokenId,
                creditorId,
                debtorId,
                amount,
                description,
                false,
                date
            );

       UUID transactionId = UUID.randomUUID();
       when(transactionDatastore
               .addTransaction(argThat(this::Match)))
               .thenReturn(transactionId);
       UUID actualTransactionId = transactionService.addTransaction(dto);
       assertEquals(transactionId, actualTransactionId);

       verify(transactionDatastore, times(1))
               .addTransaction(argThat(this::Match));
   }

    @Test
    public void getTransactionByTokenId() {
        try {
            when(transactionDatastore
                .getTransactionByTokenId(eq(tokenId)))
                .thenReturn(transaction);
            TransactionDto actualTransaction = transactionService.getTransactionByTokenId(tokenId);

            verify(transactionDatastore, times(1))
                    .getTransactionByTokenId(eq(tokenId));

            assertTrue(Match(actualTransaction));
        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    @Test
    public void refundExistingTransaction() {

        try {
            when(transactionDatastore
                    .getTransactionByTokenId(eq(tokenId)))
                    .thenReturn(transaction);
            List<TransactionDto> creditorTransactionPre = transactionService.getTransactions(creditorId);
            List<TransactionDto> debtorTransactionPre = transactionService.getTransactions(debtorId);

            transactionService.refundTransaction(tokenId);
            List<TransactionDto> creditorTransactionPost = transactionService.getTransactions(creditorId);
            List<TransactionDto> debtorTransactionPost = transactionService.getTransactions(debtorId);
            // FIXME assert differences in the lists below:
            fail(); // for now
        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    @Test
    public void refundTransaction() {

        Transaction transaction = new Transaction(creditorId, debtorId, amount, tokenId, description, date);
        try {
            when(transactionDatastore.getTransactionByTokenId(tokenId)).thenReturn(transaction);
        } catch (EntryNotFoundException e) {
            fail();
        }

        try {
            transactionService.RefundTransaction(tokenId);
        } catch (EntryNotFoundException e) {
            fail();
        }

        verify(transactionDatastore, times(1))
                    .addTransaction(any());



    }
}