package com.seleniumtests.webtests;

import java.util.GregorianCalendar;

import org.testng.annotations.Test;

public class RetryTest1 {
	
	@Test(groups="retryTest1")
	public void retryFailedTest() {
		assert 1==2:"bound to fail";
	}
}
