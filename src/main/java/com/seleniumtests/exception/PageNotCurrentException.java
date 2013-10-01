package com.seleniumtests.exception;

public class PageNotCurrentException extends MauiException {

	private static final long serialVersionUID = -6598083737880253802L;

	public PageNotCurrentException() {
	}

	public PageNotCurrentException(String message) {
		super(message);
	}

	public PageNotCurrentException(Throwable cause) {
		super(cause);
	}

	public PageNotCurrentException(String message, Throwable cause) {
		super(message, cause);
	}

}