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

package com.seleniumtests.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import static com.seleniumtests.core.CustomAssertion.assertThat;

import org.testng.annotations.Test;

import com.seleniumtests.core.SeleniumTestPlan;

/**
 * Using Matchers.
 */
public class RetryTest extends SeleniumTestPlan {

    /**
     * Will not retry as test would never fail.
     */
    @Test(groups = "retryTest2", description = "Will retry failed assertions in this test")
    public void retryFailedTest() {
        assertThat("1 is always equal to 1", 1 == 1);            // This won't fail
        assertThat("1 can not be equal to 2", 1, is(2));
        assertThat("2 is always equal to 2", 2, equalTo(2));     // This won't fail
        assertThat("2 is always equal to 2", 2, is(equalTo(2))); // Same as previous statement
        assertThat("2 can not be equal to 3", 2, is(3));
    }

}
