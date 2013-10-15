package com.seleniumtests.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import com.seleniumtests.exception.WebSessionTerminatedException;

public class DriverExceptionListener implements WebDriverEventListener {
	public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {
	}

	public void afterClickOn(WebElement arg0, WebDriver arg1) {

	}

	public void afterFindBy(By arg0, WebElement arg1, WebDriver arg2) {

	}

	public void afterNavigateBack(WebDriver arg0) {
	}

	public void afterNavigateForward(WebDriver arg0) {

	}

	public void afterNavigateTo(String arg0, WebDriver arg1) {
	}

	public void afterScript(String arg0, WebDriver arg1) {
	}

	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1) {

	}

	public void beforeClickOn(WebElement arg0, WebDriver arg1) {

	}

	public void beforeFindBy(By arg0, WebElement arg1, WebDriver arg2) {

	}

	public void beforeNavigateBack(WebDriver arg0) {

	}

	public void beforeNavigateForward(WebDriver arg0) {

	}

	public void beforeNavigateTo(String arg0, WebDriver arg1) {
	}

	public void beforeScript(String arg0, WebDriver arg1) {

	}

	public void onException(Throwable ex, WebDriver arg1) {

		if (ex.getMessage() == null) {
			return;
		} else if (ex.getMessage().contains(
				"Element must be user-editable in order to clear it"))
			return;
		else if (ex.getMessage().contains("Element is not clickable at point"))
			return;
		else if (ex instanceof UnsupportedCommandException)
			return;
		else if (ex.getMessage().contains(" read-only"))
			return;
		else if (ex.getMessage().contains(
				"No response on ECMAScript evaluation command")) {// Opera
																	// exception
			for (int i = 0; i < ex.getStackTrace().length; i++) {
				String method = ex.getStackTrace()[i].getMethodName();
				if (method.contains("getTitle")
						|| method.contains("getWindowHandle")
						|| method.contains("click")
						|| method.contains("getPageSource")) {
					return;
				}
			}
			ex.printStackTrace();
			// return;
		} else if (ex
				.getMessage()
				.contains(
						"Error communicating with the remote browser. It may have died.")) {
			// Session has lost connection, remove it then ignore quit() method.
			if (WebUIDriver.getWebUXDriver().getConfig().getMode() == DriverMode.ExistingGrid) {
				WebUIDriver.setWebDriver(null);
				throw new WebSessionTerminatedException(ex);
			}
			return;
		} else if (ex instanceof org.openqa.selenium.remote.UnreachableBrowserException) {
			return;
		} else if (ex instanceof org.openqa.selenium.UnsupportedCommandException)
			return;
		else {
			String message = ex.getMessage().split("\\n")[0];
			System.out.println("Got exception:" + message);
			if (message.matches("Session (/S*) was terminated due to(.|\\n)*")
					|| message
							.matches("cannot forward the request Connection to(.|\\n)*")) {
				WebUIDriver.setWebDriver(null);// can't quit anymore, save time.
												// since the session was
												// terminated.
				throw new WebSessionTerminatedException(ex);
			}
		}
		for (int i = 0; i < ex.getStackTrace().length; i++)// avoid dead loop
		{
			String method = ex.getStackTrace()[i].getMethodName();
			if (method.contains("getScreenshotAs")
					|| method.contains("captureWebPageSnapshot")) {
				return;
			}
		}

		if (arg1 != null) {

			try {
				System.out.println("Got exception" + ex.getMessage());
				new ScreenshotUtil(arg1).capturePageSnapshotOnException();
			} catch (Exception e) {
				// Ignore all exceptions
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		String ex = "Session [92d68b1d-7375-404d-bc32-5633c552b1c0] was terminated due to BROWSER_TIMEOUT"

		;
		System.out.println(ex.split("\\n")[0]
				.matches("Session \\[(\\S)+\\] was terminated due to(.|\\n)*"));
	}
}
