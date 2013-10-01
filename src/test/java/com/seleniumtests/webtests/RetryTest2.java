package com.seleniumtests.webtests;

import org.testng.annotations.Test;

import java.util.GregorianCalendar;

public class RetryTest2 {
	
	@Test(groups="retryTest2")
	public void retryFailedTest() {
		assert 1==2:"bound to fail";
	}
}
