import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.Random.class) // so that the tests are run in a random order
public class CategoryApiTestActualBehaviour {
	public Process RunRestAPI;

	final String BASE_URL = "http://localhost:4567/categories";

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

	// bug
	@Test
	void testCategoriesPostRequestWithNumericalTitle() {
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("title", 123);
		requestParams.put("description", "test");

		request.body(requestParams.toJSONString());
		Response response = request.post(BASE_URL);
		assertEquals(201, response.getStatusCode());
		assertEquals("123.0", response.getBody().jsonPath().getString("title"));

	}

	// bug
	@Test
	void testCategoriesPostRequestWithNumericalTitleAndDescription() {
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("title", 123);
		requestParams.put("description", 123);

		request.body(requestParams.toJSONString());
		Response response = request.post(BASE_URL);
		assertEquals(201, response.getStatusCode());
		assertEquals("123.0", response.getBody().jsonPath().getString("title"));
		assertEquals("123.0", response.getBody().jsonPath().getString("description"));

	}

	// bug
	@Test
	void testPostSpecificCategoryWithValidIdNumericalTitleAndDescription() {
		String id = "1";
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		requestParams.put("description", 123);
		requestParams.put("title", 123);

		request.body(requestParams.toJSONString());
		Response response = request.post(BASE_URL + "/" + id);

		assertEquals(200, response.getStatusCode());

	}

	// bug
	@Test
	void testGetCategoryProjectWithInvalidID() {
		String id = "3";
		Response response = RestAssured.get(BASE_URL + "/" + id + "/projects");
		assertEquals(200, response.getStatusCode());
	}
}
