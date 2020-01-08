package stepdefs;

import cucumber.api.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MyStepdefs {


    @Given("Some {string}")
    public void some(String arg0) {
        throw new PendingException();
    }

    @When("Adding with StringCalc")
    public void addingWithStringCalc() {
        throw new PendingException();
    }

    @Then("Give {int}")
    public void Give(Integer int1) {
        throw new PendingException();
    }
}
