package com.seleniumtests.webelements;

import com.seleniumtests.core.TestLogging;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;

public class TextFieldElement extends HtmlElement {
	public TextFieldElement(String label, By by) {
		super(label, by);
	}

	public void clear() {
		TestLogging.logWebStep(null, "Remove data From " + toHTML(), false);
		findElement();
		if (!element.getAttribute("type").equalsIgnoreCase("file"))
			element.clear();
	}

	public void sendKeys(String keysToSend) {
		TestLogging.logWebStep(null, "Enter data: \"" + keysToSend + "\" on "
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

    public void clearAndType(String keysToSend) {
        clear();
        type(keysToSend);
    }
}
