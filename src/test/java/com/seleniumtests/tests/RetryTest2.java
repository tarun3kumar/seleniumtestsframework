package com.seleniumtests.tests;

import com.seleniumtests.controller.TestPlan;
import org.testng.annotations.Test;

public class RetryTest2 extends TestPlan {

    /**
     * Will not retry as test would never fail
     */
	@Test(groups="retryTest2", description = "Will not retry as test would never fail")
	public void retryFailedTest() {
		assert 1==1:"always pass";
	}
}
