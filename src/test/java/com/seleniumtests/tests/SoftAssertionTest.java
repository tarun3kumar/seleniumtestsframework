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

import static com.seleniumtests.core.CustomAssertion.assertEquals;
import static com.seleniumtests.core.CustomAssertion.assertTrue;

import org.testng.annotations.Test;

import com.seleniumtests.core.SeleniumTestPlan;
import com.seleniumtests.core.TestLogging;

/**
 * Demonstrate test execution continues even though assertions fail.
 *
 * <p/>Date: 10/2/13 Time: 4:59 PM
 */
public class SoftAssertionTest extends SeleniumTestPlan {

    /**
     * Continues with test execution even though assertions fail.
     */
    @Test(groups = "softAssertionTest", description = "Continues with test execution even though assertions fail")
    public void softAssertionTest() {
        assertEquals(true, false, "boolean test failure");
        assertTrue(false, "another boolean test failure");
        assertEquals("selenium", "qtp", "universal test failure :)");
        assertTrue(1 == 1, "never fails");
        TestLogging.log(
            "This message is logged after initial assertion failures. Hence test execution continues even in the wake of test failures");
    }
}
