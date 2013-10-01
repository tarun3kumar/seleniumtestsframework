package com.seleniumtests.driver.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.seleniumtests.controller.Context;
import com.seleniumtests.controller.ContextManager;
import com.seleniumtests.controller.Logging;
import com.seleniumtests.exception.WebSessionTerminatedException;
import com.seleniumtests.helper.FileHelper;
import com.seleniumtests.helper.URLHelper;

public class ScreenshotUtil {
	private static final Logger logger = Logger.getLogger(ScreenshotUtil.class);

	public static String captureEntirePageScreenshotToString(WebDriver driver,
			String arg0) {
		if (driver == null)
			return "";
		try {
			// Don't capture snapshot for htmlunit
			if (WebUXDriver.getWebUXDriver().getBrowser()
					.equalsIgnoreCase(BrowserType.HtmlUnit.getType()))
				return null;
			// Opera has bug after upgrade selenium
			// 2.33.0,https://code.google.com/p/selenium/issues/detail?id=847
			if (WebUXDriver.getWebUXDriver().getBrowser()
					.equalsIgnoreCase(BrowserType.Opera.getType()))
				return null;

			TakesScreenshot screenShot = (TakesScreenshot) driver;
			return screenShot.getScreenshotAs(OutputType.BASE64);
		} catch (Exception ex) {
			// Ignore all exceptions
			ex.printStackTrace();
		}
		return "";
	}

	private String suiteName;
	private String outputDirectory;
	private WebDriver driver;
	private String filename;

	public ScreenshotUtil() {
		suiteName = getSuiteName();
		outputDirectory = getOutputDirectory();
		this.driver = WebUXDriver.getWebDriver();
	}

	public ScreenshotUtil(WebDriver driver) {
		suiteName = getSuiteName();
		outputDirectory = getOutputDirectory();
		this.driver = driver;
	}

	private static String getSuiteName() {
		String suiteName = null;

		suiteName = ContextManager.getGlobalContext().getTestNGContext()
				.getSuite().getName();

		return suiteName;
	}

	private static String getOutputDirectory() {
		String outputDirectory = null;
		outputDirectory = ContextManager.getGlobalContext().getTestNGContext()
				.getOutputDirectory();

		return outputDirectory;
	}

