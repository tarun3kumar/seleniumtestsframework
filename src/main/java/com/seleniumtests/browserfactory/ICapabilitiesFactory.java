package com.seleniumtests.browserfactory;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.seleniumtests.driver.WebDriverConfig;


public interface ICapabilitiesFactory {
	
	public DesiredCapabilities createCapabilities(WebDriverConfig cfg);

}
