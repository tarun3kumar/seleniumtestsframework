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

public class TestRetryAnalyzer implements IRetryAnalyzer, ITestRetryAnalyzer {

    private static final String TEST_RETRY_COUNT = "testRetryCount";
    private int count = 1;
    private int maxCount = 2;

    public TestRetryAnalyzer() {
        String retryMaxCount = System.getProperty(TEST_RETRY_COUNT);
        if (retryMaxCount != null) {
            maxCount = Integer.parseInt(retryMaxCount);
        }
    }

    public void setMaxCount(final int count) {
        this.maxCount = count;
    }

    public int getCount() {
        return this.count;
    }

    public int getMaxCount() {
        return this.maxCount;
    }

    public synchronized boolean retry(final ITestResult result) {
        String testClassName = String.format("%s.%s", result.getMethod().getRealClass().toString(),
                result.getMethod().getMethodName());

        if (count <= maxCount) {
            result.setAttribute("RETRY", new Integer(count));

            TestLogging.log("[RETRYING] " + testClassName + " FAILED, " + "Retrying " + count + " time", true);

            count += 1;
            return true;
        }

        return false;
    }

    public boolean retryPeek(final ITestResult result) {
        return count <= maxCount;
    }
}
