package com.seleniumtests.exception;

public class MauiException extends Exception {

	private static final long serialVersionUID = -2757992149932989353L;

	public MauiException() {
	}

	public MauiException(String message) {
		super(message);
	}

	public MauiException(Throwable cause) {
		super(cause);
	}

	public MauiException(String message, Throwable cause) {
		super(message, cause);
	}

}
