package stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import main.UserService;
import models.Customer;
import models.Merchant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserServiceSteps {
    private String customerName;
    private UserService userService;
    private Customer customer;
    private String merchantName;
    private Merchant merchant;


    public UserServiceSteps(UserService userService) {
        this.userService = userService;
    }

    @Given("The name of a customer")
    public void theNameOfACustomer() {
        customerName = "Edvard";
    }

    @When("The customer signs up")
    public void theCustomerSignsUp() {
        customer = userService.addAccount(new Customer(customerName));
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
        merchant = userService.addAccount(new Merchant(merchantName));
    }

    @Then("The merchant should be signed up")
    public void theMerchantShouldBeSignedUp() {
        assertNotNull(merchant);
        assertEquals(merchantName, merchant.getName());
    }
}
