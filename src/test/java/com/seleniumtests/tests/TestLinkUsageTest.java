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
import java.util.UUID;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.seleniumtests.core.Filter;
import com.seleniumtests.core.SeleniumTestPlan;

import com.seleniumtests.dataobject.TestLinkProject;
import com.seleniumtests.dataobject.User;

import com.seleniumtests.util.SpreadSheetHelper;
import com.seleniumtests.util.internal.entity.TestEntity;

import com.seleniumtests.webpage.TestLinkLoginPage;

/**
 * Create new Test Plan in test link.
 *
 * <p/>Date: 10/3/13 Time: 12:17 AM
 */
public class TestLinkUsageTest extends SeleniumTestPlan {

    @DataProvider(name = "testlinkproject", parallel = true)
    public static Iterator<Object[]> getUserInfo(final Method m) throws Exception {
        Filter filter = Filter.equalsIgnoreCase(TestEntity.TEST_METHOD, m.getName());

        LinkedHashMap<String, Class<?>> classMap = new LinkedHashMap<String, Class<?>>();
        classMap.put("TestEntity", TestEntity.class);
        classMap.put("User", User.class);
        classMap.put("TestLinkProject", TestLinkProject.class);

        return SpreadSheetHelper.getEntitiesFromSpreadsheet(TestLinkLoginTest.class, classMap, "testlinkproject.csv",
                filter);
    }

    /**
     * Creates new test project in TestLink.
     *
     * @param   testEntity
     * @param   user
     *
     * @throws  Exception
     */
    @Test(
        groups = {"createTestProject"}, dataProvider = "testlinkproject",
        description = "Creates new test project in TestLink"
    )
    public void createTestProject(final TestEntity testEntity, final User user, final TestLinkProject testLinkProject)
        throws Exception {

        new TestLinkLoginPage(true).loginAsValidUser(user).clickTestProjectManagementLink().clickCreateButton()
            .selectCreateFromExistingTestProject(testLinkProject.isCreateFromExisting())
            .enterProjectName(testLinkProject.getName() + UUID.randomUUID()) // To make test project name unique
            .enterPrefix(testLinkProject.getTestCaseIDPrefix()).submitTestProject();
    }
}
