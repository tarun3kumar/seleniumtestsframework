package com.seleniumtests.driver.web.element;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.seleniumemulation.JavascriptLibrary;
import org.openqa.selenium.internal.seleniumemulation.Windows;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.seleniumtests.controller.Assertion;
import com.seleniumtests.controller.ContextManager;
import com.seleniumtests.controller.Keyword;
import com.seleniumtests.controller.Logging;
import com.seleniumtests.driver.web.BrowserType;
import com.seleniumtests.driver.web.WebUXDriver;
import com.seleniumtests.exception.PageNotCurrentException;
import com.seleniumtests.helper.ThreadHelper;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;

/**
 * Base html page abstraction. Used by WebPage and WebPageSection
 * 
 * 
 */
public abstract class BaseHtmlPage {

	protected WebDriver driver = WebUXDriver.getWebDriver();
	protected final WebUXDriver webUXDriver = WebUXDriver.getWebUXDriver();
	private int explictWaitTimeout = WebUXDriver.getWebUXDriver()
			.getExplicitWait();
	private int sessionTimeout = WebUXDriver.getWebUXDriver()
			.getWebSessionTimeout();

	public BaseHtmlPage() {
	}

	public void acceptAlert() throws PageNotCurrentException {
		Alert alert = driver.switchTo().alert();
		alert.accept();
		driver.switchTo().defaultContent();
	}

	public void assertAlertPresent() {
		Logging.logWebStep(null, "assert alert present.", false);
		try {
			driver.switchTo().alert();
		} catch (Exception ex) {
			// AlertOverride alertOverride = new AlertOverride();
			// assertHTML(alertOverride.isAlertPresent(driver),
			// "assert alert present.");
			assertAlertHTML(false, "assert alert present.");
		}
	}

	public void assertAlertText(String text) {
		Logging.logWebStep(null, "assert alert text.", false);
		Alert alert = driver.switchTo().alert();
		String alertText = alert.getText();
		assertAlertHTML(alertText.contains(text), "assert alert text.");
	}

	/**
	 * Assert to check attribute value. Just pass in the attribute name and
	 * expected value.
	 * 
	 * @param element
	 * @param attributeName
	 * @param value
	 */
	public void assertAttribute(HtmlElement element, String attributeName,
			String value) {
		Logging.logWebStep(null, "assert " + element.toHTML() + " attribute = "
				+ attributeName + ", expectedValue ={" + value + "}.", false);

		String attributeValue = element.getAttribute(attributeName);

		assertHTML(value != null && value.equals(attributeValue),
				element.toString() + " attribute = " + attributeName
						+ ", expectedValue = {" + value + "}"
						+ ", attributeValue = {" + attributeValue + "}");
	}

	public void assertAttributeContains(HtmlElement element,
			String attributeName, String keyword) {
		Logging.logWebStep(null, "assert " + element.toHTML() + " attribute="
				+ attributeName + ", contains keyword = {" + keyword + "}.",
				false);

		String attributeValue = element.getAttribute(attributeName);

		assertHTML(
				attributeValue != null && keyword != null
						&& attributeValue.contains(keyword), element.toString()
						+ " attribute=" + attributeName
						+ ", expected to contains keyword {" + keyword + "}"
						+ ", attributeValue = {" + attributeValue + "}");
	}

	public void assertAttributeMatches(HtmlElement element,
			String attributeName, String regex) {
		Logging.logWebStep(null, "assert " + element.toHTML() + " attribute="
				+ attributeName + ", matches regex = {" + regex + "}.", false);

		String attributeValue = element.getAttribute(attributeName);

		assertHTML(
				attributeValue != null && regex != null
						&& attributeValue.matches(regex), element.toString()
						+ " attribute=" + attributeName
						+ " expected to match regex {" + regex + "}"
						+ ", attributeValue = {" + attributeValue + "}");
	}

	public void assertConfirmationText(String text) {
		Logging.logWebStep(null, "assert confirmation text.", false);
		Alert alert = driver.switchTo().alert();
		String seenText = alert.getText();

		assertAlertHTML(seenText.contains(text), "assert confirmation text.");
	}

	protected void assertCurrentPage(boolean log)
			throws PageNotCurrentException {
		// do nothing
	}

	public void assertElementNotPresent(HtmlElement element) {
		Logging.logWebStep(null, "assert " + element.toHTML()
				+ " is not present.", false);
		assertHTML(!element.isElementPresent(), element.toString() + " found.");
	}

