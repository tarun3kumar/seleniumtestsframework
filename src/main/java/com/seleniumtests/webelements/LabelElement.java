package com.seleniumtests.webelements;

import org.openqa.selenium.By;

import com.seleniumtests.core.CustomAssertion;
import com.seleniumtests.core.TestLogging;

public class LabelElement extends HtmlElement {
    public LabelElement(final String label, final By by) {
        super(label, by);
    }

    @Override
    public String getText() {
        TestLogging.logWebStep(null, "get text from " + toHTML(), false);
        return super.getText();
    }

    public boolean isTextPresent(final String pattern) {
        String text = getText();
        return (text != null && (text.contains(pattern) || text.matches(pattern)));
    }

    @Deprecated
    public String getExpectedText() {
        CustomAssertion.assertTrue(false, "NOT supported!");
        return null;
    }
}
