package com.seleniumtests.driver.web.element;

import com.seleniumtests.core.TestLogging;
import org.openqa.selenium.By;

import com.seleniumtests.core.CustomAssertion;

public class LabelElement extends HtmlElement {
	public LabelElement(String label, By by) {
		super(label, by);
	}

	public LabelElement(String label, String locator) {
		super(label, locator);
	}

	@Override
	public String getText() {
		TestLogging.logWebStep(null, "get text from " + toHTML(), false);
		return super.getText();
	}
	
	public boolean isTextPresent(String pattern){
		String text = getText();
		return (text != null && (text.contains(pattern) ||text.matches(pattern)));
	}

	@Deprecated
	public String getExpectedText()
	{
		CustomAssertion.assertTrue(false, "NOT supported!");
		return null;
	}
}