package com.seleniumtests.helper;

public class ThreadHelper {
	/**
	 * Wait For seconds
	 * 
	 * @param seconds
	 */
	public static void waitForSeconds(int seconds) {
		/*
		 * long timeout = 1000 * 60 * 3;
		 * if(ContextManager.getThreadContext()!=null) timeout =
		 * ContextManager.getThreadContext().getWebSessionTimeout(); if (seconds
		 * * 1000 > timeout) throw new RuntimeException("Can not wait for " +
		 * seconds +
		 * " seconds. Because that is longer than then web session timeout of "
		 * + timeout / 1000 +
		 * " seconds which is defined in the testng configuration file.");
		 */
		try {
			Thread.sleep(seconds * 1000);// KEEPME
		} catch (InterruptedException ignore) {

		}
	}
}
