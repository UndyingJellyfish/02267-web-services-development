package stepdefs;

import dtu.ws.fastmoney.BankServiceException_Exception;
import main.exceptions.InvalidTokenException;
import main.exceptions.TokenException;
import main.interfaces.IAccountDatastore;
import main.interfaces.IBank;
import main.interfaces.ITokenManager;
import main.exceptions.UsedTokenException;
import main.interfaces.ITransactionDatastore;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import main.services.PaymentService;
import models.Customer;
import models.Merchant;
import models.Token;
import models.Transaction;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PaymentServiceSteps {
    private final ITransactionDatastore transactionDatastore;
    private Merchant merchant;
    private BigDecimal amount;
    private ITokenManager tokenManager;
    private Token token;
    private PaymentService paymentService;
    private Transaction transaction;
    private Customer customer;
    private IAccountDatastore accountDatastore;
    private Exception exception;

    private IBank bank;

    public PaymentServiceSteps(ITokenManager tokenManager, IAccountDatastore accountDatastore, ITransactionDatastore transactionDatastore) {

        this.bank = mock(IBank.class);
        this.transactionDatastore = transactionDatastore;
        this.tokenManager = tokenManager;
        this.accountDatastore = accountDatastore;
        this.paymentService = new PaymentService(this.tokenManager, this.accountDatastore,this.transactionDatastore, this.bank);
    }


    @When("The merchant initiates the transaction")
    public void theMerchantInitiatesTheTransaction() {
        try {
            transaction = paymentService.transfer(token.getTokenId(), merchant.getAccountId(), amount,"");
            verify(bank, times(1)).transferMoney(
                    argThat( c -> c.getAccountId().equals(token.getCustomer().getAccountId())),
                    argThat(m -> m.getAccountId().equals(merchant.getAccountId())),
                    eq(amount),
                    eq(""));
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
    public void theMerchantInitiatesTheInvalidTransaction() throws BankServiceException_Exception {
        try{
            paymentService.transfer(token.getTokenId(), merchant.getAccountId(), amount,"");
        } catch (Exception e){
            verify(bank, never()).transferMoney(any(), any(), any(), any());
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

    @Given("A transaction")
    public void aTransaction() {
        merchant = new Merchant("Jens");
        customer = new Customer("Jacob");
        accountDatastore.addAccount(merchant);
        accountDatastore.addAccount(customer);
        token = tokenManager.RequestToken(customer);
        amount = new BigDecimal("150.0");
        try {
            transaction = paymentService.transfer(token.getTokenId(), merchant.getAccountId(), amount,"");
            verify(bank, times(1)).transferMoney(
                    argThat(c -> c.getAccountId().equals(token.getCustomer().getAccountId())),
                    argThat(m -> m.getAccountId().equals(merchant.getAccountId())),
                    eq(amount),
                    eq(""));
        } catch (TokenException | BankServiceException_Exception e) {
            e.printStackTrace();
        }
    }
    @When("The customer asks for a refund")
    public void theCustomerAsksForRefund() {
        try {
            paymentService.refund(customer.getAccountId(),merchant.getAccountId(),token.getTokenId());
        } catch (Exception e) {
            fail();
        }
    }

    @Then("The transaction should be refunded")
    public void theTransactionShouldBeRefunded() {
        assertEquals(token.getCustomer().getAccountId(),customer.getAccountId());
        assertTrue(token.isUsed());
    }
}
