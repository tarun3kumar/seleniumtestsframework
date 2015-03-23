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
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

import com.seleniumtests.core.TestLogging;

import com.seleniumtests.driver.DriverConfig;

public class SafariDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory {

    public SafariDriverFactory(final DriverConfig cfg) {
        super(cfg);
    }

    @Override
    public WebDriver createWebDriver() {
        DesiredCapabilities cap = new SafariCapabilitiesFactory().createCapabilities(webDriverConfig);
        System.out.println("Begin Safari");
        synchronized (this.getClass()) {
            driver = new SafariDriver(cap);
        }

        System.out.println("safari started");

        this.setWebDriver(driver);

        // Implicit Waits handles dynamic element.
        setImplicitWaitTimeout(webDriverConfig.getImplicitWaitTimeout());
        if (webDriverConfig.getPageLoadTimeout() >= 0) {
            TestLogging.log("Safari doesn't support pageLoadTimeout");
        }

        return driver;
    }

}
