package com.seleniumtests.helper;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.UUID;

import com.seleniumtests.core.SeleniumTestsContextManager;

public class StringHelper {

	public static String constructMethodSignature(Method method, Object[] parameters) {
		return method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "(" + constructParameterString(parameters) + ")";
	}

	public static String constructParameterString(Object[] parameters) {
		StringBuffer sbParam = new StringBuffer();

		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				if (parameters[i] == null) {
					sbParam.append("null, ");
				} else if (parameters[i] instanceof java.lang.String) {
					sbParam.append("\"").append(parameters[i]).append("\", ");
				} else {
					sbParam.append(parameters[i]).append(", ");
				}
			}
		}

		if (sbParam.length() > 0)
			sbParam.delete(sbParam.length() - 2, sbParam.length() - 1);

		return sbParam.toString();
	}

	public static String getRandomHashCode(String seed) {
		byte[] data = (SeleniumTestsContextManager.getThreadContext().getTestMethodSignature() + UUID.randomUUID().getLeastSignificantBits() + seed).getBytes();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			return new BigInteger(1, digest.digest(data)).toString(16);
		} catch (Exception e2) {
			// OK, we won't digest
			return new String(data);
		}
	}

}
