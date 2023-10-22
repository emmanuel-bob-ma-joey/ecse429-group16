import java.io.IOException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.Random.class)
public class TodoApiTest {

    public Process RunRestAPI;

    final String BASE_URL = "http://localhost:4567";
    final String initial_todos_count = "2";

    // store the above in a JSON object

    @BeforeEach
    void setUp() throws Exception {

        try {
            RunRestAPI = Runtime.getRuntime().exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
            Thread.sleep(3000); // wait for server to start
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @AfterEach
    void tearDown() throws Exception {
        RunRestAPI.destroy();
    }

    // ------------------ /todos Endpoints ------------------
    @Test
    void testTodoGetRequest() {

        Response response = RestAssured.get(BASE_URL + "/todos");

        assertEquals(200, response.getStatusCode());
        assertEquals(initial_todos_count, response.getBody().jsonPath().getString("todos.size()"));
    }

    @Test
    void testTodoHeadRequest() {
        Response response = RestAssured.head(BASE_URL + "/todos");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void testTodoPostRequest() {
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "test");
        requestParams.put("description", "test");

        request.body(requestParams.toJSONString());
        Response response = request.post(BASE_URL + "/todos");
        assertEquals(201, response.getStatusCode());
        assertEquals("test", response.getBody().jsonPath().getString("title"));
        assertEquals("test", response.getBody().jsonPath().getString("description"));
    }

    @Test
    void testTodoPostRequestWithoutTitle() {
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("description", "test");

        request.body(requestParams.toJSONString());
        Response response = request.post(BASE_URL + "/todos");
        assertEquals(400, response.getStatusCode());
        assertEquals("title : field is mandatory", response.getBody().jsonPath().getString("errorMessages[0]"));
    }

    // JSON malformed
    @Test
    void testTodoPostRequestMalformedJSON() {
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "test");
        requestParams.put("description", "test");

        request.body(requestParams.toJSONString() + "}");
        Response response = request.post(BASE_URL + "/todos");
        assertEquals(400, response.getStatusCode());
        assertEquals(
                "com.google.gson.stream.MalformedJsonException: Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 39 path $",
                response.getBody().jsonPath().getString("errorMessages[0]"));
    }

    @Test
    void testTodoPostRequestWithEmptyTitle() {
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "");
        requestParams.put("description", "test");

        request.body(requestParams.toJSONString());
        Response response = request.post(BASE_URL + "/todos");
        assertEquals(400, response.getStatusCode());
        assertEquals("Failed Validation: title : can not be empty",
                response.getBody().jsonPath().getString("errorMessages[0]"));
    }

    @Test
    void testTodoPostRequestWithInexistantFeld() {
        RequestSpecification request = RestAssured.given();
        String inexistantField = "inexistant";

        JSONObject requestParams = new JSONObject();
        requestParams.put(inexistantField, "test");

        request.body(requestParams.toJSONString());
        Response response = request.post(BASE_URL + "/todos");
        assertEquals(400, response.getStatusCode());
        assertEquals("Could not find field: " + inexistantField,
                response.getBody().jsonPath().getString("errorMessages[0]"));
    }

    // ------------------ /todos/{id} Endpoints ------------------

    @Test
    void testGetSpecificTodoWithID() {
        String id = "1";
        Response response = RestAssured.get(BASE_URL + "/todos" + "/" + id);
        assertEquals(200, response.getStatusCode());
        assertEquals(id, response.getBody().jsonPath().getString("todos[0].id"));
        assertEquals(Constants.title1, response.getBody().jsonPath().getString("todos[0].title"));
        assertEquals(Constants.doneStatus1, response.getBody().jsonPath().getString("todos[0].doneStatus"));
        assertEquals(Constants.description1, response.getBody().jsonPath().getString("todos[0].description"));
        assertEquals(Constants.categories1, response.getBody().jsonPath().getString("todos[0].categories"));
        assertEquals(Constants.tasksof1, response.getBody().jsonPath().getString("todos[0].tasksof"));
    }

    @Test
    void testGetSpecificTodoWithInvalidID() {
        String id = "10000000";
        Response response = RestAssured.get(BASE_URL + "/todos" + "/" + id);
        assertEquals(404, response.getStatusCode());
        assertEquals("Could not find an instance with todos/" + id,
                response.getBody().jsonPath().getString("errorMessages[0]"));
    }

