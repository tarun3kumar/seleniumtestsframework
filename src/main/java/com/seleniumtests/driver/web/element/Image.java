package com.seleniumtests.driver.web.element;

import org.openqa.selenium.By;

public class Image extends HtmlElement {

	public Image(String label, By by) {
		super(label, by);
	}

	public Image(String label, String locator) {
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
