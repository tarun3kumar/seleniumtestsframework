package com.seleniumtests.customexception;

public class DriverExceptions extends RuntimeException {

    public DriverExceptions(final String message, final Throwable throwable) {
        super(message, throwable);
    }

}
