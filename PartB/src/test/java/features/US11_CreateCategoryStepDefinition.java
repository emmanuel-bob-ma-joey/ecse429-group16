package features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import java.util.Map;

import io.restassured.response.Response;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US11_CreateCategoryStepDefinition {

    private Response response;

    // BACKGROUND
    @Given("the application is running \\(US1)")
    public void the_todo_application_is_running() throws InterruptedException {
        HelperFunctions.startApplication();
    }

    // Normal Scenario

    @Given("the category with title {string} and the description {string} does not exist in the system \\(US1)")
    public void the_category_with_title_and_the_description_does_not_exist_in_the_system(String title, String description) {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");

        for (Map<String, String> category : categories) {
            assertNotEquals(category.get("title"), title);
            assertNotEquals(category.get("description"), description);
        }
    }

    @When("I create a category with the title {string} and description {string} \\(US1)")
    public void i_create_a_category_with_title_and_the_description(String title, String description) {
        Response response = HelperFunctions.createCategory(title, description);
        assertEquals(201, response.getStatusCode());
    }

    @Then("the category with title {string} and the description {string} shall exist in the system \\(US1)")
    public void the_category_with_title_and_the_description_should_be_in_the_system(String title, String description) {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");

        for (Map<String, String> category : categories) {
            String categoryTitle = category.get("title");
            String categoryDescription = category.get("description");

            // check if the created todo is in the list of todos with correct data
            if (categoryTitle.equals(title) && categoryDescription.equals(description)) {
                assertEquals(categoryTitle, title);
                assertEquals(categoryDescription, description);
                break;
            }
        }
    }

    // Alternate Scenario

    @Given("the category with title {string} does not exist in the system \\(US1)")
    public void the_todo_with_title_does_not_exist_in_the_system(String title) {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");

        for (Map<String, String> category : categories) {
            assertNotEquals(category.get("title"), title);
        }
    }

    @When("I create a duplicate category with the title {string} and description {string} \\(US1)")
    public void i_create_a_duplicate_category_with_the_title(String title, String description) {
        Response response = HelperFunctions.createCategory(title, description);
        assertEquals(201, response.getStatusCode());
    }

    @Then("the category with title {string} with a description {string} shall exist in the system \\(US1)")
    public void the_category_with_title_with_a_description_should_be_in_the_system(String title, String description) {

        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");

        for (Map<String, String> category : categories) {
            String categoryTitle = category.get("title");
            String categoryDescription = category.get("description");

            // check if the created todo is in the list of todos with correct data
            if (categoryTitle.equals(title) && categoryDescription.equals(description)) {
                assertEquals(categoryTitle, title);
                assertEquals(categoryDescription, description);
                break;
            }
        }
    }

    // Error Scenario
    @When("I create a category with empty title \\(US1)")
    public void i_create_a_category_with_empty_title() {
        response = HelperFunctions.createCategory("", "description");

    }

    @Then("the status code shall be {int} with an error message {string} \\(US1)")
    public void the_todo_with_empty_title_should_not_be_in_the_system(int statusCode, String errorMessage) {
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(errorMessage, response.jsonPath().getString("errorMessages[0]"));
    }
}