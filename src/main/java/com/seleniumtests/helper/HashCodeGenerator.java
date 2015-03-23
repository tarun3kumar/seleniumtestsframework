/*
 * Copyright 2015 www.seleniumtests.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
