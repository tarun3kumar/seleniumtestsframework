package com.seleniumtests.helper;

public class WaitHelper {
	/**
	 * Wait For seconds
	 * 
	 * @param seconds
	 */
	public static void waitForSeconds(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException ignore) {
		}
	}
}
