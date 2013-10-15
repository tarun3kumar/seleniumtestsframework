package com.seleniumtests.tests;

import com.seleniumtests.core.Filter;
import com.seleniumtests.core.SeleniumTestPlan;
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

/**
 * Login test for TestLink
 *
 * Date: 10/2/13
 * Time: 6:36 PM
 */
public class TestLinkLoginTest extends SeleniumTestPlan {

    @DataProvider(name = "loginData", parallel = true)
    public static Iterator<Object[]> getUserInfo(Method m,
                                                 ITestContext testContext) throws Exception {
        Filter filter = Filter.equalsIgnoreCase(TestEntity.TEST_METHOD,
                m.getName());

        LinkedHashMap<String, Class<?>> classMap = new LinkedHashMap<String, Class<?>>();
        classMap.put("TestEntity", TestEntity.class);
        classMap.put("User", User.class);

        return SpreadSheetUtil.getEntitiesFromSpreadsheet(
                TestLinkLoginTest.class, classMap, "loginuser.csv", 0,
                null, filter);
    }

    /**
     * Logs in to TestLink as valid user
     *
     * @param testEntity
     * @param user
     * @throws Exception
     */
    @Test(groups = {"loginAsValidUser"}, dataProvider = "loginData",
            description = "Logs in to TestLink as admin")
    public void loginAsValidUser(TestEntity testEntity, final User user)
            throws Exception {

        new TestLinkLoginPage(true)
                .loginAsValidUser(user)
                .verifyDocumentationDropDown();
    }

    /**
     * Logs in to TestLink as invalid user
     *
     * @param testEntity
     * @param user
     * @throws Exception
     */
    @Test(groups = {"loginAsInvalidUser"}, dataProvider = "loginData",
            description = "Logs in to TestLink as invalid user and verifies login is unsuccessful")
    public void loginAsInvalidUser(TestEntity testEntity, final User user)
            throws Exception {

        new TestLinkLoginPage(true)
                .loginAsInvalidUser(user)
                .verifyLoginBoxPresence();
    }

    /**
     * A failed test
     *
     * @param testEntity
     * @param user
     * @throws Exception
     */
    @Test(groups = {"testForFailure"}, dataProvider = "loginData",
            description = "This test is bound to fail")
    public void testForFailure(TestEntity testEntity, final User user)
            throws Exception {

        new TestLinkLoginPage(true)
                .loginAsValidUser(user)
                .verifyDocumentationDropDownFail();
    }
}