	private void handleSource(String htmlSource, ScreenShot screenShot) {
		if (htmlSource == null) {
			// driver.switchTo().defaultContent();
			htmlSource = driver.getPageSource();
		}

		if (htmlSource != null) {
			try {
				FileHelper.writeToFile(outputDirectory + "/htmls/" + filename
						+ ".html", htmlSource);
				screenShot.setHtmlSourcePath(suiteName + "/htmls/" + filename
						+ ".html");
			} catch (IOException e) {
				logger.warn("Ex", e);
			}

		}
		String rlogId = null;
		String pageId = null;
		try {
			int startIndex = htmlSource.indexOf("RlogId ");
			if (startIndex != -1) {
				int endIndex1 = htmlSource.substring(startIndex).indexOf("-->");
				int endIndex2 = htmlSource.substring(startIndex).indexOf('"');
				int endIndex = 0;
				if (endIndex1 != -1)
					endIndex = endIndex1;
				if (endIndex2 != -1 && endIndex2 < endIndex)
					endIndex = endIndex2;

				if (endIndex != -1) {
					rlogId = htmlSource.substring(startIndex + 7, startIndex
							+ endIndex);
					screenShot.setRlogId(rlogId);
				}
			}
		} catch (Throwable e) {
		}// KEEPME

		// get pageid value on the page source
		try {
			int startIndex = htmlSource.indexOf("GlobalTags:p=");
			if (startIndex != -1) {
				int endIndex1 = htmlSource.substring(startIndex).indexOf(
						"\nGlobalTags:");
				// int endIndex2 =
				// htmlSource.substring(startIndex).indexOf("\nGlobalTags:");
				int endIndex = 0;
				if (endIndex1 != -1)
					endIndex = endIndex1;

				if (endIndex != -1) {
					pageId = htmlSource.substring(startIndex + 13, startIndex
							+ endIndex);
					screenShot.setPageId(pageId);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}// KEEPME

	}

	private void handlePageId(ScreenShot screenShot) {
		String pageId = screenShot.getPageId();
		if (pageId == null) {
			URL url;
			try {
				url = new URL(screenShot.getLocation());
				if (url.getQuery() != null) {
					pageId = url.getQuery();
					if (pageId.contains("&"))
						pageId = pageId.substring(0, pageId.indexOf("&"));
					screenShot.setPageId(pageId);

				}
			} catch (MalformedURLException e) {
			}

		}
	}

	private void handleImage(ScreenShot screenShot) {
		try {
			String screenshotString = captureEntirePageScreenshotToString(
					WebUXDriver.getWebDriver(), "");

			if (screenshotString != null
					&& !screenshotString.equalsIgnoreCase("")) {
				byte[] byteArray = screenshotString.getBytes();
				FileHelper.writeImage(outputDirectory + "/screenshots/"
						+ filename + ".png", byteArray);
				screenShot.setImagePath(suiteName + "/screenshots/" + filename
						+ ".png");

			}
		} catch (Throwable e) {
			logger.warn("Ex", e);
			e.printStackTrace();
		}
	}

	private void handleTitle(String title, ScreenShot screenShot) {
		if (title == null) {
			// driver.switchTo().defaultContent();
			title = driver.getTitle();
		}
		if (title == null)
			title = "";
		screenShot.setTitle(title);
	}

	public ScreenShot captureWebPageSnapshot() {

		ScreenShot screenShot = new ScreenShot();

		if (ContextManager.getThreadContext() == null
				|| outputDirectory == null)
			return screenShot;
		screenShot.setSuiteName(this.suiteName);

		try {
			String url = null;
			try {
				url = driver.getCurrentUrl();
			} catch (org.openqa.selenium.UnhandledAlertException ex) {
				// ignore alert exception
				ex.printStackTrace();
				url = driver.getCurrentUrl();
			}
			String title = driver.getTitle();
			String pageSource = driver.getPageSource();

			String filename = URLHelper.getRandomHashCode("web");
			this.filename = filename;
			screenShot.setLocation(url);

			handleTitle(title, screenShot);
			handleSource(pageSource, screenShot);
			handlePageId(screenShot);
			if (ContextManager.getThreadContext().getCaptureSnapshot()) {
				handleImage(screenShot);
			}
		}catch(WebSessionTerminatedException e){
			throw e;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		if (ContextManager.getThreadContext().getCaptureSnapshot())
			ContextManager.getThreadContext().addScreenShot(screenShot);
		return screenShot;
	}

	/**
	 * Used by WebDriverExceptionListener, don't log the exception but put it
	 * into context
	 * 
	 * @param driver
	 * @param location
	 * @param htmlSource
	 * @param title
	 */
	public void capturePageSnapshotOnException() {
		Boolean capture = ContextManager.getThreadContext()
				.getCaptureSnapshot();
		ContextManager.getThreadContext().setAttribute(
				Context.CAPTURE_SNAPSHOT, "true");
		captureWebPageSnapshot();
		ContextManager.getThreadContext().setAttribute(
				Context.CAPTURE_SNAPSHOT, Boolean.toString(capture));
		// ContextManager.getThreadContext().setScreenshotName(filename);
		// ContextManager.getThreadContext().setWebExceptionURL(location);
		// ContextManager.getThreadContext().setWebExceptionMessage(title + " ("
		// + sbMessage.toString() + ")");
		// screenShot.setException(true);
		if (ContextManager.getThreadContext().getScreenshots().size() > 0)
			ContextManager.getThreadContext().getScreenshots().getLast()
					.setException(true);
	}

	public static void captureSnapshot(String messagePrefix) {
		if (ContextManager.getThreadContext() != null
				&& ContextManager.getThreadContext().getCaptureSnapshot()
				&& getOutputDirectory() != null) {
			String filename = URLHelper.getRandomHashCode("HtmlElement");
			StringBuffer sbMessage = new StringBuffer();
			try {
				String img = ScreenshotUtil
						.captureEntirePageScreenshotToString(
								WebUXDriver.getWebDriver(), "");
				if (img == null)
					return;
				byte[] byteArray = img.getBytes();// KEEPME
				if (byteArray != null && byteArray.length > 0) {
					String imgFile = "/screenshots/" + filename + ".png";
					FileHelper.writeImage(getOutputDirectory() + imgFile,
							byteArray);
					ScreenShot screenShot = new ScreenShot();
					String imagePath = getSuiteName() + imgFile;
					screenShot.setImagePath(imagePath);
					ContextManager.getThreadContext().addScreenShot(screenShot);
					sbMessage.append(messagePrefix + ": <a href='" + imagePath
							+ "' class='lightbox'>screenshot</a>");
					Logging.logWebOutput(null, sbMessage.toString(), false);
					sbMessage = null;
				}
			}catch(WebSessionTerminatedException ex){
				throw ex;
			}
			catch (Throwable e) {
				e.printStackTrace();
			}

		}
	}

	// private static String readFileAsString(File file) throws
	// java.io.IOException {
	// StringBuffer fileData = new StringBuffer(1000);
	// BufferedReader reader = new BufferedReader(new FileReader(file));
	// char[] buf = new char[1024];
	// int numRead = 0;
	// while ((numRead = reader.read(buf)) != -1) {
	// String readData = String.valueOf(buf, 0, numRead);
	// fileData.append(readData);
	// buf = new char[1024];
	// }
	// reader.close();
	// return fileData.toString();
	// }
}
