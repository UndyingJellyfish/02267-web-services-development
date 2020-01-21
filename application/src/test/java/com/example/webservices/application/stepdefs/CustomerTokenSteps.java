package com.example.webservices.application.stepdefs;

import com.example.webservices.library.dataTransferObjects.RequestTokenDto;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.dataTransferObjects.TokenDto;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.assertj.core.util.Lists;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/** @author s164398 */
public class CustomerTokenSteps extends AbstractSteps {

    private UUID customerId;
    private List<UUID> tokenIdList;
    private List<TokenDto> tokenList;

    public CustomerTokenSteps(){
    }

    @Given("A registered user")
    public void aRegisteredUser() {
        SignupDto dto = new SignupDto("bob", "123", UUID.randomUUID().toString());
        testContext().setPayload(dto);
        executePost("/account/customer");
        customerId = getBody(UUID.class);

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
        assertEquals(arg0, tokenIdList.size());
        for(UUID id : tokenIdList){
            assertNotNull(id);
        }
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
        unusedCount = arg0;
        dto.setAmount(arg0);
        dto.setCustomerId(customerId);
        testContext().setPayload(dto);
        executePost("/tokens");
        Response response = testContext().getResponse();
        if(response.getStatusCode() != HttpStatus.OK.value()){
            fail();
        }
        tokenIdList = Lists.list(getBody(UUID[].class));
        assertEquals(arg0, tokenIdList.size());
    }

    private int unusedCount;

    @Then("The user has {int} unused tokens")
    public void theUserHasUnusedTokens(int arg0) {
        assertEquals(arg0,unusedCount + tokenIdList.size());
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
        tokenIdList = Lists.list(getBody(UUID[].class));
        assertEquals(arg0, tokenIdList.size());
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
            out = Arrays.asList(getBody(UUID[].class));
            assertEquals(arg0, out.size());
        }
    }
}
