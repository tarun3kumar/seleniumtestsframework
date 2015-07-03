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

import java.net.MalformedURLException;
import java.net.URL;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;

import com.seleniumtests.driver.DriverConfig;

import io.appium.java_client.android.AndroidDriver;

/**
 * AndroidDriverFactory.
 */
public class AndroidDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory {

    public AndroidDriverFactory(final DriverConfig webDriverConfig) {
        super(webDriverConfig);
    }

    protected WebDriver createNativeDriver() throws MalformedURLException {

        return new AndroidDriver(new URL(webDriverConfig.getAppiumServerURL()), new AndroidCapabilitiesFactory()
                    .createCapabilities(webDriverConfig));
    }

    @Override
    public WebDriver createWebDriver() throws IOException {
        DriverConfig cfg = this.getWebDriverConfig();

        driver = createNativeDriver();

        setImplicitWaitTimeout(cfg.getImplicitWaitTimeout());
        if (cfg.getPageLoadTimeout() >= 0) {
            setPageLoadTimeout(cfg.getPageLoadTimeout());
        }

        this.setWebDriver(driver);
        return driver;
    }

    protected void setPageLoadTimeout(final long timeout) {
        try {
            driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
        } catch (UnsupportedCommandException e) {
            // chromedriver does not support pageLoadTimeout
        }
    }

}
