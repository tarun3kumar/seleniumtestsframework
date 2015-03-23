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

import java.io.IOException;

import org.openqa.selenium.WebDriver;

import com.opera.core.systems.OperaDriver;

import com.seleniumtests.driver.DriverConfig;

public class OperaDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory {

    public OperaDriverFactory(final DriverConfig cfg) {
        super(cfg);
    }

    @Override
    public WebDriver createWebDriver() throws IOException {
        DriverConfig cfg = this.getWebDriverConfig();

        synchronized (this.getClass()) {
            driver = new OperaDriver(new OperaCapabilitiesFactory().createCapabilities(cfg));
        }

        // Implicit Wait handles dynamic element. The default value is 5 seconds.
        setImplicitWaitTimeout(cfg.getImplicitWaitTimeout());
        this.setWebDriver(driver);
        return driver;
    }

}
