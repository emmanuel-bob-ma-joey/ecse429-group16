// import java.io.IOException;
// import java.util.List;

// import org.apache.http.client.ClientProtocolException;
// import org.junit.After;
// import org.junit.Before;
// import org.junit.Test;




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




public class CategoriesPerformanceTest {
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
  public void testCategoryPerformance() throws ClientProtocolException, IOException {

    XYSeries series = new XYSeries("Memory Data");
    XYSeries series2 = new XYSeries("Add Time Data");
    XYSeries series3 = new XYSeries("Delete Time Data");
    XYSeries series4 = new XYSeries("Update Time Data");
    XYSeries series5 = new XYSeries("Process CPU Data");
    //XYSeries series6 = new XYSeries("System CPU Data");

    for (int ITERATIONS : Utils.ITERATIONS) {
      // Add iterations amount of categories
      for (int i = 0; i <= ITERATIONS; i++) {
        Utils.createCategory("title", "description");
      }

      System.out.println("------------ " + ITERATIONS + " Categories Added ------------");

      // count the number of categories
      Response response = Utils.getAllCategories("");
      List<Object> categories = response.jsonPath().getList("categories");
      System.out.println("Number of categories: " + categories.size());
      int size = categories.size();

      // Create a new Category

      long startTimeForAdd = System.nanoTime();
      Response newCategory = Utils.createCategory("title", "description");
      long endTimeForAdd = System.nanoTime();

      System.out.println(
          "Time taken to add a category: " + (float) (endTimeForAdd - startTimeForAdd) / Utils.millisecondsInNano
              + " milliseconds");
      series2.add(size,(float) (endTimeForAdd - startTimeForAdd) / Utils.millisecondsInNano);

      int id = Integer.parseInt(newCategory.getBody().jsonPath().getString("id"));

      // Update the Category

      long startTimeForUpdate = System.nanoTime();

      Utils.updateCategoryWithId(id,"new title", "Updated category");

      long endTimeForUpdate = System.nanoTime();

      System.out.println("Time taken to update a category: "
          + (float) (endTimeForUpdate - startTimeForUpdate) / Utils.millisecondsInNano + " milliseconds");
      series4.add(size,(float) (endTimeForUpdate - startTimeForUpdate) / Utils.millisecondsInNano);
      // Delete the Category

      long startTimeForDelete = System.nanoTime();

      Utils.deleteCategoryWithId(id);
      long endTimeForDelete = System.nanoTime();

      System.out.println("Time taken to delete a Category: "
          + (float) (endTimeForDelete - startTimeForDelete) / Utils.millisecondsInNano + " milliseconds");
      series3.add(size,(float) (endTimeForDelete - startTimeForDelete) / Utils.millisecondsInNano);
      System.gc();
      Runtime rt = Runtime.getRuntime();
      long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
      System.out.println("memory usage:" + usedMB);
      series.add(size, usedMB);
      series5.add(size,osBean.getProcessCpuLoad());
      //System.out.println("System CPU load is at:"+osBean.getSystemCpuLoad());

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
                "Category api time usage",
                "number of category objects",
                "time (milliseconds)",
                timedataset
    );
    JFreeChart memorychart = ChartFactory.createXYLineChart(
                "Category api memory usage",
                "number of category objects",
                "memory (MB)",
                memorydataset
    );

    JFreeChart cpuchart = ChartFactory.createXYLineChart(
                "Category api cpu usage",
                "number of category objects",
                "cpu  usage (%)",
                cpudataset
    );

    // Save the chart to an image file
    try {
        ChartUtils.saveChartAsPNG(new File("./graphs/category_time_chart.png"), timechart, 600, 400);
        ChartUtils.saveChartAsPNG(new File("./graphs/category_memory_chart.png"), memorychart, 600, 400);
        ChartUtils.saveChartAsPNG(new File("./graphs/category_cpu_chart.png"), cpuchart, 600, 400);
        //System.out.println("Chart saved to line_chart.png");
    } catch (IOException e) {
        e.printStackTrace();
    }

  }

}