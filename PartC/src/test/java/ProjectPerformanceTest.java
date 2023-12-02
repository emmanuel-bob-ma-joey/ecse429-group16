import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.response.Response;

public class ProjectPerformanceTest {
    public Process RunRestAPI;

    @Before
    public void setUp() throws Exception {

        try {
            RunRestAPI = Runtime.getRuntime().exec(Utils.startCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread.sleep(3000); // wait for server to start
    }

    @After
    public void tearDown() throws Exception {
        RunRestAPI.destroy();
        Thread.sleep(1000);
    }

    @Test
    public void testProjectPerformance() throws ClientProtocolException, IOException {

        for (int ITERATIONS : Utils.ITERATIONS) {

            // Add iterations amount of projects
            for (int i = 0; i <= ITERATIONS; i++) {
                Utils.createProject("title", "description", "false", "false");
            }

            System.out.println("------------ " + ITERATIONS + " Project Added ------------");

            // count the number of Projects
            Response response = Utils.getAllProjects("");
            List<Object> projects = response.jsonPath().getList("projects");
            System.out.println("Number of projects: " + projects.size());

            // Create a new project

            long startTimeForAdd = System.nanoTime();
            Response newProject = Utils.createProject("title", "description", "false", "false");
            long endTimeForAdd = System.nanoTime();

            System.out.println(
                    "Time taken to add a project: " + (float) (endTimeForAdd - startTimeForAdd) / Utils.millisecondsInNano
                            + " milliseconds");

            int id = Integer.parseInt(newProject.getBody().jsonPath().getString("id"));

            // Update the project

            long startTimeForUpdate = System.nanoTime();

            Utils.updateProjectWithId(id, "new title", "Updated project", "true");

            long endTimeForUpdate = System.nanoTime();

            System.out.println("Time taken to update a project: "
                    + (float) (endTimeForUpdate - startTimeForUpdate) / Utils.millisecondsInNano + " milliseconds");

            // Delete the project

            long startTimeForDelete = System.nanoTime();

            Utils.deleteProjectWithId(id);
            long endTimeForDelete = System.nanoTime();

            System.out.println("Time taken to delete a project: "
                    + (float) (endTimeForDelete - startTimeForDelete) / Utils.millisecondsInNano + " milliseconds");

        }
    }

}