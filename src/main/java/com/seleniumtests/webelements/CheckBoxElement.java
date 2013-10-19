package com.seleniumtests.webelements;

import com.seleniumtests.core.TestLogging;
import org.openqa.selenium.By;

public class CheckBoxElement extends HtmlElement {

	public CheckBoxElement(String label, By by) {
		super(label, by);
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