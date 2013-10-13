package com.seleniumtests.tests;

import com.seleniumtests.controller.ContextManager;
import com.seleniumtests.controller.Filter;
import com.seleniumtests.controller.SeleniumTestPlan;
import com.seleniumtests.dataobject.TestLinkProject;
import com.seleniumtests.dataobject.User;
import com.seleniumtests.util.SpreadSheetUtil;
import com.seleniumtests.util.internal.entity.TestEntity;
import com.seleniumtests.webpage.TestLinkLoginPage;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * Create new Test Plan in test link
 * <p/>
 * Date: 10/3/13
 * Time: 12:17 AM
 */
public class TestLinkUsageTest extends SeleniumTestPlan {

    @DataProvider(name = "testlinkproject", parallel = true)
    public static Iterator<Object[]> getUserInfo(Method m,
                                                 ITestContext testContext) throws Exception {
        Filter filter = Filter.equalsIgnoreCase(TestEntity.TEST_METHOD,
                m.getName());

        LinkedHashMap<String, Class<?>> classMap = new LinkedHashMap<String, Class<?>>();
        classMap.put("TestEntity", TestEntity.class);
        classMap.put("User", User.class);
        classMap.put("TestLinkProject", TestLinkProject.class);

        return SpreadSheetUtil.getEntitiesFromSpreadsheet(
                TestLinkLoginTest.class, classMap, "testlinkproject.csv", 0,
                null, filter);
    }

    /**
     * Creates new test project in TestLink
     *
     * @param testEntity
     * @param user
     * @throws Exception
     */
    @Test(groups = {"createTestProject"}, dataProvider = "testlinkproject",
            description = "Creates new test project in TestLink")
    public void createTestProject(TestEntity testEntity, final User user, final TestLinkProject testLinkProject)
            throws Exception {

        new TestLinkLoginPage(true)
                .loginAsValidUser(user)
                .clickTestProjectManagementLink()
                .clickCreateButton()
                .selectCreateFromExistingTestProject(testLinkProject.isCreateFromExisting())
                .enterProjectName(testLinkProject.getName() + UUID.randomUUID()) //To make test project name unique
                .enterPrefix(testLinkProject.getTestCaseIDPrefix())
                .submitTestProject();
    }
}
