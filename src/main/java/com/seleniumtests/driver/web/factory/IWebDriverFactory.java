package com.seleniumtests.driver.web.factory;

import org.openqa.selenium.WebDriver;

import com.seleniumtests.driver.web.WebDriverConfig;

public interface IWebDriverFactory {
	
	public void cleanUp();
	
	public WebDriver createWebDriver() throws Exception;
	
	public WebDriver getWebDriver();

	public WebDriverConfig getWebDriverConfig();
}
