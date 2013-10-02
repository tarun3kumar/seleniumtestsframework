package com.seleniumtests.driver.web.element;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.seleniumtests.controller.Logging;
import com.seleniumtests.driver.web.BrowserType;
import com.seleniumtests.driver.web.WebUXDriver;
import com.seleniumtests.helper.ThreadHelper;

public class Button extends HtmlElement {

	public Button(String label, By by) {
		super(label, by);
	}

	public Button(String label, String locator) {
		super(label, locator);
	}

	@Override
	public void click() {
		captureSnapshot("before clicking");
		Logging.logWebStep(null, "click on " + toHTML(), false);
		BrowserType browser = WebUXDriver.getWebUXDriver().getConfig()
				.getBrowser();
		if (browser == BrowserType.InternetExplore) {
			super.sendKeys(Keys.ENTER);// not stable on IE9
			ThreadHelper.waitForSeconds(2);
			super.handleLeaveAlert();
		} else {
			super.click();
		}
	}

	public void submit() {
		captureSnapshot("before form submission");
		Logging.logWebStep(null, "submit form by clicking on " + toHTML(),
				false);
		findElement();
		element.submit();
	}
}