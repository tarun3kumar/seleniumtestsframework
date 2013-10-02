package com.seleniumtests.driver.web.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;

import com.seleniumtests.controller.Logging;

public class TextField extends HtmlElement {
	public TextField(String label, By by) {
		super(label, by);
	}

	public TextField(String label, String locator) {
		super(label, locator);
	}

	public void clear() {
		Logging.logWebStep(null, "remove data on " + toHTML(), false);
		findElement();
		// clear for file input, will get Exception:Element must be
		// user-editable in order to clear it.
		if (!element.getAttribute("type").equalsIgnoreCase("file"))
			element.clear();
	}

	public void sendKeys(String keysToSend) {
		Logging.logWebStep(null, "enter data: \"" + keysToSend + "\" on "
				+ toHTML(), false);
		findElement();
		element.sendKeys(keysToSend);
	}

	public void type(String keysToSend) {
		try {
			clear();
		} catch (WebDriverException e) {
			Logging.logWebStep(null, "Exception:clear value on " + toHTML(),
					false);
		}
		sendKeys(keysToSend);
	}
}
