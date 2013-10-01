package com.seleniumtests.driver.web.element;

import org.openqa.selenium.By;

import com.seleniumtests.controller.Logging;

public class CheckBox extends HtmlElement {

	public CheckBox(String label, By by) {
		super(label, by);
	}

	public CheckBox(String label, String locator) {
		super(label, locator);
	}

	public void check() {
		Logging.logWebStep(null, "check " + toHTML(), false);
		if (!isSelected()) {
			super.click();
		}
	}

	@Override
	public void click() {
		Logging.logWebStep(null, "click on " + toHTML(), false);
		super.click();
	}

	//TODO this code is repeated in other areas--there should be an AbstractClickable and an IClickable where this gets ratified
	// and implemented
	public boolean isSelected() {
		findElement();
		return element.isSelected();
	}

	public void uncheck() {
		Logging.logWebStep(null, "uncheck " + toHTML(), false);
		if (isSelected()) {
			super.click();
		}
	}
}