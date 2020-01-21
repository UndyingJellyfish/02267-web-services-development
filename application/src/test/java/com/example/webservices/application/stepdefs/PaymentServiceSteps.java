package com.example.webservices.application.stepdefs;

import com.example.webservices.application.stepdefs.json.Account;
import com.example.webservices.application.stepdefs.json.User;
import com.example.webservices.library.dataTransferObjects.RequestTokenDto;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PaymentServiceSteps extends AbstractSteps {
    private UUID merchantId;
    private BigDecimal amount;
    private UUID tokenId;
    private TransactionDto transactionDto;
    private UUID customerId;
    private String merchantBankId;
    private String customerBankId;

    private final String bankBaseRestUrl = "http://fastmoney-00.compute.dtu.dk";

    /**
     * @author s164434
     */
    @After
    public void tearDown() {
        if(merchantBankId != null && !merchantBankId.equals("")){
            executeDelete( bankBaseRestUrl + "/rest/accounts/" + merchantBankId);
        }
        if(customerBankId != null && !customerBankId.equals("")){
            executeDelete(bankBaseRestUrl + "/rest/accounts/" + customerBankId);
        }
    }

    /**
     * @author s164424
     */
    @When("The merchant initiates the transaction")
    public void theMerchantInitiatesTheTransaction() {

        try {
            String description = "Transfer";
            TransactionDto dto = TransactionDto.Create();
            dto.setAmount(amount);
            dto.setTokenId(tokenId);
            dto.setCreditorId(merchantId);
            dto.setDescription(description);
            testContext().setPayload(dto);
            executePost("/payment/transfer");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * @author s164434
     */
    @Then("The transaction should go through")
    public void theTransactionShouldGoThrough() {
        assertEquals(HttpStatus.OK.value(), testContext().getResponse().getStatusCode());
    }

    /**
     * @author s164407
     */
    @When("The merchant initiates the invalid transaction")
    public void theMerchantInitiatesTheInvalidTransaction() {
        String invalidDesc = "12312oi3j4to3j4gp24ijgip24utgi24noi4untg";
        TransactionDto dto = TransactionDto.Create();
        dto.setAmount(amount);
        dto.setTokenId(tokenId);
        dto.setCreditorId(merchantId);
        dto.setDescription(invalidDesc);
        testContext().setPayload(dto);
        executePost("/payment/transfer");
    }

    /**
     * @author s164407
     */
    @Then("The transaction should fail")
    public void theTransactionShouldFail() {
        assertEquals(HttpStatus.NOT_FOUND.value(), testContext().getResponse().statusCode());
    }

    /**
     * @author s164424
     */
    @Given("A merchant")
    public void aMerchant() {
        merchantBankId = "";
        try {
            User usr = new User();
            usr.setCprNumber(UUID.randomUUID().toString());
            usr.setFirstName("Alice");
            usr.setLastName("Alice");
            Account json = new Account();
            json.setBalance(new BigDecimal("10000"));
            json.setUser(usr);

            testContext().setPayload(json);
            executePost(bankBaseRestUrl + "/rest/accounts");
            merchantBankId = getBody(String.class);
        } catch (Exception e){
            fail(e.getMessage());
        }
        SignupDto dto = new SignupDto("Alice", "1231231233", merchantBankId);
        try {
            testContext().setPayload(dto);
            executePost("/account/merchant");
            merchantId = getBody(UUID.class);
        } catch (ResponseStatusException e) {
            fail(e.getMessage());
        }
    }


    /**
     * @author s164424
     */
    @And("A valid token")
    public void aValidToken() {

        customerBankId = "";
        try {
            User usr = new User();
            usr.setCprNumber(UUID.randomUUID().toString());
            usr.setFirstName("Bob");
            usr.setLastName("Bob");
            Account json = new Account();
            json.setBalance(new BigDecimal("10000"));
            json.setUser(usr);

            testContext().setPayload(json);
            executePost(bankBaseRestUrl + "/rest/accounts");
            customerBankId = getBody(String.class);
        } catch (Exception e){
            fail(e.getMessage());
        }

        SignupDto dto = new SignupDto("Bob", "21312321412", customerBankId);
        try {
            testContext().setPayload(dto);
            executePost("/account/customer");
            customerId = getBody(UUID.class);
        } catch (ResponseStatusException e) {
            fail(e.getMessage());
        }
        RequestTokenDto dto2 = new RequestTokenDto();
        dto2.setAmount(1);
        dto2.setCustomerId(customerId);
        testContext().setPayload(dto2);
        executePost("/tokens");
        tokenId = Arrays.stream(getBody(UUID[].class))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    /**
     * @author s164424
     */
    @And("A positive amount")
    public void aPositiveAmount() {
        amount = new BigDecimal("100");
    }

    /**
     * @author s164424
     */
    @And("A token that doesn't exist")
    public void aTokenThatDoesnTExist() {
        tokenId = UUID.randomUUID();
    }

    /**
     * @author s164434
     */
    @And("A token that has already been used")
    public void aTokenThatHasAlreadyBeenUsed() {
        customerBankId = "";
        try {
            User usr = new User();
            usr.setCprNumber(UUID.randomUUID().toString());
            usr.setFirstName("Bob");
            usr.setLastName("Bob");
            Account json = new Account();
            json.setBalance(new BigDecimal("10000"));
            json.setUser(usr);

            testContext().setPayload(json);
            executePost(bankBaseRestUrl + "/rest/accounts");
            customerBankId = getBody(String.class);
        } catch (Exception e){
            fail(e.getMessage());
        }

        SignupDto dto = new SignupDto("Bob", "21312321412", customerBankId);
        try {
            testContext().setPayload(dto);
            executePost("/account/customer");
            customerId = getBody(UUID.class);
        } catch (ResponseStatusException e) {
            fail();
        }
        RequestTokenDto dto2 = new RequestTokenDto();
        dto2.setAmount(1);
        dto2.setCustomerId(customerId);
        testContext().setPayload(dto2);
        executePost("/tokens");
        tokenId = Arrays.stream(getBody(UUID[].class))
                .findFirst()
                .orElseThrow(RuntimeException::new);
        try {
            TransactionDto dto3 = TransactionDto.Create();
            dto3.setDescription("Something");
            dto3.setCreditorId(this.merchantId);
            dto3.setAmount(amount);
            dto3.setTokenId(tokenId);
            testContext().setPayload(dto3);
            executePost("/payment/transfer");
        } catch (ResponseStatusException e) {
            fail();
        }

    }

    /**
     * @author s164434
     */
    @Then("The transaction should fail and inform that the token is used")
    public void theTransactionShouldFailAndInformThatTheTokenIsUsed() {

        assertEquals(HttpStatus.BAD_REQUEST.value(), testContext().getResponse().statusCode());
    }

    /**
     * @author s164407
     * @param arg0 the amount to assign in test case
     */
    @And("An invalid {int}")
    public void anInvalidAmount(int arg0) {
        amount = new BigDecimal(arg0);
    }

    /**
     * @author s164407
     */
    @Then("The transaction should fail and inform that the amount is invalid")
    public void theTransactionShouldFailAndInformThatTheAmountIsInvalid() {
        assertEquals(HttpStatus.BAD_REQUEST.value(), testContext().getResponse().statusCode());
    }

    /**
     * @author s164434
     */
    @Given("A transaction")
    public void aTransaction() {

        //this.bank = mock(IBank.class);
        SignupDto dto = new SignupDto("Bob", "123", UUID.randomUUID().toString());

        SignupDto dto2 = new SignupDto("jens", "12345678", UUID.randomUUID().toString());

        try {
            testContext().setPayload(dto);
            executePost("/account/customer");
            customerId = getBody(UUID.class);
            testContext().setPayload(dto2);
            executePost("/account/merchant");
            merchantId = getBody(UUID.class);
        } catch (Exception e) {
            fail();
        }

        RequestTokenDto dto3 = new RequestTokenDto();
        dto3.setCustomerId(customerId);
        dto3.setAmount(1);
        testContext().setPayload(dto3);
        executePost("/tokens");
        tokenId = Arrays.stream(getBody(UUID[].class))
                .findFirst()
                .orElseThrow(RuntimeException::new);

        amount = new BigDecimal("150.0");
        try {
            TransactionDto dto4 = TransactionDto.Create();
            dto4.setAmount(amount);
            dto4.setCreditorId(merchantId);
            dto4.setDebtorId(customerId);
            dto4.setDescription("");
            dto4.setTokenId(tokenId);
            testContext().setPayload(dto4);
            executePost("/payment/transfer");
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * @author s164395
     */
    @When("The customer asks for a refund")
    public void theCustomerAsksForRefund() {
        try {
            TransactionDto dto = TransactionDto.Create();
            dto.setDebtorId(customerId);
            dto.setCreditorId(merchantId);
            dto.setTokenId(tokenId);
            testContext().setPayload(dto);
            executePost("/payment/transfer");
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * @author s164395
     */
    @Then("The transaction should be refunded")
    public void theTransactionShouldBeRefunded() {
        // empty because it's unverifiable in this scope
    }
}
