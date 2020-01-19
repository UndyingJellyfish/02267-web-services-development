package com.example.webservices.application.stepdefs;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.interfaces.IAccountService;
import gherkin.deps.com.google.gson.reflect.TypeToken;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.example.webservices.library.dataTransferObjects.RequestTokenDto;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.assertj.core.util.Lists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class CustomerTokenSteps extends AbstractSteps {

    private UUID customerId;
    private List<UUID> tokenIdList;
    private List<TokenDto> tokenList;

    public CustomerTokenSteps(){
    }

    @Given("A registered user")
    public void aRegisteredUser() {
        SignupDto dto = new SignupDto("bob", "123", UUID.randomUUID().toString());
        RestTemplate template = new RestTemplate();
        //ResponseEntity<UUID> response = template.postForEntity("http://localhost:8080/account/customer", dto, UUID.class);
        testContext().setPayload(dto);
        executePost("/account/customer");
        customerId = getBody(UUID.class);
        //UUID customerId = response.getBody();

    }

    @And("The user has spent all tokens")
    public void theUserHasSpentAllTokens() {

        executeGet("/tokens/{customerId}", new HashMap<String, String>(){{put("customerId",customerId.toString());}});
        List<TokenDto> tokens = Lists.list(getBody(TokenDto[].class));
        assertTrue(tokens.stream().allMatch(TokenDto::isUsed));
    }

    @When("The user requests {int} of tokens")
    public void theUserRequestsANumberOfTokens(int arg0) {
        RequestTokenDto dto = new RequestTokenDto();
        dto.setAmount(arg0);
        dto.setCustomerId(this.customerId);
        testContext().setPayload(dto);
        executePost("/tokens");
        tokenIdList = Lists.list(getBody(UUID[].class));
    }

    @Then("The user receives {int} tokens")
    public void theUserReceivesNumberTokens(int arg0) {
        List<TokenDto> tokenList = null;
        executeGet("/tokens/{customerId}", new HashMap<String, String>(){{put("customerId", customerId.toString());}});
        if(testContext().getResponse().statusCode() != HttpStatus.OK.value()){
            fail();
        }
        tokenList = Lists.list(getBody(TokenDto[].class));
        assertEquals(arg0,tokenList.size());
    }

    @After
    public void tearDown(){
        if(customerId != null){

            executeDelete("/account/{accountId}",
                    new HashMap<String, String>(){{put("accountId", customerId.toString());}});

        }
    }

    @And("The user has {int} unused token")
    public void theUserHasUnusedToken(int arg0) {
        RequestTokenDto dto = new RequestTokenDto();
        dto.setAmount(arg0);
        dto.setCustomerId(customerId);
        testContext().setPayload(dto);
        executePost("/tokens");
        Response response = testContext().getResponse();
        ResponseBody body = response.getBody();
        String message = body.prettyPrint();
        if(response.getStatusCode() != HttpStatus.OK.value()){
            fail();
        }
        executeGet("/tokens/{customerId}", new HashMap<String, String>(){{put("customerId", customerId.toString());}});
        if(testContext().getResponse().statusCode() != HttpStatus.OK.value()){
            fail();
        }

        List<TokenDto> tokens = Lists.list(getBody(TokenDto[].class));
        assertEquals(arg0, tokens.stream().filter(t ->!t.isUsed()).count());
    }



    @Then("The user has {int} unused tokens")
    public void theUserHasUnusedTokens(int arg0) {
        executeGet("/tokens/{customerId}", new HashMap<String, String>(){{put("customerId", customerId.toString());}});
        if(testContext().getResponse().statusCode() != HttpStatus.OK.value()){
            fail();
        }
        List<TokenDto> tokens = Lists.list(getBody(TokenDto[].class));
        assertEquals(arg0, tokens.stream().filter(t ->!t.isUsed()).count());
    }



    @And("The user already has {int} unused tokens")
    public void theUserAlreadyHasUnusedTokens(int arg0) {
        RequestTokenDto dto = new RequestTokenDto();
        dto.setCustomerId(customerId);
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
        dto.setCustomerId(this.customerId);
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
            dto.setCustomerId(customerId);
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
        executeGet("/tokens/{customerId}", new HashMap<String, String>(){{put("customerId", customerId.toString());}});
        if(testContext().getResponse().statusCode() != HttpStatus.OK.value()){
            fail();
        }
        this.tokenList = Lists.list(getBody(TokenDto[].class));
    }

    @Then("He should get his tokens")
    public void heShouldGetHisTokens() {
        assertEquals(2, this.tokenList.stream().filter(t -> !t.isUsed()).count());
        assertTrue(this.tokenList.size() >= 2);
    }
}
