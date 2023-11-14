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
            Thread.sleep(3000); // wait for server to start
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

    public static Response createProjectWithID(String id, String title, String description, String completed,
            String active) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        JSONObject requestParams = new JSONObject();
        requestParams.put("id", id);
        requestParams.put("title", title);
        requestParams.put("description", description);
        requestParams.put("completed", Boolean.valueOf(completed));
        requestParams.put("active", Boolean.valueOf(active));

        request.body(requestParams.toJSONString());

        Response response;

        response = request.post("/projects");

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

    public static Response getProjectWithId(int id) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        Response response = request.get("/projects/" + id);
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

    public static Response updateProjectWithIdAndInvalidCompleted(int id, String title,
            String completed) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        JSONObject requestParams = new JSONObject();
        requestParams.put("title", title);
        requestParams.put("completed", completed);

        request.body(requestParams.toJSONString());

        Response response = request.post("/projects/" + id);

        return response;
    }

    // --------- Category API Helper Functions ---------

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

    public static Response getAllCategoriesWithIncorrectEndpoint() {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        Response response = request.get("/category");
        return response;
    }

    public static Response getCategoryWithId(int id) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        Response response = request.get("/categories/" + id);
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

    public static Response updateCategoryWithId(int id, String title, String description) {
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

    // public static Response updateCategoryWithId(int id, String title) {
    // RestAssured.baseURI = baseUrl;
    // RequestSpecification request = RestAssured.given();

    // request.header("Content-Type", "application/json");
    // JSONObject requestParams = new JSONObject();
    // requestParams.put("title", title);

    // request.body(requestParams.toJSONString());

    // Response response = request.post("/categories/" + id);

    // return response;
    // }

    public static Response deleteCategoryWithId(int id) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        Response response = request.delete("/categories/" + id);

        return response;
    }

    public static Response deleteCategoryWithIDString(String id) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();

        Response response = request.delete("/categories/" + id);

        return response;
    }

}