
package com.seleniumtests.customexception;

public class CustomSeleniumTestsException extends Exception {
	
	private static final long serialVersionUID = -5567383832452234582L;

	public CustomSeleniumTestsException() {
	}

	public CustomSeleniumTestsException(String message) {
		super(message);
	}

	public CustomSeleniumTestsException(Throwable cause) {
		super(cause);
	}

	public CustomSeleniumTestsException(String message, Throwable cause) {
		super(message, cause);
	}

}
