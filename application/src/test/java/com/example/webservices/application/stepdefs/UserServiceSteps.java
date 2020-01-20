package com.example.webservices.application.stepdefs;
import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.ChangeNameDto;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.interfaces.IAccountService;
import com.example.webservices.library.interfaces.ITokenManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class UserServiceSteps extends AbstractSteps {
    private String customerName;
    private IAccountService accountService;
    private ITokenManager tokenManager;
    private UUID customerId;
    private String merchantName;
    private UUID merchantId;
    private String bankAccountId;

    public UserServiceSteps(IAccountService accountService, ITokenManager tokenManager) {
        this.accountService = accountService;
        this.tokenManager = tokenManager;
    }

    @After
    public void tearDown(){
    }

    @Given("The name of a customer")
    public void theNameOfACustomer() {
        customerName = "Edvard";
    }

    @When("The customer signs up")
    public void theCustomerSignsUp() {
        try {
            SignupDto dto = new SignupDto(customerName, "12345678", bankAccountId);
            testContext().setPayload(dto);
            executePost("/account/customer");
            assertNotNull(testContext().getResponse());
            customerId = getBody(UUID.class);
        } catch (ResponseStatusException e) {
            fail();
        }
    }

    @Then("The customer should be signed up")
    public void theCustomerShouldBeSignedUp() {
        assertNotNull(customerId);
        AccountDto customer = null;
        try {
            customer = accountService.getCustomer(customerId);
        } catch (EntryNotFoundException | JsonProcessingException e) {
            fail();
        }
        assertNotNull(customer);
        assertEquals(customer.getName(),customerName);
        assertEquals(customer.getBankAccountId(),bankAccountId);
    }

    @Given("The name of a merchant")
    public void theNameOfAMerchant() {
        merchantName = "Khajit";
    }

    @When("The merchant signs up")
    public void theMerchantSignsUp() {
        try {
            SignupDto dto = new SignupDto(merchantName, "1234", bankAccountId);
            testContext().setPayload(dto);
            executePost("/account/merchant");
            merchantId = getBody(UUID.class);
        } catch (ResponseStatusException e) {
            fail();
        }
    }

    @Then("The merchant should be signed up")
    public void theMerchantShouldBeSignedUp() {
        assertNotNull(merchantId);
        AccountDto merchant = null;
        try {
            merchant = accountService.getMerchant(merchantId);
        } catch (EntryNotFoundException | JsonProcessingException e) {
            fail();
        }
        assertNotNull(merchant);
        assertEquals(merchant.getName(),merchantName);
        assertEquals(merchant.getBankAccountId(),bankAccountId);
    }

    @Given("An account")
    public void anAccount() {
        try {
            SignupDto dto = new SignupDto("oldname", "123", UUID.randomUUID().toString());
            testContext().setPayload(dto);
            executePost("/account/customer");
            customerId = getBody(UUID.class);
        } catch (ResponseStatusException e) {
            fail();
        }
    }

    @When("The user requests a name change")
    public void theUserRequestsANameChange() {
        try {
            ChangeNameDto changeNameDto = new ChangeNameDto(customerId, "newName");
            testContext().setPayload(changeNameDto);
            executePut("/account");
            assertNotNull(testContext().getResponse());
            customerName = accountService.getAccount(customerId).getName();
        } catch (ResponseStatusException | EntryNotFoundException | JsonProcessingException e) {
            fail();
        }
    }

    @Then("The name should be changed")
    public void theNameShouldBeChanged() {
        assertEquals("newName",customerName);
    }

    @When("The user requests to be deleted")
    public void theUserRequestsToBeDeleted() {
        try {
            executeDelete("/account/"+customerId.toString());
        } catch (ResponseStatusException e) {
            fail();
        }
    }

    @Then("The user should be deleted, and unused tokens should be removed")
    public void theUserShouldBeDeletedAndUnusedTokensShouldBeRemoved() {
        List<TokenDto> tokens = null; // TODO: FIX tokenManager.getTokens(customerId);
        try {
            accountService.getAccount(customerId);
            fail();
        } catch (EntryNotFoundException e) {
            // entry not found = entry deleted
            assertEquals(0, tokens.stream().filter(t -> !t.isUsed()).count());
        } catch (JsonProcessingException e) {
            fail();
        }
    }

    @And("A bank account number")
    public void aBankAccountNumber() {
        bankAccountId = UUID.randomUUID().toString();
    }
}
