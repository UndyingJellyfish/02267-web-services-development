package stepdefs;

import main.TokenManager;
import models.Customer;
import models.Token;
import cucumber.api.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import main.ITokenManager;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CustomerTokenSteps {

    private final ITokenManager tokenManager;
    private Customer customer;

    public CustomerTokenSteps(ITokenManager tokenManager){
        this.tokenManager = tokenManager;
    }

    @Given("A registered user")
    public void aRegisteredUser() {
        this.customer = new Customer("test");
    }

    @And("The user has spent all tokens")
    public void theUserHasSpentAllTokens() {

    }

    @When("The user requests a {int} of tokens")
    public void theUserRequestsANumberOfTokens(int arg0) {
        tokenManager.RequestTokens(this.customer, arg0);
    }

    @Then("The user receives {int} tokens")
    public void theUserReceivesNumberTokens(int arg0) {
        assertEquals(arg0, this.tokenManager.GetTokens(this.customer).size());
    }

    @And("The user has {int} unused token")
    public void theUserHasUnusedToken(int arg0) {
        throw new PendingException();
    }

    @When("The user requests {int} tokens")
    public void theUserRequestsTokens(int arg0) {
        throw new PendingException();
    }

    @Then("The user has {int} unused tokens")
    public void theUserHasUnusedTokens(int arg0) {
        throw new PendingException();
    }


}
