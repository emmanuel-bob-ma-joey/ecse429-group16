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

public class RestApiTest {

    public Process RunRestAPI;

    final String BASE_URL = "http://localhost:4567";

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

    // example test
    @Test
    void testTodoHeadRequest() {
        Response response = RestAssured.head(BASE_URL + "/todos");
        assertEquals(200, response.getStatusCode());
    }
}