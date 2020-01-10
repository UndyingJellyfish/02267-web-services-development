package stepdefs;

import Interfaces.ITokenManager;
import cucumber.api.PendingException;
import exceptions.TokenException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import main.PaymentService;
import models.Customer;
import models.Merchant;
import models.Token;
import models.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;


public class TransactionHistorySteps {

    private final ITokenManager tokenManager;
    private final PaymentService paymentService;
    //private final ITransactionDataStore transactionDataStore;
    private Customer customer;
    private Merchant merchant;
    private List<Transaction> expecetedTransactions;

    public TransactionHistorySteps(ITokenManager tokenManager, PaymentService paymentService){//, ITransactionDataStore transactionDataStore){
        this.tokenManager = tokenManager;
        this.paymentService = paymentService;
        //this.transactionDataStore = transactionDataStore;
    }

    @Given("A Customer with a transaction history")
    public void aCustomerWithATransactionHistory() {
        this.customer = new Customer("Test Customer");
        this.merchant = new Merchant("Test Merchant");
        this.expecetedTransactions = new ArrayList<>();
        List<Token> tokens =  tokenManager.RequestTokens(this.customer, 5);
        for(int i = 0; i < tokens.size(); i++){
            Token token = tokens.get(i);
            BigDecimal amount = new BigDecimal( i == 0 ? 1 : i * 5);
            try {
                this.expecetedTransactions.add(paymentService.pay(token.getTokenId(),this.merchant.getAccountId(), amount));
            } catch (TokenException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Transaction> transactions;

    @When("The Customer requests the transaction history")
    public void theCustomerRequestsTheTransactionHistory() {
        throw new PendingException();
        //this.transactions = this.transactionDataStore.getTransactions(this.customer);
    }

    @Then("The Customer receives the transaction history")
    public void theCustomerReceivesTheTransactionHistory() {
        assertNotNull(transactions);
        assertEquals(expecetedTransactions.size(), transactions.size());
        for(Transaction transaction : transactions){

            Transaction expectedTransaction = this.expecetedTransactions.stream().filter(t -> t.getTransactionId().equals(transaction.getTransactionId())).findFirst().orElse(null);
            if(expectedTransaction == null){
                fail();
            }
            assertEquals(expectedTransaction.getDebtor().getAccountId(), transaction.getDebtor().getAccountId());
            assertEquals(expectedTransaction.getCreditor().getAccountId(), transaction.getCreditor().getAccountId());

            assertEquals(expectedTransaction.getToken().getTokenId(), transaction.getToken().getTokenId());
            assertEquals(expectedTransaction.getAmount(), transaction.getAmount());
            assertEquals(expectedTransaction.getTransactionDate(), transaction.getTransactionDate());
        }
    }

    @Given("A Merchant with a transaction history")
    public void aMerchantWithATransactionHistory() {
        throw new PendingException();
    }

    @When("The Merchant requests the transaction history")
    public void theMerchantRequestsTheTransactionHistory() {
        throw new PendingException();
    }

    @Then("The Merchant receives the transaction history without customer names")
    public void theMerchantReceivesTheTransactionHistoryWithoutCustomerNames() {
        throw new PendingException();
    }
}
