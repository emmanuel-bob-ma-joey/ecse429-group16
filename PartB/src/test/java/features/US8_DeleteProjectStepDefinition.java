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

public class US8_DeleteProjectStepDefinition {

    private Response response;

    // BACKGROUND
    @Given("the project application is running \\(US8)")
    public void the_project_application_is_running() throws InterruptedException {

        HelperFunctions.startApplication();

        // delete all projects so that we can start fresh
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        for (Map<String, String> project : projects) {
            int id = Integer.parseInt(project.get("id"));
            HelperFunctions.deleteProjectWithId(id);
        }
    }

    // Normal Scenario
    @Given("the active project with id {int} exists in the system \\(US8)")
    public void active_project_with_id_exists_in_the_system(int id) {
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");

        for (Map<String, String> project : projects) {
            int projID = Integer.parseInt(project.get("id"));
            System.out.println(projID + "     " + id);
            if (projID == id) {
                assertEquals(id, projID);
                return;
            }
        }
    }

    @And("the following active projects exist in the system \\(US8)")
    public void the_following_active_projects_exist_in_the_system(
            io.cucumber.datatable.DataTable dataTable) {

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            String id = columns.get("id");
            String title = columns.get("title");
            String description = columns.get("description");
            String completed = columns.get("completed");
            String active = columns.get("active");
            Response r = HelperFunctions.createProject(title, description, completed, active);
        }
    }

    @When("I delete the active project with id {int} \\(US8)")
    public void i_delete_the_project_with_id(int id) {
        HelperFunctions.deleteProjectWithId(id);
    }

    @Then("the active project with id {int} should not exist in the system \\(US8)")
    public void the_active_project_with_id_should_not_exist_in_the_system(int id) {
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        for (Map<String, String> project : projects) {
            int projID = Integer.parseInt(project.get("id"));
            assertNotEquals(projID, id);
        }
    }

    @Then("there should be 1 active project in the system \\(US8)")
    public void there_should_be_1_active_project_in_the_system() {
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        assertEquals(1, projects.size());
    }

    // Alternate Scenario
    @Given("the completed project with id {int} exists in the system \\(US8)")
    public void the_completed_project_with_id_id_exists_in_the_system(int id) {
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

    @And("the following completed projects exist in the system \\(US8)")
    public void the_following_completed_projects_exist_in_the_system(
            io.cucumber.datatable.DataTable dataTable) {

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            String id = columns.get("id");
            String title = columns.get("title");
            String description = columns.get("description");
            String completed = columns.get("completed");
            String active = columns.get("active");
            HelperFunctions.createProject(title, description, completed, active);
        }
    }

    @When("I delete the completed project with id {int} \\(US8)")
    public void i_delete_the_completed_project_with_id(int id) {
        response = HelperFunctions.deleteProjectWithId(id);
    }

    @Then("the completed project with id {int} should not exist in the system \\(US8)")
    public void the_completed_project_with_id_should_not_exist_in_the_system(int id) {
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        for (Map<String, String> project : projects) {
            int projId = Integer.parseInt(project.get("id"));
            assertNotEquals(projId, id);
        }
    }

    @Then("there should be 1 project in the current system \\(US8)")
    public void there_should_be_one_project_in_the_current_system() {
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        assertEquals(1, projects.size());
    }

    // Error flow
    @Given("the project with id {int} does not exist in the system \\(US8)")
    public void a_project_with_id_does_not_exist_in_the_system(int id) {
        response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        for (Map<String, String> project : projects) {
            int projID = Integer.parseInt(project.get("id"));
            assertNotEquals(projID, id);
        }
    }

    @When("I delete the project with inexistant id {int} \\(US8)")
    public void i_delete_the_project_with_inexistant_id(int id) {
        response = HelperFunctions.deleteProjectWithId(id);
        assertEquals(404, response.getStatusCode());
    }

    @Then("I should receive a status code {int} and see an error message {string} \\(US8)")
    public void then_i_should_receive_a_status_code_and_see_an_error_message_proj(int statusCode, String errorMessage) {
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(errorMessage, response.jsonPath().getString("errorMessages[0]"));
    }
}