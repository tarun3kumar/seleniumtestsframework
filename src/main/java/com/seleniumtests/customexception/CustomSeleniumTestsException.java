
package com.seleniumtests.customexception;

public class CustomSeleniumTestsException extends Exception {

    private static final long serialVersionUID = -5567383832452234582L;

    public CustomSeleniumTestsException() { }

    public CustomSeleniumTestsException(final String message) {
        super(message);
    }

    public CustomSeleniumTestsException(final Throwable cause) {
        super(cause);
    }

    public CustomSeleniumTestsException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
