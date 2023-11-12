package features;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import io.restassured.response.Response;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US2_UpdateCategoryStepDefinition {

    private Response response;

    // BACKGROUND
    @Given("the todo application is running \\(US2)")
    public void the_todo_application_is_running() throws InterruptedException {
        System.out.println("the_todo_application_is_running");

        HelperFunctions.startApplication();
    }

    @And("the following categories exist in the system \\(US2)")
    public void the_following_categories_with_title_description_exist_in_the_system(
            io.cucumber.datatable.DataTable dataTable) {

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            String title = columns.get("title");
            String description = columns.get("description");
            HelperFunctions.createCategory(title, description);
        }
    }

    // Normal Scenario
    @Given("a category with {int} exists in the system \\(US2)")
    public void a_category_with_id_exists_in_the_system(int id) {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        for (Map<String, String> category : categories) {
            int categoryId = Integer.parseInt(category.get("id"));
            if (categoryId == id) {
                assertEquals(id, categoryId);
                break;
            }
        }
    }

    @When("I update the category with {int} with a new description {string} \\(US2)")
    public void i_update_the_category_with_id_with_a_new_description(int id, String description) {
        Response response = HelperFunctions.updateCategoryWithId(id, null, description);
        assertEquals(200, response.getStatusCode());
    }

    @Then("the category with {int} should have title {string}, description {string} \\(US2)")
    public void the_category_with_id_should_have_a_description(int id, String title, String description) {
        Response response = HelperFunctions.getTodoWithId(id);

        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        Map<String, String> category = categories.get(0);
        assertEquals(title, category.get("title"));
        assertEquals(description, category.get("description"));

    }

    // Alternate Scenario

    @Given("a incomplete category with id {int} exists in the system \\(US2)")
    public void a_category_with_id_with_doneStatus_exists_in_the_system(int id) {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        for (Map<String, String> category : categories) {
            int categoryId = Integer.parseInt(category.get("id"));
            if (categoryId == id) {
                assertEquals(id, categoryId);
                assertEquals("false", category.get("doneStatus").toString());
                break;
            }
        }
    }

    @When("I update the category with id {int} with {string} \\(US2)")
    public void when_i_update_the_category_with_id_with(int id, String description) {
        Response response = HelperFunctions.updateCategoryWithId(id, "", description);
        assertEquals(200, response.getStatusCode());
    }

    @Then("the category with {int} should have an updated description {string} \\(US2)")
    public void the_category_with_id_should_have_an_updated_status(int id, String description) {
        Response response = HelperFunctions.getCategoryWithId(id);

        List<Map<String, String>> categories = response.jsonPath().getList("/categories");
        Map<String, String> category = categories.get(0);
        assertEquals(description, category.get("description"));
    }

    // Error Scenario

    @Given("a category with {int} that exists in the system \\(US2)")
    public void a_category_with_id_that_exists_in_the_system(int id) {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("/categories");
        for (Map<String, String> category : categories) {
            int categoryId = Integer.parseInt(category.get("id"));
            if (categoryId == id) {
                assertEquals(id, categoryId);
                break;
            }
        }
    }

    @When("I update the category with id {int} with empty title {string} \\(US2)")
    public void i_update_the_category_with_id_with_empty_title(int id) {
        response = HelperFunctions.updatecategoryWithIdAndInvalidDoneStatus(id, null);
        assertEquals(400, response.getStatusCode());
    }

    @Then("I should receive a {int} response with {string} \\(US2)")
    public void i_should_receive_a_response_with(int statusCode, String errorMessage) {
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(errorMessage, response.jsonPath().getString("errorMessages[0]"));
    }

}