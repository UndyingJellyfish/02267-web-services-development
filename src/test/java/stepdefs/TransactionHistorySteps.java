package stepdefs;

import dtu.ws.fastmoney.BankServiceException_Exception;
import interfaces.IAccountDatastore;
import interfaces.IBank;
import interfaces.ITokenManager;
import cucumber.api.PendingException;
import exceptions.TokenException;
import interfaces.ITransactionDatastore;
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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


public class TransactionHistorySteps {

    private final ITokenManager tokenManager;
    private final PaymentService paymentService;
    private final ITransactionDatastore transactionDataStore;
    private final IAccountDatastore accountDatastore;
    private Customer customer;
    private Merchant merchant;
    private List<Transaction> expecetedTransactions;
    private IBank bank;

    public TransactionHistorySteps(ITokenManager tokenManager, ITransactionDatastore transactionDataStore, IAccountDatastore accountDatastore){
        this.bank = mock(IBank.class);
        this.tokenManager = tokenManager;
        this.paymentService = new PaymentService(tokenManager, accountDatastore, transactionDataStore, bank);
        this.transactionDataStore = transactionDataStore;
        this.accountDatastore = accountDatastore;
    }

    @Given("A Customer with a transaction history")
    public void aCustomerWithATransactionHistory() {
        this.customer = new Customer("Test Customer");
        this.merchant = new Merchant("Test Merchant");
        accountDatastore.addAccount(customer);
        accountDatastore.addAccount(merchant);
        this.expecetedTransactions = new ArrayList<>();
        List<Token> tokens =  tokenManager.RequestTokens(this.customer, 5);
        for(int i = 0; i < tokens.size(); i++){
            Token token = tokens.get(i);
            BigDecimal amount = new BigDecimal( i == 0 ? 1 : i * 5);
            try {
                this.expecetedTransactions.add(paymentService.transfer(token.getTokenId(),this.merchant.getAccountId(), amount, ""));
                verify(bank, times(1)).transferMoney(
                        argThat( c -> c.getAccountId().equals(token.getCustomer().getAccountId())),
                        argThat(m -> m.getAccountId().equals(merchant.getAccountId())),
                        eq(amount),
                        eq(""));
            } catch (TokenException | BankServiceException_Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<Transaction> transactions;

    @When("The Customer requests the transaction history")
    public void theCustomerRequestsTheTransactionHistory() {
        this.transactions = this.transactionDataStore.getTransactions(this.customer);
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
