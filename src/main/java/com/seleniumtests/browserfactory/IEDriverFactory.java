package com.seleniumtests.browserfactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.seleniumtests.driver.DriverConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.seleniumtests.helper.OSHelper;

public class IEDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory{

	public IEDriverFactory(DriverConfig webDriverConfig1) {
		super(webDriverConfig1);
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
		}catch(Exception e)
		{
            e.printStackTrace();
		}
	}
	
	@Override
	public WebDriver createWebDriver() throws IOException {
		killProcess();
		if(!OSHelper.isWindows())
		{
			throw new RuntimeException("IE browser is only supported on windows!");
		}
		DriverConfig cfg = this.getWebDriverConfig();

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
	
	private void killProcess(){
		if(OSHelper.isWindows())
		{
			try {
				Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
				Runtime.getRuntime().exec("taskkill /F /IM Iexplore.exe");
			} catch (IOException e) {
                e.printStackTrace();
			}
		}
	}

}


