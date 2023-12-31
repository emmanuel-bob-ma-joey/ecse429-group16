import java.io.IOException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Random.class) // so that the tests are run in a random order
public class ProjectApiTest {
	public Process RunRestAPI;

	static final String BASE_URL = Constants.base_url + "/projects";

	static final String title = "victor's project";
	static final String completed = "true", active = "false";
	static final String description = "a fun project";
	static final String JSON_TO_POST = "{" +
			"    \"title\": \"" + title + "\"," +
			"    \"completed\": " + completed + "," +
			"    \"active\": " + active + "," +
			"    \"description\": \"" + description + "\"" +
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
		Thread.sleep(1000);
	}

	// -----------------------/projects Endpoints
	// --------------------------------------

	@Test
	void testGET() {
		RequestSpecification request = RestAssured.given();
		Response response = request.get(BASE_URL);
		assertEquals(200, response.getStatusCode());
	}

	@Test
	void testHEAD() {
		RequestSpecification request = RestAssured.given();
		Response response = request.head(BASE_URL);
		assertEquals(200, response.getStatusCode());
	}

	@Test
	void testPOSTMalformedJSON() {
		RequestSpecification request = RestAssured.given();
		request.body(JSON_TO_POST.replace(',', ' '));// all commas replaced by space, JSON is not properly formatted
		Response response = request.post(BASE_URL);
		assertEquals(400, response.getStatusCode());
		assertNull(response.jsonPath().getString("title"));
		assertNull(response.jsonPath().getString("completed"));
		assertNull(response.jsonPath().getString("active"));
		assertNull(response.jsonPath().getString("description"));
	}

	@Test
	void testPOST() {
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
	public void testXmlPayload() {
		Response response = RestAssured.given()
				.accept(ContentType.XML) // Request XML response
				.when()
				.get(BASE_URL);
		assertEquals(200, response.getStatusCode());
		assertEquals("application/xml", response.getContentType());
	}

	@Test
	void testPOSTSameJSONTwice() {
		RequestSpecification request = RestAssured.given();
		request.body(JSON_TO_POST);
		Response response1 = request.post(BASE_URL);

		request.body(JSON_TO_POST);
		Response response2 = request.post(BASE_URL);

		String ID1 = response1.jsonPath().getString("id");
		String ID2 = response2.jsonPath().getString("id");

		assertEquals(201, response1.getStatusCode());
		assertEquals(201, response2.getStatusCode());

		assertEquals(title, response1.jsonPath().getString("title"));
		assertEquals(completed, response1.jsonPath().getString("completed"));
		assertEquals(active, response1.jsonPath().getString("active"));
		assertEquals(description, response1.jsonPath().getString("description"));

		assertEquals(title, response2.jsonPath().getString("title"));
		assertEquals(completed, response2.jsonPath().getString("completed"));
		assertEquals(active, response2.jsonPath().getString("active"));
		assertEquals(description, response2.jsonPath().getString("description"));

		assertNotEquals(ID1, ID2);
	}

	// -----------------------/projects/:id Endpoints
	// --------------------------------------

	// WORKS!
	@Test
	void testPOSTValidID() {
		String validID = "1";
		RequestSpecification request = RestAssured.given();
		request.body(JSON_TO_POST);
		Response response = request.post(BASE_URL + "/" + validID);
		assertEquals(200, response.getStatusCode());
		assertEquals(validID, response.jsonPath().getString("id"));
		assertEquals(title, response.jsonPath().getString("title"));
		assertEquals(completed, response.jsonPath().getString("completed"));
		assertEquals(active, response.jsonPath().getString("active"));
		assertEquals(description, response.jsonPath().getString("description"));
	}

	@Test
	void testPOSTInvalidID() {
		String invalidID = "asdf";
		RequestSpecification request = RestAssured.given();
		request.body(JSON_TO_POST);
		Response response = request.post(BASE_URL + "/" + invalidID);
		assertEquals(404, response.getStatusCode());
		assertEquals("[No such project entity instance with GUID or ID " + invalidID + " found]",
				response.jsonPath().getString("errorMessages"));
	}

	@Test
	void testGETValidID() {
		String validID = "1";
		RequestSpecification request = RestAssured.given();
		Response response = request.get(BASE_URL + "/" + validID);
		assertEquals(200, response.getStatusCode());
		assertEquals(validID, response.jsonPath().getString("projects[0].id"));
	}

	@Test
	void testGETInvalidID() {
		String invalidID = "asdf";
		RequestSpecification request = RestAssured.given();
		Response response = request.get(BASE_URL + "/" + invalidID);
		assertEquals(404, response.getStatusCode());
		assertEquals("[Could not find an instance with projects/" + invalidID + "]",
				response.jsonPath().getString("errorMessages"));
	}
}