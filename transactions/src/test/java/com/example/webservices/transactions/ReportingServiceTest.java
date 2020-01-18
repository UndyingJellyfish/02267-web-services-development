package com.example.webservices.transactions;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.AccountType;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IAccountService;
import com.example.webservices.transactions.interfaces.ITransactionDatastore;
import com.example.webservices.transactions.models.Transaction;
import com.example.webservices.transactions.services.ReportingService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReportingServiceTest {

    private ReportingService reportingService;
    private ITransactionDatastore transactionDatastore = mock(ITransactionDatastore.class);
    private IAccountService accountService = mock(IAccountService.class);
    private AccountDto customer;
    private AccountDto merchant;
    List<Transaction> transactions = new ArrayList<>();

    public ReportingServiceTest() {
        this.reportingService = new ReportingService(transactionDatastore, accountService);
    }

    @Before
    public void SetUp(){
        customer = new AccountDto(UUID.randomUUID(),
                "Stein Bagger",
                UUID.randomUUID().toString(),
                "Leasing circus",
                AccountType.CUSTOMER);
        merchant = new AccountDto(UUID.randomUUID(),
                "OW Bunker",
                UUID.randomUUID().toString(),
                "Syrisk Olie",
                AccountType.MERCHANT);
        try {
            when(accountService.getAccount(customer.getAccountId())).thenReturn(customer);
            when(accountService.getAccount(merchant.getAccountId())).thenReturn(merchant);
        } catch (EntryNotFoundException e) {
            fail();
        }
        transactions.add(new Transaction(merchant.getAccountId(), customer.getAccountId(), new BigDecimal("1"), UUID.randomUUID(), "To trick SKAT", false));
        transactions.add(new Transaction(merchant.getAccountId(), customer.getAccountId(), new BigDecimal("100"), UUID.randomUUID(), "Keep-quite money", false));
        when(transactionDatastore.getTransactions(ArgumentMatchers.any())).thenReturn(transactions);
    }

    @Test
    public void getTransactionHistoryCustomer(){
        List<TransactionDto> history = null;
        try {
            history = reportingService.getTransactionHistory(customer.getAccountId());
        } catch (EntryNotFoundException e) {
            fail();
        }
        assertNotNull(history);
        List<TransactionDto> dtoList = transactions
                .stream()
                .map(t ->
                        new TransactionDto(t.getTransactionId(),
                                t.getTokenId(), t.getCreditorId(),
                                t.getDebtorId(), t.getAmount(),
                                t.getDescription(),
                                t.isRefund(),
                                t.getTransactionDate())
                )
                .collect(Collectors.toList());
        assertEquals(dtoList, history);
    }

    @Test
    public void getTransactionHistoryMerchant(){
        List<TransactionDto> history = null;
        try {
            history = reportingService.getTransactionHistory(customer.getAccountId());
        } catch (EntryNotFoundException e) {
            fail();
        }
        assertNotNull(history);
        List<TransactionDto> dtoList = transactions
                .stream()
                .map(t ->
                        new TransactionDto(t.getTransactionId(),
                                t.getTokenId(), t.getCreditorId(),
                                t.getDebtorId(), t.getAmount(),
                                t.getDescription(),
                                t.isRefund(),
                                t.getTransactionDate())
                )
                .collect(Collectors.toList());
        dtoList.forEach(t -> t.setDebtor(null));
        assertEquals(dtoList, history);
    }
}
