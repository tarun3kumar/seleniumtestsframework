package com.seleniumtests.browserfactory;

import com.seleniumtests.core.TestLogging;
import com.seleniumtests.driver.DriverConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HtmlUnitDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory{

	public HtmlUnitDriverFactory(DriverConfig cfg) {
		super(cfg);
	}

	
	
	@Override
	public WebDriver createWebDriver() {
		DriverConfig cfg = this.getWebDriverConfig();
		
		driver = new HtmlUnitDriver(new HtmlUnitCapabilitiesFactory().createCapabilities(cfg));
		//Implicit Waits. The default value is 5 seconds.
		setImplicitWaitTimeout(cfg.getImplicitWaitTimeout());
		if(cfg.getPageLoadTimeout()>=0)
		{
			TestLogging.log("htmlunit doesn't support pageLoadTimeout");
		}
		
		this.setWebDriver(driver);
		return driver;
	}

}