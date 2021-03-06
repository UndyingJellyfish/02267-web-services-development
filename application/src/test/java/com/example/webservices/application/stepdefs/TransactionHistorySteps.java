package com.example.webservices.application.stepdefs;

import com.example.webservices.application.stepdefs.json.Account;
import com.example.webservices.application.stepdefs.json.User;
import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.RequestTokenDto;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.exceptions.*;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.interfaces.IAccountService;
import com.example.webservices.library.interfaces.IPaymentService;
import com.example.webservices.library.interfaces.IReportingService;
import com.example.webservices.library.interfaces.ITokenManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.After;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

public class TransactionHistorySteps extends AbstractSteps {

    private AccountDto customer;
    private AccountDto merchant;
    private UUID customerId;
    private UUID merchantId;
    private String customerBankId;
    private String merchantBankId;
    private List<TransactionDto> transactions;
    private List<TransactionDto> expectedTransactions;
    private TransactionDto transferDto;

    private final String bankBaseRestUrl = "http://fastmoney-00.compute.dtu.dk";

    /**
     * @author s164410
     */
    @After
    public void tearDown(){
        if(merchantBankId != null && !merchantBankId.equals("")){
            executeDelete( bankBaseRestUrl + "/rest/accounts/" + merchantBankId);
        }
        if(customerBankId != null && !customerBankId.equals("")){
            executeDelete(bankBaseRestUrl + "/rest/accounts/" + customerBankId);
        }
    }

    /**
     * @author s164395
     */
    private String putUserInBank(String first, String last, BigDecimal balance) {
        String id = null;
        try {
            User usr = new User();
            usr.setCprNumber(UUID.randomUUID().toString());
            usr.setFirstName(first);
            usr.setLastName(last);
            Account json = new Account();
            json.setBalance(balance);
            json.setUser(usr);

            testContext().setPayload(json);
            executePost(bankBaseRestUrl + "/rest/accounts");
            id = getBody(String.class);
        } catch (Exception e){
            fail(e.getMessage());
        }
        return id;
    }

    /**
     * @author s164395
     */
    @Given("A Customer with a transaction history")
    public void aCustomerWithATransactionHistory() {
        merchantBankId = putUserInBank("Alice", "Alice", new BigDecimal("10000"));
        customerBankId = putUserInBank("Bob", "Bob", new BigDecimal("1000"));

        SignupDto mDto = new SignupDto("Alice Alice","123",merchantBankId);
        SignupDto cDto = new SignupDto("Bob", "1234",customerBankId);

        testContext().setPayload(cDto);
        executePost("/account/customer");
        this.customerId = getBody(UUID.class);

        testContext().setPayload(mDto);
        executePost("/account/merchant");
        this.merchantId = getBody(UUID.class);

        this.expectedTransactions = new ArrayList<>();
        List<UUID> tokens = null;
        RequestTokenDto rDto = new RequestTokenDto();
        rDto.setAmount(5);
        rDto.setCustomerId(this.customerId);

        testContext().setPayload(rDto);
        executePost("/tokens");
        tokens = Arrays.asList(getBody(UUID[].class));

        for(int i = 0; i < tokens.size(); i++){
            UUID token = tokens.get(i);
            BigDecimal amount = new BigDecimal( i == 0 ? 1 : i * 5);
            transferDto = new TransactionDto(token, merchantId, customerId, amount, "koioikiko" + i, false, new Date());
            expectedTransactions.add(transferDto);
            testContext().setPayload(transferDto);
            executePost("/payment/transfer");
            if(testContext().getResponse().getStatusCode() != HttpStatus.OK.value()){
                fail();
            }
        }
    }

    /**
     * @author s164395
     */
    @When("The Customer requests the transaction history")
    public void theCustomerRequestsTheTransactionHistory() {
        executeGet("/reporting/{accountId}", new HashMap<String, String>(){{put("accountId", customerId.toString());}});
        this.transactions = Arrays.asList(getBody(TransactionDto[].class));
    }

