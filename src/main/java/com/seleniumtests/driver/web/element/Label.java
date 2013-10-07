package com.seleniumtests.driver.web.element;

import org.openqa.selenium.By;

import com.seleniumtests.controller.CustomAssertion;
import com.seleniumtests.controller.Logging;

public class Label extends HtmlElement {
	public Label(String label, By by) {
		super(label, by);
	}

	public Label(String label, String locator) {
		super(label, locator);
	}

	@Override
	public String getText() {
		Logging.logWebStep(null, "get text from " + toHTML(), false);
		return super.getText();
	}
	
	public boolean isTextPresent(String pattern){
		String text = getText();
		return (text != null && (text.contains(pattern) ||text.matches(pattern)));
	}

	@Deprecated
	public String getExpectedText()
	{
		CustomAssertion.assertTrue(false, "Not supported!");
		return null;
	}
}