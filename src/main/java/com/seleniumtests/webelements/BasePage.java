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

package com.seleniumtests.webelements;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;

import com.seleniumtests.core.CustomAssertion;
import com.seleniumtests.core.TestLogging;

import com.seleniumtests.customexception.NotCurrentPageException;

import com.seleniumtests.driver.BrowserType;
import com.seleniumtests.driver.WebUIDriver;

import com.seleniumtests.helper.WaitHelper;

import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;
import com.thoughtworks.selenium.webdriven.Windows;

/**
 * Base html page abstraction. Used by PageObject and WebPageSection
 */
public abstract class BasePage {

    protected WebDriver driver = WebUIDriver.getWebDriver();
    protected final WebUIDriver webUXDriver = WebUIDriver.getWebUIDriver();
    private int explictWaitTimeout = WebUIDriver.getWebUIDriver().getExplicitWait();
    private int sessionTimeout = WebUIDriver.getWebUIDriver().getWebSessionTimeout();

    public BasePage() { }

    public void acceptAlert() throws NotCurrentPageException {
        Alert alert = driver.switchTo().alert();
        alert.accept();
        driver.switchTo().defaultContent();
    }

    public void assertAlertPresent() {
        TestLogging.logWebStep(null, "assert alert present.", false);
        try {
            driver.switchTo().alert();
        } catch (Exception ex) {
            assertAlertHTML(false, "assert alert present.");
        }
    }

    public void assertAlertText(final String text) {
        TestLogging.logWebStep(null, "assert alert text.", false);

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        assertAlertHTML(alertText.contains(text), "assert alert text.");
    }

    /**
     * @param  element
     * @param  attributeName
     * @param  value
     */
    public void assertAttribute(final HtmlElement element, final String attributeName, final String value) {
        TestLogging.logWebStep(null,
            "assert " + element.toHTML() + " attribute = " + attributeName + ", expectedValue ={" + value + "}.",
            false);

        String attributeValue = element.getAttribute(attributeName);

        assertHTML(value != null && value.equals(attributeValue),
            element.toString() + " attribute = " + attributeName + ", expectedValue = {" + value + "}"
                + ", attributeValue = {" + attributeValue + "}");
    }

    public void assertAttributeContains(final HtmlElement element, final String attributeName, final String keyword) {
        TestLogging.logWebStep(null,
            "assert " + element.toHTML() + " attribute=" + attributeName + ", contains keyword = {" + keyword + "}.",
            false);

        String attributeValue = element.getAttribute(attributeName);

        assertHTML(attributeValue != null && keyword != null && attributeValue.contains(keyword),
            element.toString() + " attribute=" + attributeName + ", expected to contains keyword {" + keyword + "}"
                + ", attributeValue = {" + attributeValue + "}");
    }

    public void assertAttributeMatches(final HtmlElement element, final String attributeName, final String regex) {
        TestLogging.logWebStep(null,
            "assert " + element.toHTML() + " attribute=" + attributeName + ", matches regex = {" + regex + "}.", false);

        String attributeValue = element.getAttribute(attributeName);

        assertHTML(attributeValue != null && regex != null && attributeValue.matches(regex),
            element.toString() + " attribute=" + attributeName + " expected to match regex {" + regex + "}"
                + ", attributeValue = {" + attributeValue + "}");
    }

    public void assertConfirmationText(final String text) {
        TestLogging.logWebStep(null, "assert confirmation text.", false);

        Alert alert = driver.switchTo().alert();
        String seenText = alert.getText();

        assertAlertHTML(seenText.contains(text), "assert confirmation text.");
    }

    protected void assertCurrentPage(final boolean log) throws NotCurrentPageException { }

    public void assertElementNotPresent(final HtmlElement element) {
        TestLogging.logWebStep(null, "assert " + element.toHTML() + " is not present.", false);
        assertHTML(!element.isElementPresent(), element.toString() + " found.");
    }

    public void assertElementPresent(final HtmlElement element) {
        TestLogging.logWebStep(null, "assert " + element.toHTML() + " is present.", false);
        assertHTML(element.isElementPresent(), element.toString() + " not found.");
    }

