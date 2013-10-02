package com.seleniumtests.driver.web;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import com.seleniumtests.controller.Logging;

public class BaseWebUtil {
	private WebDriver driver;
	
	public BaseWebUtil(WebDriver driver){
		this.driver = driver;
	}
	
	public void resizeWindow(int width, int height) {
		try {
			Logging.logWebStep(null, "Resize browser window to width " + width
					+ " height " + height, false);
			Dimension size = new Dimension(width, height);
			driver.manage().window().setPosition(new Point(0, 0));
			driver.manage().window().setSize(size);
		} catch (Exception ex) {
		}
	}

	public void maximizeWindow() {
		try {
			BrowserType browser = BrowserType.getBrowserType(WebUXDriver
					.getWebUXDriver().getBrowser());
			if (browser == BrowserType.Android || browser == BrowserType.IPad
					|| browser == BrowserType.IPhone)
				return;

			Dimension oldDimension = driver.manage().window().getSize();
			driver.manage().window().maximize();
			Dimension dimension = driver.manage().window().getSize();
		} catch (Exception ex) {

			try {
				((JavascriptExecutor) driver)
						.executeScript("if (window.screen){window.moveTo(0, 0);window.resizeTo(window.screen.availWidth,window.screen.availHeight);}");
			} catch (Exception ignore) {
				Logging.log("Exception when maximizing browser window"
						+ ignore.getMessage());
			}
		}
	}
	
	public static void main(String[] args){
		WebUXDriver.getWebUXDriver().setMode("ExistingGrid");
		WebUXDriver.getWebUXDriver().setHubUrl(" ");
		WebDriver driver = WebUXDriver.getWebDriver(true);
		System.out.print(driver.manage().window().getSize().width + ":"+driver.manage().window().getSize().height);
	}

}
