package com.example.webservices.application.stepdefs;

import dtu.ws.fastmoney.BankServiceException_Exception;
import com.example.webservices.application.accounts.AccountController;
import com.example.webservices.application.accounts.SignupDto;
import com.example.webservices.application.dataAccess.InMemoryDatastore;
import com.example.webservices.application.bank.IBank;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.example.webservices.application.tokens.RequestTokenDto;
import com.example.webservices.application.tokens.TokenController;
import com.example.webservices.application.models.Token;
import com.example.webservices.application.models.Transaction;
import com.example.webservices.application.tokens.TokenManager;
import com.example.webservices.application.transfers.PaymentService;
import com.example.webservices.application.transfers.PaymentController;
import com.example.webservices.application.transfers.TransactionDto;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PaymentServiceSteps {
    private final AccountController accountController;
    private InMemoryDatastore store;
    private final PaymentController paymentController;
    private UUID merchantId;
    private BigDecimal amount;
    private TokenController tokenController;
    private Token token;
    private Transaction transaction;
    private UUID customerId;
    private Exception exception;

    private IBank bank;

    public PaymentServiceSteps(TokenController tokenController, AccountController accountController, InMemoryDatastore store) {
        this.accountController = accountController;
        this.store = store;
        this.bank = mock(IBank.class);
        this.paymentController = new PaymentController(new PaymentService(new TokenManager(store,store),store,store,this.bank));
        this.tokenController = tokenController;
    }

    @After
    public void tearDown(){
        this.store.flush();
    }


    @When("The merchant initiates the transaction")
    public void theMerchantInitiatesTheTransaction() {

        try {
            TransactionDto dto = TransactionDto.Create();
            dto.setAmount(amount);
            dto.setTokenId(token.getTokenId());
            dto.setMerchantId(merchantId);
            dto.setDescription("");
            paymentController.TransferMoney(dto);
            verify(bank, times(1)).transferMoney(
                    argThat( c -> c.getAccountId().equals(token.getCustomer().getAccountId())),
                    argThat(m -> m.getAccountId().equals(merchantId)),
                    eq(amount),
                    eq(""));
            transaction = store.GetTransactionByTokenId(token.getTokenId());

        } catch (Exception e) {
            fail();
        }
    }

    @Then("The transaction should go through")
    public void theTransactionShouldGoThrough() {
        assertEquals(customerId, transaction.getDebtor().getAccountId());
        assertEquals(merchantId, transaction.getCreditor().getAccountId());
        assertEquals(amount, transaction.getAmount());
        assertTrue(token.isUsed());
    }


    @When("The merchant initiates the invalid transaction")
    public void theMerchantInitiatesTheInvalidTransaction() throws BankServiceException_Exception {
        String invalidDesc = "12312oi3j4to3j4gp24ijgip24utgi24noi4untg";
        this.bank = mock(IBank.class);
        try{
            TransactionDto dto = TransactionDto.Create();
            dto.setAmount(amount);
            dto.setTokenId(token.getTokenId());
            dto.setMerchantId(merchantId);
            dto.setDescription(invalidDesc);
            paymentController.TransferMoney(dto);
            transaction = store.GetTransactionByTokenId(token.getTokenId());
        } catch (ResponseStatusException e){
            verify(bank, never()).transferMoney(any(), any(), any(), any());
            exception = e;
        }
    }

    @Then("The transaction should fail")
    public void theTransactionShouldFail() {
        assertNotNull(exception);
        assertTrue(exception instanceof ResponseStatusException);
    }


    @Given("A merchant")
    public void aMerchant() {
        SignupDto dto = new SignupDto();
        dto.setName("Alice");
        dto.setCpr("123");
        try {
            merchantId = accountController.signupMerchant(dto);
        } catch (ResponseStatusException e) {
            fail();
        }

    }

    @And("A valid token")
    public void aValidToken() {
        SignupDto dto = new SignupDto();
        dto.setName("Bob");
        dto.setCpr("123");
        try {
           customerId = accountController.signupCustomer(dto);
        } catch (ResponseStatusException e) {
            fail();
        }
        RequestTokenDto dto2 = new RequestTokenDto();
        dto2.setAmount(1);
        dto2.setCustomerId(customerId);
        token = tokenController.requestTokens(dto2).stream().findFirst().orElseThrow(RuntimeException::new);
    }

    @And("A positive amount")
    public void aPositiveAmount() {
        amount = new BigDecimal("100");
    }

    @And("A token that doesn't exist")
    public void aTokenThatDoesnTExist() {
        token = new Token(null);
    }

    @And("A token that has already been used")
    public void aTokenThatHasAlreadyBeenUsed() {
        SignupDto dto = new SignupDto();
        dto.setName("Bob");
        dto.setCpr("123");
        UUID custId = null;
        try {
            custId = accountController.signupCustomer(dto);
        } catch (ResponseStatusException e) {
            fail();
        }
        RequestTokenDto dto2 = new RequestTokenDto();
        dto2.setAmount(1);
        dto2.setCustomerId(custId);
        token = tokenController.requestTokens(dto2).stream().findFirst().orElseThrow(RuntimeException::new);
        try {
            TransactionDto dto3 = TransactionDto.Create();
            dto3.setDescription("Something");
            dto3.setMerchantId(this.merchantId);
            dto3.setAmount(amount);
            dto3.setTokenId(token.getTokenId());
            paymentController.TransferMoney(dto3);
        } catch (ResponseStatusException e) {
                fail();
        }
    }

    @Then("The transaction should fail and inform that the token is used")
    public void theTransactionShouldFailAndInformThatTheTokenIsUsed() {
        assertTrue(exception instanceof ResponseStatusException);
    }

    @And("An invalid {int}")
    public void anInvalidAmount(int arg0) {
        amount = new BigDecimal(arg0);
    }

    @Then("The transaction should fail and inform that the amount is invalid")
    public void theTransactionShouldFailAndInformThatTheAmountIsInvalid() {
        assertTrue(exception instanceof ResponseStatusException);
    }

    @Given("A transaction")
    public void aTransaction() {

        //this.bank = mock(IBank.class);
        SignupDto dto = new SignupDto();
        dto.setName("Bob");
        dto.setCpr("123");

        SignupDto dto2 = new SignupDto();
        dto2.setName("jens");
        dto2.setCpr("12345678");

        try{
            customerId = accountController.signupCustomer(dto);
            merchantId = accountController.signupMerchant(dto2);
        }catch(Exception e){
            fail();
        }

        RequestTokenDto dto3 = new RequestTokenDto();
        dto3.setCustomerId(customerId);
        dto3.setAmount(1);
        token = tokenController.requestTokens(dto3).stream().findFirst().orElseThrow(RuntimeException::new);
        amount = new BigDecimal("150.0");
        try {
            TransactionDto dto4 = TransactionDto.Create();
            dto4.setAmount(amount);
            dto4.setMerchantId(merchantId);
            dto4.setCustomerId(customerId);
            dto4.setDescription("");
            dto4.setTokenId(token.getTokenId());
            paymentController.TransferMoney(dto4);
            verify(bank, times(1)).transferMoney(
                    any(),any(),any(),any());
        } catch (BankServiceException_Exception e) {
            fail();
        }
    }

    @When("The customer asks for a refund")
    public void theCustomerAsksForRefund() {
        try {
            TransactionDto dto = TransactionDto.Create();
            dto.setCustomerId(customerId);
            dto.setMerchantId(merchantId);
            dto.setTokenId(token.getTokenId());
            paymentController.RefundTransaction(dto);
        } catch (Exception e) {
            fail();
        }
    }

    @Then("The transaction should be refunded")
    public void theTransactionShouldBeRefunded() {
        assertEquals(token.getCustomer().getAccountId(), customerId);
        assertTrue(token.isUsed());
    }
}
