package com.seleniumtests.webelements;

import com.seleniumtests.core.TestLogging;
import com.seleniumtests.driver.WebUIDriver;
import com.seleniumtests.helper.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.seleniumtests.driver.BrowserType;

public class ButtonElement extends HtmlElement {

	public ButtonElement(String label, By by) {
		super(label, by);
	}

	@Override
	public void click() {
		captureSnapshot("before clicking");
		TestLogging.logWebStep(null, "click on " + toHTML(), false);
		BrowserType browser = WebUIDriver.getWebUXDriver().getConfig()
				.getBrowser();
		if (browser == BrowserType.InternetExplore) {
			super.sendKeys(Keys.ENTER);// not stable on IE9
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