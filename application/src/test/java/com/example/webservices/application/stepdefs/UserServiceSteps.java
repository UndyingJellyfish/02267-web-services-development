package com.example.webservices.application.stepdefs;

import com.example.webservices.application.InMemoryDataStoreTest;
import com.example.webservices.application.accounts.AccountController;
import com.example.webservices.application.accounts.SignupDto;
import com.example.webservices.application.dataAccess.IAccountDatastore;
import com.example.webservices.application.dataAccess.InMemoryDatastore;
import com.example.webservices.library.models.Token;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.example.webservices.application.exceptions.EntryNotFoundException;
import com.example.webservices.library.models.Customer;
import com.example.webservices.library.models.Merchant;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class UserServiceSteps {
    private String customerName;
    private AccountController accountController;
    private IAccountDatastore accountStore;
    private InMemoryDatastore store;
    private UUID customerId;
    private String merchantName;
    private UUID merchantId;


    public UserServiceSteps(AccountController accountController, IAccountDatastore accountStore, InMemoryDatastore store ) {
        this.accountController = accountController;
        this.accountStore = accountStore;
        this.store = store;
    }

    @Given("The name of a customer")
    public void theNameOfACustomer() {
        customerName = "Edvard";
    }

    @When("The customer signs up")
    public void theCustomerSignsUp() {
        try {
            SignupDto dto = new SignupDto();
            dto.setCpr("lol jg er cpr");
            dto.setName(customerName);
            customerId = accountController.signupCustomer(dto);
        } catch (ResponseStatusException e) {
            fail();
        }
    }

    @Then("The customer should be signed up")
    public void theCustomerShouldBeSignedUp() {
        assertNotNull(customerId);
        Customer customer = null;
        try {
            customer = accountStore.getCustomer(customerId);
        } catch (EntryNotFoundException e) {
            fail();
        }
        assertNotNull(customer);
        assertEquals(customer.getName(),customerName);
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
            dto.setCpr("123");
            merchantId = accountController.signupMerchant(dto);
        } catch (ResponseStatusException e) {
            fail();
        }
    }

    @Then("The merchant should be signed up")
    public void theMerchantShouldBeSignedUp() {
        assertNotNull(merchantId);
        Merchant merchant = null;
        try {
            merchant = accountStore.getMerchant(merchantId);
        } catch (EntryNotFoundException e) {
            fail();
        }
        assertNotNull(merchant);
        assertEquals(merchant.getName(),merchantName);
    }

    @Given("An account")
    public void anAccount() {
        try {
            SignupDto dto = new SignupDto();
            dto.setCpr("123");
            dto.setName("oldname");
            customerId = accountController.signupCustomer(dto);
        } catch (ResponseStatusException e) {
            fail();
        }
    }

    @When("The user requests a name change")
    public void theUserRequestsANameChange() {
        try {
            accountController.changeName(customerId, "newName");
            customerName =  accountStore.getAccount(customerId).getName();
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
            accountController.delete(customerId);
        } catch (ResponseStatusException e) {
            fail();
        }
    }

    @Then("The user should be deleted, and unused tokens should be removed")
    public void theUserShouldBeDeletedAndUnusedTokensShouldBeRemoved() {
        List<Token> tokens =  store.getTokens(customerId);
        try {
            accountStore.getAccount(customerId);
            fail();
        } catch (EntryNotFoundException e) {
            // entry not found = entry deleted
            assertEquals(0, tokens.stream().filter(t -> !t.isUsed()).count());
        }
    }

}
