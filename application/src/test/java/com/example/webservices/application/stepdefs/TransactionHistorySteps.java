package com.example.webservices.application.stepdefs;

import com.example.webservices.library.exceptions.TokenQuantityException;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import dtu.ws.fastmoney.BankServiceException_Exception;
import com.example.webservices.application.dataAccess.InMemoryDatastore;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.interfaces.IBank;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.application.reporting.ReportingController;
import com.example.webservices.library.interfaces.ITokenManager;
import com.example.webservices.library.exceptions.TokenException;
import gherkin.deps.com.google.gson.reflect.TypeToken;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.example.webservices.payments.transfers.PaymentService;
import com.example.webservices.application.accounts.Customer;
import com.example.webservices.application.accounts.Merchant;
import com.example.webservices.application.transfers.Transaction;
import org.junit.After;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TransactionHistorySteps extends AbstractSteps {

    private final ITokenManager tokenManagers;
    private final PaymentService paymentService;
    private Customer customer;
    private Merchant merchant;
    private List<Transaction> expectedTransactions;
    private IBank bank;
    private ReportingController reportingController;
    private InMemoryDatastore store;


    public TransactionHistorySteps(ITokenManager tokenManager, InMemoryDatastore store, ReportingController reportingController){
        this.store = store;
        this.reportingController = reportingController;
        this.bank = mock(IBank.class);
        this.tokenManagers = tokenManager;
        this.paymentService = new PaymentService(tokenManager, store, store, bank);
    }

    @After
    public void tearDown(){
        this.store.flush();
    }

    @Given("A Customer with a transaction history")
    public void aCustomerWithATransactionHistory() {
        this.customer = new Customer("Test Customer","123");
        this.merchant = new Merchant("Test Merchant", "1234");
        try {
            store.addAccount(customer);
            store.addAccount(merchant);
        } catch (DuplicateEntryException e) {
            fail();
        }
        this.expectedTransactions = new ArrayList<>();
        List<UUID> tokens = null;
        try {
            tokens = tokenManagers.RequestTokens(this.customer.getAccountId(), 5);
        } catch (EntryNotFoundException | TokenQuantityException e) {
            fail();
        }
        for(int i = 0; i < tokens.size(); i++){
            UUID token = tokens.get(i);
            BigDecimal amount = new BigDecimal( i == 0 ? 1 : i * 5);
            try {
                this.expectedTransactions.add(paymentService.transfer(token,this.merchant.getAccountId(), amount, ""));
                verify(bank, times(1)).transferMoney(
                        argThat(c -> c.getAccountId().equals(customer.getAccountId())),
                        argThat(m -> m.getAccountId().equals(merchant.getAccountId())),
                        eq(amount),
                        eq(""));
            } catch (TokenException | BankServiceException_Exception | EntryNotFoundException e) {
                fail();
            }
        }
    }

    private List<TransactionDto> transactions;

    @When("The Customer requests the transaction history")
    public void theCustomerRequestsTheTransactionHistory() {
        executeGet("/reporting/{accountId}", new HashMap<String, String>(){{put("accountId", customer.getAccountId().toString());}});
        this.transactions = getBody(new TypeToken<List<TransactionDto>>(){}.getType());
    }

    @Then("The Customer receives the transaction history")
    public void theCustomerReceivesTheTransactionHistory() {
        assertNotNull(transactions);
        assertEquals(expectedTransactions.size(), transactions.size());
        for(TransactionDto transaction : transactions){
            Transaction expectedTransaction = this.expectedTransactions
                    .stream()
                    .filter(t -> t.getTransaction()
                            .equals(transaction.getTransactionId()))
                    .findFirst()
                    .orElse(null);
            if(expectedTransaction == null){
                fail();
            }
            assertEquals(expectedTransaction.getDebtorId().getAccountId(), transaction.getCustomerId());
            assertEquals(expectedTransaction.getCreditorId().getAccountId(), transaction.getMerchantId());

            assertEquals(expectedTransaction.getTokenId().getTokenId(), transaction.getTokenId());
            assertEquals(expectedTransaction.getAmount(), transaction.getAmount());
        }
    }

    @Given("A Merchant with a transaction history")
    public void aMerchantWithATransactionHistory() {
        this.customer = new Customer("Test Customer","123");
        this.merchant = new Merchant("Test Merchant", "123");
        try {
            store.addAccount(customer);
            store.addAccount(merchant);
        } catch (DuplicateEntryException e) {
            fail();
        }
        this.expectedTransactions = new ArrayList<>();
        List<UUID> tokens = null;
        try {
            tokens = tokenManagers.RequestTokens(this.customer.getAccountId(), 5);
        } catch (EntryNotFoundException | TokenQuantityException e) {
            fail();
        }
        for(int i = 0; i < tokens.size(); i++){
            UUID token = tokens.get(i);
            BigDecimal amount = new BigDecimal( i == 0 ? 1 : i * 5);
            try {
                this.expectedTransactions.add(paymentService.transfer(token, this.merchant.getAccountId(), amount, ""));
                verify(bank, times(1)).transferMoney(
                        argThat( c -> c.getAccountId().equals(customer.getAccountId())),
                        argThat(m -> m.getAccountId().equals(merchant.getAccountId())),
                        eq(amount),
                        eq(""));
            } catch (TokenException |EntryNotFoundException| BankServiceException_Exception e) {
                fail();
            }
        }
    }

    @When("The Merchant requests the transaction history")
    public void theMerchantRequestsTheTransactionHistory() {
        this.transactions = reportingController.getHistory(this.merchant.getAccountId());
    }

    @Then("The Merchant receives the transaction history without customer names")
    public void theMerchantReceivesTheTransactionHistoryWithoutCustomerNames() {
        assertNotNull(transactions);
        assertEquals(expectedTransactions.size(), transactions.size());
        for(TransactionDto transaction : transactions){
            Transaction expectedTransaction = this.expectedTransactions.stream().filter(t -> t.getTransaction().equals(transaction.getTransactionId())).findFirst().orElse(null);
            if(expectedTransaction == null){
                fail();
            }
            assertNull(transaction.getCustomerId());
            assertEquals(expectedTransaction.getCreditorId().getAccountId(), transaction.getMerchantId());

            assertEquals(expectedTransaction.getTokenId().getTokenId(), transaction.getTokenId());
            assertEquals(expectedTransaction.getAmount(), transaction.getAmount());
            // TODO: Cannot compare transaction date since TransactionDto does not have data information
            //assertEquals(expectedTransaction.getTransactionDate(), transaction.getTransactionDate());
        }
    }
}
