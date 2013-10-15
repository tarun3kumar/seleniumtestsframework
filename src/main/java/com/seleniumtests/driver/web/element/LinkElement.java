package com.seleniumtests.driver.web.element;

import com.seleniumtests.core.TestLogging;
import com.seleniumtests.helper.URLAssistant;
import org.openqa.selenium.By;

public class LinkElement extends HtmlElement {

	public LinkElement(String label, By by) {
		super(label, by);
	}

	public LinkElement(String label, String locator) {
		super(label, locator);
	}

	@Override
	public void click() {
		captureSnapshot("before clicking");
		TestLogging.logWebStep(null, "click on " + toHTML(), false);
		super.click();
	}

	/**
	 * Checks if the link is not dead
	 */
	public boolean isValid() {
		try {
			URLAssistant.open(getAttribute("href"));
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	public String getUrl(){
	    return super.getAttribute("href");
	}
}