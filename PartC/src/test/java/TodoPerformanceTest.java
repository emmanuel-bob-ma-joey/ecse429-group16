import java.io.IOException;
import java.util.List;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import com.sun.management.OperatingSystemMXBean;

import org.apache.http.client.ClientProtocolException;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;

public class TodoPerformanceTest {
  public Process RunRestAPI;
  public OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean.class);

  @BeforeEach
  public void setUp() throws Exception {

    try {
      RunRestAPI = Runtime.getRuntime().exec(Utils.startCommand);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Thread.sleep(3000); // wait for server to start
  }

  @AfterEach
  public void tearDown() throws Exception {
    RunRestAPI.destroy();
    Thread.sleep(1000);
  }

  @Test
  public void testTodoPerformance() throws ClientProtocolException, IOException {
    XYSeries series = new XYSeries("Memory Data");
    XYSeries series2 = new XYSeries("Add Time Data");
    XYSeries series3 = new XYSeries("Delete Time Data");
    XYSeries series4 = new XYSeries("Update Time Data");
    XYSeries series5 = new XYSeries("Process CPU Data");

    for (int ITERATIONS : Utils.ITERATIONS) {

      // Add iterations amount of todos
      for (int i = 0; i <= ITERATIONS; i++) {
        Utils.createTodo("title", "description", false);
      }

      System.out.println("------------ " + ITERATIONS + " Todos Added ------------");

      // count the number of todos
      Response response = Utils.getAllTodos("");
      List<Object> todos = response.jsonPath().getList("todos");
      System.out.println("Number of todos: " + todos.size());
      int size = todos.size();

      // Create a new Todo

      long startTimeForAdd = System.nanoTime();
      Response newTodo = Utils.createTodo("title", "description", false);
      long endTimeForAdd = System.nanoTime();

      System.out.println(
          "Time taken to add a todo: " + (float) (endTimeForAdd - startTimeForAdd) / Utils.millisecondsInNano
              + " milliseconds");
      series2.add(size,(float) (endTimeForAdd - startTimeForAdd) / Utils.millisecondsInNano);

      int id = Integer.parseInt(newTodo.getBody().jsonPath().getString("id"));

      // Update the Todo

      long startTimeForUpdate = System.nanoTime();

      Utils.updateTodoWithId(id,
          "new title", "Updated todo", true);

      long endTimeForUpdate = System.nanoTime();

      System.out.println("Time taken to update a todo: "
          + (float) (endTimeForUpdate - startTimeForUpdate) / Utils.millisecondsInNano + " milliseconds");
      series4.add(size,(float) (endTimeForUpdate - startTimeForUpdate) / Utils.millisecondsInNano);
      // Delete the Todo

      long startTimeForDelete = System.nanoTime();

      Utils.deleteTodoWithId(id);
      long endTimeForDelete = System.nanoTime();

      System.out.println("Time taken to delete a todo: "
          + (float) (endTimeForDelete - startTimeForDelete) / Utils.millisecondsInNano + " milliseconds");
      series3.add(size,(float) (endTimeForDelete - startTimeForDelete) / Utils.millisecondsInNano);

      System.gc();
      Runtime rt = Runtime.getRuntime();
      long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
      System.out.println("memory usage:" + usedMB);
      series.add(size, usedMB);
      series5.add(size,osBean.getProcessCpuLoad());

    }
    XYSeriesCollection timedataset = new XYSeriesCollection();
    XYSeriesCollection memorydataset = new XYSeriesCollection();
    XYSeriesCollection cpudataset = new XYSeriesCollection();
    memorydataset.addSeries(series);
    timedataset.addSeries(series2);
    timedataset.addSeries(series3);
    timedataset.addSeries(series4);
    cpudataset.addSeries(series5);;
    JFreeChart timechart = ChartFactory.createXYLineChart(
                "todo api time usage",
                "number of todo objects",
                "time (milliseconds)",
                timedataset
    );
    JFreeChart memorychart = ChartFactory.createXYLineChart(
                "todo api memory usage",
                "number of todo objects",
                "memory (MB)",
                memorydataset
    );

    JFreeChart cpuchart = ChartFactory.createXYLineChart(
                "todo api cpu usage",
                "number of todo objects",
                "cpu  usage (%)",
                cpudataset
    );

    // Save the chart to an image file
    try {
        ChartUtils.saveChartAsPNG(new File("./graphs/todo_time_chart.png"), timechart, 600, 400);
        ChartUtils.saveChartAsPNG(new File("./graphs/todo_memory_chart.png"), memorychart, 600, 400);
        ChartUtils.saveChartAsPNG(new File("./graphs/todo_cpu_chart.png"), cpuchart, 600, 400);
        //System.out.println("Chart saved to line_chart.png");
    } catch (IOException e) {
        e.printStackTrace();
    }

  }

}