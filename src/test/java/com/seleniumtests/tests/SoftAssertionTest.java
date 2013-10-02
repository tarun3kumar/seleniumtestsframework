package com.seleniumtests.tests;

import static com.seleniumtests.controller.Assertion.*;

import com.seleniumtests.controller.TestPlan;
import org.testng.annotations.Test;

/**
 * User: tbhadauria
 * Date: 10/2/13
 * Time: 4:59 PM
 */
public class SoftAssertionTest extends TestPlan {

    /**
     * Continues with test execution even though assertions fail
     */
    @Test(groups = "softAssertionTest", description = "Continues with test execution even though assertions fail")
    public void softAssertionTest() {
        assertEquals(true, false, "boolean test failure");
        assertTrue(false, "another boolean test failure");
        assertEquals("selenium", "qtp", "universal test failure :)");
        assertTrue(1==1, "never fails");
    }
}
