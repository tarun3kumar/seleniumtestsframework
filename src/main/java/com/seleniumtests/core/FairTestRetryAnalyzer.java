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

public class FairTestRetryAnalyzer implements IRetryAnalyzer, ITestRetryAnalyzer {

    public static final String TEST_RETRY_COUNT = "fairTestRetryCount";
    public static final String TEST_RETRY_MAX = "fairTestRetryMax";
    private int count = 1;
    private int maxCount = 1;

    private int countLimit = 1;
    private int maxCountLimit = 2;


    public FairTestRetryAnalyzer() {
        String retryCount = System.getProperty(TEST_RETRY_COUNT);
        if (retryCount != null) {
            countLimit = Integer.parseInt(retryCount);
        }

        String retryMaxCount = System.getProperty(TEST_RETRY_MAX);
        if (retryMaxCount != null) {
            maxCountLimit = Integer.parseInt(retryMaxCount);
        }
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

        Integer retryCount = Integer.class.cast(result.getTestContext().getAttribute("fairTestRetryCount"));
        if (retryCount == null) {
            count = 1;
        } else {
            count = retryCount;
        }

        if (count <= countLimit && maxCount <= maxCountLimit) {

            result.setAttribute("RETRY", new Integer(count));
            TestLogging.log("[RETRYING] " + testClassName + " FAILED, " + "Retrying " + count + " time", true);
            result.getTestContext().setAttribute("fairTestRetryCount", ++count);

            maxCount++;
            return true;
        }

        return false;
    }


    public boolean retryPeek(final ITestResult result) {
        Integer retryCount = Integer.class.cast(result.getTestContext().getAttribute("fairTestRetryCount"));
        if (retryCount == null) {
            count = 1;
        } else {
            count = retryCount;
        }

        return count <= countLimit && maxCount <= maxCountLimit;
    }
}
