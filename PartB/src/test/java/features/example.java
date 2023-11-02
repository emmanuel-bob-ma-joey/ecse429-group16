package features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.junit.Cucumber;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class example {
    @Given("account balance is {double}")
    public void givenAccountBalance(Double initialBalance) {
        System.out.println("givenAccountBalance");
        assertEquals(0.0, initialBalance);
    }

    @When("the account is credited with {double}")
    public void whenIDeposit(Double depositAmount) {
        assertTrue(depositAmount > 0);
    }

    @Then("account should have a balance of {double}")
    public void thenAccountBalanceIs(Double newBalance) {
        assertEquals(10.0, newBalance);
    }

    // other step definitions

}