package com.seleniumtests.helper;

public class ContextHelper {

	private static class Helper extends SecurityManager {

		Class[] getContext() {
			return getClassContext();
		}
	}

	private static final Helper HELPER = new Helper();

	public static Class getCaller() {
		Class[] classContext = HELPER.getContext();
		return classContext[3];
	}

	public static String getCallerMethod() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		return stackTrace[3].getMethodName();
	}

	public static void main(String[] st) {
		System.out.println(getCallerMethod());
	}
}