    @Test
    void testHeadSpecificTodoWithID() {
        String id = "2";
        Response response = RestAssured.head(BASE_URL + "/todos" + "/" + id);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void testHeadSpecificTodoWithInvalidID() {
        String id = "100000";
        Response response = RestAssured.head(BASE_URL + "/todos" + "/" + id);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void testPostSpecificTodoWithID() {
        String id = "1";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "test");
        requestParams.put("description", "test");

        request.body(requestParams.toJSONString());
        Response response = request.post(BASE_URL + "/todos" + "/" + id);
        assertEquals(200, response.getStatusCode());
        assertEquals("test", response.getBody().jsonPath().getString("title"));
        assertEquals("test", response.getBody().jsonPath().getString("description"));
    }

    @Test
    void testPutSpecificTodoWithIDWithStringDoneStatus() {
        String id = "1";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "test");
        requestParams.put("doneStatus", "true");

        request.body(requestParams.toJSONString());
        Response response = request.put(BASE_URL + "/todos" + "/" + id);
        assertEquals(400, response.getStatusCode());
        assertEquals("Failed Validation: doneStatus should be BOOLEAN",
                response.getBody().jsonPath().getString("errorMessages[0]"));
    }

    @Test
    void testPutSpecificTodoWithID() {
        String id = "2";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "abc");
        requestParams.put("doneStatus", true);

        request.body(requestParams.toJSONString());
        Response response = request.put(BASE_URL + "/todos" + "/" + id);

