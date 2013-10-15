package com.seleniumtests.helper;

public class ClassContextHelper {

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

	/**
	 * Utility to obtain the <code>Class</code> issuing the call to the current
	 * method of execution
	 * 
	 * @return The calling <code>Class</code>
	 */
	public static Class getCallerEncloser() {
		Class[] classContext = HELPER.getContext();
		if (classContext[3].isAnonymousClass()
				|| classContext[3].isLocalClass()
				|| classContext[3].isMemberClass())
			return classContext[3].getEnclosingClass();
		return classContext[3];
	}

	public static String getCallerMethod() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		return stackTrace[3].getMethodName();
	}

	public static void main(String[] st) {
		System.out.println(getCallerMethod()); // KEEPME
	}
}
