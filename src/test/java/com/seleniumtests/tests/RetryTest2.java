package com.seleniumtests.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import static com.seleniumtests.core.CustomAssertion.assertThat;

import org.testng.annotations.Test;

import com.seleniumtests.core.SeleniumTestPlan;

/**
 * Using Matchers.
 */
public class RetryTest2 extends SeleniumTestPlan {

    /**
     * Will not retry as test would never fail.
     */
    @Test(groups = "retryTest2", description = "Will retry failed assertions in this test")
    public void retryFailedTest() {
        assertThat("1 is always equal to 1", 1 == 1);            // This won't fail
        assertThat("1 can not be equal to 2", 1, is(2));
        assertThat("2 is always equal to 2", 2, equalTo(2));     // This won't fail
        assertThat("2 is always equal to 2", 2, is(equalTo(2))); // Same as previous statement
        assertThat("2 can not be equal to 3", 2, is(3));
    }

}
