package features;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import io.restassured.response.Response;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US3_GetAllCategoriesStepDefinition {

    private Response response;

    // BACKGROUND
    @Given("the todo application is running \\(US3)")
    public void the_todo_application_is_running() throws InterruptedException {
        System.out.println("the_todo_application_is_running");

        HelperFunctions.startApplication();

         // delete all todos so that we can start fresh
         Response response = HelperFunctions.getAllCategories("");
         List<Map<String, String>> categories = response.jsonPath().getList("categories");
         for (Map<String, String> category : categories) {
             int id = Integer.parseInt(category.get("id"));
             HelperFunctions.deleteCategoryWithId(id);
         }
    }

    @And("the following categories exist in the system \\(US3)")
    public void the_following_categories_with_title_description_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            String title = columns.get("title");
            String description = columns.get("description");
            Response response = HelperFunctions.createCategory(title, description);
            assertEquals(201, response.getStatusCode());
        }
    }

    // Normal Scenario
    @When("I get all categories \\(US3)")
    public void i_get_all_categories() {
        Response response = HelperFunctions.getAllCategories("");
        assertEquals(200, response.getStatusCode());
    }

    @Then("the categories with title {string}, description {string} exist \\(US3)")
    public void the_categories_with_title_description(String title, String description) {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        for (Map<String, String> category :categories) {
            String categoryTitle = category.get("title");
            String categoryDescription = category.get("description");
            // Check if the category is in the list of todos
            if (categoryTitle.equals(title) && categoryDescription.equals(description)) {
                assertEquals(categoryTitle, title);
                assertEquals(categoryDescription, description);
                return;
            }
            
        }
        assertEquals(1, 0);
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

            // Check if the todo is in the list of todos
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
    @When("I get all categories with mistyped endpoint \\(US3)")
    public void i_get_all_categories_with_mistyped_endpoint() {
        response = HelperFunctions.getAllCategoriesWithIncorrectEndpoint();
    }

    @Then("I should receive an error code {int} \\(US3)")
    public void i_should_see_an_error_code(int statusCode) {
        assertEquals(statusCode, response.getStatusCode());
    }
}