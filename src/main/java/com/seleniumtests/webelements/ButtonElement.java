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

package com.seleniumtests.webelements;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.seleniumtests.core.TestLogging;

import com.seleniumtests.driver.BrowserType;
import com.seleniumtests.driver.WebUIDriver;

public class ButtonElement extends HtmlElement {

    public ButtonElement(final String label, final By by) {
        super(label, by);
    }

    @Override
    public void click() {
        TestLogging.logWebStep(null, "click on " + toHTML(), false);

        BrowserType browser = WebUIDriver.getWebUIDriver().getConfig().getBrowser();
        if (browser == BrowserType.InternetExplore) {
            super.sendKeys(Keys.ENTER);
        } else {
            super.click();
        }
    }

    public void submit() {
        TestLogging.logWebStep(null, "Submit form by clicking on " + toHTML(), false);
        findElement();
        element.submit();
    }
}
