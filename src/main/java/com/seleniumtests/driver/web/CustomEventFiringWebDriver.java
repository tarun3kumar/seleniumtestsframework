package com.seleniumtests.driver.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.FileDetector;
import org.openqa.selenium.remote.UselessFileDetector;
import org.openqa.selenium.support.events.EventFiringWebDriver;

/**
 * This file is used to support upload file in remote webdriver
 *
 */
public class CustomEventFiringWebDriver extends EventFiringWebDriver {
	private FileDetector fileDetector = new UselessFileDetector();
	private WebDriver driver = null;

	public CustomEventFiringWebDriver(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public void setFileDetector(FileDetector detector) {
		if (detector == null) {
			throw new WebDriverException(
					"You may not set a file detector that is null");
		}
		fileDetector = detector;
	}

	public FileDetector getFileDetector() {
		return fileDetector;
	}
	
	public WebDriver getWebDriver(){
		return driver;
	}
}