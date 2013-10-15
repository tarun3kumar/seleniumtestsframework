package com.seleniumtests.browserfactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.seleniumtests.driver.WebDriverConfig;

public class ChromeDriverFactory extends AbstractWebDriverFactory implements
		IWebDriverFactory {

	public ChromeDriverFactory(WebDriverConfig cfg) {
		super(cfg);
	}

	/**
	 * create native driver instance, designed for unit testing
	 * 
	 * @return
	 */
	protected WebDriver createNativeDriver() {
		return new ChromeDriver(
				new ChromeCapabilitiesFactory().createCapabilities(webDriverConfig));
	}

	@Override
	public WebDriver createWebDriver() throws IOException {
		WebDriverConfig cfg = this.getWebDriverConfig();

		driver = createNativeDriver();

		setImplicitWaitTimeout(cfg.getImplicitWaitTimeout());
		if (cfg.getPageLoadTimeout() >= 0) {
			setPageLoadTimeout(cfg.getPageLoadTimeout());
		}
		this.setWebDriver(driver);
		return driver;
	}

	protected void setPageLoadTimeout(long timeout) {
		try {
			driver.manage().timeouts()
					.pageLoadTimeout(timeout, TimeUnit.SECONDS);
		} catch (UnsupportedCommandException e) {
			// chromedriver1 does not support pageLoadTimeout
		}
	}

}
