import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProjectsTest {
	public static ProjectsAPI projectsAPI;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		projectsAPI = new ProjectsAPI();
		// TODO NEED TO RUN java -jar runTodoManagerRestAPI-1.5.5.jar
		// in local host
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		// TODO stop execution of java -jar runTodoManagerRestAPI-1.5.5.jar
	}

	@BeforeEach
	void setUp() throws Exception {

	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testProjectsAPI() {
		assertEquals(1,1);
	}

	@Test
	void testGET() {
		fail("Not yet implemented");
	}

	@Test
	void testGETInt() {
		fail("Not yet implemented");
	}

	@Test
	void testPOSTJSONObject() {
		fail("Not yet implemented");
	}

	@Test
	void testPOSTJSONObjectInt() {
		fail("Not yet implemented");
	}

}
