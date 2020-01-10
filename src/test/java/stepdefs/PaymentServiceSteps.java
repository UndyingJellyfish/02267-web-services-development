package stepdefs;

import exceptions.TokenException;
import interfaces.IAccountDatastore;
import interfaces.ITokenManager;
import exceptions.TokenException;
import exceptions.UsedTokenException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import main.PaymentService;
import models.Customer;
import models.Merchant;
import models.Token;
import models.Transaction;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class PaymentServiceSteps {
    private Merchant merchant;
    private BigDecimal amount;
    private ITokenManager tokenManager;
    private Token token;
    private PaymentService paymentService;
    private Transaction transaction;
    private Customer customer;
    private IAccountDatastore accountDatastore;
    private boolean refundSuccessful;
    private Exception exception;

    public PaymentServiceSteps(ITokenManager tokenManager, PaymentService paymentService, IAccountDatastore accountDatastore) {
        this.tokenManager = tokenManager;
        this.paymentService = paymentService;
        this.accountDatastore = accountDatastore;
    }


    @When("The merchant initiates the transaction")
    public void theMerchantInitiatesTheTransaction() {
        try {
            transaction = paymentService.pay(token.getTokenId(), merchant.getAccountId(), amount);
        } catch (Exception e) {
            fail();
        }
    }

    @Then("The transaction should go through")
    public void theTransactionShouldGoThrough() {
        assertEquals(customer, transaction.getDebtor());
        assertEquals(merchant, transaction.getCreditor());
        assertEquals(amount, transaction.getAmount());
        assertTrue(token.isUsed());
    }


    @When("The merchant initiates the invalid transaction")
    public void theMerchantInitiatesTheInvalidTransaction() {
        try{
            paymentService.pay(token.getTokenId(), merchant.getAccountId(), amount);
        } catch (Exception e){
            exception = e;
        }
    }

    @Then("The transaction should fail")
    public void theTransactionShouldFail() {
        assertNotNull(exception);
        assertTrue(exception instanceof InvalidTokenException);
    }


    @Given("A merchant")
    public void aMerchant() {
        merchant = new Merchant("Alice");
        accountDatastore.addAccount(merchant);

    }

    @And("A valid token")
    public void aValidToken() {
        customer = new Customer("Bob");
        accountDatastore.addAccount(customer);
        token = tokenManager.RequestToken(customer);
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
        token = tokenManager.RequestToken(customer);
        try {
            tokenManager.UseToken(token.getTokenId());
        } catch (TokenException e) {
            fail();
        }
    }

    @Then("The transaction should fail and inform that the token is used")
    public void theTransactionShouldFailAndInformThatTheTokenIsUsed() {
        assertTrue(exception instanceof UsedTokenException);
    }

    @And("An invalid {int}")
    public void anInvalidAmount(int arg0) {
        amount = new BigDecimal(arg0);
    }

    @Then("The transaction should fail and inform that the amount is invalid")
    public void theTransactionShouldFailAndInformThatTheAmountIsInvalid() {
        assertTrue(exception instanceof IllegalArgumentException);
    }

    @Given("A transaction and a registered customer")
    public void aTransactionAndACustomer() {
        merchant = new Merchant("Jens");
        customer = new Customer("Jacob");
        accountDatastore.addAccount(merchant);
        accountDatastore.addAccount(customer);
        token = tokenManager.RequestToken(customer);
        amount = new BigDecimal(150.0);
        try {
            transaction = paymentService.pay(token.getTokenId(), merchant.getAccountId(), amount);
        } catch (TokenException e) {
            e.printStackTrace();
        }
    }

    @When("The customer asks for a refund")
    public void theCustomerAsksForRefund() {
        refundSuccessful = paymentService.refund(customer.getAccountId(),merchant.getAccountId(),token.getTokenId());
    }

    @Then("The transaction should be refunded")
    public void theTransactionShouldBeRefunded() {
        assertEquals(token.getCustomer().getAccountId(),customer.getAccountId());
        assertTrue(refundSuccessful);
        assertTrue(token.isUsed());
    }
}
