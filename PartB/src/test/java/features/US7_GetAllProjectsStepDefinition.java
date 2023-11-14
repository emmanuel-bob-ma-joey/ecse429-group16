package features;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import io.restassured.response.Response;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US7_GetAllProjectsStepDefinition {

    private Response response;

    // BACKGROUND
    @Given("the project application is running \\(US7)")
    public void the_project_application_is_running() throws InterruptedException {
        HelperFunctions.startApplication();

        // delete all todos so that we can start fresh
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        for (Map<String, String> project : projects) {
            int id = Integer.parseInt(project.get("id"));
            HelperFunctions.deleteProjectWithId(id);
        }
    }

    @And("the following projects exist in the system \\(US7)")
    public void the_following_projects_exist_in_the_system(
            io.cucumber.datatable.DataTable dataTable) {

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            String title = columns.get("title");
            String description = columns.get("description");
            String active = columns.get("active");
            String completed = columns.get("completed");
            Response response = HelperFunctions.createProject(title, description, completed, active);
            assertEquals(201, response.getStatusCode());
        }
    }

    // Normal Scenario
    @When("I get all projects \\(US7)")
    public void i_get_all_projects() {
        Response response = HelperFunctions.getAllProjects("");
        assertEquals(200, response.getStatusCode());
    }

    @Then("the projects with title {string}, description {string}, completed {string} and active {string} \\(US7)")
    public void the_todos_with_title_description_and_doneStatus(String title, String description, String completed,
            String active) {
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        for (Map<String, String> project : projects) {
            String projectTitle = project.get("title");
            String projectDescription = project.get("description");
            String projectCompleted = project.get("completed");
            String projectActive = project.get("active");

            // Check if the project is in the list of todos
            if (projectTitle.equals(title) && projectDescription.equals(description)
                    && projectCompleted.equals(completed) && projectActive.equals(active)) {
                assertEquals(projectTitle, title);
                assertEquals(projectDescription, description);
                assertEquals(projectCompleted, completed);
                assertEquals(projectActive, active);
                break;
            }
        }
    }

    // Alternate Scenario
    @When("I get all projects with completed is true \\(US7)")
    public void i_get_all_projects_with_completed_true() {
        response = HelperFunctions.getAllProjects("completed=true");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        for (Map<String, String> project : projects) {
            String completed = project.get("completed");
            assertEquals(completed, "true");
        }
    }

    @Then("I should see 1 project \\(US7)")
    public void i_should_see_1_project() {
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        assertEquals(1, projects.size());
    }

    @Then("the projects with title {string}, description {string}, and active {string} \\(US7)")
    public void the_projects_with_title_description_completecTrue_active(String title, String description,
            String active) {
        List<Map<String, String>> projects = response.jsonPath().getList("projects");
        for (Map<String, String> project : projects) {
            String projectTitle = project.get("title");
            String projectDescription = project.get("description");
            String projectActive = project.get("active");
            String projectCompleted = project.get("completed");

            // Check if the project is in the list of projects
            if (projectTitle.equals(title) && projectDescription.equals(description)
                    && projectActive.equals(active)) {
                assertEquals(projectTitle, title);
                assertEquals(projectDescription, description);
                assertEquals(projectActive, active);
                assertEquals(projectCompleted, "true");
                break;
            }
        }
    }

    // Error Scenario
    @When("I get all projects with mistyped endpoint \\(US7)")
    public void i_get_all_projects_with_mistyped_endpoint() {
        response = HelperFunctions.getAllProjectsWithIncorrectEndpoint();
    }

    @Then("I should receive error code {int} \\(US7)")
    public void i_should_see_error_code(int statusCode) {
        assertEquals(statusCode, response.getStatusCode());
    }
}