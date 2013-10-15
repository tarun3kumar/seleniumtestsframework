package com.seleniumtests.browserfactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.seleniumtests.driver.web.WebDriverConfig;
import com.seleniumtests.helper.OSHelper;
import com.seleniumtests.helper.ThreadHelper;

public class IEDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory{

	public IEDriverFactory(WebDriverConfig cfg) {
		super(cfg);
	}
	
	@Override
	public void cleanUp()
	{
		try{
		if (driver != null) {
			try {
				driver.quit();
			} catch (WebDriverException ex) {
					ex.printStackTrace();
			}
			driver = null;
		}
		}catch(Exception ex)
		{
			//Ignore all exceptions
		}
	}
	
	@Override
	public WebDriver createWebDriver() throws IOException {
		killprocess();
		ThreadHelper.waitForSeconds(2);
		if(!OSHelper.isWindows())
		{
			throw new RuntimeException("IE can only run in windows!");
		}
		WebDriverConfig cfg = this.getWebDriverConfig();

		driver = new InternetExplorerDriver(new IECapabilitiesFactory().createCapabilities(cfg));

		//Implicit Waits to handle dynamic element. The default value is 5 seconds.
		setImplicitWaitTimeout(cfg.getImplicitWaitTimeout());
		if(cfg.getPageLoadTimeout()>=0)
		{
			driver.manage().timeouts().pageLoadTimeout(cfg.getPageLoadTimeout(), TimeUnit.SECONDS);
		}

		this.setWebDriver(driver);
		return driver;
	}
	
	private void killprocess(){
		//So please make sure only one test case run at a time
		if(OSHelper.isWindows())
		{
			try {
				Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
				Runtime.getRuntime().exec("taskkill /F /IM Iexplore.exe");
			} catch (IOException e) {
			}
		}
	}

}


