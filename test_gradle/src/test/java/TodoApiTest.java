import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TodoApiTest {

    public Process RunRestAPI;

    final String BASE_URL = "http://localhost:4567";
    private final String listOfTodosJSON = "{\"todos\":[{\"id\":\"1\",\"title\":\"scan paperwork\",\"doneStatus\":\"false\",\"description\":\"\",\"categories\":[{\"id\":\"1\"}],\"tasksof\":[{\"id\":\"1\"}]},{\"id\":\"2\",\"title\":\"file paperwork\",\"doneStatus\":\"false\",\"description\":\"\",\"tasksof\":[{\"id\":\"1\"}]}]}";
    private final String initialTodoWithId2 = "{\"todos\":[{\"id\":\"2\",\"title\":\"file paperwork\",\"doneStatus\":\"false\",\"description\":\"\",\"tasksof\":[{\"id\":\"1\"}]}]}";

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
        Thread.sleep(1000);
    }

    @Test
    // todos
    // FIXME: this test is failing due to the order of the todos being different
    void testTodoGetRequest() {
        Response response = RestAssured.get(BASE_URL + "/todos");

        assertEquals(200, response.getStatusCode());
        assertEquals(listOfTodosJSON, response.getBody().asString());

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
    void testGetSpecificTodoWithID() {
        String id = "1";
        Response response = RestAssured.get(BASE_URL + "/todos" + "/" + id);
        assertEquals(200, response.getStatusCode());
        assertEquals(initialTodoWithId2, response.getBody().asString());
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
    void testDeleteSpecificTodoWithID() {
        String id = "1";
        Response response = RestAssured.delete(BASE_URL + "/todos" + "/" + id);
        assertEquals(200, response.getStatusCode());

        // check if deleted
        response = RestAssured.get(BASE_URL + "/todos" + "/" + id);
        assertEquals(404, response.getStatusCode());
    }

    // /todos/{id}/categories
    @Test
    void testGetSpecificTodoWithIDCategories() {
        String id = "1";
        Response response = RestAssured.get(BASE_URL + "/todos" + "/" + id + "/categories");
        assertEquals(200, response.getStatusCode());
        assertEquals("{\"categories\":[{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}",
                response.getBody().asString());
    }

    @Test
    // BUG: Actual behaviour
    void testGetSpecificTodoWithIDCategoriesInvalidIDActual() {
        String id = "1000000000";
        Response response = RestAssured.get(BASE_URL + "/todos" + "/" + id + "/categories");
        assertEquals(200, response.getStatusCode());
        assertEquals("{\"categories\":[{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}",
                response.getBody().asString());
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
        assertEquals("{\"categories\":[{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}",
                response.getBody().asString());
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

    // /todos/{id}/tasksof

    @Test
    void testGetSpecificTodoWithIDTasksof() {
        String id = "1";
        Response response = RestAssured.get(BASE_URL + "/todos" + "/" + id + "/tasksof");
        assertEquals(200, response.getStatusCode());
        assertEquals(
                "{\"projects\":[{\"id\":\"1\",\"title\":\"Office Work\",\"completed\":\"false\",\"active\":\"false\",\"description\":\"\",\"tasks\":[{\"id\":\"2\"},{\"id\":\"1\"}]}]}",
                response.getBody().asString());
    }

    // BUG Actual behaviour
    @Test
    void testGetSpecificTodoWithIDTasksofInvalidIDActual() {
        String id = "1000000000";
        Response response = RestAssured.get(BASE_URL + "/todos" + "/" + id + "/tasksof");
        assertEquals(200, response.getStatusCode());
        // gives back all the projects relations to the tasks
        assertEquals(
                "{\"projects\":[{\"id\":\"1\",\"title\":\"Office Work\",\"completed\":\"false\",\"active\":\"false\",\"description\":\"\",\"tasks\":[{\"id\":\"2\"},{\"id\":\"1\"}]},{\"id\":\"1\",\"title\":\"Office Work\",\"completed\":\"false\",\"active\":\"false\",\"description\":\"\",\"tasks\":[{\"id\":\"2\"},{\"id\":\"1\"}]}]}",
                response.getBody().asString());
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
        assertEquals(
                "{\"projects\":[{\"id\":\"1\",\"title\":\"Office Work\",\"completed\":\"false\",\"active\":\"false\",\"description\":\"\",\"tasks\":[{\"id\":\"2\"},{\"id\":\"1\"}]}]}",
                response.getBody().asString());
    }

}
