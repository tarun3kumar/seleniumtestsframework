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

package com.seleniumtests.browserfactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.seleniumtests.core.TestLogging;

import com.seleniumtests.driver.DriverConfig;

public class HtmlUnitDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory {

    public HtmlUnitDriverFactory(final DriverConfig cfg) {
        super(cfg);
    }

    @Override
    public WebDriver createWebDriver() {
        DriverConfig cfg = this.getWebDriverConfig();

        driver = new HtmlUnitDriver(new HtmlUnitCapabilitiesFactory().createCapabilities(cfg));

        // Implicit Waits. The default value is 5 seconds.
        setImplicitWaitTimeout(cfg.getImplicitWaitTimeout());
        if (cfg.getPageLoadTimeout() >= 0) {
            TestLogging.log("htmlunit doesn't support pageLoadTimeout");
        }

        this.setWebDriver(driver);
        return driver;
    }

}
