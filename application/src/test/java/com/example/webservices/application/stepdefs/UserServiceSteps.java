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
import sun.security.util.PendingException;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class UserServiceSteps extends AbstractSteps {
    private String customerName;
    private UUID customerId;
    private String merchantName;
    private UUID merchantId;
    private String bankAccountId;

    public UserServiceSteps() {

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
        SignupDto dto = new SignupDto(customerName, "12345678", bankAccountId);
        testContext().setPayload(dto);
        executePost("/account/customer");
        assertNotNull(testContext().getResponse());
        customerId = getBody(UUID.class);
    }

    @Then("The customer should be signed up")
    public void theCustomerShouldBeSignedUp() {
        assertNotNull(customerId);
        executeGet("/account/customer/" + customerId);
        AccountDto  customer = getBody(AccountDto.class);
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
        SignupDto dto = new SignupDto(merchantName, "1234", bankAccountId);
        testContext().setPayload(dto);
        executePost("/account/merchant");
        merchantId = getBody(UUID.class);
    }

    @Then("The merchant should be signed up")
    public void theMerchantShouldBeSignedUp() {
        assertNotNull(merchantId);
        executeGet("/account/merchant/" + customerId);
        AccountDto  merchant = getBody(AccountDto.class);
        assertNotNull(merchant);
        assertEquals(merchant.getName(),merchantName);
        assertEquals(merchant.getBankAccountId(),bankAccountId);
    }

    @Given("An account")
    public void anAccount() {
        SignupDto dto = new SignupDto("oldname", "123", UUID.randomUUID().toString());
        testContext().setPayload(dto);
        executePost("/account/customer");
        customerId = getBody(UUID.class);
    }

    @When("The user requests a name change")
    public void theUserRequestsANameChange() {
        ChangeNameDto changeNameDto = new ChangeNameDto(customerId, "newName");
        testContext().setPayload(changeNameDto);
        executePut("/account");
        assertNotNull(testContext().getResponse());
        executeGet("/account/customer/" + customerId);
        AccountDto  customer = getBody(AccountDto.class);
        customerName = customer.getName();
    }

    @Then("The name should be changed")
    public void theNameShouldBeChanged() {
        assertEquals("newName",customerName);
    }

    @When("The user requests to be deleted")
    public void theUserRequestsToBeDeleted() {

        executeDelete("/account/"+customerId.toString());

    }

    @Then("The user should be deleted, and unused tokens should be removed")
    public void theUserShouldBeDeletedAndUnusedTokensShouldBeRemoved() {

        executeGet("tokens/" + customerId);
        int tokens = getBody(int.class);
        executeGet("/account/customer/" + customerId);
        assertEquals(0, tokens);

    }

    @And("A bank account number")
    public void aBankAccountNumber() {
        bankAccountId = UUID.randomUUID().toString();
    }
}
