package features;

import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US14_DeleteCategoryStepDefinition {

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

    @Given("the category with id {int} exists in the system \\(US14)")
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

    @And("the following categories exist in the current system \\(US14)")
    public void the_following_categories_with_title_description_exist_in_the_system(
            io.cucumber.datatable.DataTable dataTable) {

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            String title = columns.get("title");
            String description = columns.get("description");
            Response response = HelperFunctions.createCategory(title, description);
            assertEquals(201, response.getStatusCode());
        }
    }

    @When("I delete the category with id {int} \\(US14)")
    public void i_delete_the_category_with_id(Integer categoryId) {
        Response response = HelperFunctions.deleteCategoryWithId(categoryId);
    }

    @Then("the category with id {int} should not exist in the system \\(US14)")
    public void the_category_with_id_should_not_exist_in_the_system(Integer categoryId) {
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

    @Then("there should be 1 todo in the system \\(US14)")
    public void there_should_be_1_todo_in_the_system() {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        assertEquals(1, categories.size());
    }

    // Alternate Flow
    @Given("the category with id {int} no description exists in the system \\(US14)")
    public void the_category_with_id_does_not_exist_in_the_system(Integer categoryId) {
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

    @And("the following categories exist in the system \\(US14)")
    public void the_following_categories_with_title_description_exist_in_the_system2(
            io.cucumber.datatable.DataTable dataTable) {

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            String title = columns.get("title");
            String description = columns.get("description");
            Response response = HelperFunctions.createCategory(title, description);
        }
    }

    @When("I delete the category without a description with id {int} \\(US14)")
    public void i_delete_the_category_without_a_description_with_id(Integer categoryId) {
        Response response = HelperFunctions.deleteCategoryWithId(categoryId);
    }

    @Then("the category without a description with id {int} should not exist in the system \\(US14)")
    public void the_category_without_a_description_with_id_should_not_exist_in_the_system(Integer categoryId) {
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

    @Then("there should be 1 todo in the current system \\(US14)")
    public void there_should_be_1_todo_in_the_current_system() {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        assertEquals(1, categories.size());
    }

    // Error Flow
    @Given("the category with invalid id {string} does not exist in the system \\(US14)")
    public void the_category_with_id_does_not_exist_in_the_system2(String categoryId) {
        Response response = HelperFunctions.getAllCategories("");
        List<Map<String, String>> categories = response.jsonPath().getList("categories");
        for (Map<String, String> category : categories) {
            int id = Integer.parseInt(category.get("id"));
            if (id == Integer.parseInt(categoryId)) {
                assertEquals(id, Integer.parseInt(categoryId));
                break;
            }
        }
    }

    @When("I delete the category with inexistant id {string} \\(US14)")
    public void i_delete_the_category_with_inexistant_id(String categoryId) {
        response = HelperFunctions.deleteCategoryWithIDString(categoryId);
    }

    @Then("I should receive a status code {int} and see an error message {string} \\(US14)")
    public void i_should_receive_a_status_code_and_see_an_error_message(int statusCode, String errorMessage) {
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(errorMessage, response.jsonPath().get("errorMessages[0]"));
    }

}
