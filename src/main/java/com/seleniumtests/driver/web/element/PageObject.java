package com.seleniumtests.driver.web.element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.seleniumemulation.Windows;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.testng.Assert;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.seleniumtests.controller.AbstractPageListener;
import com.seleniumtests.controller.Assertion;
import com.seleniumtests.controller.ContextManager;
import com.seleniumtests.controller.HTTPStatusCode;
import com.seleniumtests.controller.Logging;
import com.seleniumtests.driver.web.BaseWebUtil;
import com.seleniumtests.driver.web.ScreenShot;
import com.seleniumtests.driver.web.ScreenshotUtil;
import com.seleniumtests.driver.web.WebUXDriver;
import com.seleniumtests.exception.SeleniumTestsException;
import com.seleniumtests.exception.PageNotCurrentException;
import com.seleniumtests.exception.WebResponseException;
import com.seleniumtests.helper.OSHelper;
import com.seleniumtests.helper.ThreadHelper;
import com.seleniumtests.helper.URLHelper;
import com.thoughtworks.selenium.Wait;
import com.thoughtworks.selenium.Wait.WaitTimedOutException;

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
	// private String location = null;
	private String bodyText = null;
	private String htmlSource = null;
	private String htmlSavedToPath = null;
	private String rlogId = null;
	private String pageId = null;
	private String suiteName = null;
	private String outputDirectory = null;
	// private String testMethodSignature = null;
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
		
		if (ContextManager.getGlobalContext() != null
				&& ContextManager.getGlobalContext().getTestNGContext() != null) {
			suiteName = ContextManager.getGlobalContext().getTestNGContext()
					.getSuite().getName();
			outputDirectory = ContextManager.getGlobalContext()
					.getTestNGContext().getOutputDirectory();
		}

		this.pageIdentifierElement = pageIdentifierElement;
		this.popupFlag = isPopup;
		this.popupWindowName = popupWindowName;

		driver = WebUXDriver.getWebDriver();

		if (!popupFlag) {

			// Open web page
			if (url != null) {
				open(url, convert);
			}

			// Wait For Page Load
			waitForPageToLoad();

		} else {
			Logging.logWebStep(null, "wait for popup page to load.", false);
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

		// Take care of redirecting pages like SignIn
		int count = 0;
		boolean pageNotCurrent = true;
		do {
			count++;
			try {
				// Check for Page errors
				assertNoPageErrors();
				pageNotCurrent = false;
			} catch (PageNotCurrentException e) {
				if (!waitForRedirection
						|| count >= MAX_WAIT_TIME_FOR_REDIRECTION)
					throw e;
				waitForSeconds(1);
				waitForPageToLoad();
			}
		} while (waitForRedirection && pageNotCurrent
				&& count < MAX_WAIT_TIME_FOR_REDIRECTION);

		AbstractPageListener.notifyPageLoad(this);
		Calendar end = Calendar.getInstance();
		start.setTime(new Date());
		long startTime = start.getTimeInMillis();
		long endTime = end.getTimeInMillis();
		if((endTime-startTime)/1000>0)
			Logging.log("Open web page in :"+(endTime-startTime)/1000+"seconds");		
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

	// ///////////////// Getters & Setters End
	// ///////////////////////////////////////

	/**
	 * Constructor for Entry point page.
	 * 
	 * <pre>
	 * WARNING!!! WARNING!!! WARNING!!! 
	 * Should only be used in following cases
	 * 1) Use this only with v3console adminURLs for 
	 *    which feature pools point to staging.
	 * 2) Tools like DCT, QATools
	 * </pre>
	 * 
	 * @param pageIdentifierElement
	 * @param url
	 * @param isAbsoluteURL
	 * @throws Exception
	 */
	public PageObject(HtmlElement pageIdentifierElement, String url,
                      boolean isAbsoluteURL, boolean waitForRedirection) throws Exception {

		this(false, null, pageIdentifierElement, url, !isAbsoluteURL,
				waitForRedirection);
	}

	/**
	 * Constructor for Entry point page. Null PageIdentifier Element, always
	 * convert
	 * 
	 * @param url
	 * @throws Exception
	 */
	public PageObject(String url) throws Exception {
		this(false, null, null, url, false, false);
	}

	/**
	 * Constructor for Entry point page. Null PageIdentifier Element, not
	 * convert if convert=false
	 * 
	 * @param url
	 * @param convert
	 * @throws Exception
	 */
	public PageObject(String url, boolean convert) throws Exception {
		this(false, null, null, url, convert, false);
	}

	public void assertCookiePresent(String name) {
		Logging.logWebStep(null, "assert cookie " + name + " is present.",
				false);
		assertHTML(getCookieByName(name) != null, "Cookie: {" + name
				+ "} not found.");
	}

	@Override
	protected void assertCurrentPage(boolean log)
			throws PageNotCurrentException {

		if (pageIdentifierElement == null) {

		} else if (this.isElementPresent(pageIdentifierElement.getBy())) {

		} else {
			try {
				if (!ContextManager.getThreadContext().getCaptureSnapshot())
					new ScreenshotUtil(driver).capturePageSnapshotOnException();
			} catch (Exception e) {
				// Ignore all exceptions
				e.printStackTrace();
			}
			throw new PageNotCurrentException(getClass().getCanonicalName()
					+ " is not the current page.\nPageIdentifierElement "
					+ pageIdentifierElement.toString() + " is not found.");
		}

		if (log)
			Logging.logWebStep(
					null,
					"assert \""
							+ getClass().getSimpleName()
							+ "\" is the current page"
							+ (pageIdentifierElement != null ? " (assert PageIdentifierElement "
									+ pageIdentifierElement.toHTML()
									+ " is present)."
									: "."), false);
	}

	// ////////////////// assertion methods //////////////////////////

	public void assertHtmlSource(String text) {
		Logging.logWebStep(null, "assert text \"" + text
				+ "\" is present in page source.", false);
		assertHTML(getHtmlSource().contains(text), "Text: {" + text
				+ "} not found on page source.");
	}

	public void assertKeywordNotPresent(String text) {
		Logging.logWebStep(null, "assert text \"" + text
				+ "\" is present in page source.", false);
		Assert.assertFalse(getHtmlSource().contains(text), "Text: {" + text
				+ "} not found on page source.");

	}

	public void assertLocation(String urlPattern) {
		Logging.logWebStep(null, "assert location \"" + urlPattern + "\".",
				false);
		assertHTML(getLocation().contains(urlPattern), "Pattern: {"
				+ urlPattern + "} not found on page location.");
	}

	/**
	 * Check HTTP Status Code Errors. This is going be tricky. Selenium does not
	 * provide us with HTTP Response codes. So have to rely on the DOM content
	 * to check for Errors. For example: QA PNR Page If page error, take error
	 * snapshot and throw exception
	 * 
	 * @throws PageNotCurrentException
	 */
	public final void assertNoPageErrors() throws PageNotCurrentException {

		for (HTTPStatusCode statusCode : HTTPStatusCode.values()) {
			if (getBodyText() != null
					&& getBodyText().toLowerCase().contains(
							statusCode.getTitle().toLowerCase())) {
				throw new WebResponseException(statusCode.toString()
						+ " found.", null, this);
			}
		}

		// Check whether it's the expected page.
		assertCurrentPage(true);
	}

	public void assertPageSectionPresent(WebPageSection pageSection) {
		Logging.logWebStep(null,
				"assert pagesection \"" + pageSection.getName()
						+ "\"  is present.", false);
		assertElementPresent(new HtmlElement(pageSection.getName(),
				pageSection.getBy()));
	}

	public void assertTitle(String text) {
		Logging.logWebStep(null, "assert text \"" + text
				+ "\"  is present on title.", false);
		assertHTML(getTitle().contains(text), "Text: {" + text
				+ "} not found on page title.");

	}

	public void capturePageSnapshot() {
		ScreenShot screenShot = new ScreenshotUtil(driver)
				.captureWebPageSnapshot();
		this.title = screenShot.getTitle();
		this.rlogId = screenShot.getRlogId();

		if (screenShot.getHtmlSourcePath() != null) {
			htmlFilePath = screenShot.getHtmlSourcePath().replace(suiteName,
					outputDirectory);
			htmlSavedToPath = screenShot.getHtmlSourcePath();
		}
		if (screenShot.getImagePath() != null) {
			imageFilePath = screenShot.getImagePath().replace(suiteName,
					outputDirectory);
		}
		Logging.logWebOutput(url,
				title + " (" + Logging.buildScreenshotLog(screenShot) + ")",
				false);

	}

	public final void close() throws PageNotCurrentException {
		if (WebUXDriver.getWebDriver() == null) {
			return;
		}
		AbstractPageListener.notifyPageUnload(this);
		Logging.log("close web page");
		boolean isMultipleWindow = false;
		if (driver.getWindowHandles().size() > 1)
			isMultipleWindow = true;
		try{
			driver.close();
		}catch(WebDriverException ignore){
		}
		if (WebUXDriver.getWebUXDriver().getMode()
				.equalsIgnoreCase("LocallyOnRC")) {
			try {
				Thread.sleep(1000 * 2);
			} catch (InterruptedException e) {
				// Ignore
			}
		}
		try {
			if (isMultipleWindow)
				this.selectWindow();
			else
				WebUXDriver.setWebDriver(null);
		} catch (UnreachableBrowserException ex) {
			WebUXDriver.setWebDriver(null);

		}
	}

	/*public String convert(String url) {
		if (ContextManager.getThreadContext() != null) {
			String urlConvertClass = ContextManager.getThreadContext()
					.getUrlConvertClass();
			if (urlConvertClass != null) {
				try {
					Class<?> converterClass = Class.forName(urlConvertClass);
					Object converter = converterClass.newInstance();
					Method convert = converterClass.getMethod("convertURL",
							String.class);
					return (String) convert.invoke(converter, url);
				} catch (Exception e) {
					logger.warn("Convert URL failed", e);
				}

			}
		}
		return url;
	}*/

	/**
	 * Download a file to test-output\**\downloads\ folder regardless
	 * of local or GRID
	 * 
	 * @param fileName
	 * @return full path of download file
	 * @throws SeleniumTestsException
	 */
	public final String downloadFile(By by, String fileName)
			throws SeleniumTestsException {
		return downloadFile(driver.findElement(by).getAttribute("href"),
				fileName);
	}

	/**
	 * Download a file to test-output\**\downloads\ folder regardless
	 * of local or GRID
	 * 
	 * @return full path of download file
	 * @throws SeleniumTestsException
	 */
	public final String downloadFile(String locatorOrDownloadUrl,
			String fileNamePostFix) throws SeleniumTestsException {
		if (locatorOrDownloadUrl.startsWith("http://")
				|| locatorOrDownloadUrl.startsWith("https://")) {
			// direct download
			try {
//				locatorOrDownloadUrl = convert(locatorOrDownloadUrl);
			} catch (Exception e) {

			}
			/*
			HtmlUnitDriver htmlUnitdriver = new HtmlUnitDriver();
			htmlUnitdriver.get(locatorOrDownloadUrl);
			String fileAsString = htmlUnitdriver.getPageSource();*/
			// String fileAsString =
			// session.downloadFileToString(locatorOrDownloadUrl, fileName);
			/*fileAsString = trimOKPrefix(fileAsString);
			byte[] byteArray = fileAsString.getBytes();
			return saveDownloadedFile(byteArray, fileName);*/
			InputStream inputStream;
			StringBuffer sbMessage = new StringBuffer("");
			String fileName = URLHelper.getRandomHashCode("download") + "-"
					+ fileNamePostFix;
			try {
				inputStream = URLHelper.openAsStream(locatorOrDownloadUrl);
			
			String fileType = url.substring(url.lastIndexOf(".") + 1);
			
			if (fileType != null && fileType.length() < 5)
				fileName = fileName + "." + fileType;
			String outputDirectory = ContextManager.getGlobalContext()
					.getTestNGContext().getOutputDirectory();
			String suiteName = ContextManager.getGlobalContext().getTestNGContext()
					.getSuite().getName();
			

				String downloadDir = outputDirectory + "/downloads/";
				File d = new File(downloadDir);
				if (!d.exists()) {
					d.mkdirs();
				}

				OutputStream out = new FileOutputStream(downloadDir + fileName);
				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = inputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				out.close();

				sbMessage.append("Downloading file <a href='" + suiteName
						+ "/downloads/" + fileName + " title='" + fileName + "'>"
						+ fileNamePostFix + "</a>");
			} catch (Throwable e) {
				logger.warn("Ex", e);
			}

			Logging.logWebOutput(url, sbMessage.toString(), false);
			fileName = outputDirectory + "/downloads/" + fileName;
			// correct file path format
			if (OSHelper.isWindows())
				fileName = fileName.replaceAll("/", "\\\\");

			return fileName;
			
		} else {
			throw new SeleniumTestsException("url not starts with http:// or https://");
		}

	}
	
	public static String saveFile(String url) throws Exception {
		// url = URLHelper.encode(url);
		String fileNamePostFix = "save";
		InputStream inputStream = URLHelper.openAsStream(url);
		String fileType = url.substring(url.lastIndexOf(".") + 1);
		StringBuffer sbMessage = new StringBuffer("");
		String fileName = URLHelper.getRandomHashCode("download") + "-"
				+ fileNamePostFix;
		if (fileType != null && fileType.length() < 5)
			fileName = fileName + "." + fileType;
		String outputDirectory = ContextManager.getGlobalContext()
				.getTestNGContext().getOutputDirectory();
		String suiteName = ContextManager.getGlobalContext().getTestNGContext()
				.getSuite().getName();
		try {

			String downloadDir = outputDirectory + "/downloads/";
			File d = new File(downloadDir);
			if (!d.exists()) {
				d.mkdirs();
			}

			OutputStream out = new FileOutputStream(downloadDir + fileName);
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.close();

			sbMessage.append("Downloading file <a href='" + suiteName
					+ "/downloads/" + fileName + " title='" + fileName + "'>"
					+ fileNamePostFix + "</a>");
		} catch (Throwable e) {
			logger.warn("Ex", e);
		}

		Logging.logWebOutput(url, sbMessage.toString(), false);
		fileName = outputDirectory + "/downloads/" + fileName;
		// correct file path format
		if (OSHelper.isWindows())
			fileName = fileName.replaceAll("/", "\\\\");

		return fileName;
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
		Logging.logWebStep(null, "dragAndDrop " + element.toHTML()
				+ " to offset(x,y): (" + offsetX + "," + offsetY + ")", false);
		element.captureSnapshot("before draging");

		new Actions(driver).dragAndDropBy((WebElement) element.getElement(),
				offsetX, offsetY).perform();
		element.captureSnapshot("after dropping");
	}

	// ////////////////////// Drag-n-Drop Methods ////////////////////////////

	public String getBodyText() {
		// return driver.findElement(By.tagName("body")).getText();
		return bodyText;
	}

	// ////////////////////// Flash Enable/Disable Methods ////////////

	public final String getCookieByName(String name) {
		if (driver.manage().getCookieNamed(name) == null)
			return null;
		return driver.manage().getCookieNamed(name).getValue();
	}

	public final int getElementCount(HtmlElement element) throws SeleniumTestsException {
		return driver.findElements(element.getBy()).size();
	}

	// add for avoid compile error for migration
	public String getEval(String expression) {
		Assertion.assertTrue(false, "focus not implemented yet");
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
	 * @return jsErrors in format "line number, error message, source name; "
	 */
	public String getJSErrors() {
		// return (String) ((JavascriptExecutor)
		// driver).executeScript("window.ecaf_js_error");
		if (WebUXDriver.getWebUXDriver().isAddJSErrorCollectorExtension()) {
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

	public String getPageId() {
		return pageId;
	}

	public String getPopupWindowName() {
		return popupWindowName;
	}

	public String getRlogId() {
		return rlogId;
	}

	public int getTimeout() {
		return ContextManager.getThreadContext().getWebSessionTimeout();
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
		Logging.logWebStep(null, "goBack", false);
		driver.navigate().back();
		frameFlag = false;
	}

	public final void goForward() {
		Logging.logWebStep(null, "goForward", false);
		driver.navigate().forward();
		frameFlag = false;
	}

	public void initWebUXElements() {
		// Element will be initialized right before using, so no need additional
		// steps
		/*
		 * try{ PageFactory.initElements(driver, this); }catch(Exception e) {
		 * //ignore exception } driver = WebUXDriver.getWebDriver(); Field[]
		 * fields = this.getClass().getDeclaredFields(); for (Field field :
		 * fields) { //System.out.println(field.getName());
		 * 
		 * try { if (field.get(this) instanceof HtmlElement) { Method method =
		 * HtmlElement.class.getMethod("init"); method.invoke(field.get(this));
		 * 
		 * }else if(field.get(this) instanceof SelectList) { Method method =
		 * SelectList.class.getMethod("init"); method.invoke(field.get(this)); }
		 * } catch (Exception e) { }
		 * 
		 * }
		 */

	}

	public final boolean isCookiePresent(String name) {
		return getCookieByName(name) != null;
	}

	public boolean isFrame() {
		return frameFlag;
	}

	public final void maximizeWindow() {
		new BaseWebUtil(driver).maximizeWindow();
	}

	public void open(String url) throws Exception {
		open(url, true);
	}

	private void open(String url, boolean convert) throws Exception {

		if (this.getDriver() == null) {
			Logging.logWebStep(url, "Launch browser", false);
			driver = webUXDriver.createWebDriver();
			//maximizeWindow();
		}

		if (convert) {
//			String tempURL = convert(url);
//			url = tempURL;
		}
		setUrl(url);
		Logging.logWebStep(url, "Launch application URL: <a href='" + url + "'>" + url
				+ "</a>", false);

		try {
			driver.navigate().to(url);
		} catch (UnreachableBrowserException e) {
			// handle if the last window is closed
			Logging.logWebStep(url, "Launch browser", false);
			driver = webUXDriver.createWebDriver();
			maximizeWindow();
			driver.navigate().to(url);
		} catch (UnsupportedCommandException e) {
			Logging.log("get UnsupportedCommandException, retry");
			driver = webUXDriver.createWebDriver();
			maximizeWindow();
			driver.navigate().to(url);
		} catch (org.openqa.selenium.TimeoutException ex) {
			Logging.log("got time out when loading " + url + ", ignored");
		} catch (org.openqa.selenium.UnhandledAlertException ex) {
			Logging.log("got UnhandledAlertException, retry");
			driver.navigate().to(url);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new SeleniumTestsException(e);
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

	public final void refresh() throws PageNotCurrentException {
		Logging.logWebStep(null, "refresh", false);
		try {
			driver.navigate().refresh();
		} catch (org.openqa.selenium.TimeoutException ex) {
			Logging.log("got time out exception, ignore");
		}

		frameFlag = false;

		try {
			waitForPageToLoad();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		assertNoPageErrors();
	}

	public final void resizeTo(int width, int height) {
		new BaseWebUtil(driver).resizeWindow(width, height);
	}

	public final void selectFrame(By by) {
		Logging.logWebStep(null, "select frame, locator={\"" + by.toString()
				+ "\"}", false);
		driver.switchTo().frame(driver.findElement(by));
		frameFlag = true;
	}

	public final void selectFrame(String locator) {
		Logging.logWebStep(null, "select frame, locator={\"" + locator + "\"}",
				false);
		driver.switchTo().frame(locator);
		frameFlag = true;
	}

	public final void selectWindow() throws PageNotCurrentException {
		Logging.logWebStep(null, "select window, locator={\""
				+ getPopupWindowName() + "\"}", false);
		// selectWindow(getPopupWindowName());
		driver.switchTo().window(
				(String) driver.getWindowHandles().toArray()[0]);
		waitForSeconds(1);

		// Check whether it's the expected page.
		assertCurrentPage(true);
	}

	public final void selectWindow(int index) throws PageNotCurrentException {
		Logging.logWebStep(null, "select window, locator={\"" + index + "\"}",
				false);
		driver.switchTo().window(
				(String) driver.getWindowHandles().toArray()[index]);
	}

	public final void selectNewWindow() throws PageNotCurrentException {
		Logging.logWebStep(null, "select new window", false);
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
						ThreadHelper.waitForSeconds(2);
					} catch (WebDriverException e) {
					}
					return false;
				}
			}.wait(String.format("Timed out waiting for page to load"),
					WebUXDriver.getWebUXDriver().getWebSessionTimeout());
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
			Logging.error(elementName + " is not found with locator - "
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
