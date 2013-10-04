package com.seleniumtests.controller;

import org.testng.ITestContext;

public interface IContexAttributeListener {
	public void load(ITestContext testNGCtx, Context seleniumTestsCtx);
}