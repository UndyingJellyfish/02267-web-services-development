package com.example.webservices.application.stepdefs;

import com.example.webservices.application.transfers.Transaction;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import dtu.ws.fastmoney.BankServiceException_Exception;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.application.dataAccess.InMemoryDatastore;
import com.example.webservices.library.interfaces.IBank;
import gherkin.deps.com.google.gson.reflect.TypeToken;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.example.webservices.library.dataTransferObjects.RequestTokenDto;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PaymentServiceSteps extends AbstractSteps {
    private InMemoryDatastore store;
    private UUID merchantId;
    private BigDecimal amount;
    private UUID tokenId;
    private Transaction transaction;
    private UUID customerId;
    private IBank bank;

    public PaymentServiceSteps(InMemoryDatastore store, IBank bank) {
        this.store = store;
        this.bank = bank;
    }

    @After
    public void tearDown(){
        this.store.flush();
    }


    @When("The merchant initiates the transaction")
    public void theMerchantInitiatesTheTransaction() {

        try {
            String description = "Transfer";
            TransactionDto dto = TransactionDto.Create();
            dto.setAmount(amount);
            dto.setTokenId(tokenId);
            dto.setMerchantId(merchantId);
            dto.setDescription(description);
            testContext().setPayload(dto);
            executePost("/payment/transfer");
            transaction = store.GetTransactionByTokenId(tokenId);
            verify(bank, times(1)).transferMoney(
                    argThat(c -> c.getAccountId().equals(customerId)),
                    argThat(m -> m.getAccountId().equals(merchantId)),
                    eq(amount), eq(description)
                );
            reset(bank);
        } catch (Exception e) {
            fail();
        }
    }

    @Then("The transaction should go through")
    public void theTransactionShouldGoThrough() {
        assertEquals(customerId, transaction.getDebtorId().getAccountId());
        assertEquals(merchantId, transaction.getCreditorId().getAccountId());
        assertEquals(amount, transaction.getAmount());
    }


    @When("The merchant initiates the invalid transaction")
    public void theMerchantInitiatesTheInvalidTransaction() throws BankServiceException_Exception {
        String invalidDesc = "12312oi3j4to3j4gp24ijgip24utgi24noi4untg";
        this.bank = mock(IBank.class);
            TransactionDto dto = TransactionDto.Create();
            dto.setAmount(amount);
            dto.setTokenId(tokenId);
            dto.setMerchantId(merchantId);
            dto.setDescription(invalidDesc);
            testContext().setPayload(dto);
            executePost("/payment/transfer");
        try {
            transaction = store.GetTransactionByTokenId(tokenId);
        } catch (EntryNotFoundException ignored) {
        }
            verify(bank, never()).transferMoney(any(), any(), any(), any());
            reset(bank);
    }

    @Then("The transaction should fail")
    public void theTransactionShouldFail() {
        assertEquals(HttpStatus.BAD_REQUEST.value(), testContext().getResponse().statusCode());
    }

/*    private String createBankAccountCustomer(SignupDto signupDto){
        Account account = new Customer(signupDto.getName(), signupDto.getCpr(), signupDto.getBankAccountId());
        try {
            return this.bank.addAccount(account);
        } catch (BankServiceException_Exception | ClassNotFoundException e) {
            fail();
        }
        return "";
    }

    private String createBankAccountMerchant(SignupDto signupDto){
        Account account = new Merchant(signupDto.getName(), signupDto.getCpr(), signupDto.getBankAccountId());
        try {
            return this.bank.addAccount(account);
        } catch (BankServiceException_Exception | ClassNotFoundException e) {
            fail();
        }
        return "";
    }*/




    @Given("A merchant")
    public void aMerchant() {

        SignupDto dto = new SignupDto();
        dto.setName("Alice");
        dto.setCpr("123");
        try {
            testContext().setPayload(dto);
            executePost("/account/merchant");
            merchantId = getBody(UUID.class);
        } catch (ResponseStatusException e) {
            fail();
        }

    }

    @And("A valid token")
    public void aValidToken() {
        SignupDto dto = new SignupDto();
        dto.setName("Bob");
        dto.setCpr("123");
        try {
            testContext().setPayload(dto);
            executePost("/account/customer");
           customerId = getBody(UUID.class);
        } catch (ResponseStatusException e) {
            fail();
        }
        RequestTokenDto dto2 = new RequestTokenDto();
        dto2.setAmount(1);
        dto2.setCustomerId(customerId);
        testContext().setPayload(dto2);
        executePost("/tokens");
        tokenId = ((ArrayList<UUID>)getBody(new TypeToken<List<UUID>>(){}.getType()))
                .stream()
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @And("A positive amount")
    public void aPositiveAmount() {
        amount = new BigDecimal("100");
    }

    @And("A token that doesn't exist")
    public void aTokenThatDoesnTExist() {
        tokenId = null;
    }

    @And("A token that has already been used")
    public void aTokenThatHasAlreadyBeenUsed() {
        SignupDto dto = new SignupDto();
        dto.setName("Bob");
        dto.setCpr("123");
        try {
            testContext().setPayload(dto);
            executePost("/account/customer");
            customerId = getBody(UUID.class);
        } catch (ResponseStatusException e) {
            fail();
        }
        RequestTokenDto dto2 = new RequestTokenDto();
        dto2.setAmount(1);
        dto2.setCustomerId(customerId);
        testContext().setPayload(dto2);
        executePost("/tokens");
        tokenId = ((ArrayList<UUID>)getBody(new TypeToken<List<UUID>>(){}.getType()))
                .stream()
                .findFirst()
                .orElseThrow(RuntimeException::new);
        try {
            TransactionDto dto3 = TransactionDto.Create();
            dto3.setDescription("Something");
            dto3.setMerchantId(this.merchantId);
            dto3.setAmount(amount);
            dto3.setTokenId(tokenId);
            testContext().setPayload(dto3);
            executePost("/payment/transfer");
            reset(bank);
        } catch (ResponseStatusException e) {
                fail();
        }
    }

    @Then("The transaction should fail and inform that the token is used")
    public void theTransactionShouldFailAndInformThatTheTokenIsUsed() {
        assertEquals(HttpStatus.BAD_REQUEST.value(),testContext().getResponse().statusCode());
    }

    @And("An invalid {int}")
    public void anInvalidAmount(int arg0) {
        amount = new BigDecimal(arg0);
    }

    @Then("The transaction should fail and inform that the amount is invalid")
    public void theTransactionShouldFailAndInformThatTheAmountIsInvalid() {
        assertEquals(HttpStatus.BAD_REQUEST.value(),testContext().getResponse().statusCode());
    }

    @Given("A transaction")
    public void aTransaction() {

        //this.bank = mock(IBank.class);
        SignupDto dto = new SignupDto();
        dto.setName("Bob");
        dto.setCpr("123");

        SignupDto dto2 = new SignupDto();
        dto2.setName("jens");
        dto2.setCpr("12345678");

        try{
            testContext().setPayload(dto);
            executePost("/account/customer");
            customerId = getBody(UUID.class);
            testContext().setPayload(dto2);
            executePost("/account/merchant");
            merchantId = getBody(UUID.class);
        }catch(Exception e){
            fail();
        }

        RequestTokenDto dto3 = new RequestTokenDto();
        dto3.setCustomerId(customerId);
        dto3.setAmount(1);
        testContext().setPayload(dto3);
        executePost("/tokens");
        tokenId = ((ArrayList<UUID>)getBody(new TypeToken<List<UUID>>(){}.getType()))
                .stream()
                .findFirst()
                .orElseThrow(RuntimeException::new);

        amount = new BigDecimal("150.0");
        try {
            TransactionDto dto4 = TransactionDto.Create();
            dto4.setAmount(amount);
            dto4.setMerchantId(merchantId);
            dto4.setCustomerId(customerId);
            dto4.setDescription("");
            dto4.setTokenId(tokenId);
            testContext().setPayload(dto4);
            executePost("/payment/transfer");
            verify(bank, times(1)).transferMoney(
                    any(),any(),any(),any());
            reset(bank);
        } catch (BankServiceException_Exception e) {
            fail();
        }
    }

    @When("The customer asks for a refund")
    public void theCustomerAsksForRefund() {
        try {
            TransactionDto dto = TransactionDto.Create();
            dto.setCustomerId(customerId);
            dto.setMerchantId(merchantId);
            dto.setTokenId(tokenId);
            testContext().setPayload(dto);
            executePost("/payment/transfer");
        } catch (Exception e) {
            fail();
        }
    }

    @Then("The transaction should be refunded")
    public void theTransactionShouldBeRefunded() {

    }
}
