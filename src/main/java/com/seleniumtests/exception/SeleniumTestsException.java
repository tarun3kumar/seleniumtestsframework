
package com.seleniumtests.exception;

public class SeleniumTestsException extends Exception {
	
	private static final long serialVersionUID = -2757992149932989353L;

	public SeleniumTestsException() {
	}

	public SeleniumTestsException(String message) {
		super(message);
	}

	public SeleniumTestsException(Throwable cause) {
		super(cause);
	}

	public SeleniumTestsException(String message, Throwable cause) {
		super(message, cause);
	}

}
