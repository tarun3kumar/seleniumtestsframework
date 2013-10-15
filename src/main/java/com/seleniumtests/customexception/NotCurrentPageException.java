package com.seleniumtests.customexception;

public class NotCurrentPageException extends CustomSeleniumTestsException {

	private static final long serialVersionUID = -5663838190837384823L;

	public NotCurrentPageException() {
	}

	public NotCurrentPageException(String message) {
		super(message);
	}

	public NotCurrentPageException(Throwable cause) {
		super(cause);
	}

	public NotCurrentPageException(String message, Throwable cause) {
		super(message, cause);
	}

}