	public void assertElementPresent(HtmlElement element) {
		Logging.logWebStep(null, "assert " + element.toHTML() + " is present.",
				false);
		assertHTML(element.isElementPresent(), element.toString()
				+ " not found.");
	}

	void assertHTML(boolean condiition, String message) {
		if (!condiition) {
			capturePageSnapshot();
			Assertion.assertTrue(condiition, message);
		}
	}
	
	void assertAlertHTML(boolean condiition, String message) {
		if (!condiition) {
			Assertion.assertTrue(condiition, message);
		}
	}

	@Deprecated
	public void assertLabelNotPresent(Label label) {
		Logging.logWebStep(null, "assert " + label.toHTML()
				+ " is not present.", false);
		assertHTML(!isTextPresent(getKeywordValue(label.getLabel())),
				"Label: {" + label.getExpectedText() + "} found.");
	}

	@Deprecated
	public void assertLabelPresent(Label label) {
		Logging.logWebStep(null, "assert " + label.toHTML() + " is present.",
				false);
		assertHTML(isTextPresent(getKeywordValue(label.getLabel())), "Label: {"
				+ label.getExpectedText() + "} not found.");
	}

	public void assertPromptText(String text) {
		Logging.logWebStep(null, "assert prompt text.", false);
		Alert alert = driver.switchTo().alert();
		String seenText = alert.getText();
		assertAlertHTML(seenText.contains(text), "assert prompt text.");
	}

	public void assertTable(Table table, int row, int col, String text) {
		Logging.logWebStep(null,
				"assert text \"" + text + "\" equals " + table.toHTML()
						+ " at (row, col) = (" + row + ", " + col + ").", false);
		String content = table.getContent(row, col);
		assertHTML(content != null && content.equals(text), "Text= {" + text
				+ "} not found on " + table.toString()
				+ " at cell(row, col) = {" + row + "," + col + "}");
	}

	public void assertTableContains(Table table, int row, int col, String text) {
		Logging.logWebStep(null, "assert text \"" + text + "\" contains "
				+ table.toHTML() + " at (row, col) = (" + row + ", " + col
				+ ").", false);
		String content = table.getContent(row, col);
		assertHTML(content != null && content.contains(text), "Text= {" + text
				+ "} not found on " + table.toString()
				+ " at cell(row, col) = {" + row + "," + col + "}");
	}

	public void assertTableMatches(Table table, int row, int col, String text) {
		Logging.logWebStep(null, "assert text \"" + text + "\" matches "
				+ table.toHTML() + " at (row, col) = (" + row + ", " + col
				+ ").", false);
		String content = table.getContent(row, col);
		assertHTML(content != null && content.matches(text), "Text= {" + text
				+ "} not found on " + table.toString()
				+ " at cell(row, col) = {" + row + "," + col + "}");
	}

	// ////////////////// assertion methods //////////////////////////

	public void assertTextNotPresent(String text) {
		Logging.logWebStep(null,
				"assert text \"" + text + "\" is not present.", false);
		assertHTML(!isTextPresent(text), "Text= {" + text + "} found.");
	}

	public void assertTextNotPresentIgnoreCase(String text) {
		Logging.logWebStep(null, "assert text \"" + text
				+ "\" is not present.(ignore case)", false);
		assertHTML(!getBodyText().toLowerCase().contains(text.toLowerCase()),
				"Text= {" + text + "} found.");
	}

	public void assertTextPresent(String text) {
		Logging.logWebStep(null, "assert text \"" + text + "\" is present.",
				false);
		assertHTML(isTextPresent(text), "Text= {" + text + "} not found.");
	}

	public void assertTextPresentIgnoreCase(String text) {
		Logging.logWebStep(null, "assert text \"" + text
				+ "\" is present.(ignore case)", false);
		assertHTML(getBodyText().toLowerCase().contains(text.toLowerCase()),
				"Text= {" + text + "} not found.");
	}

	public String cancelConfirmation() throws PageNotCurrentException {
		Alert alert = driver.switchTo().alert();
		String seenText = alert.getText();
		alert.dismiss();
		driver.switchTo().defaultContent();
		return seenText;
	}

	protected abstract void capturePageSnapshot();

