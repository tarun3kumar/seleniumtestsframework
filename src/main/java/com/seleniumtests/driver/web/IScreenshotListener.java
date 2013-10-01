package com.seleniumtests.driver.web;

public interface IScreenshotListener {
	
	public void doScreenCapture(String pageId, String rlogId, String pageTitle,
			String imgType, String imgPath);

}
