package com.seleniumtests.webelements;

import com.seleniumtests.core.TestLogging;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;

public class TextFieldElement extends HtmlElement {
	public TextFieldElement(String label, By by) {
		super(label, by);
	}

	public TextFieldElement(String label, String locator) {
		super(label, locator);
	}

	public void clear() {
		TestLogging.logWebStep(null, "remove data on " + toHTML(), false);
		findElement();
		// clear for file input, will get Exception:Element must be
		// user-editable in order to clear it.
		if (!element.getAttribute("type").equalsIgnoreCase("file"))
			element.clear();
	}

	public void sendKeys(String keysToSend) {
		TestLogging.logWebStep(null, "enter data: \"" + keysToSend + "\" on "
                + toHTML(), false);
		findElement();
		element.sendKeys(keysToSend);
	}

	public void type(String keysToSend) {
		try {
			clear();
		} catch (WebDriverException e) {
			TestLogging.logWebStep(null, "Exception:clear value on " + toHTML(),
                    false);
		}
		sendKeys(keysToSend);
	}
}
