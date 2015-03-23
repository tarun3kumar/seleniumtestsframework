package com.seleniumtests.helper;

import java.math.BigInteger;

import java.security.MessageDigest;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.seleniumtests.core.SeleniumTestsContextManager;

public class HashCodeGenerator {

    private static Logger logger = Logger.getLogger(HashCodeGenerator.class);

    public static String getRandomHashCode(final String seed) {
        String signature;
        if (SeleniumTestsContextManager.getThreadContext() != null) {
            signature = SeleniumTestsContextManager.getThreadContext().getTestMethodSignature();
        } else {
            signature = "";
        }

        byte[] data = (signature + UUID.randomUUID().getLeastSignificantBits() + seed).getBytes();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return new BigInteger(1, digest.digest(data)).toString(16);
        } catch (Exception e2) { }

        return new String(data);
    }
}
