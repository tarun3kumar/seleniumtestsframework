package com.seleniumtests.browserfactory;

import org.openqa.selenium.WebDriver;

import com.seleniumtests.driver.DriverConfig;

public interface IWebDriverFactory {

    void cleanUp();

    WebDriver createWebDriver() throws Exception;

    WebDriver getWebDriver();

    DriverConfig getWebDriverConfig();
}
