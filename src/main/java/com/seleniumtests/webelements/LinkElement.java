package com.seleniumtests.webelements;

import org.openqa.selenium.By;

import com.seleniumtests.core.TestLogging;

public class LinkElement extends HtmlElement {

    public LinkElement(final String label, final By by) {
        super(label, by);
    }

    @Override
    public void click() {
        captureSnapshot("before clicking");
        TestLogging.logWebStep(null, "click on " + toHTML(), false);
        super.click();
    }

    public String getUrl() {
        return super.getAttribute("href");
    }
}