	// ////////////////// get dialog methods //////////////////////////
	public Alert getAlert() {
		Alert alert = driver.switchTo().alert();
		return alert;
	}

	public String getAlertText() {
		Alert alert = driver.switchTo().alert();
		String seenText = alert.getText();

		return seenText;
	}

	private String getBodyText() {
		WebElement body = driver.findElement(By.tagName("body"));
		return body.getText();
	}

	public String getConfirmation() {
		Alert alert = driver.switchTo().alert();
		String seenText = alert.getText();

		return seenText;
	}

	public WebDriver getDriver() {
		driver = WebUXDriver.getWebDriver();
		return driver;

	}

	public String getKeywordValue(String key) {
		String site = null;
		if (ContextManager.getThreadContext().getGBHSite() != null) {
			site = ContextManager.getThreadContext().getGBHSite();
		} else {
			site = ContextManager.getThreadContext().getSite();
		}
		return Keyword.get(this.getClass(), key, site);
	}

	// ////////////////////// Wait Methods ////////////////////////////

	public String getPrompt() {
		Alert alert = driver.switchTo().alert();
		String seenText = alert.getText();

		return seenText;
	}

	public boolean isElementPresent(By by) {
		int count = 0;
		try {
			count = WebUXDriver.getWebDriver().findElements(by).size();
		} catch (RuntimeException e) {
			if ((e instanceof InvalidSelectorException)
					|| (e.getMessage() != null && e
							.getMessage()
							.contains(
									"TransformedEntriesMap cannot be cast to java.util.List"))) {
				Logging.log("InvalidSelectorException or CastException got, retry");
				ThreadHelper.waitForSeconds(2);
				count = WebUXDriver.getWebDriver().findElements(by).size();
			} else
				throw e;
		}
		if (count == 0)
			return false;
		return true;

	}

	public boolean isFrame() {
		return false;
	}

	public boolean isTextPresent(String text) {
		Assertion
				.assertNotNull(text, "isTextPresent: text should not be null!");
		driver = WebUXDriver.getWebDriver();
		WebElement body = driver.findElement(By.tagName("body"));

		if (WebUXDriver.getWebUXDriver().getBrowser()
				.equalsIgnoreCase(BrowserType.HtmlUnit.getType())) {
			return body.getText().contains(text);
		}
		Boolean result = false;

		if (body.getText().contains(text))
			return true;

		JavascriptLibrary js = new JavascriptLibrary();
		String script = js.getSeleniumScript("isTextPresent.js");

		result = (Boolean) ((JavascriptExecutor) driver).executeScript(
				"return (" + script + ")(arguments[0]);", text);

		// Handle the null case
		return Boolean.TRUE == result;
	}

	public void selectFrame(By by) {
		Logging.logWebStep(null, "select frame, locator={\"" + by.toString()
				+ "\"}", false);
		driver.switchTo().frame(driver.findElement(by));
	}

	/**
	 * If current window is closed, you can't use this method to swith to
	 * another window. Please use driver.switchTo.window(handle) to handle this
	 * situation. You need to capture the handle you need to switch before this
	 * statement.
	 * 
	 * @param windowName
	 * @throws PageNotCurrentException
	 */
	public final void selectWindow(String windowName)
			throws PageNotCurrentException {
		if (windowName == null) {
			Windows windows = new Windows(driver);
			try {
				windows.selectBlankWindow(driver);
			} catch (SeleniumException e) {
				driver.switchTo().defaultContent();
			}

		} else {
			Windows windows = new Windows(driver);
			windows.selectWindow(driver, "name=" + windowName);
		}
		waitForSeconds(1);

		// Check whether it's the expected page.
		// assertCurrentPage(true);
	}

	public void waitForElementChecked(HtmlElement element) {
		Assert.assertNotNull(element, "Element can't be null");
		Logging.logWebStep(null, "wait for " + element.toString()
				+ " to be checked.", false);
		WebDriverWait wait = new WebDriverWait(driver, explictWaitTimeout);
		wait.until(ExpectedConditions.elementToBeSelected(element.getBy()));
	}

	public void waitForElementEditable(HtmlElement element) {
		Assert.assertNotNull(element, "Element can't be null");
		Logging.logWebStep(null, "wait for " + element.toString()
				+ " to be editable.", false);
		WebDriverWait wait = new WebDriverWait(driver, explictWaitTimeout);
		wait.until(ExpectedConditions.elementToBeClickable(element.getBy()));
	}

