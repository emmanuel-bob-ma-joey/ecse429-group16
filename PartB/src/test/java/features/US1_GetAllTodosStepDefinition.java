package features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import javax.naming.Context;

import io.cucumber.junit.Cucumber;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US1_GetAllTodosStepDefinition {

    // BACKGROUND
    @Given("the application is running")
    public void the_todo_application_is_running() throws InterruptedException {
        System.out.println("the_todo_application_is_running");
        HelperFunctions.startApplication();
        assertEquals(true, true);

    }

    @When("I get all TODOS")
    public void i_get_all_TODOS() {
        System.out.println("I get all TODOS");
    }

    @Then("I receive {int}")
    public void i_receive(Integer int1) {
        System.out.println("I receive" + int1);
        assertEquals(200, int1);
    }
}