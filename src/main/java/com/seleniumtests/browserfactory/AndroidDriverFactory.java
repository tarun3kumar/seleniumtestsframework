package com.seleniumtests.browserfactory;

import com.seleniumtests.driver.DriverConfig;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;

public class AndroidDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory{

	public AndroidDriverFactory(DriverConfig webDriverConfig) {
		super(webDriverConfig);
	}

	@Override
	public WebDriver createWebDriver() throws MalformedURLException {
		/*AndroidDriver driver = null;
		DriverConfig cfg = this.getWebDriverConfig();
		
		driver = new AndroidDriver(new AndroidCapabilitiesFactory().createCapabilities(cfg));
		
		this.setWebDriver(driver);*/
		return null;
	}

}
