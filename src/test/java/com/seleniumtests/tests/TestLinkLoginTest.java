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

import java.lang.reflect.Method;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.testng.ITestContext;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.seleniumtests.core.Filter;
import com.seleniumtests.core.SeleniumTestPlan;

import com.seleniumtests.dataobject.User;

import com.seleniumtests.util.SpreadSheetHelper;
import com.seleniumtests.util.internal.entity.TestEntity;

import com.seleniumtests.webpage.TestLinkLoginPage;

/**
 * Login test for TestLink.
 *
 * <p/>Date: 10/2/13 Time: 6:36 PM
 */
public class TestLinkLoginTest extends SeleniumTestPlan {

    @DataProvider(name = "loginData", parallel = true)
    public static Iterator<Object[]> getUserInfo(final Method m, final ITestContext testContext) throws Exception {
        Filter filter = Filter.equalsIgnoreCase(TestEntity.TEST_METHOD, m.getName());

        LinkedHashMap<String, Class<?>> classMap = new LinkedHashMap<String, Class<?>>();
        classMap.put("TestEntity", TestEntity.class);
        classMap.put("User", User.class);

        return SpreadSheetHelper.getEntitiesFromSpreadsheet(TestLinkLoginTest.class, classMap, "loginuser.csv", 0, null,
                filter);
    }

    /**
     * Logs in to TestLink as valid user.
     *
     * @param   testEntity
     * @param   user
     *
     * @throws  Exception
     */
    @Test(groups = {"loginAsValidUser"}, dataProvider = "loginData", description = "Logs in to TestLink as admin")
    public void loginAsValidUser(final TestEntity testEntity, final User user) throws Exception {

        new TestLinkLoginPage(true).loginAsValidUser(user).verifyDocumentationDropDown();
    }

    /**
     * Logs in to TestLink as invalid user.
     *
     * @param   testEntity
     * @param   user
     *
     * @throws  Exception
     */
    @Test(
        groups = {"loginAsInvalidUser"}, dataProvider = "loginData",
        description = "Logs in to TestLink as invalid user and verifies login is unsuccessful"
    )
    public void loginAsInvalidUser(final TestEntity testEntity, final User user) throws Exception {

        new TestLinkLoginPage(true).loginAsInvalidUser(user).verifyLoginBoxPresence();
    }

    /**
     * A failed test.
     *
     * @param   testEntity
     * @param   user
     *
     * @throws  Exception
     */
    @Test(groups = {"testForFailure"}, dataProvider = "loginData", description = "This test is bound to fail")
    public void testForFailure(final TestEntity testEntity, final User user) throws Exception {

        new TestLinkLoginPage(true).loginAsValidUser(user).verifyDocumentationDropDownFail();
    }

    /**
     * A failed test.
     *
     * @param   testEntity
     * @param   user
     *
     * @throws  Exception
     */
    @Test(
        groups = {"testsSkippedMethod"}, dataProvider = "loginData", description = "This test is also bound to fail",
        dependsOnMethods = "testForFailure"
    )
    public void testsSkippedMethod(final TestEntity testEntity, final User user) throws Exception {

        new TestLinkLoginPage(true).loginAsValidUser(user).verifyDocumentationDropDownFail();
    }
}
