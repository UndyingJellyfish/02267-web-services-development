package com.example.webservices.transactions;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.AccountType;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IAccountService;
import com.example.webservices.library.interfaces.ITransactionService;
import com.example.webservices.transactions.services.ReportingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReportingServiceTest {

    private ReportingService reportingService;
    private ITransactionService transactionService = mock(ITransactionService.class);
    private IAccountService accountService = mock(IAccountService.class);
    private AccountDto customer;
    private AccountDto merchant;
    List<TransactionDto> transactionDtos = new ArrayList<>();

    public ReportingServiceTest() {
        this.reportingService = new ReportingService(transactionService, accountService);
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
        } catch (EntryNotFoundException | JsonProcessingException e) {
            fail();
        }
        transactionDtos.add(new TransactionDto(UUID.randomUUID(), merchant.getAccountId(), customer.getAccountId(), new BigDecimal("1"), "To trick SKAT", false, new Date()));
        transactionDtos.add(new TransactionDto(UUID.randomUUID(), merchant.getAccountId(), customer.getAccountId(), new BigDecimal("100"), "Keep-quite money", false, new Date()));
        when(transactionService.getTransactions(ArgumentMatchers.any())).thenReturn(transactionDtos);
    }

    @Test
    public void getTransactionHistoryCustomer(){
        List<TransactionDto> history = null;
        try {
            history = reportingService.getTransactionHistory(customer.getAccountId());
        } catch (EntryNotFoundException | JsonProcessingException e) {
            fail();
        }
        assertNotNull(history);
        assertEquals(transactionDtos, history);
    }

    @Test
    public void getTransactionHistoryMerchant(){
        List<TransactionDto> history = null;
        try {
            history = reportingService.getTransactionHistory(merchant.getAccountId());
        } catch (EntryNotFoundException | JsonProcessingException e) {
            fail();
        }
        assertNotNull(history);
        transactionDtos.forEach(t -> t.setDebtorId(null));
        assertEquals(transactionDtos, history);
    }
}
