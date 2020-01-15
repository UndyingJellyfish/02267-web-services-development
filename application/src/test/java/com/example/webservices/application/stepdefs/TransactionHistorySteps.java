package com.example.webservices.application.stepdefs;

import com.example.webservices.application.exceptions.TokenQuantityException;
import dtu.ws.fastmoney.BankServiceException_Exception;
import com.example.webservices.application.dataAccess.InMemoryDatastore;
import com.example.webservices.application.exceptions.DuplicateEntryException;
import com.example.webservices.application.bank.IBank;
import com.example.webservices.application.exceptions.EntryNotFoundException;
import com.example.webservices.application.reporting.ReportingController;
import com.example.webservices.application.tokens.ITokenManager;
import com.example.webservices.application.exceptions.TokenException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.example.webservices.application.transfers.PaymentService;
import com.example.webservices.library.models.Customer;
import com.example.webservices.library.models.Merchant;
import com.example.webservices.library.models.Token;
import com.example.webservices.library.models.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


public class TransactionHistorySteps {

    private final ITokenManager tokenManagers;
    private final PaymentService paymentService;
    private Customer customer;
    private Merchant merchant;
    private List<Transaction> expecetedTransactions;
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

    @Given("A Customer with a transaction history")
    public void aCustomerWithATransactionHistory() {
        this.customer = new Customer("Test Customer","123");
        this.merchant = new Merchant("Test Merchant", "123");
        try {
            store.addAccount(customer);
            store.addAccount(merchant);
        } catch (DuplicateEntryException e) {
            fail();
        }
        this.expecetedTransactions = new ArrayList<>();
        List<Token> tokens = null;
        try {
            tokens = tokenManagers.RequestTokens(this.customer.getAccountId(), 5);
        } catch (EntryNotFoundException | TokenQuantityException e) {
            fail();
        }
        for(int i = 0; i < tokens.size(); i++){
            Token token = tokens.get(i);
            BigDecimal amount = new BigDecimal( i == 0 ? 1 : i * 5);
            try {
                this.expecetedTransactions.add(paymentService.transfer(token.getTokenId(),this.merchant.getAccountId(), amount, ""));
                verify(bank, times(1)).transferMoney(
                        argThat( c -> c.getAccountId().equals(token.getCustomer().getAccountId())),
                        argThat(m -> m.getAccountId().equals(merchant.getAccountId())),
                        eq(amount),
                        eq(""));
            } catch (TokenException | BankServiceException_Exception | EntryNotFoundException e) {
                fail();
            }
        }
    }

    private List<Transaction> transactions;

    @When("The Customer requests the transaction history")
    public void theCustomerRequestsTheTransactionHistory() {
        this.transactions = reportingController.getHistory(this.customer.getAccountId());
    }

    @Then("The Customer receives the transaction history")
    public void theCustomerReceivesTheTransactionHistory() {
        assertNotNull(transactions);
        assertEquals(expecetedTransactions.size(), transactions.size());
        for(Transaction transaction : transactions){
            Transaction expectedTransaction = this.expecetedTransactions.stream().filter(t -> t.getTransactionId().equals(transaction.getTransactionId())).findFirst().orElse(null);
            if(expectedTransaction == null){
                fail();
            }
            assertEquals(expectedTransaction.getDebtor().getAccountId(), transaction.getDebtor().getAccountId());
            assertEquals(expectedTransaction.getCreditor().getAccountId(), transaction.getCreditor().getAccountId());

            assertEquals(expectedTransaction.getToken().getTokenId(), transaction.getToken().getTokenId());
            assertEquals(expectedTransaction.getAmount(), transaction.getAmount());
            assertEquals(expectedTransaction.getTransactionDate(), transaction.getTransactionDate());
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
        this.expecetedTransactions = new ArrayList<>();
        List<Token> tokens = null;
        try {
            tokens = tokenManagers.RequestTokens(this.customer.getAccountId(), 5);
        } catch (EntryNotFoundException | TokenQuantityException e) {
            fail();
        }
        for(int i = 0; i < tokens.size(); i++){
            Token token = tokens.get(i);
            BigDecimal amount = new BigDecimal( i == 0 ? 1 : i * 5);
            try {
                this.expecetedTransactions.add(paymentService.transfer(token.getTokenId(),this.merchant.getAccountId(), amount, ""));
                verify(bank, times(1)).transferMoney(
                        argThat( c -> c.getAccountId().equals(token.getCustomer().getAccountId())),
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
        assertEquals(expecetedTransactions.size(), transactions.size());
        for(Transaction transaction : transactions){
            Transaction expectedTransaction = this.expecetedTransactions.stream().filter(t -> t.getTransactionId().equals(transaction.getTransactionId())).findFirst().orElse(null);
            if(expectedTransaction == null){
                fail();
            }
            assertNull(transaction.getDebtor());
            assertEquals(expectedTransaction.getCreditor().getAccountId(), transaction.getCreditor().getAccountId());

            assertEquals(expectedTransaction.getToken().getTokenId(), transaction.getToken().getTokenId());
            assertEquals(expectedTransaction.getAmount(), transaction.getAmount());
            assertEquals(expectedTransaction.getTransactionDate(), transaction.getTransactionDate());
        }
    }
}
