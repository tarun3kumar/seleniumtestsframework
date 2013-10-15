package com.seleniumtests.core;

import org.testng.ITestContext;

public interface IContextAttributeListener {
	public void load(ITestContext testNGCtx, SeleniumTestsContext seleniumTestsCtx);
}