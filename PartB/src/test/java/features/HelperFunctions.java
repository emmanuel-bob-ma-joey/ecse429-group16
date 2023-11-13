package features;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import org.json.simple.JSONObject;
public class HelperFunctions {
    public static Process application;

    private static String baseUrl = "http://localhost:4567";
    public static void startApplication() throws InterruptedException {
        try {
            application = Runtime.getRuntime().exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
            Thread.sleep(2000); // wait for server to start
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopApplication() {
        if (application != null) {
            application.destroy();
        }
    }

    public static Response getAllProjects(String queryString) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        Response response;

        if (queryString.equals("")) {
            response = request.get("/projects");

        } else {
            response = request.get("/projects?" + queryString);
        }
        return response;
    }


    public static Response createProject(String id, String title, String description, boolean completed, boolean active) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", title);
        requestParams.put("description", description);
        requestParams.put("completed", completed);
        requestParams.put("active", active);

        request.body(requestParams.toJSONString());


        Response response;
        if (id.equals(""))
            response = request.post("/projects");
        else
            response = request.post("/projects/"+id);

        return response;
    }


    public static Response deleteProjectWithId(int id) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        Response response = request.delete("/projects/" + id);

        return response;
    }

    public static Response getAllProjectsWithIncorrectEndpoint() {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        Response response = request.get("/project");
        return response;
    }

}
