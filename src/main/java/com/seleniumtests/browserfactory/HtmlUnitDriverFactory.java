package com.seleniumtests.browserfactory;

import com.seleniumtests.core.TestLogging;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.seleniumtests.driver.WebDriverConfig;

public class HtmlUnitDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory{

	public HtmlUnitDriverFactory(WebDriverConfig cfg) {
		super(cfg);
	}

	
	
	@Override
	public WebDriver createWebDriver() {
		WebDriverConfig cfg = this.getWebDriverConfig();
		
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