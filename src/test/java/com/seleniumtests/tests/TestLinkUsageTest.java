package com.seleniumtests.tests;

import com.seleniumtests.controller.ContextManager;
import com.seleniumtests.controller.EasyFilter;
import com.seleniumtests.dataobject.User;
import com.seleniumtests.util.SpreadSheetUtil;
import com.seleniumtests.util.internal.entity.TestObject;
import com.seleniumtests.webpage.TestLinkLoginPage;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * User: tbhadauria
 * Date: 10/3/13
 * Time: 12:17 AM
 */
public class TestLinkUsageTest {

    @DataProvider(name = "loginData", parallel = true)
    public static Iterator<Object[]> getUserInfo(Method m,
                                                 ITestContext testContext) throws Exception {
        EasyFilter filter = EasyFilter.equalsIgnoreCase(TestObject.TEST_METHOD,
                m.getName());
        filter = EasyFilter.and(filter, EasyFilter.equalsIgnoreCase(
                TestObject.TEST_SITE,
                ContextManager.getTestLevelContext(testContext).getSite()));

        LinkedHashMap<String, Class<?>> classMap = new LinkedHashMap<String, Class<?>>();
        classMap.put("TestObject", TestObject.class);
        classMap.put("User", User.class);

        return SpreadSheetUtil.getEntitiesFromSpreadsheet(
                TestLinkLoginTest.class, classMap, "loginuser.csv", 0,
                null, filter);
    }

    /**
     * Logs in to TestLink as admin
     * This test fails because of verifyDocumentationDropDownFail and is retried
     *
     * @param testObject
     * @param user
     * @throws Exception
     */
    @Test(groups = {"wrongNavigation"}, dataProvider = "loginData",
            description = "Logs in to TestLink as admin, This test is marked for deliberate failure")
    public void wrongNavigation(TestObject testObject, final User user)
            throws Exception {

        new TestLinkLoginPage(true)
                .loginAsValidUser(user)
                .selectGivenTestProject(2)
                .verifyDocumentationDropDownFail()      // Injected failure. Test would continue to run despite this failure
                .selectGivenTestProject(1)
                .verifyDocumentationDropDown();
    }

}
