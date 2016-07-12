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

import com.seleniumtests.core.SeleniumTestsContextManager;
import com.seleniumtests.webelements.PageObject;
import com.seleniumtests.webelements.TextFieldElement;
import org.openqa.selenium.By;

public class UICatalogScreen extends PageObject {

    public UICatalogScreen() throws Exception { }

    /**
     * Opens log in page.
     *
     * @param   openAPP
     *
     * @throws  Exception
     */
    public UICatalogScreen(final boolean openAPP) throws Exception {
        super(null, openAPP ? SeleniumTestsContextManager.getThreadContext().getApp() : null);
    }

    // appium does not support locating element with name hence xpath is used
    private static TextFieldElement uiTextField = new TextFieldElement("UI Text Field", By.xpath("//*[@name='Normal']"));

    public TextFieldElement getTextFields(final String symbol) {
        return new TextFieldElement("Text Element", By.xpath("//*[@name='"+symbol+"']"));
    }

    public UICatalogScreen clickSymbol(final String symbol) {
        getTextFields(symbol).click();
        return this;
    }

    public static boolean isUITextFieldDisplayed() {
        return uiTextField.isDisplayed();
    }


}
