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

public class US9_GetSpecificProjectStepDefinition {
    
    private Response response;

    // BACKGROUND
    @Given("the project application is running \\(US9)")
    public void the_project_application_is_running() throws InterruptedException {

        HelperFunctions.startApplication();

        // delete all projects
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        for (Map<String, String> project : projects) {
            int id = Integer.parseInt(project.get("id"));
            HelperFunctions.deleteProjectWithId(id);
        }
    }

    @And("the following projects exist in the system: \\(US9)")
    public void the_following_projectss_exist_in_the_system(
            io.cucumber.datatable.DataTable dataTable) {

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            String title = columns.get("title");
            String description = columns.get("description");
            String completed = columns.get("completed");
            String active = columns.get("active");
            HelperFunctions.createProject("", title, description, completed, active);
        }
    }

    // Normal Flow

    @Given("the project with id {int} exists in the system \\(US9)")
    public void a_project_with_id_exists_in_the_system(int id) {
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        for (Map<String, String> project : projects) {
            int projId = Integer.parseInt(project.get("id"));
            if (projId == id) {
                assertEquals(id, projId);
                break;
            }
        }
    }

    @When("I get the project with id {int} \\(US9)")
    public void i_get_the_project_with_id(int id) {
        response = HelperFunctions.getProjectWithId(id);

    }

    @Then("I should get a project with id {int}, title {string}, description {string}, completed {string} and active {string} \\(US9)")
    public void the_project_with_id_should_have_title_description_completed_active(int id,
                                                                      String title, String description,
                                                                      String completed, String active) {
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        Map<String, String> project = projects.get(0);
        assertEquals(id, Integer.parseInt(project.get("id")));
        assertEquals(title, project.get("title"));
        assertEquals(description, project.get("description"));
        assertEquals(completed, project.get("completed"));
        assertEquals(active, project.get("active"));

    }

    // Alternate Flow
    @Given("the project with id {int} and no description exists in the system \\(US9)")
    public void the_project_with_id_and_no_description_exists_in_the_system(int id) {
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        for (Map<String, String> project : projects) {
            int projId = Integer.parseInt(project.get("id"));
            if (projId == id) {
                assertEquals(id, projId);
                break;
            }
        }
    }

    @When("I get the project with id {int} that has no description \\(US9)")
    public void i_get_the_project_with_id_that_has_no_description(int id) {
        response = HelperFunctions.getProjectWithId(id);
    }

    @Then("I should get a project that has an empty description with id {int}, title {string}, description {string}, completed {string} and active {string} \\(US9)")
    public void i_should_get_a_project_that_has_an_empty_description_with_id_title_description_completed_active(int id,
                                                                                                           String title, String description,
                                                                                                           String completed, String active) {
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        Map<String, String> project = projects.get(0);
        assertEquals(id, Integer.parseInt(project.get("id")));
        assertEquals(title, project.get("title"));
        assertEquals(description, project.get("description"));
        assertEquals(completed, project.get("completed"));
        assertEquals(active, project.get("active"));
    }

    // Error Flow

    @Given("the project with id {int} does not exist in the system \\(US9)")
    public void the_project_with_id_does_not_exist_in_the_system(int id) {
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        for (Map<String, String> project : projects) {
            int projID = Integer.parseInt(project.get("id"));
            assertNotEquals(projID, id);
        }
    }

    @When("I get the project with invalid id {int} \\(US9)")
    public void i_get_the_project_with_invalid_id(int id) {
        response = HelperFunctions.getProjectWithId(id);
    }

    @Then("I should receive a {int} status code and an error message {string} \\(US9)")
    public void i_should_get_a_status_code_(int statusCode, String errorMessage) {
        System.out.println(response.jsonPath().getString("errorMessage"));
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(errorMessage, response.jsonPath().getString("errorMessages[0]"));
    }
}