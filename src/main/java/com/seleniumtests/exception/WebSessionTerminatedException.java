package com.seleniumtests.exception;

import org.openqa.selenium.WebDriverException;

public class WebSessionTerminatedException extends WebDriverException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4870889731005206071L;
	public WebSessionTerminatedException(){
		super();
	}
	public WebSessionTerminatedException(Throwable ex){
		super(ex);
	}
}
