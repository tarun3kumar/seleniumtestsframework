package com.seleniumtests.customexception;

public class NotCurrentPageException extends CustomSeleniumTestsException {

    private static final long serialVersionUID = -5663838190837384823L;

    public NotCurrentPageException() { }

    public NotCurrentPageException(final String message) {
        super(message);
    }

    public NotCurrentPageException(final Throwable cause) {
        super(cause);
    }

    public NotCurrentPageException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
