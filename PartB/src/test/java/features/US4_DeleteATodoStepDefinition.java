package features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import java.util.Map;

import io.restassured.response.Response;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US4_DeleteATodoStepDefinition {

    private Response response;

    // BACKGROUND
    @Given("the todo application is running \\(US4)")
    public void the_todo_application_is_running() throws InterruptedException {
        System.out.println("the_todo_application_is_running");

        HelperFunctions.startApplication();

        // delete all todos so that we can start fresh
        Response response = HelperFunctions.getAllTodos("");
        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        for (Map<String, String> todo : todos) {
            int id = Integer.parseInt(todo.get("id"));
            HelperFunctions.deleteTodoWithId(id);
        }
    }

    @And("the following todos exist in the system \\(US4)")
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
    @Given("the todo with id {int} exists in the system \\(US4)")
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

    @When("I delete the todo with id {int} \\(US4)")
    public void i_delete_the_todo_with_id(int id) {
        Response response = HelperFunctions.deleteTodoWithId(id);
        assertEquals(200, response.getStatusCode());
    }

    @Then("the todo with id {int} should not exist in the system \\(US4)")
    public void the_todo_with_id_should_not_exist_in_the_system(int id) {
        Response response = HelperFunctions.getAllTodos("");
        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        for (Map<String, String> todo : todos) {
            int todoId = Integer.parseInt(todo.get("id"));
            assertNotEquals(todoId, id);
        }
    }

    @Then("there should be 1 todo in the system \\(US4)")
    public void there_should_be_1_todos_in_the_system() {
        Response response = HelperFunctions.getAllTodos("");
        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        assertEquals(1, todos.size());
    }

    // Alternate Scenario

    // Error flow
    @Given("the todo with id {int} does not exist in the system \\(US4)")
    public void a_todo_with_id_does_not_exist_in_the_system(int id) {
        response = HelperFunctions.getAllTodos("");
        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        for (Map<String, String> todo : todos) {
            int todoId = Integer.parseInt(todo.get("id"));
            assertNotEquals(todoId, id);
        }
    }

    @When("I delete the todo with inexistant id {int} \\(US4)")
    public void i_delete_the_todo_with_inexistant_id(int id) {
        response = HelperFunctions.deleteTodoWithId(id);
        assertEquals(404, response.getStatusCode());
    }

    @Then("I should receive a status code {int} and see an error message {string} \\(US4)")
    public void then_i_should_receive_a_status_code_and_see_an_error_message(int statusCode, String errorMessage) {
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(errorMessage, response.jsonPath().getString("errorMessages[0]"));
    }
}
