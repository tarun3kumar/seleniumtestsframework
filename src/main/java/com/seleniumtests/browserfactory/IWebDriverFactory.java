package com.seleniumtests.browserfactory;

import org.openqa.selenium.WebDriver;

import com.seleniumtests.driver.WebDriverConfig;

public interface IWebDriverFactory {
	
	public void cleanUp();
	
	public WebDriver createWebDriver() throws Exception;
	
	public WebDriver getWebDriver();

	public WebDriverConfig getWebDriverConfig();
}
