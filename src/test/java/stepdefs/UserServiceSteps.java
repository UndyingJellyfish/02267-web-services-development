package stepdefs;

import main.accounts.AccountController;
import main.accounts.SignupDto;
import main.dataAccess.IAccountDatastore;
import main.exceptions.DuplicateEntryException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import main.accounts.AccountService;
import main.exceptions.EntryNotFoundException;
import main.models.Customer;
import main.models.Merchant;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.Assert.*;

public class UserServiceSteps {
    private String customerName;
    private AccountController accountController;
    private IAccountDatastore accountStore;
    private UUID customerId;
    private String merchantName;
    private UUID merchantId;


    public UserServiceSteps(AccountController accountController, IAccountDatastore accountStore) {
        this.accountController = accountController;
        this.accountStore = accountStore;
    }

    @Given("The name of a customer")
    public void theNameOfACustomer() {
        customerName = "Edvard";
    }

    @When("The customer signs up")
    public void theCustomerSignsUp() {
        try {
            SignupDto dto = new SignupDto();
            dto.setCpr("lol jg er cpr");
            dto.setName(customerName);
            customerId = accountController.signupCustomer(dto);
        } catch (ResponseStatusException e) {
            fail();
        }
    }

    @Then("The customer should be signed up")
    public void theCustomerShouldBeSignedUp() {
        assertNotNull(customerId);
        Customer customer = null;
        try {
            customer = accountStore.getCustomer(customerId);
        } catch (EntryNotFoundException e) {
            fail();
        }
        assertNotNull(customer);
        assertEquals(customer.getName(),customerName);
    }

    @Given("The name of a merchant")
    public void theNameOfAMerchant() {
        merchantName = "Khajit";
    }

    @When("The merchant signs up")
    public void theMerchantSignsUp() {
        try {
            SignupDto dto = new SignupDto();
            dto.setName(merchantName);
            dto.setCpr("123");
            merchantId = accountController.signupMerchant(dto);
        } catch (ResponseStatusException e) {
            fail();
        }
    }

    @Then("The merchant should be signed up")
    public void theMerchantShouldBeSignedUp() {
        assertNotNull(merchantId);
        Merchant merchant = null;
        try {
            merchant = accountStore.getMerchant(merchantId);
        } catch (EntryNotFoundException e) {
            fail();
        }
        assertNotNull(merchant);
        assertEquals(merchant.getName(),merchantName);
    }
}
