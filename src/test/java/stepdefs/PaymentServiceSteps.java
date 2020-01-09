package stepdefs;

import Interfaces.IAccountDatastore;
import Interfaces.ITokenManager;
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

    public PaymentServiceSteps(ITokenManager tokenManager, PaymentService paymentService, IAccountDatastore accountDatastore) {
        this.tokenManager = tokenManager;
        this.paymentService = paymentService;
        this.accountDatastore = accountDatastore;
    }

    @Given("A merchant, a valid token and a positive amount")
    public void aMerchantAValidTokenAndAPositiveAmount() {
        merchant = new Merchant("Alice");
        customer = new Customer("Bob");
        accountDatastore.addAccount(merchant);
        accountDatastore.addAccount(customer);
        token = tokenManager.RequestToken(customer);
        amount = new BigDecimal(100.0);
    }

    @When("The merchant initiates the transaction")
    public void theMerchantInitiatesTheTransaction() {
        transaction = paymentService.pay(token.getTokenId(), merchant.getAccountId(), amount);
    }

    @Then("The transaction should go through")
    public void theTransactionShouldGoThrough() {
        assertEquals(customer, transaction.getDebtor());
        assertEquals(merchant, transaction.getCreditor());
        assertEquals(amount, transaction.getAmount());
        assertTrue(token.isUsed());
    }
}
