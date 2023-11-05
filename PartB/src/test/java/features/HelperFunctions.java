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
            Thread.sleep(1000); // wait for server to start
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopApplication() {
        if (application != null) {
            application.destroy();
        }
    }

    // --------- Todo API Helper Functions ---------

    public static Response getAllTodos(String queryString) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        Response response;

        if (queryString.equals("")) {
            response = request.get("/todos");

        } else {
            response = request.get("/todos?" + queryString);
        }
        return response;
    }

    public static Response getAllTodosWithIncorrectEndpoint() {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        Response response = request.get("/todo");
        return response;
    }

    public static Response getTodoWithId(int id) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        Response response = request.get("/todos/" + id);
        return response;
    }

    public static Response createTodo(String title, String description, boolean doneStatus) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", title);
        requestParams.put("description", description);
        requestParams.put("doneStatus", doneStatus);

        request.body(requestParams.toJSONString());

        Response response = request.post("/todos");

        return response;
    }

    public static Response updateTodoWithId(int id, String title, String description, boolean doneStatus) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", title);
        requestParams.put("description", description);
        requestParams.put("doneStatus", doneStatus);

        request.body(requestParams.toJSONString());

        Response response = request.post("/todos/" + id);

        return response;
    }

    public static Response updateTodoWithIdAndInvalidDoneStatus(int id, String title,
            String doneStatus) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", title);
        requestParams.put("doneStatus", doneStatus);

        request.body(requestParams.toJSONString());

        Response response = request.post("/todos/" + id);

        return response;
    }

    public static Response deleteTodoWithId(int id) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        Response response = request.delete("/todos/" + id);

        return response;
    }

    // --------- Category API Helper Functions ---------

}
