package stepdefs;

import models.Customer;
import models.Token;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import interfaces.ITokenManager;

import java.util.List;

import static org.junit.Assert.*;

public class CustomerTokenSteps {

    private final ITokenManager tokenManager;
    private Customer customer;
    private List<Token> tokenList;

    public CustomerTokenSteps(ITokenManager tokenManager){
        this.tokenManager = tokenManager;
    }

    @Given("A registered user")
    public void aRegisteredUser() {

        this.customer = new Customer("test");
    }

    @And("The user has spent all tokens")
    public void theUserHasSpentAllTokens() {

        assertTrue(tokenManager.GetTokens(customer).stream().allMatch(Token::isUsed));
    }

    @When("The user requests {int} of tokens")
    public void theUserRequestsANumberOfTokens(int arg0) {
        tokenList = tokenManager.RequestTokens(this.customer, arg0);
    }

    @Then("The user receives {int} tokens")
    public void theUserReceivesNumberTokens(int arg0) {
        List<Token> tokenList = this.tokenManager.GetTokens(this.customer);
        assertEquals(arg0,tokenList.size());
        assertEquals(this.tokenList, tokenList);
    }

    @And("The user has {int} unused token")
    public void theUserHasUnusedToken(int arg0) {
        tokenManager.RequestTokens(customer,arg0);
        assertEquals(arg0, tokenManager.GetTokens(customer).stream().filter(t ->!t.isUsed()).count());
    }

    /*@When("The user requests {int} tokens")
    public void theUserRequestsTokens(int arg0) {
        tokenManager.RequestTokens(this.customer, arg0);
    }*/

    @Then("The user has {int} unused tokens")
    public void theUserHasUnusedTokens(int arg0) {
        assertEquals(arg0, tokenManager.GetTokens(customer).stream().filter(t ->!t.isUsed()).count());
    }



    @And("The user already has {int} unused tokens")
    public void theUserAlreadyHasUnusedTokens(int arg0) {
        tokenManager.RequestTokens(customer,arg0);
    }

    private IllegalArgumentException excp = null;
    @When("The user requests {int} tokens")
    public void theUserRequestsTokens(int arg0) {
        try{
            tokenManager.RequestTokens(customer,arg0);
            fail();
        }catch(IllegalArgumentException e){
            excp = e;
        }
    }

    @Then("It should fail")
    public void itShouldFail() {
        assertNotNull(excp);
    }

    @And("the user has {int} unused tokens")
    public void theUserHasNumberUnusedTokens(int arg0) {
        if(arg0 > 0){
            List<Token> out = tokenManager.RequestTokens(customer,arg0);
            assertEquals(arg0, out.size());
        }
    }


}
