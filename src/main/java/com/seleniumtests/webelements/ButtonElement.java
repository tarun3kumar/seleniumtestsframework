package com.seleniumtests.webelements;

import com.seleniumtests.core.TestLogging;
import com.seleniumtests.driver.web.WebUIDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.seleniumtests.driver.web.BrowserType;
import com.seleniumtests.helper.ThreadHelper;

public class ButtonElement extends HtmlElement {

	public ButtonElement(String label, By by) {
		super(label, by);
	}

	public ButtonElement(String label, String locator) {
		super(label, locator);
	}

	@Override
	public void click() {
		captureSnapshot("before clicking");
		TestLogging.logWebStep(null, "click on " + toHTML(), false);
		BrowserType browser = WebUIDriver.getWebUXDriver().getConfig()
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
		TestLogging.logWebStep(null, "submit form by clicking on " + toHTML(),
                false);
		findElement();
		element.submit();
	}
}