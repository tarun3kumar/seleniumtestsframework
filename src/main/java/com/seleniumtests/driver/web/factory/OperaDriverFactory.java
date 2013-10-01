package com.seleniumtests.driver.web.factory;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

import com.seleniumtests.driver.web.WebDriverConfig;
import com.opera.core.systems.OperaDriver;

public class OperaDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory{
	
	public OperaDriverFactory(WebDriverConfig cfg) {
		super(cfg);
	}

	@Override
	public WebDriver createWebDriver() throws IOException {
		WebDriverConfig cfg = this.getWebDriverConfig();
			
		
		synchronized(this.getClass())
		{
			driver = new OperaDriver(new OperaCapabilitiesFactory().createCapabilities(cfg));
		}
		//Implicit Waits to handle dynamic element. The default value is 5 seconds.
		setImplicitWaitTimeout(cfg.getImplicitWaitTimeout());
		if(cfg.getPageLoadTimeout()>=0)
		{
			//Logging.log("Opera doesn't support pageLoadTimeout, ignoring...");
			//driver.manage().timeouts().pageLoadTimeout(cfg.getPageLoadTimeout(), TimeUnit.SECONDS);
		}

		this.setWebDriver(driver);
		return driver;
	}

}