    /**
     * @author s164395
     */
    @Then("The Customer receives the transaction history")
    public void theCustomerReceivesTheTransactionHistory() {
        assertNotNull(transactions);
        assertEquals(expectedTransactions.size(), transactions.size());
        for(TransactionDto transaction : transactions){
            TransactionDto expectedTransaction = this.expectedTransactions
                    .stream()
                    .filter(t -> t.getDescription()
                            .equals(transaction.getDescription()))
                    .findFirst()
                    .orElse(null);
            if(expectedTransaction == null){
                fail();
            }
            assertEquals(expectedTransaction.getDebtorId(), transaction.getDebtorId());
            assertEquals(expectedTransaction.getCreditorId(), transaction.getCreditorId());

            assertEquals(expectedTransaction.getTokenId(), transaction.getTokenId());
            assertEquals(expectedTransaction.getAmount(), transaction.getAmount());
        }
    }

    /*
    * @author s164410
    */
    @Given("A Merchant with a transaction history")
    public void aMerchantWithATransactionHistory() {
        merchantBankId = putUserInBank("Alice", "Alice", new BigDecimal("10000"));
        customerBankId = putUserInBank("Bob", "Bob", new BigDecimal("1000"));

        SignupDto mDto = new SignupDto("Alice Alice","123",merchantBankId);
        SignupDto cDto = new SignupDto("Bob", "1234",customerBankId);

        testContext().setPayload(cDto);
        executePost("/account/customer");
        this.customerId = getBody(UUID.class);

        testContext().setPayload(mDto);
        executePost("/account/merchant");
        this.merchantId = getBody(UUID.class);

        this.expectedTransactions = new ArrayList<>();
        List<UUID> tokens = null;
        RequestTokenDto rDto = new RequestTokenDto();
        rDto.setAmount(5);
        rDto.setCustomerId(this.customerId);

        testContext().setPayload(rDto);
        executePost("/tokens");
        tokens = Arrays.asList(getBody(UUID[].class));

        for(int i = 0; i < tokens.size(); i++){
            UUID token = tokens.get(i);
            BigDecimal amount = new BigDecimal( i == 0 ? 1 : i * 5);
            transferDto = new TransactionDto(token, merchantId, customerId, amount, "dadssadasd" +i, false, new Date());
            expectedTransactions.add(transferDto);
            testContext().setPayload(transferDto);
            executePost("/payment/transfer");
            if(testContext().getResponse().getStatusCode() != HttpStatus.OK.value()){
                fail();
            }
        }
    }

    /**
     * @author s164410
     */
    @When("The Merchant requests the transaction history")
    public void theMerchantRequestsTheTransactionHistory() {
        executeGet("/reporting/{accountId}", new HashMap<String, String>(){{put("accountId", merchantId.toString());}});
        this.transactions = Arrays.asList(getBody(TransactionDto[].class));
    }

    /**
     * @author s164410
     */
    @Then("The Merchant receives the transaction history without customer names")
    public void theMerchantReceivesTheTransactionHistoryWithoutCustomerNames() {
        assertNotNull(transactions);
        assertEquals(expectedTransactions.size(), transactions.size());
        for(TransactionDto transaction : transactions){
            TransactionDto expectedTransaction = this.expectedTransactions
                    .stream()
                    .filter(t -> t.getDescription()
                            .equals(transaction.getDescription()))
                    .findFirst()
                    .orElse(null);
            if(expectedTransaction == null){
                fail();
            }
            assertNull(transaction.getDebtorId());
            assertEquals(expectedTransaction.getCreditorId(), transaction.getCreditorId());

            assertEquals(expectedTransaction.getTokenId(), transaction.getTokenId());
            assertEquals(expectedTransaction.getAmount(), transaction.getAmount());
        }
    }
}
