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

import static com.seleniumtests.core.CustomAssertion.assertThat;
import com.seleniumtests.core.Filter;
import com.seleniumtests.core.SeleniumTestPlan;

import com.seleniumtests.dataobject.User;

import com.seleniumtests.util.SpreadSheetHelper;
import com.seleniumtests.util.internal.entity.TestEntity;

import com.seleniumtests.webpage.GoogleHomePage;
import com.seleniumtests.webpage.LoginPage;
import com.seleniumtests.webpage.RegistrationPage;

import static org.hamcrest.CoreMatchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import java.util.Iterator;
import java.util.LinkedHashMap;


public class RegistrationTest extends SeleniumTestPlan {

    @DataProvider(
        name = "loginData",
        parallel = true
    )
    public static Iterator<Object[]> getUserInfo(final Method m)
        throws Exception {
        Filter filter = Filter.equalsIgnoreCase(TestEntity.TEST_METHOD,
                m.getName());

        LinkedHashMap<String, Class<?>> classMap =
            new LinkedHashMap<String, Class<?>>();
        classMap.put("TestEntity", TestEntity.class);
        classMap.put("User", User.class);

        return SpreadSheetHelper.getEntitiesFromSpreadsheet(
                RegistrationTest.class, classMap, "loginuser.csv", filter);
    }

    @Test(
        groups = { "registerWithValidUserData" },
        dataProvider = "loginData",
        description = "Register with valid login data"
    )
    public void registerWithValidUserData(final TestEntity testEntity,
        final User user) throws Exception {

        new RegistrationPage(true).submitValidRegistrationData(user);
        assertThat("User Name text box is missing on Login Page!!!",
            LoginPage.isUserNameDisplayed(), is(true));

    }

    @Test(
        groups = { "registerWithInvalidUserData" },
        dataProvider = "loginData",
        description = "Register with Invalid login data"
    )
    public void registerWithInvalidUserData(final TestEntity testEntity,
        final User user) throws Exception {
        RegistrationPage registrationPage = new RegistrationPage(true);
        registrationPage.enterFirstName(user.getFirstName()).enterLastName(
            user.getLastName()).enterUserName(user.getUserName()).enterPassword(
            user.getPassword());
        assertThat(
            "User Name text box is shown despite not completing registration process!!!",
            LoginPage.isUserNameDisplayed(), is(true));

    }

    @Test(
        groups = { "loginTest" },
        dataProvider = "loginData",
        description =
            "Login test to verify that google home page can be reached"
    )
    public void loginTest(final TestEntity testEntity, final User user)
        throws Exception {

        GoogleHomePage googleHomePage = new RegistrationPage(true)
            .submitValidRegistrationData(user).enterLoginData(user);

        assertThat("Google home page is not displayed after login",
            googleHomePage.isSearchBoxDisplayed(), is(true));

    }
}
