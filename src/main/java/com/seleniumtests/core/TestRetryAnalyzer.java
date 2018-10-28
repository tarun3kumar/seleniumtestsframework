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

import java.util.concurrent.atomic.AtomicInteger;

public class TestRetryAnalyzer implements IRetryAnalyzer {

    public static final int MAX_RETRY_COUNT = 3;
    private static final AtomicInteger count = new AtomicInteger(MAX_RETRY_COUNT);

    public static void resetCount() {
        count.set(MAX_RETRY_COUNT);
    }

    public int getCount() {
        return count.get();
    }

    private boolean isRetryAvailable() {
        return (count.get() > 0);
    }

    @Override
    public boolean retry(ITestResult result) {
        boolean retry = false;
        if (isRetryAvailable()) {
            System.out.println("Going to retry test case: " + result.getMethod() + ", " + (((MAX_RETRY_COUNT - count.get()) + 1)) + " out of " + MAX_RETRY_COUNT);
            retry = true;
            count.decrementAndGet();
        }
        return retry;
    }
}
