package com.example.webservices.transactions;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.DuplicateEntryException;
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

/** @author 164410 */
public class TransactionServiceTest {

    private TransactionService transactionService;
    private ITransactionDatastore transactionDatastore = mock(ITransactionDatastore.class);
    private List<Transaction> transactions;
    private UUID creditorId = UUID.randomUUID();
    private UUID debtorId = UUID.randomUUID();
    private UUID tokenId = UUID.randomUUID();
    private UUID transactionId;
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
        this.transactionId = this.transaction.getTransactionId();
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
    public void addTransaction() throws DuplicateEntryException {
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
    public void getTransaction() {
        try {
            when(transactionDatastore
                .getTransaction(eq(transactionId)))
                .thenReturn(transaction);
            TransactionDto actualTransaction = transactionService.getTransaction(transactionId);

            verify(transactionDatastore, times(1))
                    .getTransaction(eq(transactionId));

            assertTrue(Match(actualTransaction));
        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    @Test
    public void refundExistingTransaction() throws DuplicateEntryException {
        // Given
        UUID refundId = UUID.randomUUID();
        Transaction refundTransaction = new Transaction(
                transaction.getDebtorId(),
                transaction.getCreditorId(),
                transaction.getAmount(),
                transaction.getTokenId(),
                transaction.getDescription(),
                true,
                transaction.getTransactionDate());
        UUID serviceReturn = null;

        // When
        try {
            when(transactionDatastore
                    .getTransaction(eq(transactionId)))
                    .thenReturn(transaction);
            when(transactionDatastore
                    .addTransaction(any()))
                    .thenReturn(refundId);

            serviceReturn = transactionService.refundTransaction(transaction.getTransactionId());

        } catch (EntryNotFoundException | DuplicateEntryException e) {
            fail();
        }

        // Then
        verify(transactionDatastore, times(1))
                .addTransaction(argThat(dto -> dto.getTokenId() == tokenId));
        assertNotNull(serviceReturn);
        assertEquals(refundId, serviceReturn);
    }
}