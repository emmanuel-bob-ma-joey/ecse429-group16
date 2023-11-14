package features;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import io.restassured.response.Response;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US2_UpdateTodoStepDefinition {

    private Response response;

    // BACKGROUND
    @Given("the todo application is running \\(US2)")
    public void the_todo_application_is_running() throws InterruptedException {
        System.out.println("the_todo_application_is_running");

        HelperFunctions.startApplication();
    }

    @And("the following todos exist in the system \\(US2)")
    public void the_following_todos_with_title_description_doneStatus_exist_in_the_system(
            io.cucumber.datatable.DataTable dataTable) {

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            String title = columns.get("title");
            String description = columns.get("description");
            String doneStatus = columns.get("doneStatus");
            HelperFunctions.createTodo(title, description, Boolean.parseBoolean(doneStatus));
        }
    }

    // Normal Scenario
    @Given("a todo with {int} exists in the system \\(US2)")
    public void a_todo_with_id_exists_in_the_system(int id) {
        Response response = HelperFunctions.getAllTodos("");
        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        for (Map<String, String> todo : todos) {
            int todoId = Integer.parseInt(todo.get("id"));
            if (todoId == id) {
                assertEquals(id, todoId);
                break;
            }
        }
    }

    @When("I update the todo with {int} with a new description {string} \\(US2)")
    public void i_update_the_todo_with_id_with_a_new_description(int id, String description) {
        Response response = HelperFunctions.updateTodoWithId(id, null, description, false);
        assertEquals(200, response.getStatusCode());
    }

    @Then("the todo with {int} should have title {string}, description {string}, status {string} \\(US2)")
    public void the_todo_with_id_should_have_a_description(int id, String title, String description,
            String doneStatus) {
        Response response = HelperFunctions.getTodoWithId(id);

        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        Map<String, String> todo = todos.get(0);
        assertEquals(title, todo.get("title"));
        assertEquals(description, todo.get("description"));
        assertEquals(doneStatus, todo.get("doneStatus"));
    }

    // Alternate Scenario

    @Given("a incomplete todo with id {int} exists in the system \\(US2)")
    public void a_todo_with_id_with_doneStatus_exists_in_the_system(int id) {
        Response response = HelperFunctions.getAllTodos("");
        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        for (Map<String, String> todo : todos) {
            int todoId = Integer.parseInt(todo.get("id"));
            if (todoId == id) {
                assertEquals(id, todoId);
                assertEquals("false", todo.get("doneStatus").toString());
                break;
            }
        }
    }

    @When("I update the todo with id {int} with {string} \\(US2)")
    public void when_i_update_the_todo_with_id_with(int id, String doneStatus) {
        Response response = HelperFunctions.updateTodoWithId(id, null, null, Boolean.parseBoolean(doneStatus));
        assertEquals(200, response.getStatusCode());
    }

    @Then("the todo with {int} should have an updated status {string} \\(US2)")
    public void the_todo_with_id_should_have_an_updated_status(int id, String doneStatus) {
        Response response = HelperFunctions.getTodoWithId(id);

        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        Map<String, String> todo = todos.get(0);
        assertEquals(doneStatus, todo.get("doneStatus"));
    }

    // Error Scenario

    @Given("a todo with {int} that exists in the system \\(US2)")
    public void a_todo_with_id_that_exists_in_the_system(int id) {
        Response response = HelperFunctions.getAllTodos("");
        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        for (Map<String, String> todo : todos) {
            int todoId = Integer.parseInt(todo.get("id"));
            if (todoId == id) {
                assertEquals(id, todoId);
                break;
            }
        }
    }

    @When("I update the todo with id {int} with invalid doneStatus {string} \\(US2)")
    public void i_update_the_todo_with_id_with_invalid_doneStatus(int id, String doneStatus) {
        response = HelperFunctions.updateTodoWithIdAndInvalidDoneStatus(id, null, doneStatus);
        assertEquals(400, response.getStatusCode());
    }

    @Then("I should receive a {int} response with {string} \\(US2)")
    public void i_should_receive_a_response_with(int statusCode, String errorMessage) {
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(errorMessage, response.jsonPath().getString("errorMessages[0]"));
    }

}