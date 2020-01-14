package stepdefs;

import main.accounts.AccountController;
import main.accounts.SignupDto;
import main.dataAccess.IAccountDatastore;
import main.models.Customer;
import main.models.Token;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import main.tokens.ITokenManager;
import main.tokens.TokenController;
import main.tokens.RequestTokenDto;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class CustomerTokenSteps {

    private final ITokenManager tokenManager;
    private AccountController accountController;
    private IAccountDatastore accountDatastore;
    private TokenController tokenController;
    private Customer customer;
    private List<Token> tokenList;

    public CustomerTokenSteps(ITokenManager tokenManager, AccountController accountController, IAccountDatastore accountDatastore, TokenController tokenController){
        this.tokenManager = tokenManager;
        this.accountController = accountController;
        this.accountDatastore = accountDatastore;
        this.tokenController = tokenController;
    }

    @Given("A registered user")
    public void aRegisteredUser() {
        SignupDto dto = new SignupDto();
        dto.setName("bob");
        dto.setCpr("123");
        UUID custId = accountController.signupCustomer(dto);
        this.customer = accountDatastore.getCustomer(custId);
    }

    @And("The user has spent all tokens")
    public void theUserHasSpentAllTokens() {

        assertTrue(tokenManager.GetTokens(customer.getAccountId()).stream().allMatch(Token::isUsed));
    }

    @When("The user requests {int} of tokens")
    public void theUserRequestsANumberOfTokens(int arg0) {
        RequestTokenDto dto = new RequestTokenDto();
        dto.setAmount(arg0);
        dto.setCustomerId(this.customer.getAccountId());
        tokenList = tokenController.requestTokens(dto);
        //tokenList = tokenManager.RequestTokens(this.customer, arg0);
    }

    @Then("The user receives {int} tokens")
    public void theUserReceivesNumberTokens(int arg0) {
        List<Token> tokenList = this.tokenManager.GetTokens(this.customer.getAccountId());
        assertEquals(arg0,tokenList.size());
        assertEquals(this.tokenList, tokenList);
    }

    @And("The user has {int} unused token")
    public void theUserHasUnusedToken(int arg0) {
        tokenManager.RequestTokens(customer.getAccountId(),arg0);
        assertEquals(arg0, tokenManager.GetTokens(customer.getAccountId()).stream().filter(t ->!t.isUsed()).count());
    }



    @Then("The user has {int} unused tokens")
    public void theUserHasUnusedTokens(int arg0) {
        assertEquals(arg0, tokenManager.GetTokens(customer.getAccountId()).stream().filter(t ->!t.isUsed()).count());
    }



    @And("The user already has {int} unused tokens")
    public void theUserAlreadyHasUnusedTokens(int arg0) {
        tokenManager.RequestTokens(customer.getAccountId(),arg0);
    }

    private IllegalArgumentException excp = null;
    @When("The user requests {int} tokens")
    public void theUserRequestsTokens(int arg0) {
        try{
            RequestTokenDto dto = new RequestTokenDto();
            dto.setAmount(arg0);
            dto.setCustomerId(this.customer.getAccountId());
            tokenController.requestTokens(dto);
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
            List<Token> out = tokenManager.RequestTokens(customer.getAccountId(),arg0);
            assertEquals(arg0, out.size());
        }
    }


}
