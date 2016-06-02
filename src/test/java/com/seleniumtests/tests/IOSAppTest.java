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

import com.seleniumtests.core.SeleniumTestPlan;
import com.seleniumtests.webpage.CalculatorScreen;
import com.seleniumtests.webpage.UICatalogScreen;
import org.testng.annotations.Test;

import static com.seleniumtests.core.CustomAssertion.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * iOS app test suite.
 */
public class IOSAppTest extends SeleniumTestPlan {

    @Test(groups = {"verifyUICatalogScreen"}, description = "Verifies UI Catalog screen")
    public void verifyUICatalogScreen() throws Exception {

        UICatalogScreen uiCatalogScree = new UICatalogScreen(true);

        uiCatalogScree.clickSymbol("TextFields");
        assertThat("UI Text Field is missing", UICatalogScreen.isUITextFieldDisplayed(), is(equalTo(true)));
    }
}
