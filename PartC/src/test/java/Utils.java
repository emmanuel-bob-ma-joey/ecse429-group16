import org.json.simple.JSONObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Utils {
    public static final String baseUrl = "http://localhost:4567";
    public static int[] ITERATIONS = { 1, 30, 50, 80, 100, 200, 300, 400, 500,
            600, 800, 900, 1000, 2000, 3000, 5000 };
    public static final int millisecondsInNano = 1000000;
    public static final String startCommand = "java -jar runTodoManagerRestAPI-1.5.5.jar";

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

    public static Response deleteTodoWithId(int id) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        Response response = request.delete("/todos/" + id);
        return response;
    }

    public static Response createCategory(String title, String description) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", title);
        requestParams.put("description", description);
        request.body(requestParams.toJSONString());

        Response response = request.post("/categories");

        return response;
    }

    public static Response getAllCategories(String queryString) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        Response response;

        if (queryString.equals("")) {
            response = request.get("/categories");

        } else {
            response = request.get("/categories?" + queryString);
        }
        return response;
    }

    public static Response updateCategoryWithId(int id, String title, String description){
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", title);
        requestParams.put("description", description);

        request.body(requestParams.toJSONString());

        Response response = request.post("/categories/" + id);
        return response;
    }

    public static Response deleteCategoryWithId(int id) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        Response response = request.delete("/todos/" + id);
        return response;
    }
    // --------- Projects API Helper Functions ---------

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

    public static Response createProject(String title, String description, String completed, String active) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", title);
        requestParams.put("description", description);
        requestParams.put("completed", Boolean.valueOf(completed));
        requestParams.put("active", Boolean.valueOf(active));

        request.body(requestParams.toJSONString());

        Response response;

        response = request.post("/projects");

        return response;
    }

    public static Response updateProjectWithId(int id, String title, String description, String completed) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        JSONObject requestParams = new JSONObject();
        
        if (title != null) {
            requestParams.put("title", title);
        }

        if (description != null) {
            requestParams.put("description", description);
        }

        if (completed.equals("true")) {
            requestParams.put("completed", true);
        } else if (completed.equals("true")) {
            requestParams.put("completed", false);
        }

        request.body(requestParams.toJSONString());

        Response response = request.post("/projects/" + id);

        return response;
    }
    public static Response deleteProjectWithId(int id) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        Response response = request.delete("/projects/" + id);

        return response;
    }

}
