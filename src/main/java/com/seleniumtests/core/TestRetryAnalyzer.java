/*
 * Copyright 2015 www.seleniumtests.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seleniumtests.core;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class TestRetryAnalyzer implements IRetryAnalyzer {

    public int getCount() {
        return count;
    }

    public static final int MAX_RETRY_COUNT = 3;

    private int count = MAX_RETRY_COUNT;

    private boolean isRetryAvailable() {
        return (count > 0);
    }

    public void resetCount() {
        count = MAX_RETRY_COUNT;
    }

    @Override
    public boolean retry(final ITestResult result) {
        boolean retry = false;
        if (isRetryAvailable()) {
            TestLogging.log("<br> Going to retry test case: " + result.getMethod() + ", " + (MAX_RETRY_COUNT - count + 1) + " out of " + MAX_RETRY_COUNT + "</br>");
            retry = true;
            count = count-1;
        }
        return retry;
    }
}
