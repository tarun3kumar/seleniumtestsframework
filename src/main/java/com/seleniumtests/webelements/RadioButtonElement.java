package com.seleniumtests.webelements;

import com.seleniumtests.core.TestLogging;
import org.openqa.selenium.By;

public class RadioButtonElement extends HtmlElement {

	public RadioButtonElement(String label, By by) {
		super(label, by);
	}

	public void check() {
		TestLogging.logWebStep(null, "check " + toHTML(), false);
		super.click();
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
}