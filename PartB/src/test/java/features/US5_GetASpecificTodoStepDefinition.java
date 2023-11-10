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

public class US5_GetASpecificTodoStepDefinition {

    private Response response;

    // BACKGROUND
    @Given("the todo application is running \\(US5)")
    public void the_todo_application_is_running() throws InterruptedException {
        System.out.println("the_todo_application_is_running");

        HelperFunctions.startApplication();

        // delete all todos
        Response response = HelperFunctions.getAllTodos("");
        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        for (Map<String, String> todo : todos) {
            int id = Integer.parseInt(todo.get("id"));
            HelperFunctions.deleteTodoWithId(id);
        }
    }

    @And("the following todos exist in the system: \\(US5)")
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

    // Normal Flow

    @Given("the todo with id {int} exists in the system \\(US5)")
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

    @When("I get the todo with id {int} \\(US5)")
    public void i_get_the_todo_with_id(int id) {
        response = HelperFunctions.getTodoWithId(id);

    }

    @Then("I should get a todo with id {int}, title {string}, description {string} and doneStatus {string} \\(US5)")
    public void the_todo_with_id_should_have_title_description_status(int id,
            String title, String description,
            String doneStatus) {
        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        Map<String, String> todo = todos.get(0);
        assertEquals(id, Integer.parseInt(todo.get("id")));
        assertEquals(title, todo.get("title"));
        assertEquals(description, todo.get("description"));
        assertEquals(doneStatus, todo.get("doneStatus"));

    }

    // Alternate Flow
    @Given("the todo with id {int} and no description exists in the system \\(US5)")
    public void the_todo_with_id_and_no_description_exists_in_the_system(int id) {
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

    @When("I get the todo with id {int} that has no description \\(US5)")
    public void i_get_the_todo_with_id_that_has_no_description(int id) {
        response = HelperFunctions.getTodoWithId(id);
    }

    @Then("I should get a todo that has an empty description with id {int}, title {string}, description {string} and doneStatus {string} \\(US5)")
    public void i_should_get_a_todo_that_has_an_empty_description_with_id_title_description_and_doneStatus(int id,
            String title, String description,
            String doneStatus) {
        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        Map<String, String> todo = todos.get(0);
        assertEquals(id, Integer.parseInt(todo.get("id")));
        assertEquals(title, todo.get("title"));
        assertEquals(description, todo.get("description"));
        assertEquals(doneStatus, todo.get("doneStatus"));
    }

    // Error Flow

    @Given("the todo with id {int} does not exist in the system \\(US5)")
    public void the_todo_with_id_does_not_exist_in_the_system(int id) {
        Response response = HelperFunctions.getAllTodos("");
        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        for (Map<String, String> todo : todos) {
            int todoId = Integer.parseInt(todo.get("id"));
            assertNotEquals(todoId, id);
        }
    }

    @When("I get the todo with invalid id {int} \\(US5)")
    public void i_get_the_todo_with_invalid_id(int id) {
        response = HelperFunctions.getTodoWithId(id);
    }

    @Then("I should receive a {int} status code and an error message {string} \\(US5)")
    public void i_should_get_a_status_code(int statusCode, String errorMessage) {
        System.out.println(response.jsonPath().getString("errorMessage"));
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(errorMessage, response.jsonPath().getString("errorMessages[0]"));
    }
}
