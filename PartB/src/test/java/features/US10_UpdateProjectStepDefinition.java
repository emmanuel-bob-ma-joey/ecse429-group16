package features;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import io.restassured.response.Response;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US10_UpdateProjectStepDefinition {

    private Response response;

    // BACKGROUND
    @Given("the project application is running \\(US10)")
    public void the_project_application_is_running() throws InterruptedException {
        HelperFunctions.startApplication();
    }

    @And("the following projects exist in the system \\(US10)")
    public void the_following_projects_with_title_description_completed_active_exist_in_the_system(
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

    // Normal Scenario
    @Given("a project with {int} exists in the system \\(US10)")
    public void a_project_with_id_exists_in_the_system(int id) {
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        for (Map<String, String> project : projects) {
            int projID = Integer.parseInt(project.get("id"));
            if (projID == id) {
                assertEquals(id, projID);
                break;
            }
        }
    }

    @When("I update the project with {int} with a new description {string} \\(US10)")
    public void i_update_the_project_with_id_with_a_new_description(int id, String description) {
        Response response = HelperFunctions.updateProjectWithId(id, null, description, false);
        assertEquals(200, response.getStatusCode());
    }

    @Then("the project with {int} should have title {string}, description {string}, completed {string} and active {string} \\(US10)")
    public void the_project_with_id_should_have_a_description(int id, String title, String description,
                                                           String completed, String active) {
        Response response = HelperFunctions.getProjectWithId(id);

        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        Map<String, String> project = projects.get(0);
        assertEquals(title, project.get("title"));
        assertEquals(description, project.get("description"));
        assertEquals(completed, project.get("completed"));
        assertEquals(active, project.get("active"));
    }

    // Alternate Scenario

    @Given("a incomplete project with id {int} exists in the system \\(US10)")
    public void a_project_with_id__exists_in_the_system(int id) {
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        for (Map<String, String> project : projects) {
            int projID = Integer.parseInt(project.get("id"));
            if (projID == id) {
                assertEquals(id, projID);
                assertEquals("false", project.get("completed").toString());
                break;
            }
        }
    }

    @When("I update the project with id {int} with {string} \\(US10)")
    public void when_i_update_the_project_with_id_with(int id, String completed) {
        Response response = HelperFunctions.updateProjectWithId(id, null, null, Boolean.getBoolean(completed));
        assertEquals(200, response.getStatusCode());
    }

    @Then("the project with {int} should have an updated completed {string} \\(US10)")
    public void the_project_with_id_should_have_an_updated_completed(int id, String completed) {
        Response response = HelperFunctions.getProjectWithId(id);

        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        Map<String, String> project = projects.get(0);
        assertEquals(completed, project.get("completed"));
    }

    // Error Scenario

    @Given("a project with {int} that exists in the system \\(US10)")
    public void a_project_with_id_that_exists_in_the_system(int id) {
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        for (Map<String, String> project : projects) {
            int projID = Integer.parseInt(project.get("id"));
            if (projID == id) {
                assertEquals(id, projID);
                break;
            }
        }
    }

    @When("I update the project with id {int} with invalid completed {string} \\(US10)")
    public void i_update_the_project_with_id_with_invalid_completed(int id, String completed) {
        response = HelperFunctions.updateProjectWithIdAndInvalidCompleted(id, null, completed);
        assertEquals(400, response.getStatusCode());
    }

    @Then("I should receive a {int} response with {string} \\(US10)")
    public void i_should_receive_a_response_with_(int statusCode, String errorMessage) {
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(errorMessage, response.jsonPath().getString("errorMessages[0]"));
    }

}