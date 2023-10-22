import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;


//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import io.restassured.RestAssured.*;

import java.io.IOException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig.NumberReturnType;


import static org.hamcrest.Matchers.*;
import java.util.List;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CategoryApiTest {
	 public Process RunRestAPI;

    final String BASE_URL = "http://localhost:4567/categories";
    //private final String listOfTodosJSON = "{\"todos\":[{\"id\":\"1\",\"title\":\"scan paperwork\",\"doneStatus\":\"false\",\"description\":\"\",\"categories\":[{\"id\":\"1\"}],\"tasksof\":[{\"id\":\"1\"}]},{\"id\":\"2\",\"title\":\"file paperwork\",\"doneStatus\":\"false\",\"description\":\"\",\"tasksof\":[{\"id\":\"1\"}]}]}";

    //private final String initialCategories = "{\"categories\":[{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"},{\"id\":\"2\",\"title\":\"Home\",\"description\":\"\"}]}";
    private final String initialCategory1 = "{\"categories\":[{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}";
    private final String initialCategories = "{\"categories\":[{\"id\":\"2\",\"title\":\"Home\",\"description\":\"\"},{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}";
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
    void testCategoryGetRequest() {
        Response response = RestAssured.get(BASE_URL);

        assertEquals(200, response.getStatusCode());
        //System.out.println(response.getBody().asString());
        //assertEquals(initialCategories, response.getBody().asString());
        assertEquals("2", response.getBody().jsonPath().getString("categories.size()"));

    }
    
	@Test
	void testCategoriesHeadRequest() {
		Response response = RestAssured.head(BASE_URL);
	    assertEquals(200, response.getStatusCode());
	}
	
	@Test
	  void testCategoriesPostRequest() {
	      RequestSpecification request = RestAssured.given();
	
	      JSONObject requestParams = new JSONObject();
	      requestParams.put("title", "test");
		  requestParams.put("description", "test");
		
		  request.body(requestParams.toJSONString());
		  Response response = request.post(BASE_URL);
		  assertEquals(201, response.getStatusCode());
		  assertEquals("test", response.getBody().jsonPath().getString("title"));
		  assertEquals("test", response.getBody().jsonPath().getString("description"));
	  }
	
	@Test
	  void testCategoriesPostRequestWithoutTitle() {
	      RequestSpecification request = RestAssured.given();
	
	      JSONObject requestParams = new JSONObject();
	      requestParams.put("description", "test");
		
		  request.body(requestParams.toJSONString());
		  Response response = request.post(BASE_URL);
		  assertEquals(400, response.getStatusCode());
		  assertEquals("title : field is mandatory", response.getBody().jsonPath().getString("errorMessages[0]"));
	  }
	
	@Test
	  void testCategoriesPostRequestWithEmptyTitle() {
	      RequestSpecification request = RestAssured.given();
	
	      JSONObject requestParams = new JSONObject();
	      requestParams.put("title", "");
	      requestParams.put("description", "test");
	
	      request.body(requestParams.toJSONString());
	      Response response = request.post(BASE_URL);
	      assertEquals(400, response.getStatusCode());
	      assertEquals("Failed Validation: title : can not be empty",
	              response.getBody().jsonPath().getString("errorMessages[0]"));
	  }
	
	//bug
	@Test
	  void testCategoriesPostRequestWithNumericalTitle() {
	      RequestSpecification request = RestAssured.given();
	
	      JSONObject requestParams = new JSONObject();
	      requestParams.put("title", 123);
	      requestParams.put("description", "test");
	
	      request.body(requestParams.toJSONString());
	      Response response = request.post(BASE_URL);
	      assertEquals(400, response.getStatusCode());
	     
	  }
	
	//bug
	@Test
	  void testCategoriesPostRequestWithNumericalTitleAndDescription() {
	      RequestSpecification request = RestAssured.given();
	
	      JSONObject requestParams = new JSONObject();
	      requestParams.put("title", 123);
	      requestParams.put("description", 123);
	
	      request.body(requestParams.toJSONString());
	      Response response = request.post(BASE_URL);
	      assertEquals(400, response.getStatusCode());
	     
	  }
	
	 @Test
	   void testGetSpecificCategoryWithID() {
	       String id = "1";
	       Response response = RestAssured.get(BASE_URL+"/" + id);
	       assertEquals(200, response.getStatusCode());
	       assertEquals(initialCategory1, response.getBody().asString());
	   }
	
	   @Test
	   void testGetSpecificCategoryWithInvalidID() {
	       String id = "3";
	       Response response = RestAssured.get(BASE_URL + "/" + id);
	       assertEquals(404, response.getStatusCode());
	       assertEquals("Could not find an instance with categories/" + id,
	               response.getBody().jsonPath().getString("errorMessages[0]"));
	   }
	   
	   @Test
	    void testHeadSpecificCategoryWithID() {
	        String id = "1";
	        Response response = RestAssured.head(BASE_URL + "/" + id);
	        assertEquals(200, response.getStatusCode());
	    }
	
	    @Test
	    void testHeadSpecificCategoryWithInvalidID() {
	        String id = "3";
	        Response response = RestAssured.head(BASE_URL + "/" + id);
	        assertEquals(404, response.getStatusCode());
	    }
	    
	    @Test
	    void testPostSpecificCategoryWithInvalidID() {
	        String id = "3";
	        RequestSpecification request = RestAssured.given();
	
	        JSONObject requestParams = new JSONObject();
	        requestParams.put("title", "test");
	        requestParams.put("description", "test");
	
	        request.body(requestParams.toJSONString());
	        Response response = request.post(BASE_URL + "/" + id);
	        assertEquals(404, response.getStatusCode());
	        assertEquals("No such category entity instance with GUID or ID "+id+" found",
		               response.getBody().jsonPath().getString("errorMessages[0]"));
	        
	    }
	    
	    @Test
	    void testPostSpecificCategoryWithValidID() {
	        String id = "1";
	        RequestSpecification request = RestAssured.given();
	
	        JSONObject requestParams = new JSONObject();
	        requestParams.put("title", "test");
	        requestParams.put("description", "test");
	
	        request.body(requestParams.toJSONString());
	        Response response = request.post(BASE_URL + "/" + id);
	        assertEquals("test", response.getBody().jsonPath().getString("title"));
	        assertEquals("test", response.getBody().jsonPath().getString("description"));
	        
	    }
	    @Test
	    void testPostSpecificCategoryWithValidIdOnlyTitle() {
	        String id = "1";
	        RequestSpecification request = RestAssured.given();
	
	        JSONObject requestParams = new JSONObject();
	        requestParams.put("title", "test");

	
	        request.body(requestParams.toJSONString());
	        Response response = request.post(BASE_URL + "/" + id);
	        assertEquals("test", response.getBody().jsonPath().getString("title"));
	        assertEquals("", response.getBody().jsonPath().getString("description"));
	        
	    }
	    @Test
	    void testPostSpecificCategoryWithValidIdOnlyDescription() {
	        String id = "1";
	        RequestSpecification request = RestAssured.given();
	
	        JSONObject requestParams = new JSONObject();
	        requestParams.put("description", "test");

	
	        request.body(requestParams.toJSONString());
	        Response response = request.post(BASE_URL + "/" + id);
	        assertEquals("Office", response.getBody().jsonPath().getString("title"));
	        assertEquals("test", response.getBody().jsonPath().getString("description"));
	        
	    }
	    //bug
	    @Test
	    void testPostSpecificCategoryWithValidIdNumericalTitleAndDescription() {
	        String id = "1";
	        RequestSpecification request = RestAssured.given();
	
	        JSONObject requestParams = new JSONObject();
	        requestParams.put("description", 123);
	        requestParams.put("title", 123);
	
	        request.body(requestParams.toJSONString());
	        Response response = request.post(BASE_URL + "/" + id);
	        
	        assertEquals(404, response.getStatusCode());
	        
	        
	    }
	    
	    @Test
	    void testPutSpecificCategoryWithInvalidID() {
	        String id = "3";
	        RequestSpecification request = RestAssured.given();
	
	        JSONObject requestParams = new JSONObject();
	
	        request.body(requestParams.toJSONString());
	        Response response = request.put(BASE_URL + "/" + id);
	        assertEquals(404, response.getStatusCode());
	        assertEquals("Invalid GUID for "+id+" entity category",
		               response.getBody().jsonPath().getString("errorMessages[0]"));
	        
	    }
	    
	    @Test
	    void testPutSpecificCategoryWithValidIDEmptyBody() {
	        String id = "1";
	        RequestSpecification request = RestAssured.given();
	
	        JSONObject requestParams = new JSONObject();
	
	        request.body(requestParams.toJSONString());
	        Response response = request.put(BASE_URL + "/" + id);
	        assertEquals(400, response.getStatusCode());
	        assertEquals("title : field is mandatory",
		               response.getBody().jsonPath().getString("errorMessages[0]"));
	        
	    }
	    
	    @Test
	    void testPutSpecificCategoryWithValidIDTitleAndDescription() {
	        String id = "1";
	        RequestSpecification request = RestAssured.given();
	
	        JSONObject requestParams = new JSONObject();
	        requestParams.put("description", "test");
	        requestParams.put("title", "test");
	
	        request.body(requestParams.toJSONString());
	        Response response = request.put(BASE_URL + "/" + id);
	        assertEquals(200, response.getStatusCode());
	        assertEquals("test", response.getBody().jsonPath().getString("title"));
	        assertEquals("test", response.getBody().jsonPath().getString("description"));
	        
	    }
	    
	  
	    
	    @Test
	    void testPutSpecificCategoryWithValidIDTitleOnly() {
	        String id = "1";
	        RequestSpecification request = RestAssured.given();
	
	        JSONObject requestParams = new JSONObject();
	        requestParams.put("title", "test");
	
	        request.body(requestParams.toJSONString());
	        Response response = request.put(BASE_URL + "/" + id);
	        assertEquals(200, response.getStatusCode());
	        assertEquals("test", response.getBody().jsonPath().getString("title"));
	        assertEquals("", response.getBody().jsonPath().getString("description"));
	        
	    }
	    
	    @Test
	    void testPutSpecificCategoryWithValidIDDescriptionOnly() {
	        String id = "1";
	        RequestSpecification request = RestAssured.given();
	
	        JSONObject requestParams = new JSONObject();
	        requestParams.put("description", "test");
	
	        request.body(requestParams.toJSONString());
	        Response response = request.put(BASE_URL + "/" + id);
	        assertEquals(400, response.getStatusCode());
	        assertEquals("title : field is mandatory",
		               response.getBody().jsonPath().getString("errorMessages[0]"));

	        
	    }
	    
	    @Test
	    void testDeleteSpecificCategoryWithInvalidID() {
	        String id = "3";
	        Response response = RestAssured.delete(BASE_URL + "/" + id);
	        assertEquals(404, response.getStatusCode());
	        assertEquals("Could not find any instances with categories/"+id,
		               response.getBody().jsonPath().getString("errorMessages[0]"));

	    }
	    
	    @Test
	    void testDeleteSpecificCategoryWithValidID() {
	        String id = "1";
	        Response response = RestAssured.delete(BASE_URL + "/" + id);
	        assertEquals(200, response.getStatusCode());
	
	        // check if deleted
	        response = RestAssured.get(BASE_URL  + "/" + id);
	        assertEquals(404, response.getStatusCode());
	    }
	    
	    //bug
	    @Test
	   void testGetCategoryProjectWithInvalidID() {
	       String id = "3";
	       Response response = RestAssured.get(BASE_URL + "/" + id+"/projects");
	       assertEquals(404, response.getStatusCode());
	   }
	    
	    @Test
	   void testGetCategoryProjectWithValidID() {
	       String id = "1";
	       Response response = RestAssured.get(BASE_URL + "/" + id+"/projects");
	       assertEquals(200, response.getStatusCode());
	       assertEquals(null, response.getBody().jsonPath().getString("description"));
	   }
	    
	    @Test
	   void testHeadCategoryProjectWithValidID() {
	       String id = "1";
	       Response response = RestAssured.head(BASE_URL + "/" + id+"/projects");
	       assertEquals(200, response.getStatusCode());
	   }
	    
	    @Test
	   void testPostProjectToInValidCategory() {
	       String id = "3";
	       
	       RequestSpecification request = RestAssured.given();
	       JSONObject requestParams = new JSONObject();
	        requestParams.put("id", 2);
	
	       request.body(requestParams.toJSONString());
	       
	       Response response = request.post(BASE_URL + "/" + id+"/projects");
	       assertEquals(404, response.getStatusCode());
	       assertEquals("Could not find parent thing for relationship categories/"+id+"/projects",
	               response.getBody().jsonPath().getString("errorMessages[0]"));

	   }
	    
	    @Test
	   void testPostInvalidProjectToInValidCategory() {
	       String id = "3";
	       
	       RequestSpecification request = RestAssured.given();
	       JSONObject requestParams = new JSONObject();
	        requestParams.put("id", 2);
	
	       request.body(requestParams.toJSONString());
	       
	       Response response = request.post(BASE_URL + "/" + id+"/projects");
	       assertEquals(404, response.getStatusCode());
	       assertEquals("Could not find parent thing for relationship categories/"+id+"/projects",
	               response.getBody().jsonPath().getString("errorMessages[0]"));
	       

	   }
	    
	   @Test
	   void testPostProjectToCategory() {
	       String id = "1";
	       
	       RequestSpecification request = RestAssured.given();
	       JSONObject requestParams = new JSONObject();
	        requestParams.put("id", "1");
	
	       request.body(requestParams.toJSONString());
	       
	       Response response = request.post(BASE_URL + "/" + id+"/projects");
	       assertEquals(201, response.getStatusCode());
	       
	
	   }
	   
	   @Test
	    void testDeleteInvalidProjectIdFromInvalidCategoryId() {
	        String id = "10";
	        Response response = RestAssured.delete(BASE_URL + "/" + id+"/projects/"+id);
	        assertEquals(404, response.getStatusCode());
	
	    }
	   
	   @Test
	    void testDeleteValidProjectIdFromInvalidCategoryId() {
	        String id = "10";
	        Response response = RestAssured.delete(BASE_URL + "/" + id+"/projects/1");
	        assertEquals(404, response.getStatusCode());
		    assertEquals("Could not find any instances with categories/"+id+"/projects/1",
		               response.getBody().jsonPath().getString("errorMessages[0]"));
	
	    }
	   
	   @Test
	    void testDeleteValidProjectIdFromValidCategoryId() {
		   //add project category first
		   
		   String CategoryId = "2";
		   String ProjectId = "1";
	       
	       RequestSpecification request = RestAssured.given();
	       JSONObject requestParams = new JSONObject();
	        requestParams.put("id", ProjectId);
	
	       request.body(requestParams.toJSONString());
	       
	       Response response = request.post(BASE_URL + "/" + CategoryId+"/projects");
	       //delete project from category
	       
	        response = RestAssured.delete(BASE_URL + "/" + CategoryId+"/projects/"+ProjectId);
	        assertEquals(200, response.getStatusCode());
	        
	        //check if its deleted
	        
	        response = RestAssured.get(BASE_URL + "/" + CategoryId+"/projects");
	        assertEquals(200, response.getStatusCode());
	        assertEquals(null, response.getBody().jsonPath().getString("projects"));
	        
	        
		    
	
	    }
	   
	   
	   
	   
	    
	    
	    
	    
	


}
