import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

	@BeforeAll
	static void setUp() throws Exception {
		int numTries = 10;
		while (numTries>0) {
			try {
				RunRestAPI = Runtime.getRuntime().exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
				Thread.sleep(2000); // wait for server to start
				numTries = 0;
			} catch (IOException e) {
				//System.out.println("ERROR!");
				//e.printStackTrace();
				numTries--;
			}
		}
	}

	@AfterAll
	static void tearDown() throws Exception {
		RunRestAPI.destroy();
	}

	//WORKS!
	@Test
	void testGET() {
		RequestSpecification request = RestAssured.given();
		Response response = request.get(BASE_URL);
		assertEquals(200, response.getStatusCode());
	}

	//WORKS!
	@Test
	void testGETValidID() {
		String validID = "1";
		RequestSpecification request = RestAssured.given();
		Response response = request.get(BASE_URL+"/"+validID);
		assertEquals(200, response.getStatusCode());
		assertEquals(validID, response.jsonPath().getString("projects[0].id"));
	}

	//WORKS!
	@Test
	void testGETInvalidID() {
		String invalidID = "asdf";
		RequestSpecification request = RestAssured.given();
		Response response = request.get(BASE_URL + "/" + invalidID);
		assertEquals(404, response.getStatusCode());
		assertEquals("[Could not find an instance with projects/"+invalidID+"]",response.jsonPath().getString("errorMessages"));
	}



	//WORKS!
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


	//WORKS!
	@Test
	void testPOSTValidID() {
		String validID = "1";
		RequestSpecification request = RestAssured.given();
		request.body(JSON_TO_POST);
		Response response = request.post(BASE_URL+"/"+validID);
		assertEquals(200, response.getStatusCode());
		assertEquals(validID, response.jsonPath().getString("id"));
		assertEquals(title, response.jsonPath().getString("title"));
		assertEquals(completed, response.jsonPath().getString("completed"));
		assertEquals(active, response.jsonPath().getString("active"));
		assertEquals(description, response.jsonPath().getString("description"));
	}


	//WORKS!
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
}
