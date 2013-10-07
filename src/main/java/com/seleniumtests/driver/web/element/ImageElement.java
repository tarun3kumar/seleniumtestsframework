package com.seleniumtests.driver.web.element;

import org.openqa.selenium.By;

public class ImageElement extends HtmlElement {

	public ImageElement(String label, By by) {
		super(label, by);
	}

	public ImageElement(String label, String locator) {
		super(label, locator);
	}

	public int getHeight() {
		return super.getSize().getHeight();
	}

	public int getWidth() {
		return super.getSize().getWidth();
	}
	
	public String getUrl(){
	    return super.getAttribute("src");
	}
}
