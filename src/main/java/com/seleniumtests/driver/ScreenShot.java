package com.seleniumtests.driver;

import com.seleniumtests.core.SeleniumTestsContextManager;

public class ScreenShot {

	private String location;
	private String htmlSourcePath;
	private String imagePath;
	private String rlogId;
	private String title;
	private String suiteName;
	private boolean isException;
	private String outputDirectory;

	public ScreenShot() {
		if (SeleniumTestsContextManager.getGlobalContext().getTestNGContext() != null) {
			suiteName = SeleniumTestsContextManager.getGlobalContext().getTestNGContext()
					.getSuite().getName();
			outputDirectory = SeleniumTestsContextManager.getGlobalContext()
					.getTestNGContext().getOutputDirectory();
		}
	}

	public boolean isException() {
		return isException;
	}

	public void setException(boolean isException) {
		this.isException = isException;
	}

	public String getSuiteName() {
		return suiteName;
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setHtmlSourcePath(String htmlSourcePath) {
		this.htmlSourcePath = htmlSourcePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getRlogId() {
		return rlogId;
	}

	public void setRlogId(String rlogId) {
		this.rlogId = rlogId;
	}

	public String getHtmlSourcePath() {
		return htmlSourcePath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFullImagePath() {
		if (this.imagePath != null)
			return this.imagePath.replace(suiteName, outputDirectory);
		else
			return null;
	}

	public String getFullHtmlPath() {
		if (this.htmlSourcePath != null) {
			return this.htmlSourcePath.replace(suiteName, outputDirectory);
		} else
			return null;
	}

	@Override
	public String toString() {
		return "exception:" + this.isException + "|location:" + this.location
				+ "|title:" + this.title + "|htmlSource:"
				+ this.getFullHtmlPath() + "|image:" + this.getFullImagePath();
	}
}
