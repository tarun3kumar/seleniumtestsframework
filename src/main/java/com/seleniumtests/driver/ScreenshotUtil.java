package com.seleniumtests.driver;

import java.io.IOException;

import com.seleniumtests.core.SeleniumTestsContextManager;
import com.seleniumtests.customexception.WebSessionEndedException;
import com.seleniumtests.helper.URLAssistant;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.seleniumtests.core.SeleniumTestsContext;
import com.seleniumtests.core.TestLogging;
import com.seleniumtests.helper.FileHelper;

public class ScreenshotUtil {
	private static final Logger logger = Logger.getLogger(ScreenshotUtil.class);

	public static String captureEntirePageScreenshotToString(WebDriver driver,
			String arg0) {
		if (driver == null)
			return "";
		try {
			// Don't capture snapshot for htmlunit
			if (WebUIDriver.getWebUXDriver().getBrowser()
					.equalsIgnoreCase(BrowserType.HtmlUnit.getBrowserType()))
				return null;
			// Opera has bug after upgrade selenium
			// 2.33.0,https://code.google.com/p/selenium/issues/detail?id=847
			if (WebUIDriver.getWebUXDriver().getBrowser()
					.equalsIgnoreCase(BrowserType.Opera.getBrowserType()))
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
		this.driver = WebUIDriver.getWebDriver();
	}

	public ScreenshotUtil(WebDriver driver) {
		suiteName = getSuiteName();
		outputDirectory = getOutputDirectory();
		this.driver = driver;
	}

	private static String getSuiteName() {
		String suiteName = null;

		suiteName = SeleniumTestsContextManager.getGlobalContext().getTestNGContext()
				.getSuite().getName();

		return suiteName;
	}

	private static String getOutputDirectory() {
		String outputDirectory = null;
		outputDirectory = SeleniumTestsContextManager.getGlobalContext().getTestNGContext()
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
	}

	private void handleImage(ScreenShot screenShot) {
		try {
			String screenshotString = captureEntirePageScreenshotToString(
					WebUIDriver.getWebDriver(), "");

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

		if (SeleniumTestsContextManager.getThreadContext() == null
				|| outputDirectory == null)
			return screenShot;
		screenShot.setSuiteName(this.suiteName);

		try {
			String url = null;
			try {
				url = driver.getCurrentUrl();
			} catch (org.openqa.selenium.UnhandledAlertException ex) {
				// ignore alert customexception
				ex.printStackTrace();
				url = driver.getCurrentUrl();
			}
			String title = driver.getTitle();
			String pageSource = driver.getPageSource();

			String filename = URLAssistant.getRandomHashCode("web");
			this.filename = filename;
			screenShot.setLocation(url);

			handleTitle(title, screenShot);
			handleSource(pageSource, screenShot);
			if (SeleniumTestsContextManager.getThreadContext().getCaptureSnapshot()) {
				handleImage(screenShot);
			}
		}catch(WebSessionEndedException e){
			throw e;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		if (SeleniumTestsContextManager.getThreadContext().getCaptureSnapshot())
			SeleniumTestsContextManager.getThreadContext().addScreenShot(screenShot);
		return screenShot;
	}

	/**
	 * Used by DriverExceptionListener, don't log the customexception but put it
	 * into context
	 * 
	 */
	public void capturePageSnapshotOnException() {
		Boolean capture = SeleniumTestsContextManager.getThreadContext()
				.getCaptureSnapshot();
		SeleniumTestsContextManager.getThreadContext().setAttribute(
				SeleniumTestsContext.CAPTURE_SNAPSHOT, "true");
		captureWebPageSnapshot();
		SeleniumTestsContextManager.getThreadContext().setAttribute(
				SeleniumTestsContext.CAPTURE_SNAPSHOT, Boolean.toString(capture));
		// SeleniumTestsContextManager.getThreadContext().setScreenshotName(filename);
		// SeleniumTestsContextManager.getThreadContext().setWebExceptionURL(location);
		// SeleniumTestsContextManager.getThreadContext().setWebExceptionMessage(title + " ("
		// + sbMessage.toString() + ")");
		// screenShot.setException(true);
		if (SeleniumTestsContextManager.getThreadContext().getScreenshots().size() > 0)
			SeleniumTestsContextManager.getThreadContext().getScreenshots().getLast()
					.setException(true);
	}

	public static void captureSnapshot(String messagePrefix) {
		if (SeleniumTestsContextManager.getThreadContext() != null
				&& SeleniumTestsContextManager.getThreadContext().getCaptureSnapshot()
				&& getOutputDirectory() != null) {
			String filename = URLAssistant.getRandomHashCode("HtmlElement");
			StringBuffer sbMessage = new StringBuffer();
			try {
				String img = ScreenshotUtil
						.captureEntirePageScreenshotToString(
								WebUIDriver.getWebDriver(), "");
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
					SeleniumTestsContextManager.getThreadContext().addScreenShot(screenShot);
					sbMessage.append(messagePrefix + ": <a href='" + imagePath
							+ "' class='lightbox'>screenshot</a>");
					TestLogging.logWebOutput(null, sbMessage.toString(), false);
					sbMessage = null;
				}
			}catch(WebSessionEndedException ex){
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
