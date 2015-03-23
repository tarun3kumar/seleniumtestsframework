package com.seleniumtests.webelements;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.seleniumtests.core.TestLogging;

import com.seleniumtests.driver.BrowserType;
import com.seleniumtests.driver.WebUIDriver;

public class ButtonElement extends HtmlElement {

    public ButtonElement(final String label, final By by) {
        super(label, by);
    }

    @Override
    public void click() {
        captureSnapshot("Before clicking");
        TestLogging.logWebStep(null, "click on " + toHTML(), false);

        BrowserType browser = WebUIDriver.getWebUXDriver().getConfig().getBrowser();
        if (browser == BrowserType.InternetExplore) {
            super.sendKeys(Keys.ENTER);             // not stable on IE9
            super.handleLeaveAlert();
        } else {
            super.click();
        }
    }

    public void submit() {
        captureSnapshot("Before form submission");
        TestLogging.logWebStep(null, "Submit form by clicking on " + toHTML(), false);
        findElement();
        element.submit();
    }
}
