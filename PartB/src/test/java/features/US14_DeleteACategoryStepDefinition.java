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

public class US14_DeleteACategoryStepDefinition {

    private Response response;

    // BACKGROUND
    @Given("the todo application is running \\(US14)")
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

    // Normal Scenario
   

    @Given("the following categories exist in the system \\(US14)")
    public void the_following_categories_with_title_description_exist_in_the_system_US141(
            io.cucumber.datatable.DataTable dataTable) {

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            String title = columns.get("title");
            String description = columns.get("description");
            HelperFunctions.createCategory(title, description);
        }
    }

    @And("the category with id {int} exists in the system \\(US14)")
    public void a_category_with_id_exists_in_the_system(int id) {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");

        for (Map<String, String> category : categories) {
            int categoryId = Integer.parseInt(category.get("id"));
            if (categoryId == id) {
                assertEquals(id, categoryId);
                return;
            }
        }
        assertEquals(1, 0);
    }

    @When("I delete the category with id {int} \\(US14)")
    public void i_delete_the_category_with_id(int id) {
        Response response = HelperFunctions.deleteCategoryWithId(id);
        assertEquals(200, response.getStatusCode());
    }

    @Then("the category with id {int} should not exist in the system \\(US14)")
    public void the_category_with_id_should_not_exist_in_the_system(int id) {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        for (Map<String, String> category : categories) {
            int categoryId = Integer.parseInt(category.get("id"));
            assertNotEquals(categoryId, id);
        }
    }

    @Then("there should be 1 category in the system \\(US14)")
    public void there_should_be_1_category_in_the_system() {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        assertEquals(1, categories.size());
    }

    // Alternate Scenario
    // @Given("the incompleted category with id {int} exists in the system \\(US4)")
    // public void the_incompleted_todo_with_id_id_exists_in_the_system(int id) {
    //     Response response = HelperFunctions.getAllTodos("");
    //     List<Map<String, String>> todos = response.jsonPath().getList("todos");

    //     for (Map<String, String> todo : todos) {
    //         int todoId = Integer.parseInt(todo.get("id"));
    //         if (todoId == id) {
    //             assertEquals(id, todoId);
    //             break;
    //         }
    //     }
    // }

    @Given("the following incomplete categories exist in the system \\(US14)")
    public void the_following_categories_exist_in_the_system_US142(
            io.cucumber.datatable.DataTable dataTable) {

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            String title = columns.get("title");
            String description = columns.get("description");
            HelperFunctions.createCategory(title, description);
        }
    }

    @When("I delete the incompleted category with id {int} \\(US14)")
    public void i_delete_the_incompleted_category_with_id(int id) {
        Response response = HelperFunctions.deleteCategoryWithId(id);
        assertEquals(200, response.getStatusCode());
    }

    @Then("the incompleted category with id {int} should not exist in the system \\(US14)")
    public void the_incompleted_category_with_id_should_not_exist_in_the_system(int id) {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        for (Map<String, String> category : categories) {
            int categoryId = Integer.parseInt(category.get("id"));
            assertNotEquals(categoryId, id);
        }
    }

    @Then("there should be 1 category in the current system \\(US14)")
    public void there_should_be_1_category_in_the_current_system() {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        assertEquals(1, categories.size());
    }

    // Error flow
    @Given("the category with id {int} does not exist in the system \\(US14)")
    public void a_category_with_id_does_not_exist_in_the_system(int id) {
        response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        for (Map<String, String> category : categories) {
            int categoryId = Integer.parseInt(category.get("id"));
            assertNotEquals(categoryId, id);
        }
    }

    @When("I delete the category with inexistant id {int} \\(US14)")
    public void i_delete_the_category_with_inexistant_id(int id) {
        response = HelperFunctions.deleteCategoryWithId(id);
        assertEquals(404, response.getStatusCode());
    }

    @Then("I should receive a status code {int} and see an error message {string} \\(US14)")
    public void then_i_should_receive_a_status_code_and_see_an_error_message(int statusCode, String errorMessage) {
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(errorMessage, response.jsonPath().getString("errorMessages[0]"));
    }
}