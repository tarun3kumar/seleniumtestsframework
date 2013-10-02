package com.seleniumtests.webtests;

import java.util.GregorianCalendar;

import com.seleniumtests.controller.TestPlan;
import org.testng.annotations.Test;

public class RetryTest1 extends TestPlan{

    /**
     * Retires thrice as test fails
     */
	@Test(groups="retryTest1")
	public void retryFailedTest() {
		assert 1==2:"bound to fail";
	}
}
