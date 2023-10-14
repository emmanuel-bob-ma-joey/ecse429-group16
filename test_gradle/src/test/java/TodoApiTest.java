import static org.junit.Assert.assertThat;
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
            Thread.sleep(2000); // wait for server to start
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @AfterEach
    void tearDown() throws Exception {
        RunRestAPI.destroy();
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
    void testGetSpecificTodoWithID() {
        String id = "2";
        Response response = RestAssured.get(BASE_URL + "/todos" + "/" + id);
        assertEquals(200, response.getStatusCode());
        System.out.println(response.getBody().asString());
        assertEquals(initialTodoWithId2, response.getBody().asString());
    }

    @Test
    void testHeadSpecificTodoWithID() {
        String id = "2";
        Response response = RestAssured.head(BASE_URL + "/todos" + "/" + id);
        assertEquals(200, response.getStatusCode());
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

}
