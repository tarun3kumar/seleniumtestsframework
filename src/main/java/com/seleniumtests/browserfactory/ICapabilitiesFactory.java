package com.seleniumtests.browserfactory;

import com.seleniumtests.driver.DriverConfig;
import org.openqa.selenium.remote.DesiredCapabilities;


public interface ICapabilitiesFactory {
	
	public DesiredCapabilities createCapabilities(DriverConfig cfg);

}
