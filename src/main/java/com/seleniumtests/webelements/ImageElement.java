package com.seleniumtests.webelements;

import org.openqa.selenium.By;

public class ImageElement extends HtmlElement {

	public ImageElement(String label, By by) {
		super(label, by);
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
