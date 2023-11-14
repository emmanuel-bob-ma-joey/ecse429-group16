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
    public void AfterScenario() {
        System.out.println(" ------ After Scenario ------");
        HelperFunctions.stopApplication();
        System.out.println("application stopped");
    }
}
