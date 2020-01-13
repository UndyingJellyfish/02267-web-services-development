package stepdefs;

import exceptions.DuplicateEntryException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import main.AccountService;
import models.Customer;
import models.Merchant;

import static org.junit.Assert.*;

public class UserServiceSteps {
    private String customerName;
    private AccountService userService;
    private Customer customer;
    private String merchantName;
    private Merchant merchant;


    public UserServiceSteps(AccountService userService) {
        this.userService = userService;
    }

    @Given("The name of a customer")
    public void theNameOfACustomer() {
        customerName = "Edvard";
    }

    @When("The customer signs up")
    public void theCustomerSignsUp() {
        try {
            customer = userService.addAccount(new Customer(customerName,"lol jeg er cpr"));
        } catch (DuplicateEntryException e) {
            fail();
        }
    }

    @Then("The customer should be signed up")
    public void theCustomerShouldBeSignedUp() {
        assertNotNull(customer);
        assertEquals(customerName, customer.getName());
    }

    @Given("The name of a merchant")
    public void theNameOfAMerchant() {
        merchantName = "Khajit";
    }

    @When("The merchant signs up")
    public void theMerchantSignsUp() {
        try {
            merchant = userService.addAccount(new Merchant(merchantName));
        } catch (DuplicateEntryException e) {
            fail();
        }
    }

    @Then("The merchant should be signed up")
    public void theMerchantShouldBeSignedUp() {
        assertNotNull(merchant);
        assertEquals(merchantName, merchant.getName());
    }
}
