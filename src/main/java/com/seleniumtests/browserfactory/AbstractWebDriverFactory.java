package com.seleniumtests.browserfactory;

import java.util.concurrent.TimeUnit;

import com.seleniumtests.core.TestLogging;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.seleniumtests.driver.WebDriverConfig;
import com.seleniumtests.driver.WebUIDriver;

public abstract class AbstractWebDriverFactory {

	protected WebDriverConfig webDriverConfig;

	protected WebDriver driver;

	public AbstractWebDriverFactory(WebDriverConfig cfg) {
		this.webDriverConfig = cfg;
	}

	public void cleanUp() {
		try {
			if (driver != null) {
				try {
					TestLogging.log("quiting webdriver" + Thread.currentThread().getId());
					driver.quit();
				} catch (WebDriverException ex) {
                    TestLogging.log("Exception encountered when quiting driver: "
								+ WebUIDriver.getWebUXDriver().getConfig()
										.getBrowser().name() + ":"
								+ ex.getMessage());
				}
				driver = null;
			}
		} catch (Exception e) {
            e.printStackTrace();
		}
	}

	public WebDriver createWebDriver() throws Exception {
		return null;
	}

    /**
     * Accessed by sub classes so that they don't have be declared abstract
     *
     * @return driver instance
     */
	public WebDriver getWebDriver() {
		return driver;
	}

	public WebDriverConfig getWebDriverConfig() {
		return webDriverConfig;
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
		this.webDriverConfig = cfg;
	}
}
