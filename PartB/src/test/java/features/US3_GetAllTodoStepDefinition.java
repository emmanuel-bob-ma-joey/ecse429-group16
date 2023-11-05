package features;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import io.restassured.response.Response;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US3_GetAllTodoStepDefinition {

    private Response response;

    // BACKGROUND
    @Given("the todo application is running \\(US3)")
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

    @And("the following todos exist in the system \\(US3)")
    public void the_following_todos_with_title_description_doneStatus_exist_in_the_system(
            io.cucumber.datatable.DataTable dataTable) {

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            String title = columns.get("title");
            String description = columns.get("description");
            String doneStatus = columns.get("doneStatus");
            Response response = HelperFunctions.createTodo(title, description, Boolean.parseBoolean(doneStatus));
            assertEquals(201, response.getStatusCode());
        }
    }

    // Normal Scenario
    @When("I get all todos \\(US3)")
    public void i_get_all_todos() {
        Response response = HelperFunctions.getAllTodos("");
        assertEquals(200, response.getStatusCode());
    }

    @Then("the todos with title {string}, description {string} and doneStatus {string} \\(US3)")
    public void the_todos_with_title_description_and_doneStatus(String title, String description, String doneStatus) {
        Response response = HelperFunctions.getAllTodos("");
        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        for (Map<String, String> todo : todos) {
            String todoTitle = todo.get("title");
            String todoDescription = todo.get("description");
            String todoDoneStatus = todo.get("doneStatus");
            if (todoTitle.equals(title) && todoDescription.equals(description)
                    && todoDoneStatus.equals(doneStatus)) {
                assertEquals(todoTitle, title);
                assertEquals(todoDescription, description);
                assertEquals(todoDoneStatus, doneStatus);
                break;
            }
        }
    }

    // Alternate Scenario
    @When("I get all todos with status doneStatus {string} \\(US3)")
    public void i_get_all_todos_with_status_doneStatus(String doneStatus) {
        response = HelperFunctions.getAllTodos("doneStatus=false");
        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        for (Map<String, String> todo : todos) {
            String todoDoneStatus = todo.get("doneStatus");
            assertEquals(todoDoneStatus, doneStatus);
        }
    }

    @Then("I should see 2 todos \\(US3)")
    public void i_should_see_2_todos() {
        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        assertEquals(2, todos.size());
    }

    @Then("the todos with title {string}, description {string} and incomplete doneStatus {string} \\(US3)")
    public void the_todos_with_title_description_and_incompleted_doneStatus(String title, String description,
            String doneStatus) {
        List<Map<String, String>> todos = response.jsonPath().getList("todos");
        for (Map<String, String> todo : todos) {
            String todoTitle = todo.get("title");
            String todoDescription = todo.get("description");
            String todoDoneStatus = todo.get("doneStatus");
            if (todoTitle.equals(title) && todoDescription.equals(description)
                    && todoDoneStatus.equals(doneStatus)) {
                assertEquals(todoTitle, title);
                assertEquals(todoDescription, description);
                assertEquals(todoDoneStatus, doneStatus);
                break;
            }
        }
    }

    // Error Scenario
    @When("I get all todos with mistyped endpoint \\(US3)")
    public void i_get_all_todos_with_mistyped_endpoint() {
        response = HelperFunctions.getAllTodosWithIncorrectEndpoint();
    }

    @Then("I should receive an error code {int} \\(US3)")
    public void i_should_see_an_error_code(int statusCode) {
        assertEquals(statusCode, response.getStatusCode());
    }
}
