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

public class ProjectPerformanceTest {
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
    public void testProjectPerformance() throws ClientProtocolException, IOException {
        XYSeries series = new XYSeries("Memory Data");
        XYSeries series2 = new XYSeries("Add Time Data");
        XYSeries series3 = new XYSeries("Delete Time Data");
        XYSeries series4 = new XYSeries("Update Time Data");
        XYSeries series5 = new XYSeries("Process CPU Data");

        for (int ITERATIONS : Utils.ITERATIONS) {

            // Add iterations amount of projects
            for (int i = 0; i <= ITERATIONS; i++) {
                Utils.createProject("title", "description", "false", "false");
            }

            System.out.println("------------ " + ITERATIONS + " Project Added ------------");

            // count the number of Projects
            Response response = Utils.getAllProjects("");
            List<Object> projects = response.jsonPath().getList("projects");
            System.out.println("Number of projects: " + projects.size());
            int size = projects.size();

            // Create a new project

            long startTimeForAdd = System.nanoTime();
            Response newProject = Utils.createProject("title", "description", "false", "false");
            long endTimeForAdd = System.nanoTime();

            System.out.println(
                    "Time taken to add a project: "
                            + (float) (endTimeForAdd - startTimeForAdd) / Utils.millisecondsInNano
                            + " milliseconds");
            series2.add(size,(float) (endTimeForAdd - startTimeForAdd) / Utils.millisecondsInNano);

            int id = Integer.parseInt(newProject.getBody().jsonPath().getString("id"));

            // Update the project

            long startTimeForUpdate = System.nanoTime();

            Utils.updateProjectWithId(id, "new title", "Updated project", "true");

            long endTimeForUpdate = System.nanoTime();

            System.out.println("Time taken to update a project: "
                    + (float) (endTimeForUpdate - startTimeForUpdate) / Utils.millisecondsInNano + " milliseconds");
            series4.add(size,(float) (endTimeForUpdate - startTimeForUpdate) / Utils.millisecondsInNano);
            // Delete the project

            long startTimeForDelete = System.nanoTime();

            Utils.deleteProjectWithId(id);
            long endTimeForDelete = System.nanoTime();

            System.out.println("Time taken to delete a project: "
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
                    "project api time usage",
                    "number of project objects",
                    "time (milliseconds)",
                    timedataset
        );
        JFreeChart memorychart = ChartFactory.createXYLineChart(
                    "project api memory usage",
                    "number of project objects",
                    "memory (MB)",
                    memorydataset
        );

        JFreeChart cpuchart = ChartFactory.createXYLineChart(
                    "project api cpu usage",
                    "number of project objects",
                    "cpu  usage (%)",
                    cpudataset
        );

        // Save the chart to an image file
        try {
            ChartUtils.saveChartAsPNG(new File("./graphs/project_time_chart.png"), timechart, 600, 400);
            ChartUtils.saveChartAsPNG(new File("./graphs/project_memory_chart.png"), memorychart, 600, 400);
            ChartUtils.saveChartAsPNG(new File("./graphs/project_cpu_chart.png"), cpuchart, 600, 400);
            //System.out.println("Chart saved to line_chart.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}