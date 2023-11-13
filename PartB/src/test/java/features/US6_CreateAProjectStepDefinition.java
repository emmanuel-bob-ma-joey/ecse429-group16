package features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import java.util.Map;

import io.restassured.response.Response;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US6_CreateAProjectStepDefinition {

    private Response response;

    // BACKGROUND
    @Given("the application is running \\(US6)")
    public void the_project_application_is_running() throws InterruptedException {
        HelperFunctions.startApplication();
    }

    // Normal Scenario

    @Given("the project with title {string} and the description {string} does not exist in the system \\(US6)")
    public void the_project_with_title_and_the_description_does_not_exist_in_the_system(String title,
                                                                                     String description) {

        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");

        for (Map<String, String> project : projects) {
            assertNotEquals(project.get("title"), title);
            assertNotEquals(project.get("description"), description);
        }
    }

    @When("I create a project with the title {string} and description {string} \\(US6)")
    public void i_create_a_project_with_title_and_the_description(String title, String description) {
        Response response = HelperFunctions.createProject("", title, description, false, false);
        assertEquals(201, response.getStatusCode());
    }

    @Then("the project with title {string} and the description {string} shall exist in the system \\(US6)")
    public void the_project_with_title_and_the_description_should_be_in_the_system(String title, String description) {
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");

        for (Map<String, String> project : projects) {
            String projectTitle = project.get("title");
            String projectDescription = project.get("description");


            if (projectTitle.equals(title) && projectDescription.equals(description)) {
                assertEquals(projectTitle, title);
                assertEquals(projectDescription, description);
                break;
            }
        }
    }

    // Alternate Scenario

    @Given("the project with title {string} does not exist in the system \\(US6)")
    public void the_project_with_title_does_not_exist_in_the_system(String title) {
        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");

        for (Map<String, String> project : projects) {
            assertNotEquals(project.get("title"), title);
        }
    }

    @When("I create a project with the title {string} and completed {string} \\(US6)")
    public void i_create_a_project_with_the_title(String title, String completed) {
        Response response = HelperFunctions.createProject("", title, "", Boolean.parseBoolean(completed), false);
        assertEquals(201, response.getStatusCode());
    }

    @Then("the project with title {string} with a completed {string} shall exist in the system \\(US6)")
    public void the_project_with_title_with_a_completed_should_be_in_the_system(String title, String completed) {
        boolean completedBool = Boolean.parseBoolean(completed);

        Response response = HelperFunctions.getAllProjects("");
        List<Map<String, String>> projects = response.jsonPath().getList("projects");

        for (Map<String, String> project : projects) {
            String projectTitle = project.get("title");
            Boolean projectCompleted = Boolean.parseBoolean(project.get("completed"));

            // check if the created todo is in the list of todos with correct data
            if (projectTitle.equals(title) && projectCompleted == completedBool) {
                assertEquals(projectTitle, title);
                assertEquals(projectCompleted.toString(), completed);
                break;
            }
        }
    }



    // Error Scenario
    @When("I create a project with an invalid id \\(US6)")
    public void i_create_a_project_with_invalid_id() {
        String invalidID = "asdf";
        response = HelperFunctions.createProject(invalidID, "title!", "description", false, false);

    }

    @Then("the status code shall be {int} with an error message {string} \\(US6)")
    public void the_project_with_invalid_id_should_not_be_in_the_system(int statusCode, String errorMessage) {
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(errorMessage, response.jsonPath().getString("errorMessages[0]"));
    }
}