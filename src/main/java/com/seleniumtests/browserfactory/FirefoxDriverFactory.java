package com.seleniumtests.browserfactory;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.seleniumtests.driver.web.WebDriverConfig;

public class FirefoxDriverFactory extends AbstractWebDriverFactory implements
		IWebDriverFactory {
	private long timeout = 60;

	/**
	 * 
	 * @param cfg
	 *            the configuration of the firefoxDriver
	 */
	public FirefoxDriverFactory(WebDriverConfig cfg) {
		super(cfg);
	}

	/**
	 * create native driver instance, designed for unit testing
	 * 
	 * @return
	 */
	protected WebDriver createNativeDriver() {
		return new FirefoxDriver( 
				new FirefoxCapabilitiesFactory().createCapabilities(cfg));
	}

	@Override
	public WebDriver createWebDriver() {
		WebDriverConfig cfg = this.getWebDriverConfig();

		System.out.println("start create firefox");
		driver = createWebDriverWithTimeout();

		System.out.println("end create firefox");

		// Implicit Waits to handle dynamic element. The default value is 5
		// seconds.
		setImplicitWaitTimeout(cfg.getImplicitWaitTimeout());
		if (cfg.getPageLoadTimeout() >= 0) {
			setPageLoadTimeout(cfg.getPageLoadTimeout());
		}

		this.setWebDriver(driver);
		return driver;
	}

	/**
	 * Create webDriver, capture socket exception and retry with timeout
	 * 
	 * @return WebDriver
	 */
	protected WebDriver createWebDriverWithTimeout() {
		long time = 0;
		while (time < getTimeout()) {
			try {
				driver = createNativeDriver();
				return driver;
			} catch (WebDriverException ex) {
				if (ex.getMessage().contains("SocketException")
						|| ex.getMessage().contains(
								"Failed to connect to binary FirefoxBinary")
						|| ex.getMessage()
								.contains(
										"Unable to bind to locking port 7054 within 45000 ms")) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					time++;
				} else
					throw new RuntimeException(ex);
			}
		}
		throw new RuntimeException(
				"Got exception when creating webDriver with socket timeout 1 minute");
	}

	/**
	 * It's designed for shorten tiemout in unit testing
	 * 
	 * @return timeout
	 */
	protected long getTimeout() {
		return timeout;
	}

	protected void setPageLoadTimeout(long timeout) {
		driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
	}
}
