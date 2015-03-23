package com.seleniumtests.webelements;

import org.openqa.selenium.By;

import com.seleniumtests.core.TestLogging;

public class TextFieldElement extends HtmlElement {
    public TextFieldElement(final String label, final By by) {
        super(label, by);
    }

    public void clear() {
        TestLogging.logWebStep(null, "Remove data From " + toHTML(), false);
        findElement();
        if (!element.getAttribute("type").equalsIgnoreCase("file")) {
            element.clear();
        }
    }

    public void sendKeys(final String keysToSend) {
        TestLogging.logWebStep(null, "Enter data: \"" + keysToSend + "\" on " + toHTML(), false);
        findElement();
        element.sendKeys(keysToSend);
    }

    public void type(final String keysToSend) {
        sendKeys(keysToSend);
    }

    public void clearAndType(final String keysToSend) {
        clear();
        type(keysToSend);
    }
}
