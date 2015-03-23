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

package com.seleniumtests.core;

import java.util.Collection;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import org.testng.Assert;
import org.testng.Reporter;

/**
 * soft assert - test case continues when validation fails. soft assert is enabled only if context softAssertEnabled is
 * set to true.
 */
public class CustomAssertion {

    private static void addVerificationFailure(final Throwable e) {
        SeleniumTestsContextManager.getThreadContext().addVerificationFailures(Reporter.getCurrentTestResult(), e);
        TestLogging.log("!!!FAILURE ALERT!!! - Assertion Failure: " + e.getMessage(), true, true);
    }

    // CustomAssertion Methods

    public static void assertEquals(final boolean actual, final boolean expected, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertEquals(actual, expected, message);
        } else {
            Assert.assertEquals(actual, expected, message);
        }
    }

    public static void assertEquals(final byte actual, final byte expected, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertEquals(actual, expected, message);
        } else {
            Assert.assertEquals(actual, expected, message);
        }
    }

    public static void assertEquals(final byte[] actual, final byte[] expected, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertEquals(actual, expected, message);
        } else {
            Assert.assertEquals(actual, expected, message);
        }
    }

    public static void assertEquals(final char actual, final char expected, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertEquals(actual, expected, message);
        } else {
            Assert.assertEquals(actual, expected, message);
        }
    }

    public static void assertEquals(final Collection actual, final Collection expected, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertEquals(actual, expected, message);
        } else {
            Assert.assertEquals(actual, expected, message);
        }
    }

    public static void assertEquals(final double actual, final double expected, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertEquals(actual, expected, message);
        } else {
            Assert.assertEquals(actual, expected, message);
        }
    }

    public static void assertEquals(final float actual, final float expected, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertEquals(actual, expected, message);
        } else {
            Assert.assertEquals(actual, expected, message);
        }
    }

    public static void assertEquals(final int actual, final int expected, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertEquals(actual, expected, message);
        } else {
            Assert.assertEquals(actual, expected, message);
        }
    }

    public static void assertEquals(final long actual, final long expected, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertEquals(actual, expected, message);
        } else {
            Assert.assertEquals(actual, expected, message);
        }
    }

    public static void assertEquals(final Object actual, final Object expected, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertEquals(actual, expected, message);
        } else {
            Assert.assertEquals(actual, expected, message);
        }
    }

    public static void assertEquals(final Object[] actual, final Object[] expected, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertEquals(actual, expected, message);
        } else {
            Assert.assertEquals(actual, expected, message);
        }
    }

    public static void assertEquals(final short actual, final short expected, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertEquals(actual, expected, message);
        } else {
            Assert.assertEquals(actual, expected, message);
        }
    }

    public static void assertEquals(final String actual, final String expected, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertEquals(actual, expected, message);
        } else {
            Assert.assertEquals(actual, expected, message);
        }
    }

    public static void assertFalse(final boolean condition, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertFalse(condition, message);
        } else {
            Assert.assertFalse(condition, message);
        }
    }

    public static void assertNotNull(final Object object, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertNotNull(object, message);
        } else {
            Assert.assertNotNull(object, message);
        }
    }

    public static void assertNotSame(final Object actual, final Object expected, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertNotSame(actual, expected, message);
        } else {
            Assert.assertNotSame(actual, expected, message);
        }
    }

    public static void assertNull(final Object object, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertNull(object, message);
        } else {
            Assert.assertNull(object, message);
        }
    }

    public static void assertSame(final Object actual, final Object expected, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertSame(actual, expected, message);
        } else {
            Assert.assertSame(actual, expected, message);
        }
    }

    public static void assertTrue(final boolean condition, final String message) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertTrue(condition, message);
        } else {
            Assert.assertTrue(condition, message);
        }
    }

    ////////////// Hamcrest Matchers with soft assertion ////////////////
    public static void assertThat(final String reason, final boolean assertion) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertThat(reason, assertion);
        } else {
            MatcherAssert.assertThat(reason, assertion);
        }
    }

    public static <T> void assertThat(final String reason, final T actual, final Matcher<? super T> matcher) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertThat(reason, actual, matcher);
        } else {
            MatcherAssert.assertThat(reason, actual, matcher);
        }
    }

    public static <T> void assertThat(final T actual, final Matcher<? super T> matcher) {
        if (SeleniumTestsContextManager.getThreadContext().isSoftAssertEnabled()) {
            softAssertThat(actual, matcher);
        } else {
            MatcherAssert.assertThat(actual, matcher);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void fail(final String message) {
        Assert.fail(message);
    }

    public static List<Throwable> getVerificationFailures() {
        return SeleniumTestsContextManager.getThreadContext().getVerificationFailures(Reporter.getCurrentTestResult());
    }

    // Soft CustomAssertion Methods

    public static void softAssertEquals(final boolean actual, final boolean expected, final String message) {

        try {
            Assert.assertEquals(actual, expected, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertEquals(final byte actual, final byte expected, final String message) {

        try {
            Assert.assertEquals(actual, expected, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertEquals(final byte[] actual, final byte[] expected, final String message) {
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }

    }

    public static void softAssertEquals(final char actual, final char expected, final String message) {
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertEquals(final Collection actual, final Collection expected, final String message) {
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertEquals(final double actual, final double expected, final String message) {
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertEquals(final float actual, final float expected, final String message) {
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertEquals(final int actual, final int expected, final String message) {
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertEquals(final long actual, final long expected, final String message) {
        try {
            Assert.assertEquals(actual, expected, message);

        } catch (Throwable e) {

            addVerificationFailure(e);
        }
    }

    public static void softAssertEquals(final Object actual, final Object expected, final String message) {
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertEquals(final Object[] actual, final Object[] expected, final String message) {
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertEquals(final short actual, final short expected, final String message) {
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertEquals(final String actual, final String expected, final String message) {
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertFalse(final boolean condition, final String message) {
        try {
            Assert.assertFalse(condition, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertNotNull(final Object object, final String message) {
        try {
            Assert.assertNotNull(object, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertNotSame(final Object actual, final Object expected, final String message) {
        try {
            Assert.assertNotSame(actual, expected, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertNull(final Object object, final String message) {
        try {
            Assert.assertNull(object, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertSame(final Object actual, final Object expected, final String message) {
        try {
            Assert.assertSame(actual, expected, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertTrue(final boolean condition, final String message) {
        try {
            Assert.assertTrue(condition, message);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static void softAssertThat(final String reason, final boolean assertion) {
        try {
            MatcherAssert.assertThat(reason, assertion);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static <T> void softAssertThat(final String reason, final T actual, final Matcher<? super T> matcher) {
        try {
            MatcherAssert.assertThat(reason, actual, matcher);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }

    public static <T> void softAssertThat(final T actual, final Matcher<? super T> matcher) {
        try {
            MatcherAssert.assertThat(actual, matcher);
        } catch (Throwable e) {
            addVerificationFailure(e);
        }
    }
}
