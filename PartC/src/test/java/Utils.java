import org.json.simple.JSONObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Utils {
    public static final String baseUrl = "http://localhost:4567";
    public static int[] ITERATIONS = { 1, 10, 100, 1000, 5000, 10000 };
    public static final int millisecondsInNano = 1000000;

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

}
