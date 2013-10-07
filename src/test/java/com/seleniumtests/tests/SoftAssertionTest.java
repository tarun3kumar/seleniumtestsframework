package com.seleniumtests.tests;

import static com.seleniumtests.controller.CustomAssertion.*;

import com.seleniumtests.controller.Logging;
import com.seleniumtests.controller.SeleniumTestPlan;
import org.testng.annotations.Test;

/**
 * Demonstrate test execution continues even though assertions fail
 *
 * Date: 10/2/13
 * Time: 4:59 PM
 */
public class SoftAssertionTest extends SeleniumTestPlan {

    /**
     * Continues with test execution even though assertions fail
     */
    @Test(groups = "softAssertionTest", description = "Continues with test execution even though assertions fail")
    public void softAssertionTest() {
        assertEquals(true, false, "boolean test failure");
        assertTrue(false, "another boolean test failure");
        assertEquals("selenium", "qtp", "universal test failure :)");
        assertTrue(1==1, "never fails");
        Logging.log("This message is logged after initial assertion failures. Hence test execution continues even in the wake of test failures");
    }
}
