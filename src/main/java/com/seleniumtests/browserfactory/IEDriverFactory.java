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

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.seleniumtests.driver.DriverConfig;

import com.seleniumtests.helper.OSUtility;

public class IEDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory {

    public IEDriverFactory(final DriverConfig webDriverConfig1) {
        super(webDriverConfig1);
    }

    @Override
    public void cleanUp() {
        try {
            if (driver != null) {
                try {
                    driver.quit();
                } catch (WebDriverException ex) {
                    ex.printStackTrace();
                }

                driver = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public WebDriver createWebDriver() throws IOException {

        // killProcess();
        if (!OSUtility.isWindows()) {
            throw new RuntimeException("With gods grace IE browser is only supported on windows, Imagine a "
                    + "situation when you have to fix IE bugs on Unix and Mac as well");
        }

        DriverConfig cfg = this.getWebDriverConfig();

        driver = new InternetExplorerDriver(new IECapabilitiesFactory().createCapabilities(cfg));

        // Implicit Waits to handle dynamic element. The default value is 5 seconds.
        setImplicitWaitTimeout(cfg.getImplicitWaitTimeout());
        if (cfg.getPageLoadTimeout() >= 0) {
            driver.manage().timeouts().pageLoadTimeout(cfg.getPageLoadTimeout(), TimeUnit.SECONDS);
        }

        this.setWebDriver(driver);
        return driver;
    }

    private void killProcess() {
        if (OSUtility.isWindows()) {
            try {
                Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
                Runtime.getRuntime().exec("taskkill /F /IM Iexplore.exe");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
