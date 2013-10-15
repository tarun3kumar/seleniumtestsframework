package com.seleniumtests.driver.web.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

import com.seleniumtests.core.TestLogging;
import com.seleniumtests.driver.web.WebDriverConfig;

public class SafariDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory{

	public SafariDriverFactory(WebDriverConfig cfg) {
		super(cfg);
	}

	
	
	@Override
	public WebDriver createWebDriver() {
		//WebDriverConfig cfg = this.getWebDriverConfig();
		
		DesiredCapabilities cap = new SafariCapabilitiesFactory().createCapabilities(cfg);
		
		System.out.println("start safari...");
//		if(this.getClass().getResource("/SafariDriver.safariextz")!=null)
//			System.setProperty("webdriver.safari.driver", this.getClass().getResource("/SafariDriver.safariextz").getFile());
		synchronized(this.getClass())
		{
			driver = new SafariDriver(cap);
		}
		System.out.println("start safari finished...");
		
		this.setWebDriver(driver);
		
		//Implicit Waits to handle dynamic element. The default value is 5 seconds.
		setImplicitWaitTimeout(cfg.getImplicitWaitTimeout());
		if(cfg.getPageLoadTimeout()>=0)
		{
			TestLogging.log("Safari doesn't support pageLoadTimeout, ignoring...");
			//driver.manage().timeouts().pageLoadTimeout(cfg.getPageLoadTimeout(), TimeUnit.SECONDS);
		}
		
		return driver;
	}

}


