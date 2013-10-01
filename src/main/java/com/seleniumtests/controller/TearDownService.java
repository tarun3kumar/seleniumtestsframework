package com.seleniumtests.controller;

/**
 * This interface provides a way for test author to cleanup resources 
 * after a test method is completed executing.
 * 
 */
public interface TearDownService {
	
	public void tearDown();
}
