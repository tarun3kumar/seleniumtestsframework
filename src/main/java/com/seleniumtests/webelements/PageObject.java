package com.seleniumtests.webelements;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.seleniumtests.core.CustomAssertion;
import com.seleniumtests.core.SeleniumTestsContextManager;
import com.seleniumtests.core.SeleniumTestsPageListener;
import com.seleniumtests.core.TestLogging;
import com.seleniumtests.customexception.CustomSeleniumTestsException;
import com.seleniumtests.customexception.NotCurrentPageException;
import com.seleniumtests.driver.ScreenShot;
import com.seleniumtests.driver.ScreenshotUtil;
import com.seleniumtests.driver.WebUIDriver;
import com.seleniumtests.driver.WebUtility;
import com.seleniumtests.helper.WaitHelper;
import com.thoughtworks.selenium.Wait;
import com.thoughtworks.selenium.Wait.WaitTimedOutException;
import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.seleniumemulation.Windows;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.testng.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PageObject extends BasePage implements IPage {

	private static final Logger logger = Logger.getLogger(PageObject.class);
	private static final int MAX_WAIT_TIME_FOR_REDIRECTION = 3;
	private boolean popupFlag = false;
	private boolean frameFlag = false;
	private HtmlElement pageIdentifierElement = null;
	private String popupWindowName = null;
	private String windowHandle = null;
	private String title = null;
	private String url = null;
	private String bodyText = null;
	private String htmlSource = null;
	private String htmlSavedToPath = null;
	private String suiteName = null;
	private String outputDirectory = null;
	private String htmlFilePath = null;
	private String imageFilePath = null;

	/**
	 * Constructor for non-entry point page
	 * 
	 * @throws Exception
	 */
	public PageObject() throws Exception {
		this(false, null, null, null, true, false);
	}

	public PageObject(boolean isPopup, String popUpWindowName) throws Exception {
		this(isPopup, popUpWindowName, null, null, true, false);
	}

	/**
	 * Base Constructor
	 * 
	 * @param url
	 * @param convert
	 * @throws Exception
	 */
	private PageObject(boolean isPopup, String popupWindowName,
                       HtmlElement pageIdentifierElement, String url, boolean convert,
                       boolean waitForRedirection) throws Exception {
		Calendar start = Calendar.getInstance();
		start.setTime(new Date());
		
		if (SeleniumTestsContextManager.getGlobalContext() != null
				&& SeleniumTestsContextManager.getGlobalContext().getTestNGContext() != null) {
			suiteName = SeleniumTestsContextManager.getGlobalContext().getTestNGContext()
					.getSuite().getName();
			outputDirectory = SeleniumTestsContextManager.getGlobalContext()
					.getTestNGContext().getOutputDirectory();
		}

		this.pageIdentifierElement = pageIdentifierElement;
		this.popupFlag = isPopup;
		this.popupWindowName = popupWindowName;

		driver = WebUIDriver.getWebDriver();

		if (!popupFlag) {

			if (url != null) {
				open(url);
			}

			waitForPageToLoad();

		} else {
			TestLogging.logWebStep(null, "wait for popup to load.", false);
			waitForPopup(popupWindowName);

			Windows windows = new Windows(driver);
			windows.selectWindow(driver, "name=" + popupWindowName);

			// populate page info
			populateAndCapturePageSnapshot();
		}

		try {
			this.windowHandle = driver.getWindowHandle();
		} catch (Exception ex) {
			// Ignore for OperaDriver
		}

		SeleniumTestsPageListener.informPageLoad(this);
		Calendar end = Calendar.getInstance();
		start.setTime(new Date());
		long startTime = start.getTimeInMillis();
		long endTime = end.getTimeInMillis();
		if((endTime-startTime)/1000>0)
			TestLogging.log("Open web page in :" + (endTime - startTime) / 1000 + "seconds");
	}

	/**
	 * Constructor for non-entry point page
	 * 
	 * @param pageIdentifierElement
	 * @throws Exception
	 */
	public PageObject(HtmlElement pageIdentifierElement) throws Exception {
		this(false, null, pageIdentifierElement, null, true, false);
	}

	/**
	 * Constructor for non-entry point page after redirection
	 * 
	 * @param pageIdentifierElement
	 * @param waitForRedirection
	 * @throws Exception
	 */
	public PageObject(HtmlElement pageIdentifierElement, boolean waitForRedirection)
			throws Exception {
		this(false, null, pageIdentifierElement, null, true, waitForRedirection);
	}

	/**
	 * Constructor for Pop-up Pages
	 * 
	 * @param pageIdentifierElement
	 * @param isPopup
	 * @param popupWindowName
	 * @throws Exception
	 */
	public PageObject(HtmlElement pageIdentifierElement, boolean isPopup,
                      String popupWindowName) throws Exception {
		this(isPopup, popupWindowName, pageIdentifierElement, null, true, false);
	}

	/**
	 * Constructor for Entry point page. URLs will be converted based on feature
	 * pool.
	 * 
	 * @param pageIdentifierElement
	 * @param url
	 * @throws Exception
	 */
	public PageObject(HtmlElement pageIdentifierElement, String url)
			throws Exception {

		this(false, null, pageIdentifierElement, url, true, false);
	}

	/**
	 * Constructor for entry point page after redirection
	 * 
	 * @param pageIdentifierElement
	 * @param url
	 * @param waitForRedirection
	 * @throws Exception
	 */
	public PageObject(HtmlElement pageIdentifierElement, String url,
                      boolean waitForRedirection) throws Exception {
		this(false, null, pageIdentifierElement, url, false, waitForRedirection);
	}


    public void assertCookiePresent(String name) {
		TestLogging.logWebStep(null, "assert cookie " + name + " is present.",
                false);
		assertHTML(getCookieByName(name) != null, "Cookie: {" + name
				+ "} not found.");
	}

	@Override
	protected void assertCurrentPage(boolean log)
			throws NotCurrentPageException {

		if (pageIdentifierElement == null) {

		} else if (this.isElementPresent(pageIdentifierElement.getBy())) {

		} else {
			try {
				if (!SeleniumTestsContextManager.getThreadContext().getCaptureSnapshot())
					new ScreenshotUtil(driver).capturePageSnapshotOnException();
			} catch (Exception e) {
				// Ignore all exceptions
				e.printStackTrace();
			}
			throw new NotCurrentPageException(getClass().getCanonicalName()
					+ " is not the current page.\nPageIdentifierElement "
					+ pageIdentifierElement.toString() + " is not found.");
		}

		if (log)
			TestLogging.logWebStep(
                    null,
                    "assert \""
                            + getClass().getSimpleName()
                            + "\" is the current page"
                            + (pageIdentifierElement != null ? " (assert PageIdentifierElement "
                            + pageIdentifierElement.toHTML()
                            + " is present)."
                            : "."), false);
	}

	public void assertHtmlSource(String text) {
		TestLogging.logWebStep(null, "assert text \"" + text
                + "\" is present in page source.", false);
		assertHTML(getHtmlSource().contains(text), "Text: {" + text
				+ "} not found on page source.");
	}

	public void assertKeywordNotPresent(String text) {
		TestLogging.logWebStep(null, "assert text \"" + text
                + "\" is present in page source.", false);
		Assert.assertFalse(getHtmlSource().contains(text), "Text: {" + text
				+ "} not found on page source.");

	}

	public void assertLocation(String urlPattern) {
		TestLogging.logWebStep(null, "assert location \"" + urlPattern + "\".",
                false);
		assertHTML(getLocation().contains(urlPattern), "Pattern: {"
				+ urlPattern + "} not found on page location.");
	}

	public void assertPageSectionPresent(WebPageSection pageSection) {
		TestLogging.logWebStep(null,
                "assert pagesection \"" + pageSection.getName()
                        + "\"  is present.", false);
		assertElementPresent(new HtmlElement(pageSection.getName(),
				pageSection.getBy()));
	}

	public void assertTitle(String text) {
		TestLogging.logWebStep(null, "assert text \"" + text
                + "\"  is present on title.", false);
		assertHTML(getTitle().contains(text), "Text: {" + text
				+ "} not found on page title.");

	}

	public void capturePageSnapshot() {
		ScreenShot screenShot = new ScreenshotUtil(driver)
				.captureWebPageSnapshot();
		this.title = screenShot.getTitle();

		if (screenShot.getHtmlSourcePath() != null) {
			htmlFilePath = screenShot.getHtmlSourcePath().replace(suiteName,
					outputDirectory);
			htmlSavedToPath = screenShot.getHtmlSourcePath();
		}
		if (screenShot.getImagePath() != null) {
			imageFilePath = screenShot.getImagePath().replace(suiteName,
					outputDirectory);
		}
		TestLogging.logWebOutput(url,
                title + " (" + TestLogging.buildScreenshotLog(screenShot) + ")",
                false);

	}

	public final void close() throws NotCurrentPageException {
		if (WebUIDriver.getWebDriver() == null) {
			return;
		}
		SeleniumTestsPageListener.informPageUnload(this);
		TestLogging.log("close web page");
		boolean isMultipleWindow = false;
		if (driver.getWindowHandles().size() > 1)
			isMultipleWindow = true;
		try{
			driver.close();
		}catch(WebDriverException ignore){
		}
		if (WebUIDriver.getWebUXDriver().getMode()
				.equalsIgnoreCase("LOCAL")) {
			try {
				Thread.sleep(1000 * 2);
			} catch (InterruptedException e) {
			}
		}
		try {
			if (isMultipleWindow)
				this.selectWindow();
			else
				WebUIDriver.setWebDriver(null);
		} catch (UnreachableBrowserException ex) {
			WebUIDriver.setWebDriver(null);

		}
	}

    /**
	 * Drags an element a certain distance and then drops it
	 * 
	 * @param element
	 *            to dragAndDrop
	 * @param offsetX
	 *            in pixels from the current location to which the element
	 *            should be moved, e.g., 70
	 * @param offsetY
	 *            in pixels from the current location to which the element
	 *            should be moved, e.g., -300
	 */
	public void dragAndDrop(HtmlElement element, int offsetX, int offsetY) {
		TestLogging.logWebStep(null, "dragAndDrop " + element.toHTML()
                + " to offset(x,y): (" + offsetX + "," + offsetY + ")", false);
		element.captureSnapshot("before draging");

		new Actions(driver).dragAndDropBy((WebElement) element.getElement(),
				offsetX, offsetY).perform();
		element.captureSnapshot("after dropping");
	}


	public String getBodyText() {
		return bodyText;
	}

	public final String getCookieByName(String name) {
		if (driver.manage().getCookieNamed(name) == null)
			return null;
		return driver.manage().getCookieNamed(name).getValue();
	}

	public final int getElementCount(HtmlElement element) throws CustomSeleniumTestsException {
		return driver.findElements(element.getBy()).size();
	}

	public String getEval(String expression) {
		CustomAssertion.assertTrue(false, "focus not implemented yet");
		return null;
	}

	public String getHtmlFilePath() {
		return htmlFilePath;
	}

	public String getHtmlSavedToPath() {
		return htmlSavedToPath;
	}

	public String getHtmlSource() {
		return htmlSource;
	}

	public String getImageFilePath() {
		return imageFilePath;
	}

	/**
	 * Get JS Error by JSErrorCollector which only supports Firefox browser.
	 * 
	 * @return jsErrors in format "line number, errorLogger message, source name; "
	 */
	public String getJSErrors() {
		if (WebUIDriver.getWebUXDriver().isAddJSErrorCollectorExtension()) {
			List<JavaScriptError> jsErrorList = JavaScriptError
					.readErrors(driver);
			if (!jsErrorList.isEmpty()) {
				String jsErrors = "";
				for (int i = 0; i < jsErrorList.size(); i++) {
					jsErrors += jsErrorList.get(i).getLineNumber() + ", "
							+ jsErrorList.get(i).getErrorMessage() + ", "
							+ jsErrorList.get(i).getSourceName() + "; ";
				}
				return jsErrors;
			}
		}
		return null;
	}

	public String getLocation() {
		return driver.getCurrentUrl();
	}

	public String getPopupWindowName() {
		return popupWindowName;
	}

	public int getTimeout() {
		return SeleniumTestsContextManager.getThreadContext().getWebSessionTimeout();
	}

	public String getTitle() {
		return driver.getTitle();
	}

	public String getUrl() {
		return url;
	}

	public String getWindowHandle() {
		return windowHandle;
	};

	public final void goBack() {
		TestLogging.logWebStep(null, "goBack", false);
		driver.navigate().back();
		frameFlag = false;
	}

	public final void goForward() {
		TestLogging.logWebStep(null, "goForward", false);
		driver.navigate().forward();
		frameFlag = false;
	}

    public final boolean isCookiePresent(String name) {
		return getCookieByName(name) != null;
	}

	public boolean isFrame() {
		return frameFlag;
	}

	public final void maximizeWindow() {
		new WebUtility(driver).maximizeWindow();
	}

	private void open(String url) throws Exception {

		if (this.getDriver() == null) {
			TestLogging.logWebStep(url, "Launch browser", false);
			driver = webUXDriver.createWebDriver();
		}

		setUrl(url);
		TestLogging.logWebStep(url, "Launch application URL: <a href='" + url + "'>" + url
                + "</a>", false);

		try {
			driver.navigate().to(url);
		} catch (UnreachableBrowserException e) {
			// handle if the last window is closed
			TestLogging.logWebStep(url, "Launch browser", false);
			driver = webUXDriver.createWebDriver();
			maximizeWindow();
			driver.navigate().to(url);
		} catch (UnsupportedCommandException e) {
			TestLogging.log("get UnsupportedCommandException, retry");
			driver = webUXDriver.createWebDriver();
			maximizeWindow();
			driver.navigate().to(url);
		} catch (org.openqa.selenium.TimeoutException ex) {
			TestLogging.log("got time out when loading " + url + ", ignored");
		} catch (org.openqa.selenium.UnhandledAlertException ex) {
			TestLogging.log("got UnhandledAlertException, retry");
			driver.navigate().to(url);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new CustomSeleniumTestsException(e);
		}
		switchToDefaultContent();
	}

	private void populateAndCapturePageSnapshot() {
		try {
			setTitle(driver.getTitle());

			/*
			 * IE driver will get timeout frequently when calling getText Logs:
			 * Line 10943: INFO | jvm 1 | 2013/06/30 17:57:56 | 17:57:56.514
			 * INFO - Executing: [find element: By.tagName: body] at URL:
			 * /session/2c3f9f68-d782-4162-9297-c628f31a50d2/element) Line
			 * 10944: INFO | jvm 1 | 2013/06/30 17:57:56 | 17:57:56.561 INFO -
			 * Done: /session/2c3f9f68-d782-4162-9297-c628f31a50d2/element Line
			 * 10945: INFO | jvm 1 | 2013/06/30 17:57:56 | 17:57:56.561 INFO -
			 * Executing: [get text: 6 org.openqa.selenium.support.events.
			 * EventFiringWebDriver$EventFiringWebElement@96812584] at URL:
			 * /session/2c3f9f68-d782-4162-9297-c628f31a50d2/element/6/text)
			 * Line 12020: INFO | jvm 1 | 2013/06/30 18:02:12 | 18:02:12.105
			 * WARN - Session 2c3f9f68-d782-4162-9297-c628f31a50d2 deleted due
			 * to in-browser timeout. Terminating driver with DeleteSession
			 * since it does not support Killable, the driver in question does
			 * not support selenium-server timeouts fully
			 * 
			 * so use htmlsource to replace gettext.
			 */
			htmlSource = driver.getPageSource();
			try{
				bodyText = driver.findElement(By.tagName("body")).getText();
			}catch(StaleElementReferenceException ignore){
				logger.warn("StaleElementReferenceException got in populateAndCapturePageSnapshot");
				bodyText = driver.findElement(By.tagName("body")).getText();
			}

		} catch (UnreachableBrowserException e) {// throw
													// UnreachableBrowserException
			throw new WebDriverException(e);
		}catch (WebDriverException e) {
			throw e;
		}

		capturePageSnapshot();
	}

	public final void refresh() throws NotCurrentPageException {
		TestLogging.logWebStep(null, "refresh", false);
		try {
			driver.navigate().refresh();
		} catch (org.openqa.selenium.TimeoutException ex) {
			TestLogging.log("got time out customexception, ignore");
		}
	}

	public final void resizeTo(int width, int height) {
		new WebUtility(driver).resizeWindow(width, height);
	}

    public final void selectFrame(int index) {
        TestLogging.logWebStep(null, "select frame using index" +index, false);
        driver.switchTo().frame(index);
        frameFlag = true;
    }

	public final void selectFrame(By by) {
		TestLogging.logWebStep(null, "select frame, locator={\"" + by.toString()
                + "\"}", false);
		driver.switchTo().frame(driver.findElement(by));
		frameFlag = true;
	}

	public final void selectFrame(String locator) {
		TestLogging.logWebStep(null, "select frame, locator={\"" + locator + "\"}",
                false);
		driver.switchTo().frame(locator);
		frameFlag = true;
	}

	public final void selectWindow() throws NotCurrentPageException {
		TestLogging.logWebStep(null, "select window, locator={\""
                + getPopupWindowName() + "\"}", false);
		// selectWindow(getPopupWindowName());
		driver.switchTo().window(
				(String) driver.getWindowHandles().toArray()[0]);
		waitForSeconds(1);

		// Check whether it's the expected page.
		assertCurrentPage(true);
	}

	public final void selectWindow(int index) throws NotCurrentPageException {
		TestLogging.logWebStep(null, "select window, locator={\"" + index + "\"}",
                false);
		driver.switchTo().window(
				(String) driver.getWindowHandles().toArray()[index]);
	}

	public final void selectNewWindow() throws NotCurrentPageException {
		TestLogging.logWebStep(null, "select new window", false);
		driver.switchTo().window(
				(String) driver.getWindowHandles().toArray()[1]);
		waitForSeconds(1);
	}

	protected void setHtmlSavedToPath(String htmlSavedToPath) {
		this.htmlSavedToPath = htmlSavedToPath;
	}

	protected void setTitle(String title) {
		this.title = title;
	}

	protected void setUrl(String openUrl) {
		this.url = openUrl;
	}

	public void switchToDefaultContent() {
		try {
			driver.switchTo().defaultContent();
		} catch (UnhandledAlertException e) {
		}
	}

	private final void waitForPageToLoad() throws Exception {
		try {
			new Wait() {
				@Override
				public boolean until() {
					try {
						driver.switchTo().defaultContent();
						return true;
					} catch (UnhandledAlertException ex) {
						WaitHelper.waitForSeconds(2);
					} catch (WebDriverException e) {
					}
					return false;
				}
			}.wait(String.format("Timed out waiting for page to load"),
					WebUIDriver.getWebUXDriver().getWebSessionTimeout());
		} catch (WaitTimedOutException ex) {

		}
		// populate page info
		try {
			populateAndCapturePageSnapshot();
		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		}
	}

	public WebElement getElement(By by, String elementName) {
		WebElement element = null;
		try {
			element = driver.findElement(by);
		} catch (ElementNotFoundException e) {
			TestLogging.errorLogger(elementName + " is not found with locator - "
                    + by.toString());
			throw e;
		}
		return element;
	}

	public String getElementUrl(By by, String name) {
		return getElement(by, name).getAttribute("href");
	}

	public String getElementText(By by, String name) {
		return getElement(by, name).getText();
	}

	public String getElementSrc(By by, String name) {
		return getElement(by, name).getAttribute("src");
	}
}
