package com.seleniumtests.controller;

import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.Reporter;

/**
 * A enhanced CustomAssertion supports soft assert. It uses soft assert if context
 * softAssertEnabled is true.
 * 
 * soft assert - test case continues when validation fails.
 * 
 */
public class CustomAssertion {

	private static void _addVerificationFailure(Throwable e) {
		ContextManager.getThreadContext().addVerificationFailures(Reporter.getCurrentTestResult(), e);
		Logging.log(null, "ASSERTION FAILED: " + e.getMessage(), true, true);
	}

	// /////////////// CustomAssertion Methods ////////////////////////////

	public static void assertEquals(boolean actual, boolean expected, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertEquals(actual, expected, message);
		} else {
			Assert.assertEquals(actual, expected, message);
		}
	}

	public static void assertEquals(byte actual, byte expected, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertEquals(actual, expected, message);
		} else {
			Assert.assertEquals(actual, expected, message);
		}
	}

	public static void assertEquals(byte[] actual, byte[] expected, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertEquals(actual, expected, message);
		} else {
			Assert.assertEquals(actual, expected, message);
		}
	}

	public static void assertEquals(char actual, char expected, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertEquals(actual, expected, message);
		} else {
			Assert.assertEquals(actual, expected, message);
		}
	}

	@SuppressWarnings("rawtypes")
	public static void assertEquals(Collection actual, Collection expected, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertEquals(actual, expected, message);
		} else {
			Assert.assertEquals(actual, expected, message);
		}
	}

	public static void assertEquals(double actual, double expected, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertEquals(actual, expected, message);
		} else {
			Assert.assertEquals(actual, expected, message);
		}
	}

	public static void assertEquals(float actual, float expected, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertEquals(actual, expected, message);
		} else {
			Assert.assertEquals(actual, expected, message);
		}
	}

	public static void assertEquals(int actual, int expected, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertEquals(actual, expected, message);
		} else {
			Assert.assertEquals(actual, expected, message);
		}
	}

	public static void assertEquals(long actual, long expected, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertEquals(actual, expected, message);
		} else {
			Assert.assertEquals(actual, expected, message);
		}
	}

	public static void assertEquals(Object actual, Object expected, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertEquals(actual, expected, message);
		} else {
			Assert.assertEquals(actual, expected, message);
		}
	}

	public static void assertEquals(Object[] actual, Object[] expected, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertEquals(actual, expected, message);
		} else {
			Assert.assertEquals(actual, expected, message);
		}
	}

	public static void assertEquals(short actual, short expected, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertEquals(actual, expected, message);
		} else {
			Assert.assertEquals(actual, expected, message);
		}
	}

	public static void assertEquals(String actual, String expected, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertEquals(actual, expected, message);
		} else {
			Assert.assertEquals(actual, expected, message);
		}
	}

	public static void assertFalse(boolean condition, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertFalse(condition, message);
		} else {
			Assert.assertFalse(condition, message);
		}
	}

	public static void assertNotNull(Object object, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertNotNull(object, message);
		} else {
			Assert.assertNotNull(object, message);
		}
	}

	public static void assertNotSame(Object actual, Object expected, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertNotSame(actual, expected, message);
		} else {
			Assert.assertNotSame(actual, expected, message);
		}
	}

	public static void assertNull(Object object, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertNull(object, message);
		} else {
			Assert.assertNull(object, message);
		}
	}

	public static void assertSame(Object actual, Object expected, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertSame(actual, expected, message);
		} else {
			Assert.assertSame(actual, expected, message);
		}
	}

	public static void assertTrue(boolean condition, String message) {
		if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
			softAssertTrue(condition, message);
		} else {
			Assert.assertTrue(condition, message);
		}
	}

	public static void fail(String message) {
		Assert.fail(message);
	}

	public static List<Throwable> getVerificationFailures() {
		return ContextManager.getThreadContext().getVerificationFailures(Reporter.getCurrentTestResult());
	}

	// /////////////// Soft CustomAssertion Methods ////////////////////////////

	public static void softAssertEquals(boolean actual, boolean expected, String message) {

		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {

			_addVerificationFailure(e);
		}
	}

	public static void softAssertEquals(byte actual, byte expected, String message) {

		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {

			_addVerificationFailure(e);
		}
	}

	public static void softAssertEquals(byte[] actual, byte[] expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {

			_addVerificationFailure(e);
		}

	}

	public static void softAssertEquals(char actual, char expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);

		} catch (Throwable e) {

			_addVerificationFailure(e);
		}

	}

	@SuppressWarnings("rawtypes")
	public static void softAssertEquals(Collection actual, Collection expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);

		} catch (Throwable e) {

			_addVerificationFailure(e);
		}

	}

	public static void softAssertEquals(double actual, double expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);

		} catch (Throwable e) {

			_addVerificationFailure(e);
		}

	}

	public static void softAssertEquals(float actual, float expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);

		} catch (Throwable e) {

			_addVerificationFailure(e);
		}
	}

	public static void softAssertEquals(int actual, int expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);

		} catch (Throwable e) {

			_addVerificationFailure(e);
		}

	}

	public static void softAssertEquals(long actual, long expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);

		} catch (Throwable e) {

			_addVerificationFailure(e);
		}

	}

	public static void softAssertEquals(Object actual, Object expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);

		} catch (Throwable e) {

			_addVerificationFailure(e);
		}

	}

	public static void softAssertEquals(Object[] actual, Object[] expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);

		} catch (Throwable e) {

			_addVerificationFailure(e);
		}

	}

	public static void softAssertEquals(short actual, short expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);

		} catch (Throwable e) {

			_addVerificationFailure(e);
		}

	}

	public static void softAssertEquals(String actual, String expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);

		} catch (Throwable e) {
			_addVerificationFailure(e);
		}

	}

	public static void softAssertFalse(boolean condition, String message) {
		try {
			Assert.assertFalse(condition, message);

		} catch (Throwable e) {
			_addVerificationFailure(e);
		}

	}

	public static void softAssertNotNull(Object object, String message) {
		try {
			Assert.assertNotNull(object, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}

	}

	public static void softAssertNotSame(Object actual, Object expected, String message) {
		try {
			Assert.assertNotSame(actual, expected, message);

		} catch (Throwable e) {
			_addVerificationFailure(e);
		}

	}

	public static void softAssertNull(Object object, String message) {
		try {
			Assert.assertNull(object, message);

		} catch (Throwable e) {
			_addVerificationFailure(e);
		}

	}

	public static void softAssertSame(Object actual, Object expected, String message) {
		try {
			Assert.assertSame(actual, expected, message);

		} catch (Throwable e) {
			_addVerificationFailure(e);
		}

	}

	public static void softAssertTrue(boolean condition, String message) {
		try {
			Assert.assertTrue(condition, message);

		} catch (Throwable e) {
			_addVerificationFailure(e);
		}

	}

}
