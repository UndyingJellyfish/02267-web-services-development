package com.example.webservices.application.stepdefs;
import com.example.webservices.library.dataTransferObjects.ChangeNameDto;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.application.dataAccess.InMemoryDatastore;
import com.example.webservices.application.tokens.Token;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.application.accounts.Customer;
import com.example.webservices.application.accounts.Merchant;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class UserServiceSteps extends AbstractSteps {
    private String customerName;
    private InMemoryDatastore store;
    private UUID customerId;
    private String merchantName;
    private UUID merchantId;
    private String bankAccountId;

    public UserServiceSteps(InMemoryDatastore inMemoryDatastore) {
        this.store = inMemoryDatastore;
    }

    @After
    public void tearDown(){
        this.store.flush();
    }

    @Given("The name of a customer")
    public void theNameOfACustomer() {
        customerName = "Edvard";
    }

    @When("The customer signs up")
    public void theCustomerSignsUp() {
        try {
            SignupDto dto = new SignupDto();
            dto.setIdentifier("lol jg er cpr");
            dto.setName(customerName);
            dto.setBankAccountId(bankAccountId);
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
        Customer customer = null;
        try {
            customer = store.getCustomer(customerId);
        } catch (EntryNotFoundException e) {
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
            SignupDto dto = new SignupDto();
            dto.setName(merchantName);
            dto.setIdentifier("123");
            dto.setBankAccountId(bankAccountId);
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
        Merchant merchant = null;
        try {
            merchant = store.getMerchant(merchantId);
        } catch (EntryNotFoundException e) {
            fail();
        }
        assertNotNull(merchant);
        assertEquals(merchant.getName(),merchantName);
        assertEquals(merchant.getBankAccountId(),bankAccountId);
    }

    @Given("An account")
    public void anAccount() {
        try {
            SignupDto dto = new SignupDto();
            dto.setIdentifier("1234");
            dto.setName("oldname");
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
            ChangeNameDto changeNameDto = new ChangeNameDto();
            changeNameDto.setNewName("newName");
            testContext().setPayload(changeNameDto);
            executePut("/account/{accountId}", new HashMap<String,String>(){{put("accountId", customerId.toString());}});
            assertNotNull(testContext().getResponse());
            customerName =  store.getAccount(customerId).getName();
        } catch (ResponseStatusException | EntryNotFoundException e) {
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
        List<Token> tokens =  store.getTokens(customerId);
        try {
            store.getAccount(customerId);
            fail();
        } catch (EntryNotFoundException e) {
            // entry not found = entry deleted
            assertEquals(0, tokens.stream().filter(t -> !t.isUsed()).count());
        }
    }

    @And("A bank account number")
    public void aBankAccountNumber() {
        bankAccountId = UUID.randomUUID().toString();
    }
}
