package com.seleniumtests.exception;

import com.seleniumtests.webelements.IPage;

public class WebResponseException extends RuntimeException {

	private static final long serialVersionUID = -5365732347362556776L;

	private IPage page = null;

	public WebResponseException() {
	}

	public WebResponseException(String message) {
		super(message);
	}

	public WebResponseException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebResponseException(String message, Throwable cause, IPage page) {
		super(message, cause);

		this.page = page;
	}

	public WebResponseException(Throwable cause) {
		super(cause);
	}

	public IPage getPage() {
		return page;
	}
}
