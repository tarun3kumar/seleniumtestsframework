package com.seleniumtests.controller;

import org.testng.ITestContext;

public interface IContextAttributeListener {
	public void load(ITestContext testNGCtx, SeleniumTestsContext seleniumTestsCtx);
}