        assertEquals(200, response.getStatusCode());
        assertEquals("abc", response.getBody().jsonPath().getString("title"));
        assertEquals("true", response.getBody().jsonPath().getString("doneStatus"));
    }

    @Test
    void testPutSpecificTodoWithInvalidID() {
        String id = "100000";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("title", "abc");
        requestParams.put("doneStatus", true);

        request.body(requestParams.toJSONString());
        Response response = request.put(BASE_URL + "/todos" + "/" + id);

        assertEquals(404, response.getStatusCode());
        assertEquals("Invalid GUID for " + id + " entity todo",
                response.getBody().jsonPath().getString("errorMessages[0]"));
    }

    @Test
    void testDeleteSpecificTodoWithID() {
        String id = "1";
        Response response = RestAssured.delete(BASE_URL + "/todos" + "/" + id);
        assertEquals(200, response.getStatusCode());

        // check if deleted
        response = RestAssured.get(BASE_URL + "/todos" + "/" + id);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void testDeleteSpecificTodoWithInvalidID() {
        String id = "100000";
        Response response = RestAssured.delete(BASE_URL + "/todos" + "/" + id);
        assertEquals(404, response.getStatusCode());
    }

    // ------------------ Extra out of scope tests ------------------

    // ------------------ /todos/{id}/categories Endpoints ------------------
    @Test
    void testGetSpecificTodoWithIDCategories() {
        String id = "1";
        Response response = RestAssured.get(BASE_URL + "/todos" + "/" + id + "/categories");
        assertEquals(200, response.getStatusCode());
        assertEquals(id, response.getBody().jsonPath().getString("categories[0].id"));
        assertEquals(Constants.category1Title, response.getBody().jsonPath().getString("categories[0].title"));
        assertEquals(Constants.description1,
                response.getBody().jsonPath().getString("categories[0].description"));
    }

    @Test
    // BUG: Actual behaviour
    void testGetSpecificTodoWithIDCategoriesInvalidIDActual() {
        String id = "1000000000";
        Response response = RestAssured.get(BASE_URL + "/todos" + "/" + id + "/categories");
        assertEquals(200, response.getStatusCode());

    }

    @Test
    // BUG: Expected behaviour
    void testGetSpecificTodoWithIDCategoriesInvalidIDExpected() {
        String id = "1000000000";
        Response response = RestAssured.get(BASE_URL + "/todos" + "/" + id + "/categories");
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void testHeadSpecificTodoWithIDCategories() {
    	System.out.println("poop");
        String id = "1";
        Response response = RestAssured.head(BASE_URL + "/todos" + "/" + id + "/categories");
        assertEquals(200, response.getStatusCode());
    }

    // BUG: Actual behaviour
    @Test
    void testHeadSpecificTodoWithInvalidIDCategoriesActual() {
        String id = "100000";
        Response response = RestAssured.head(BASE_URL + "/todos" + "/" + id + "/categories");
        assertEquals(200, response.getStatusCode());
    }

    // BUG: Expected behaviour
    @Test
    void testHeadSpecificTodoWithInvalidIDCategoriesExpected() {
        String id = "100000";
        Response response = RestAssured.head(BASE_URL + "/todos" + "/" + id + "/categories");
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void testPostSpecificTodoWithIDCategories() {
        String id = "2";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("id", "1");

        request.body(requestParams.toJSONString());
        Response response = request.post(BASE_URL + "/todos" + "/" + id + "/categories");
        assertEquals(201, response.getStatusCode());

        // check if it really updated
        response = RestAssured.get(BASE_URL + "/todos" + "/" + id + "/categories");
        assertEquals(200, response.getStatusCode());
        assertEquals("1", response.getBody().jsonPath().getString("categories[0].id"));
    }

    @Test
    void testPostSpecificTodoWithIDCategoriesInvalidId() {
        String id = "2";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("id", "100000");

        request.body(requestParams.toJSONString());
        Response response = request.post(BASE_URL + "/todos" + "/" + id + "/categories");
        assertEquals(404, response.getStatusCode());
        assertEquals("Could not find thing matching value for id",
                response.getBody().jsonPath().getString("errorMessages[0]"));
    }

    // ------------------ /todos/{id}/tasksof Endpoints ------------------
    @Test
    void testGetSpecificTodoWithIDTasksof() {
        String id = "1";
        Response response = RestAssured.get(BASE_URL + "/todos" + "/" + id + "/tasksof");
        assertEquals(200, response.getStatusCode());
        assertEquals(id, response.getBody().jsonPath().getString("projects[0].id"));
        assertEquals(Constants.project1Title, response.getBody().jsonPath().getString("projects[0].title"));
        assertEquals(Constants.description1, response.getBody().jsonPath().getString("projects[0].description"));
        assertEquals(Constants.project1Active, response.getBody().jsonPath().getString("projects[0].active"));
        assertEquals(Constants.project1Completed,
                response.getBody().jsonPath().getString("projects[0].completed"));
    }

    // BUG Actual behaviour
    @Test
    void testGetSpecificTodoWithIDTasksofInvalidIDActual() {
        String id = "1000000000";
        Response response = RestAssured.get(BASE_URL + "/todos" + "/" + id + "/tasksof");
        assertEquals(200, response.getStatusCode());
    }

    // BUG Expected behaviour
    @Test
    void testGetSpecificTodoWithIDTasksofInvalidIDExpected() {
        String id = "1000000000";
        Response response = RestAssured.get(BASE_URL + "/todos" + "/" + id + "/tasksof");
        assertEquals(404, response.getStatusCode());
        assertEquals("Could not find an instance with todos/" + id,
                response.getBody().jsonPath().getString("errorMessages[0]"));
    }

    @Test
    void testHeadSpecificTodoWithIDTasksof() {
        String id = "1";
        Response response = RestAssured.head(BASE_URL + "/todos" + "/" + id + "/tasksof");
        assertEquals(200, response.getStatusCode());
    }

    // BUG Actual behaviour
    @Test
    void testHeadSpecificTodoWithInvalidIDTasksofActual() {
        String id = "100000";
        Response response = RestAssured.head(BASE_URL + "/todos" + "/" + id + "/tasksof");
        assertEquals(200, response.getStatusCode());
    }

    // BUG Expected behaviour
    @Test
    void testHeadSpecificTodoWithInvalidIDTasksofExpected() {
        String id = "100000";
        Response response = RestAssured.head(BASE_URL + "/todos" + "/" + id + "/tasksof");
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void testPostSpecificTodoWithIDTasksof() {
        String id = "2";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("id", "1");

        request.body(requestParams.toJSONString());
        Response response = request.post(BASE_URL + "/todos" + "/" + id + "/tasksof");
        assertEquals(201, response.getStatusCode());

        // check if it really updated
        response = RestAssured.get(BASE_URL + "/todos" + "/" + id + "/tasksof");
        assertEquals(200, response.getStatusCode());
        assertEquals("1", response.getBody().jsonPath().getString("projects[0].id"));

    }
}
