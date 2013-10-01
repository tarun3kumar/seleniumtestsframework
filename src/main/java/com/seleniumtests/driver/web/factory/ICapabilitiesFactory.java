package com.seleniumtests.driver.web.factory;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.seleniumtests.driver.web.WebDriverConfig;


public interface ICapabilitiesFactory {
	
	public DesiredCapabilities createCapabilities(WebDriverConfig cfg);

}
