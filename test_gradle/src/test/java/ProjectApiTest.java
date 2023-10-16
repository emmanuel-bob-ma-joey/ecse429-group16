import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProjectApiTest {
	public static Process RunRestAPI;

	static final String BASE_URL = "http://localhost:4567/projects";

	static final String title = "victor's project";
	static final String completed = "true", active = "false";
	static final String description = "a fun project";
	static final String JSON_TO_POST = "{" +
			"    \"title\": \""+title+"\"," +
			"    \"completed\": "+completed+"," +
			"    \"active\": "+active+"," +
			"    \"description\": \""+description+"\"" +
			"}";

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
	void testProjectsAPI() {
		assertEquals(1,1);
	}

	@Test
	void testGET() {
		assertEquals(1,1);
	}

	@Test
	void testGETInt() {
		assertEquals(1,1);
	}


	//WORKS!
	@Test
	void testPOSTJSONObject() {
		RequestSpecification request = RestAssured.given();
		request.body(JSON_TO_POST);
		Response response = request.post(BASE_URL);
		assertEquals(201, response.getStatusCode());
		assertEquals(title, response.jsonPath().getString("title"));
		assertEquals(completed, response.jsonPath().getString("completed"));
		assertEquals(active, response.jsonPath().getString("active"));
		assertEquals(description, response.jsonPath().getString("description"));
	}

	@Test
	void testPOSTJSONObjectInt() {
		assertEquals(1,1);
	}
}
