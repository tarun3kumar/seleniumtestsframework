package com.seleniumtests.core;

import org.testng.ITestResult;


public interface ITestRetryAnalyzer {
    boolean retryPeek(ITestResult var1);

    int getCount();
}