	public void waitForElementPresent(By by) {
		Logging.logWebStep(null, "wait for " + by.toString()
				+ " to be present.", false);
		WebDriverWait wait = new WebDriverWait(driver, explictWaitTimeout);
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
	}

	public void waitForElementPresent(By by, int timeout) {
		Logging.logWebStep(null, "wait for " + by.toString()
				+ " to be present.", false);
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
	}

	public void waitForElementPresent(HtmlElement element) {
		Assert.assertNotNull(element, "Element can't be null");
		Logging.logWebStep(null, "wait for " + element.toString()
				+ " to be present.", false);
		WebDriverWait wait = new WebDriverWait(driver, explictWaitTimeout);
		wait.until(ExpectedConditions.presenceOfElementLocated(element.getBy()));
	}

	public void waitForElementToBeVisible(HtmlElement element) {
		Assert.assertNotNull(element, "Element can't be null");
		Logging.logWebStep(null, "wait for " + element.toString()
				+ " to be visible.", false);
		WebDriverWait wait = new WebDriverWait(driver, explictWaitTimeout);
		wait.until(ExpectedConditions.visibilityOfElementLocated(element
				.getBy()));
	}

	public void waitForElementToDisappear(HtmlElement element) {
		Assert.assertNotNull(element, "Element can't be null");
		Logging.logWebStep(null, "wait for " + element.toString()
				+ " to disappear.", false);
		WebDriverWait wait = new WebDriverWait(driver, explictWaitTimeout);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(element
				.getBy()));
	}

	public void waitForPopup(String locator) throws Exception {
		waitForPopUp(locator, sessionTimeout + "");
	}

	public void waitForPopUp(final String windowID, String timeout) {
		final long millis = Long.parseLong(timeout);
		final String current = driver.getWindowHandle();
		final Windows windows = new Windows(driver);

		if (webUXDriver.getConfig().getBrowser() == BrowserType.InternetExplore)
			waitForSeconds(3);

		new Wait() {
			@Override
			public boolean until() {
				try {
					if ("_blank".equals(windowID)) {
						windows.selectBlankWindow(driver);
					} else {
						driver.switchTo().window(windowID);
					}
					return !"about:blank".equals(driver.getCurrentUrl());
				} catch (SeleniumException e) {
					// Swallow
				} catch (NoSuchWindowException e) {
				}
				return false;
			}
		}.wait(String.format("Timed out waiting for %s. Waited %s", windowID,
				timeout), millis);

		driver.switchTo().window(current);

	}

	/**
	 * Wait For seconds. Provide a value less than WebSessionTimeout i.e. 180
	 * Seconds
	 * 
	 * @param seconds
	 */
	protected void waitForSeconds(int seconds) {
		ThreadHelper.waitForSeconds(seconds);
	}

	public void waitForTextPresent(HtmlElement element, String text) {
		Assert.assertNotNull(text, "Text can't be null");
		Logging.logWebStep(null, "wait for text \"" + text
				+ "\" to be present.", false);
		WebDriverWait wait = new WebDriverWait(driver, explictWaitTimeout);
		wait.until(ExpectedConditions.textToBePresentInElement(element.getBy(),
				text));
	}

	public void waitForTextPresent(String text) {
		Assert.assertNotNull(text, "Text can't be null");
		Logging.logWebStep(null, "wait for text \"" + text
				+ "\" to be present.", false);
		boolean b = false;
		for (int millisec = 0; millisec < explictWaitTimeout * 1000; millisec += 1000) {
			try {
				if ((isTextPresent(text))) {
					b = true;
					break;
				}
			} catch (Exception ignore) {
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		assertHTML(b, "Timed out waiting for text \"" + text
				+ "\" to be there.");
	}

	public void waitForTextToDisappear(String text) {
		Assert.assertNotNull(text, "Text can't be null");
		Logging.logWebStep(null,
				"wait for text \"" + text + "\" to disappear.", false);
		boolean textPresent = true;
		for (int millisec = 0; millisec < explictWaitTimeout * 1000; millisec += 1000) {
			try {
				if (!(isTextPresent(text))) {
					textPresent = false;
					break;
				}
			} catch (Exception ignore) {
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		assertHTML(!textPresent, "Timed out waiting for text \"" + text
				+ "\" to be gone.");
	}
}
