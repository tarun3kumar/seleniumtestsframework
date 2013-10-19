package com.seleniumtests.webelements;

import com.seleniumtests.core.TestLogging;
import org.openqa.selenium.By;

public class LinkElement extends HtmlElement {

	public LinkElement(String label, By by) {
		super(label, by);
	}

	@Override
	public void click() {
		captureSnapshot("before clicking");
		TestLogging.logWebStep(null, "click on " + toHTML(), false);
		super.click();
	}

    public String getUrl(){
	    return super.getAttribute("href");
	}
}