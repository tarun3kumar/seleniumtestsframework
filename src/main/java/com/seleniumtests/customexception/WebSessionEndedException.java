package com.seleniumtests.customexception;

import org.openqa.selenium.WebDriverException;

public class WebSessionEndedException extends WebDriverException {

    private static final long serialVersionUID = -647233887439084123L;

    public WebSessionEndedException() {
        super();
    }

    public WebSessionEndedException(final Throwable ex) {
        super(ex);
    }
}