    public void assertElementEnabled(final HtmlElement element) {
        TestLogging.logWebStep(null, "assert " + element.toHTML() + " is enabled.", false);
        assertHTML(element.isEnabled(), element.toString() + " not found.");
    }

    public void assertElementNotEnabled(final HtmlElement element) {
        TestLogging.logWebStep(null, "assert " + element.toHTML() + " is not enabled.", false);
        assertHTML(!element.isEnabled(), element.toString() + " not found.");
    }

    public void assertElementDisplayed(final HtmlElement element) {
        TestLogging.logWebStep(null, "assert " + element.toHTML() + " is displayed.", false);
        assertHTML(element.isDisplayed(), element.toString() + " not found.");
    }

    public void assertElementSelected(final HtmlElement element) {
        TestLogging.logWebStep(null, "assert " + element.toHTML() + " is selected.", false);
        assertHTML(element.isSelected(), element.toString() + " not found.");
    }

    public void assertElementNotSelected(final HtmlElement element) {
        TestLogging.logWebStep(null, "assert " + element.toHTML() + " is NOT selected.", false);
        assertHTML(!element.isSelected(), element.toString() + " not found.");
    }

    public void assertCondition(final boolean condition, final String message) {
        TestLogging.logWebStep(null, "assert that " + message, false);
        assert condition;
    }

    void assertHTML(final boolean condition, final String message) {
        if (!condition) {
            capturePageSnapshot();
            CustomAssertion.assertTrue(condition, message);
        }
    }

    void assertAlertHTML(final boolean condition, final String message) {
        if (!condition) {
            CustomAssertion.assertTrue(condition, message);
        }
    }

    public void assertPromptText(final String text) {
        TestLogging.logWebStep(null, "assert prompt text.", false);

        Alert alert = driver.switchTo().alert();
        String seenText = alert.getText();
        assertAlertHTML(seenText.contains(text), "assert prompt text.");
    }

    public void assertTable(final Table table, final int row, final int col, final String text) {
        TestLogging.logWebStep(null,
            "assert text \"" + text + "\" equals " + table.toHTML() + " at (row, col) = (" + row + ", " + col + ").",
            false);

        String content = table.getContent(row, col);
        assertHTML(content != null && content.equals(text),
            "Text= {" + text + "} not found on " + table.toString() + " at cell(row, col) = {" + row + "," + col + "}");
    }

    public void assertTableContains(final Table table, final int row, final int col, final String text) {
        TestLogging.logWebStep(null,
            "assert text \"" + text + "\" contains " + table.toHTML() + " at (row, col) = (" + row + ", " + col + ").",
            false);

        String content = table.getContent(row, col);
        assertHTML(content != null && content.contains(text),
            "Text= {" + text + "} not found on " + table.toString() + " at cell(row, col) = {" + row + "," + col + "}");
    }

    public void assertTableMatches(final Table table, final int row, final int col, final String text) {
        TestLogging.logWebStep(null,
            "assert text \"" + text + "\" matches " + table.toHTML() + " at (row, col) = (" + row + ", " + col + ").",
            false);

        String content = table.getContent(row, col);
        assertHTML(content != null && content.matches(text),
            "Text= {" + text + "} not found on " + table.toString() + " at cell(row, col) = {" + row + "," + col + "}");
    }

    public void assertTextNotPresent(final String text) {
        TestLogging.logWebStep(null, "assert text \"" + text + "\" is not present.", false);
        assertHTML(!isTextPresent(text), "Text= {" + text + "} found.");
    }

    public void assertTextNotPresentIgnoreCase(final String text) {
        TestLogging.logWebStep(null, "assert text \"" + text + "\" is not present.(ignore case)", false);
        assertHTML(!getBodyText().toLowerCase().contains(text.toLowerCase()), "Text= {" + text + "} found.");
    }

    public void assertTextPresent(final String text) {
        TestLogging.logWebStep(null, "assert text \"" + text + "\" is present.", false);
        assertHTML(isTextPresent(text), "Text= {" + text + "} not found.");
    }

    public void assertTextPresentIgnoreCase(final String text) {
        TestLogging.logWebStep(null, "assert text \"" + text + "\" is present.(ignore case)", false);
        assertHTML(getBodyText().toLowerCase().contains(text.toLowerCase()), "Text= {" + text + "} not found.");
    }

