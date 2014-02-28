package com.seleniumtests.tests;

import com.seleniumtests.core.SeleniumTestPlan;
import org.testng.annotations.Test;

import static com.seleniumtests.core.CustomAssertion.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Using Matchers
 */
public class RetryTest2 extends SeleniumTestPlan {

    /**
     * Will not retry as test would never fail
     */
	@Test(groups="retryTest2", description = "Will retry failed assertions in this test")
	public void retryFailedTest() {
        assertThat("1 is always equal to 1", 1==1);  // This won't fail
        assertThat("1 can not be equal to 2", 1, is(2));
        assertThat("2 is always equal to 2", 2, equalTo(2));   // This won't fail
        assertThat("2 is always equal to 2", 2, is(equalTo(2)));   // Same as previous statement
        assertThat("2 can not be equal to 3", 2, is(3));
       /* assertThat("Hello", is(allOf(equalTo("Hello"), containsString("selenium"), startsWith("YouTube"), endsWith("Tellurium")))); // This fails
        assertThat("Hello", is(not(allOf(equalTo("Hello"), containsString("selenium"), startsWith("YouTube"), endsWith("Tellurium"))))); // Negative condition hence succeeds
        assertThat("Hello", is(allOf(equalTo("Hello"), containsString("Hello"), startsWith("Hello"), endsWith("Hello"))));
        assertThat("Hello", is(anyOf(equalTo("Hello1"), is("Hello2"), containsString("ell1"), startsWith("1Hell"), endsWith("llo123")))); // This fails
*/
    }

}
