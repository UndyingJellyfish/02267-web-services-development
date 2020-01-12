package stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import main.UserService;
import models.Customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserServiceSteps {
    private String customerName;
    private UserService userService;
    private Customer customer;

    public UserServiceSteps(UserService userService) {
        this.userService = userService;
    }

    @Given("The name of a customer")
    public void theNameOfACustomer() {
        customerName = "Edvard";
    }

    @When("The customer signs up")
    public void theCustomerSignsUp() {
        customer = userService.SignUpCustomer(customerName);
    }

    @Then("The customer should be signed up")
    public void theCustomerShouldBeSignedUp() {
        assertNotNull(customer);
        assertEquals(customerName, customer.getName());
    }
}