    public String cancelConfirmation() throws NotCurrentPageException {
        Alert alert = driver.switchTo().alert();
        String seenText = alert.getText();
        alert.dismiss();
        driver.switchTo().defaultContent();
        return seenText;
    }

    protected abstract void capturePageSnapshot();

    public Alert getAlert() {
        Alert alert = driver.switchTo().alert();
        return alert;
    }

    public String getAlertText() {
        Alert alert = driver.switchTo().alert();
        String seenText = alert.getText();

        return seenText;
    }

    private String getBodyText() {
        WebElement body = driver.findElement(By.tagName("body"));
        return body.getText();
    }

    public String getConfirmation() {
        Alert alert = driver.switchTo().alert();
        String seenText = alert.getText();

        return seenText;
    }

    public WebDriver getDriver() {
        driver = WebUIDriver.getWebDriver();
        return driver;

    }

    public String getPrompt() {
        Alert alert = driver.switchTo().alert();
        String seenText = alert.getText();

        return seenText;
    }

    public boolean isElementPresent(final By by) {
        int count = 0;
        try {
            count = WebUIDriver.getWebDriver().findElements(by).size();
        } catch (RuntimeException e) {
            if ((e instanceof InvalidSelectorException)
                    || (e.getMessage() != null
                        && e.getMessage().contains("TransformedEntriesMap cannot be cast to java.util.List"))) {
                TestLogging.log("InvalidSelectorException or CastException got, retry");
                WaitHelper.waitForSeconds(2);
                count = WebUIDriver.getWebDriver().findElements(by).size();
            } else {
                throw e;
            }
        }

        if (count == 0) {
            return false;
        }

        return true;

    }

    public boolean isFrame() {
        return false;
    }

    public boolean isTextPresent(final String text) {
        CustomAssertion.assertNotNull(text, "isTextPresent: text should not be null!");
        driver = WebUIDriver.getWebDriver();

        WebElement body = driver.findElement(By.tagName("body"));

        if (WebUIDriver.getWebUIDriver().getBrowser().equalsIgnoreCase(BrowserType.HtmlUnit.getBrowserType())) {
            return body.getText().contains(text);
        }

        Boolean result = false;

        if (body.getText().contains(text)) {
            return true;
        }

        JavascriptLibrary js = new JavascriptLibrary();
        String script = js.getSeleniumScript("isTextPresent.js");

        result = (Boolean) ((JavascriptExecutor) driver).executeScript("return (" + script + ")(arguments[0]);", text);

        // Handle the null case
        return Boolean.TRUE == result;
    }

    public void selectFrame(final By by) {
        TestLogging.logWebStep(null, "select frame, locator={\"" + by.toString() + "\"}", false);
        driver.switchTo().frame(driver.findElement(by));
    }

    /**
     * If current window is closed then use driver.switchTo.window(handle).
     *
     * @param   windowName
     *
     * @throws  com.seleniumtests.customexception.NotCurrentPageException
     */
    public final void selectWindow(final String windowName) throws NotCurrentPageException {
        if (windowName == null) {
            Windows windows = new Windows(driver);
            try {
                windows.selectBlankWindow(driver);
            } catch (SeleniumException e) {
                driver.switchTo().defaultContent();
            }

        } else {
            Windows windows = new Windows(driver);
            windows.selectWindow(driver, "name=" + windowName);
        }
    }

    public void waitForElementChecked(final HtmlElement element) {
        Assert.assertNotNull(element, "Element can't be null");
        TestLogging.logWebStep(null, "wait for " + element.toString() + " to be checked.", false);

        WebDriverWait wait = new WebDriverWait(driver, explictWaitTimeout);
        wait.until(ExpectedConditions.elementToBeSelected(element.getBy()));
    }

    public void waitForElementEditable(final HtmlElement element) {
        Assert.assertNotNull(element, "Element can't be null");
        TestLogging.logWebStep(null, "wait for " + element.toString() + " to be editable.", false);

        WebDriverWait wait = new WebDriverWait(driver, explictWaitTimeout);
        wait.until(ExpectedConditions.elementToBeClickable(element.getBy()));
    }

