package com.example.webservices.application.stepdefs;

import com.example.webservices.application.accounts.SignupDto;
import com.example.webservices.application.dataAccess.IAccountDatastore;
import com.example.webservices.application.dataAccess.InMemoryDatastore;
import com.example.webservices.application.exceptions.EntryNotFoundException;
import com.example.webservices.application.models.Customer;
import com.example.webservices.application.tokens.TokenDto;
import gherkin.deps.com.google.gson.reflect.TypeToken;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.example.webservices.application.tokens.RequestTokenDto;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class CustomerTokenSteps extends AbstractSteps {

    private Customer customer;
    private List<UUID> tokenIdList;
    private IAccountDatastore accountDatastore;
    private List<TokenDto> tokenList;

    public CustomerTokenSteps(IAccountDatastore accountDatastore){

        this.accountDatastore = accountDatastore;
    }
    @After
    public void tearDown(){
        ((InMemoryDatastore)this.accountDatastore).flush();
    }

    @Given("A registered user")
    public void aRegisteredUser() {
        SignupDto dto = new SignupDto();
        dto.setName("bob");
        dto.setIdentifier("123");
        dto.setBankAccountId(UUID.randomUUID().toString());
        testContext().setPayload(dto);
        executePost("/account/customer");
        UUID customerId = getBody(UUID.class);
        try {
            this.customer = accountDatastore.getCustomer(customerId);
        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    @And("The user has spent all tokens")
    public void theUserHasSpentAllTokens() {

        executeGet("/tokens/{customerId}", new HashMap<String, String>(){{put("customerId",customer.getAccountId().toString());}});
        List<TokenDto> tokens = getBody(new TypeToken<List<TokenDto>>(){}.getType());
        assertTrue(tokens.stream().allMatch(TokenDto::isUsed));
    }

    @When("The user requests {int} of tokens")
    public void theUserRequestsANumberOfTokens(int arg0) {
        RequestTokenDto dto = new RequestTokenDto();
        dto.setAmount(arg0);
        dto.setCustomerId(this.customer.getAccountId());
        testContext().setPayload(dto);
        executePost("/tokens");
        tokenIdList = getBody(new TypeToken<List<UUID>>(){}.getType());
    }

    @Then("The user receives {int} tokens")
    public void theUserReceivesNumberTokens(int arg0) {
        List<TokenDto> tokenList = null;
        executeGet("/tokens/{customerId}", new HashMap<String, String>(){{put("customerId", customer.getAccountId().toString());}});
        if(testContext().getResponse().statusCode() != HttpStatus.OK.value()){
            fail();
        }
        tokenList = getBody(new TypeToken<List<TokenDto>>(){}.getType());
        assertEquals(arg0,tokenList.size());
    }

    @And("The user has {int} unused token")
    public void theUserHasUnusedToken(int arg0) {
        RequestTokenDto dto = new RequestTokenDto();
        dto.setAmount(arg0);
        dto.setCustomerId(customer.getAccountId());
        testContext().setPayload(dto);
        executePost("/tokens");
        Response response = testContext().getResponse();
        ResponseBody body = response.getBody();
        String message = body.prettyPrint();
        if(response.getStatusCode() != HttpStatus.OK.value()){
            fail();
        }
        executeGet("/tokens/{customerId}", new HashMap<String, String>(){{put("customerId", customer.getAccountId().toString());}});
        if(testContext().getResponse().statusCode() != HttpStatus.OK.value()){
            fail();
        }
        List<TokenDto> tokens = getBody(new TypeToken<List<TokenDto>>(){}.getType());
        assertEquals(arg0, tokens.stream().filter(t ->!t.isUsed()).count());
    }



    @Then("The user has {int} unused tokens")
    public void theUserHasUnusedTokens(int arg0) {
        executeGet("/tokens/{customerId}", new HashMap<String, String>(){{put("customerId", customer.getAccountId().toString());}});
        if(testContext().getResponse().statusCode() != HttpStatus.OK.value()){
            fail();
        }
        List<TokenDto> tokens = getBody(new TypeToken<List<TokenDto>>(){}.getType());
        assertEquals(arg0, tokens.stream().filter(t ->!t.isUsed()).count());
    }



    @And("The user already has {int} unused tokens")
    public void theUserAlreadyHasUnusedTokens(int arg0) {
        RequestTokenDto dto = new RequestTokenDto();
        dto.setCustomerId(customer.getAccountId());
        dto.setAmount(arg0);
        testContext().setPayload(dto);
        executePost("/tokens");
        if(testContext().getResponse().statusCode() != HttpStatus.OK.value()){
            fail();
        }
    }

    private int excp = -1;
    @When("The user requests {int} tokens")
    public void theUserRequestsTokens(int arg0) {
            RequestTokenDto dto = new RequestTokenDto();
            dto.setAmount(arg0);
            dto.setCustomerId(this.customer.getAccountId());
            testContext().setPayload(dto);
            executePost("/tokens");
            if(testContext().getResponse().statusCode() == HttpStatus.OK.value()){
                fail();
            }
            excp = testContext().getResponse().statusCode();
    }

    @Then("It should fail")
    public void itShouldFail() {
        assertNotEquals(HttpStatus.OK, excp);
    }

    @And("the user has {int} unused tokens")
    public void theUserHasNumberUnusedTokens(int arg0) {
        if(arg0 > 0){
            List<UUID> out = null;
            RequestTokenDto dto = new RequestTokenDto();
            dto.setCustomerId(customer.getAccountId());
            dto.setAmount(arg0);
            testContext().setPayload(dto);
            executePost("/tokens");
            if(testContext().getResponse().statusCode() != HttpStatus.OK.value()){
                fail();
            }
            out = getBody(new TypeToken<List<UUID>>(){}.getType());
            assertEquals(arg0, out.size());
        }
    }




    @When("The user queries his tokens")
    public void theUserQueriesHisTokens() {
        executeGet("/tokens/{customerId}", new HashMap<String, String>(){{put("customerId", customer.getAccountId().toString());}});
        if(testContext().getResponse().statusCode() != HttpStatus.OK.value()){
            fail();
        }
        this.tokenList = getBody(new TypeToken<List<TokenDto>>(){}.getType());
    }

    @Then("He should get his tokens")
    public void heShouldGetHisTokens() {
        assertEquals(2, this.tokenList.stream().filter(t -> !t.isUsed()).count());
        assertTrue(this.tokenList.size() >= 2);
    }
}
