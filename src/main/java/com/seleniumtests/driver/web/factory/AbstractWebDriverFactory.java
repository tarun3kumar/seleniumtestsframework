package com.seleniumtests.driver.web.factory;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.seleniumtests.driver.web.WebDriverConfig;
import com.seleniumtests.driver.web.WebUXDriver;

public abstract class AbstractWebDriverFactory {

	protected WebDriverConfig cfg;

	protected WebDriver driver;

	public AbstractWebDriverFactory(WebDriverConfig cfg) {
		this.cfg = cfg;
	}

	public void cleanUp() {
		try {
			if (driver != null) {
				try {
					System.out.println("quiting webdriver...."+Thread.currentThread().getId());
					driver.quit();
				} catch (WebDriverException ex) {
//					if (WebUXDriver.getWebUXDriver().getConfig().getBrowser() != BrowserType.InternetExplore)
						System.out.println("Quit exception--"
								+ WebUXDriver.getWebUXDriver().getConfig()
										.getBrowser().name() + ":"
								+ ex.getMessage());
				}
				driver = null;
			}
		} catch (Exception ex) {
			// Ignore all exceptions
		}
	}

	public WebDriver createWebDriver() throws Exception {
		return null;
	}

	public WebDriver getWebDriver() {
		return driver;
	}

	public WebDriverConfig getWebDriverConfig() {
		return cfg;
	}

	public void setImplicitWaitTimeout(double timeout) {
		if (timeout < 1)
			driver.manage()
					.timeouts()
					.implicitlyWait((long) (timeout * 1000),
							TimeUnit.MILLISECONDS);
		else {
			try {
				driver.manage()
						.timeouts()
						.implicitlyWait(new Double(timeout).intValue(),
								TimeUnit.SECONDS);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void setWebDriver(WebDriver driver) {
		this.driver = driver;
	}

	public void setWebDriverConfig(WebDriverConfig cfg) {
		this.cfg = cfg;
	}
}
