package com.seleniumtests.browserfactory;

import com.seleniumtests.driver.DriverConfig;
import org.openqa.selenium.WebDriver;

public interface IWebDriverFactory {
	
	public void cleanUp();
	
	public WebDriver createWebDriver() throws Exception;
	
	public WebDriver getWebDriver();

	public DriverConfig getWebDriverConfig();
}
