//TODO: fix this kthx plz
package com.example.webservices.application.stepdefs;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.exceptions.*;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.interfaces.IAccountService;
import com.example.webservices.library.interfaces.IPaymentService;
import com.example.webservices.library.interfaces.IReportingService;
import com.example.webservices.library.interfaces.ITokenManager;
import gherkin.deps.com.google.gson.reflect.TypeToken;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.After;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class TransactionHistorySteps extends AbstractSteps {

    private final ITokenManager tokenManagers;
    private final IPaymentService paymentService;
    private final IReportingService reportingService;
    private final IAccountService accountService;
    private AccountDto customer;
    private AccountDto merchant;
    private List<TransactionDto> expectedTransactions;


    public TransactionHistorySteps(ITokenManager tokenManager, IPaymentService paymentService, IReportingService reportingService, IAccountService accountService){
        this.paymentService = paymentService;
        this.tokenManagers = tokenManager;
        this.reportingService = reportingService;
        this.accountService = accountService;
    }

    @After
    public void tearDown(){
        //TODO: Stuff here
    }

    @Given("A Customer with a transaction history")
    public void aCustomerWithATransactionHistory() {
        SignupDto cDto = new SignupDto("Test Customer","123",UUID.randomUUID().toString());
        SignupDto mDto = new SignupDto("Test Merchant", "1234",UUID.randomUUID().toString());
        try {
            this.customer = accountService.addCustomer(cDto);
            this.merchant = accountService.addMerchant(mDto);
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

            } catch (TokenException | BankException | EntryNotFoundException | InvalidTransferAmountException e) {
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
            TransactionDto expectedTransaction = this.expectedTransactions
                    .stream()
                    .filter(t -> t.getTransactionId()
                            .equals(transaction.getTransactionId()))
                    .findFirst()
                    .orElse(null);
            if(expectedTransaction == null){
                fail();
            }
            assertEquals(expectedTransaction.getDebtorId(), transaction.getDebtorId());
            assertEquals(expectedTransaction.getCreditorId(), transaction.getCreditorId());

            assertEquals(expectedTransaction.getTokenId(), transaction.getTokenId());
            assertEquals(expectedTransaction.getAmount(), transaction.getAmount());
        }
    }

    @Given("A Merchant with a transaction history")
    public void aMerchantWithATransactionHistory() {
        SignupDto cDto = new SignupDto("Test Customer","123", UUID.randomUUID().toString());
        SignupDto mDto = new SignupDto("Test Merchant", "123", UUID.randomUUID().toString());
        try {
            this.customer = accountService.addCustomer(cDto);
            this.merchant = accountService.addMerchant(mDto);
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

            } catch (TokenException | EntryNotFoundException | BankException | InvalidTransferAmountException e) {
                fail();
            }
        }
    }

    @When("The Merchant requests the transaction history")
    public void theMerchantRequestsTheTransactionHistory() {
        try {
            this.transactions = reportingService.getTransactionHistory(this.merchant.getAccountId());
        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    @Then("The Merchant receives the transaction history without customer names")
    public void theMerchantReceivesTheTransactionHistoryWithoutCustomerNames() {
        assertNotNull(transactions);
        assertEquals(expectedTransactions.size(), transactions.size());
        for(TransactionDto transaction : transactions){
            TransactionDto expectedTransaction = this.expectedTransactions.stream().filter(t -> t.equals(transaction.getTransactionId())).findFirst().orElse(null);
            if(expectedTransaction == null){
                fail();
            }
            assertNull(transaction.getDebtorId());
            assertEquals(expectedTransaction.getCreditorId(), transaction.getCreditorId());

            assertEquals(expectedTransaction.getTokenId(), transaction.getTokenId());
            assertEquals(expectedTransaction.getAmount(), transaction.getAmount());
            // TODO: Cannot compare transaction date since TransactionDto does not have date information
            //assertEquals(expectedTransaction.getTransactionDate(), transaction.getTransactionDate());
        }
    }
}
