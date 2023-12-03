import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.response.Response;

public class CategoriesPerformanceTest {
  public Process RunRestAPI;

  @Before
  public void setUp() throws Exception {

    try {
      RunRestAPI = Runtime.getRuntime().exec(Utils.startCommand);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Thread.sleep(3000); // wait for server to start
  }

  @After
  public void tearDown() throws Exception {
    RunRestAPI.destroy();
    Thread.sleep(1000);
  }

  @Test
  public void testTodoPerformance() throws ClientProtocolException, IOException {

    for (int ITERATIONS : Utils.ITERATIONS) {

      // Add iterations amount of todos
      for (int i = 0; i <= ITERATIONS; i++) {
        Utils.createCategory("title", "description", false);
      }

      System.out.println("------------ " + ITERATIONS + " Todos Added ------------");

      // count the number of todos
      Response response = Utils.getAllCategories("");
      List<Object> todos = response.jsonPath().getList("todos");
      System.out.println("Number of todos: " + todos.size());

      // Create a new Todo

      long startTimeForAdd = System.nanoTime();
      Response newTodo = Utils.createCategory("title", "description", false);
      long endTimeForAdd = System.nanoTime();

      System.out.println(
          "Time taken to add a todo: " + (float) (endTimeForAdd - startTimeForAdd) / Utils.millisecondsInNano
              + " milliseconds");

      int id = Integer.parseInt(newTodo.getBody().jsonPath().getString("id"));

      // Update the Todo

      long startTimeForUpdate = System.nanoTime();

      Utils.updateCategoryWithId(id,
          "new title", "Updated todo", true);

      long endTimeForUpdate = System.nanoTime();

      System.out.println("Time taken to update a todo: "
          + (float) (endTimeForUpdate - startTimeForUpdate) / Utils.millisecondsInNano + " milliseconds");

      // Delete the Todo

      long startTimeForDelete = System.nanoTime();

      Utils.deleteCategoryWithId(id);
      long endTimeForDelete = System.nanoTime();

      System.out.println("Time taken to delete a todo: "
          + (float) (endTimeForDelete - startTimeForDelete) / Utils.millisecondsInNano + " milliseconds");

    }
  }

}