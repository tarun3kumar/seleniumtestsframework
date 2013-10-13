package com.seleniumtests.helper;

public class ThreadHelper {
	/**
	 * Wait For seconds
	 * 
	 * @param seconds
	 */
	public static void waitForSeconds(int seconds) {
		try {
			Thread.sleep(seconds * 1000);// KEEPME
		} catch (InterruptedException ignore) {

		}
	}
}
