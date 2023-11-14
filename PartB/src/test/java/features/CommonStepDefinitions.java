package features;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class CommonStepDefinitions {
    /**
     * Method used to reset system before the next
     * test. This is effective
     * for all scenerios in all feature files
     */
    @After
    public void tearDown() {
        System.out.println(" ------ After Scenario ------");
        HelperFunctions.stopApplication();
        System.out.println("application stopped");
    }

    @Before
    public void startUp() throws InterruptedException {
        System.out.println(" ------ Before Scenario ------");
        HelperFunctions.startApplication();
        System.out.println("application started");
    }
}
