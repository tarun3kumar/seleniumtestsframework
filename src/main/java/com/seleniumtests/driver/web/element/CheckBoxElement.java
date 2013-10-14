package com.seleniumtests.driver.web.element;

import com.seleniumtests.controller.TestLogging;
import org.openqa.selenium.By;

public class CheckBoxElement extends HtmlElement {

	public CheckBoxElement(String label, By by) {
		super(label, by);
	}

	public CheckBoxElement(String label, String locator) {
		super(label, locator);
	}

	public void check() {
		TestLogging.logWebStep(null, "check " + toHTML(), false);
		if (!isSelected()) {
			super.click();
		}
	}

	@Override
	public void click() {
		TestLogging.logWebStep(null, "click on " + toHTML(), false);
		super.click();
	}

	//TODO this code is repeated in other areas--there should be an AbstractClickable and an IClickable where this gets ratified
	// and implemented
	public boolean isSelected() {
		findElement();
		return element.isSelected();
	}

	public void uncheck() {
		TestLogging.logWebStep(null, "uncheck " + toHTML(), false);
		if (isSelected()) {
			super.click();
		}
	}
}