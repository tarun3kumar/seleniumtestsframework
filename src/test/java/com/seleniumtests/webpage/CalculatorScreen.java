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

package com.seleniumtests.webpage;

import org.openqa.selenium.By;

import com.seleniumtests.core.SeleniumTestsContextManager;

import com.seleniumtests.webelements.ButtonElement;
import com.seleniumtests.webelements.LabelElement;
import com.seleniumtests.webelements.PageObject;

/**
 * Defines service for Calculator screen.
 */
public class CalculatorScreen extends PageObject {

    public CalculatorScreen() throws Exception { }

    /**
     * Opens log in page.
     *
     * @param   openAPP
     *
     * @throws  Exception
     */
    public CalculatorScreen(final boolean openAPP) throws Exception {
        super(null, openAPP ? SeleniumTestsContextManager.getThreadContext().getApp() : null);
    }

    public ButtonElement getSymbolElement(final String symbol) {
        return new ButtonElement("Button Element", By.name(symbol));
    }

    public CalculatorScreen clickSymbol(final String symbol) {
        getSymbolElement(symbol).click();
        return this;
    }

    public String getResultText() {

        // You can also locate this element using class name - By.className("android.widget.EditText"))
        return new LabelElement("Result Text", By.id("com.android.calculator2:id/formula")).getText();
    }
}
