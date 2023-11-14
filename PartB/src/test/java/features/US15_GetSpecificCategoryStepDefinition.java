package features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class US15_GetSpecificCategoryStepDefinition {

    private Response response;

    // BACKGROUND
    @Given("the todo application is running \\(US15)")
    public void the_todo_application_is_running() throws InterruptedException {
        System.out.println("the_todo_application_is_running");

        assertTrue(HelperFunctions.isApplicationRunning());
    }

    @And("the following categories exist in the system: \\(US15)")
    public void the_following_categories_with_title_description_exist_in_the_system(
            io.cucumber.datatable.DataTable dataTable) {

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            String title = columns.get("title");
            String description = columns.get("description");
            Response response = HelperFunctions.createCategory(title, description);
        }
    }

    // Normal Scenario

    @Given("the category with id {int} exists in the system \\(US15)")
    public void the_category_with_id_exists_in_the_system(Integer categoryId) {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        for (Map<String, String> category : categories) {
            int id = Integer.parseInt(category.get("id"));
            if (id == categoryId) {
                assertEquals(id, categoryId);
                break;
            }
        }
    }

    @When("I get the category with specific id {int} \\(US15)")
    public void i_get_the_category_with_id(Integer categoryId) {
        Response response = HelperFunctions.getCategoryWithId(categoryId);
    }

    @Then("I should get a category with id {int}, title {string}, description {string} \\(US15)")
    public void then_i_should_get_a_category_with_id_title_description(int categoryId, String title,
            String description) {
        Response response = HelperFunctions.getCategoryWithId(categoryId);

        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        Map<String, String> category = categories.get(0);

        assertEquals(categoryId, Integer.parseInt(category.get("id")));
        assertEquals(title, category.get("title"));
        assertEquals(description, category.get("description"));

    }

    // Alternate Scenario
    @Given("the category with id {int} and no description exists in the system \\(US15)")
    public void the_category_with_id_and_no_description_exists_in_the_system(Integer categoryId) {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        for (Map<String, String> category : categories) {
            int id = Integer.parseInt(category.get("id"));
            if (id == categoryId) {
                assertEquals(id, categoryId);
                break;
            }
        }
    }

    @When("I get the category with id {int} that has no description \\(US15)")
    public void i_get_the_category_with_id_that_has_no_description(Integer categoryId) {
        Response response = HelperFunctions.getCategoryWithId(categoryId);
    }

    @Then("I should get a category that has an empty description with id {int}, title {string} and description {string} \\(US15)")
    public void i_should_get_a_category_that_has_an_empty_description_with_id_title_and_description(Integer categoryId,
            String title, String description) {
        Response response = HelperFunctions.getCategoryWithId(categoryId);
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        Map<String, String> category = categories.get(0);

        assertEquals(categoryId, Integer.parseInt(category.get("id")));
        assertEquals(title, category.get("title"));
        assertEquals(description, category.get("description"));
    }

    // Error Flow
    @Given("the category with id {int} does not exist in the system \\(US15)")
    public void the_category_with_id_does_not_exist_in_the_system(Integer categoryId) {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        for (Map<String, String> category : categories) {
            int id = Integer.parseInt(category.get("id"));
            assertNotEquals(id, categoryId);
        }
    }

    @When("I get the category with invalid id {int} \\(US15)")
    public void i_get_the_category_with_invalid_id(Integer categoryId) {
        response = HelperFunctions.getCategoryWithId(categoryId);
    }

    @Then("I should receive a {int} status code and an error message {string} \\(US15)")
    public void i_should_receive_a_status_code_and_an_error_message(int statusCode, String errorMessage) {
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(errorMessage, response.jsonPath().get("errorMessages[0]"));
    }
}
