package com.seleniumtests.browserfactory;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.seleniumtests.driver.web.WebDriverConfig;


public interface ICapabilitiesFactory {
	
	public DesiredCapabilities createCapabilities(WebDriverConfig cfg);

}
