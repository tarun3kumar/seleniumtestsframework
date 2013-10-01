package com.seleniumtests.driver.web.element;

import org.openqa.selenium.By;

import com.seleniumtests.controller.Logging;
import com.seleniumtests.helper.URLHelper;

public class Link extends HtmlElement {

	public Link(String label, By by) {
		super(label, by);
	}

	public Link(String label, String locator) {
		super(label, locator);
	}

	@Override
	public void click() {
		captureSnapshot("before clicking");
		Logging.logWebStep(null, "click on " + toHTML(), false);
		super.click();
	}

	/**
	 * Checks if the link is not dead
	 */
	public boolean isValid() {
		try {
			URLHelper.open(getAttribute("href"));
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	public String getUrl(){
	    return super.getAttribute("href");
	}
}