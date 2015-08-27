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

import static org.hamcrest.CoreMatchers.is;

import static com.seleniumtests.core.CustomAssertion.assertThat;

import org.testng.annotations.Test;

import com.seleniumtests.core.SeleniumTestPlan;

import com.seleniumtests.webpage.CalculatorScreen;

/**
 * Android app test suite.
 */
public class AndroidAppTest extends SeleniumTestPlan {

    /**
     * Adds two numbers using calculator program.
     */
    @Test(groups = {"addTwoNumbers"}, description = "Adds two numbers using calculator program")
    public void addTwoNumbers() throws Exception {

        CalculatorScreen calculatorScreen = new CalculatorScreen(true);
        String result = calculatorScreen.clickSymbol("2").clickSymbol("+").clickSymbol("4").clickSymbol("=")
                                        .getResultText();
        assertThat(result, is("6"));
    }
}