    public void waitForElementPresent(final By by) {
        TestLogging.logWebStep(null, "wait for " + by.toString() + " to be present.", false);

        WebDriverWait wait = new WebDriverWait(driver, explictWaitTimeout);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public void waitForElementPresent(final By by, final int timeout) {
        TestLogging.logWebStep(null, "wait for " + by.toString() + " to be present.", false);

        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public void waitForElementPresent(final HtmlElement element) {
        Assert.assertNotNull(element, "Element can't be null");
        TestLogging.logWebStep(null, "wait for " + element.toString() + " to be present.", false);

        WebDriverWait wait = new WebDriverWait(driver, explictWaitTimeout);
        wait.until(ExpectedConditions.presenceOfElementLocated(element.getBy()));
    }

    public void waitForElementToBeVisible(final HtmlElement element) {
        Assert.assertNotNull(element, "Element can't be null");
        TestLogging.logWebStep(null, "wait for " + element.toString() + " to be visible.", false);

        WebDriverWait wait = new WebDriverWait(driver, explictWaitTimeout);
        wait.until(ExpectedConditions.visibilityOfElementLocated(element.getBy()));
    }

    public void waitForElementToDisappear(final HtmlElement element) {
        Assert.assertNotNull(element, "Element can't be null");
        TestLogging.logWebStep(null, "wait for " + element.toString() + " to disappear.", false);

        WebDriverWait wait = new WebDriverWait(driver, explictWaitTimeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(element.getBy()));
    }

    public void waitForPopup(final String locator) throws Exception {
        waitForPopUp(locator, sessionTimeout + "");
    }

    public void waitForPopUp(final String windowID, final String timeout) {
        final long millis = Long.parseLong(timeout);
        final String current = driver.getWindowHandle();
        final Windows windows = new Windows(driver);

        if (webUXDriver.getConfig().getBrowser() == BrowserType.InternetExplore) {
            waitForSeconds(3);
        }

        new Wait() {
            @Override
            public boolean until() {
                try {
                    if ("_blank".equals(windowID)) {
                        windows.selectBlankWindow(driver);
                    } else {
                        driver.switchTo().window(windowID);
                    }

                    return !"about:blank".equals(driver.getCurrentUrl());
                } catch (SeleniumException e) { }
                catch (NoSuchWindowException e) { }

                return false;
            }
        }.wait(String.format("Timed out waiting for %s. Waited %s", windowID, timeout), millis);

        driver.switchTo().window(current);

    }

    /**
     * Wait For seconds. Provide a value less than WebSessionTimeout i.e. 180 Seconds
     *
     * @param  seconds
     */
    protected void waitForSeconds(final int seconds) {
        WaitHelper.waitForSeconds(seconds);
    }

    public void waitForTextPresent(final HtmlElement element, final String text) {
        Assert.assertNotNull(text, "Text can't be null");
        TestLogging.logWebStep(null, "wait for text \"" + text + "\" to be present.", false);

        WebDriverWait wait = new WebDriverWait(driver, explictWaitTimeout);
        wait.until(ExpectedConditions.textToBePresentInElement(element.getBy(), text));
    }

    public void waitForTextPresent(final String text) {
        Assert.assertNotNull(text, "Text can't be null");
        TestLogging.logWebStep(null, "wait for text \"" + text + "\" to be present.", false);

        boolean b = false;
        for (int millisec = 0; millisec < explictWaitTimeout * 1000; millisec += 1000) {
            try {
                if ((isTextPresent(text))) {
                    b = true;
                    break;
                }
            } catch (Exception ignore) { }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        assertHTML(b, "Timed out waiting for text \"" + text + "\" to be there.");
    }

    public void waitForTextToDisappear(final String text) {
        Assert.assertNotNull(text, "Text can't be null");
        TestLogging.logWebStep(null, "wait for text \"" + text + "\" to disappear.", false);

        boolean textPresent = true;
        for (int millisec = 0; millisec < explictWaitTimeout * 1000; millisec += 1000) {
            try {
                if (!(isTextPresent(text))) {
                    textPresent = false;
                    break;
                }
            } catch (Exception ignore) { }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        assertHTML(!textPresent, "Timed out waiting for text \"" + text + "\" to be gone.");
    }

}
