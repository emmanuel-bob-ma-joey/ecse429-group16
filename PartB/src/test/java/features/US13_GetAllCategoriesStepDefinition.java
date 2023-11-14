package features;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import io.restassured.response.Response;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US13_GetAllCategoriesStepDefinition {

    private Response response;

    // BACKGROUND
    @Given("the todo application is running \\(US13)")
    public void the_todo_application_is_running() throws InterruptedException {
        System.out.println("the_todo_application_is_running");

        HelperFunctions.startApplication();

         // delete all categories so that we can start fresh
         Response response = HelperFunctions.getAllCategories("");
         List<Map<String, String>> categories = response.jsonPath().getList("categories");
         for (Map<String, String> category : categories) {
             int id = Integer.parseInt(category.get("id"));
             HelperFunctions.deleteCategoryWithId(id);
         }
    }

    @And("the following categories exist in the system \\(US13)")
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
    @When("I get all categories \\(US13)")
    public void i_get_all_categories() {
        Response response = HelperFunctions.getAllCategories("");
        assertEquals(200, response.getStatusCode());
    }

    @Then("the categories with title {string}, description {string} exist \\(US13)")
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
    @When("I get all categories with a specific title {string} \\(US13)")
    public void i_get_all_categories_with_a_specific_title(String title) {
        response = HelperFunctions.getAllCategories("title="+title);
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        for (Map<String, String> category : categories) {
            String categoryTitle = category.get("title");
            assertEquals(categoryTitle, title);
        }
    }

    @Then("I should see {int} categories with title {string} \\(US13)")
    public void i_should_see_category_us3(Integer num, String title) {
        response = HelperFunctions.getAllCategories("title="+title);
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        assertEquals(num, categories.size());

    }
    
    // Error Scenario
    @When("I get all categories with mistyped endpoint \\(US13)")
    public void i_get_all_categories_with_mistyped_endpoint() {
        response = HelperFunctions.getAllCategoriesWithIncorrectEndpoint();
    }

    @Then("I should receive an error code {int} \\(US13)")
    public void i_should_see_an_error_code(int statusCode) {
        assertEquals(statusCode, response.getStatusCode());
    }
}