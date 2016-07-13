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

import com.seleniumtests.core.Filter;
import com.seleniumtests.core.SeleniumTestPlan;
import com.seleniumtests.dataobject.User;
import com.seleniumtests.util.SpreadSheetHelper;
import com.seleniumtests.util.internal.entity.TestEntity;
import com.seleniumtests.webpage.GoogleHomePage;
import com.seleniumtests.webpage.JSErrorPage;
import com.seleniumtests.webpage.LoginPage;
import com.seleniumtests.webpage.RegistrationPage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;

import static com.seleniumtests.core.CustomAssertion.assertThat;
import static org.hamcrest.CoreMatchers.is;


public class JSErrorTest extends SeleniumTestPlan {

    @Test(groups = {"jsErrorTest"},
            description = "jsErrorTest")
    public void jsErrorTest() throws Exception {

        new JSErrorPage(true);
    }

}
