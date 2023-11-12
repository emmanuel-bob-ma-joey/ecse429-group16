package features;

import io.cucumber.java.After;

public class CommonStepDefinitions {
    /**
     * Method used to delete the current divesafe system instance before the next
     * test. This is effective
     * for all scenerios in all feature files
     */
    @After
    public void tearDown() {
        HelperFunctions.stopApplication();
    }

}