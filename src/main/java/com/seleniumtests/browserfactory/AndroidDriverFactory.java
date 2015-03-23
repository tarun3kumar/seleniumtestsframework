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

import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;

import com.seleniumtests.driver.DriverConfig;

/**
 * AndroidDriverFactory.
 */
public class AndroidDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory {

    public AndroidDriverFactory(final DriverConfig webDriverConfig) {
        super(webDriverConfig);
    }

    @Override
    // TO Do - update API to use Selendroid APIs
    public WebDriver createWebDriver() throws MalformedURLException {

        /*AndroidDriver driver = null;
         * DriverConfig cfg = this.getWebDriverConfig();
         *
         * driver = new AndroidDriver(new AndroidCapabilitiesFactory().createCapabilities(cfg));
         *
         *this.setWebDriver(driver);*/
        return null;
    }

}
