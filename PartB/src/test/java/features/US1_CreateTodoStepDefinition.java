package features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import java.util.Map;

import io.restassured.response.Response;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US1_CreateTodoStepDefinition {

    private Response response;

    // BACKGROUND
    @Given("the application is running \\(US1)")
    public void the_todo_application_is_running() throws InterruptedException {
        HelperFunctions.startApplication();
    }

    // Normal Scenario

    @Given("the todo with title {string} and the description {string} does not exist in the system \\(US1)")
    public void the_todo_with_title_and_the_description_does_not_exist_in_the_system(String title,
            String description) {

        Response response = HelperFunctions.getAllTodos("");
        List<Map<String, String>> todos = response.jsonPath().getList("todos");

        for (Map<String, String> todo : todos) {
            assertNotEquals(todo.get("title"), title);
            assertNotEquals(todo.get("description"), description);
        }
    }

    @When("I create a todo with the title {string} and description {string} \\(US1)")
    public void i_create_a_todo_with_title_and_the_description(String title, String description) {
        Response response = HelperFunctions.createTodo(title, description, false);
        assertEquals(201, response.getStatusCode());
    }

    @Then("the todo with title {string} and the description {string} shall exist in the system \\(US1)")
    public void the_todo_with_title_and_the_description_should_be_in_the_system(String title, String description) {
        Response response = HelperFunctions.getAllTodos("");
        List<Map<String, String>> todos = response.jsonPath().getList("todos");

        for (Map<String, String> todo : todos) {
            String todoTitle = todo.get("title");
            String todoDescription = todo.get("description");

            // check if the created todo is in the list of todos with correct data
            if (todoTitle.equals(title) && todoDescription.equals(description)) {
                assertEquals(todoTitle, title);
                assertEquals(todoDescription, description);
                break;
            }
        }
    }

    // Alternate Scenario

    @Given("the todo with title {string} does not exist in the system \\(US1)")
    public void the_todo_with_title_does_not_exist_in_the_system(String title) {
        Response response = HelperFunctions.getAllTodos("");
        List<Map<String, String>> todos = response.jsonPath().getList("todos");

        for (Map<String, String> todo : todos) {
            assertNotEquals(todo.get("title"), title);
        }
    }

    @When("I create a todo with the title {string} and doneStatus {string} \\(US1)")
    public void i_create_a_todo_with_the_title(String title, String doneStatus) {
        Response response = HelperFunctions.createTodo(title, "", Boolean.parseBoolean(doneStatus));
        assertEquals(201, response.getStatusCode());
    }

    @Then("the todo with title {string} with a doneStatus {string} shall exist in the system \\(US1)")
    public void the_todo_with_title_with_a_doneStatus_should_be_in_the_system(String title, String doneStatus) {
        boolean doneStatusBool = Boolean.parseBoolean(doneStatus);

        Response response = HelperFunctions.getAllTodos("");
        List<Map<String, String>> todos = response.jsonPath().getList("todos");

        for (Map<String, String> todo : todos) {
            String todoTitle = todo.get("title");
            Boolean todoDoneStatus = Boolean.parseBoolean(todo.get("doneStatus"));

            // check if the created todo is in the list of todos with correct data
            if (todoTitle.equals(title) && todoDoneStatus == doneStatusBool) {
                assertEquals(todoTitle, title);
                assertEquals(todoDoneStatus.toString(), doneStatus);
                break;
            }
        }
    }

    // Error Scenario
    @When("I create a todo with empty title \\(US1)")
    public void i_create_a_todo_with_empty_title() {
        response = HelperFunctions.createTodo("", "description", false);

    }

    @Then("the status code shall be {int} with an error message {string} \\(US1)")
    public void the_todo_with_empty_title_should_not_be_in_the_system(int statusCode, String errorMessage) {
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(errorMessage, response.jsonPath().getString("errorMessages[0]"));
    }
}