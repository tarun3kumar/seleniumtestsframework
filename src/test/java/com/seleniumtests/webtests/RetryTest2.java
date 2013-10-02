package com.seleniumtests.webtests;

import com.seleniumtests.controller.TestPlan;
import org.testng.annotations.Test;

import java.util.GregorianCalendar;

public class RetryTest2 extends TestPlan {

    /**
     * Will not retry as test would not fail
     */
	@Test(groups="retryTest2")
	public void retryFailedTest() {
		assert 1==1:"always pass";
	}
}
