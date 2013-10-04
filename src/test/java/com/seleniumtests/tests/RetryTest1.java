package com.seleniumtests.tests;

import com.seleniumtests.controller.SeleniumTestPlan;
import org.testng.annotations.Test;

public class RetryTest1 extends SeleniumTestPlan {

    /**
     * Retires test thrice as test is bound to fail
     */
	@Test(groups="retryTest1", description = "Retires thrice as test is bound to fail")
	public void retryFailedTest() {
		assert 1==2:"bound to fail";
	}
}
