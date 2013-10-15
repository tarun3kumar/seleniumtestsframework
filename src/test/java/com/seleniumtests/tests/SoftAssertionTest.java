package com.seleniumtests.tests;

import static com.seleniumtests.core.CustomAssertion.*;

import com.seleniumtests.core.TestLogging;
import com.seleniumtests.core.SeleniumTestPlan;
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
        TestLogging.log("This message is logged after initial assertion failures. Hence test execution continues even in the wake of test failures");
    }
}
