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

import com.seleniumtests.core.CustomAssertion;
import com.seleniumtests.core.TestLogging;
import com.seleniumtests.customexception.NotCurrentPageException;
import com.seleniumtests.driver.WebUIDriver;
import com.seleniumtests.helper.WaitHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Base html page abstraction. Used by PageObject and WebPageSection
 */
public abstract class BasePage {

    protected WebDriver driver = WebUIDriver.getWebDriver();
    protected final WebUIDriver webUXDriver = WebUIDriver.getWebUIDriver();
    private static final int EXPLICT_WAIT_TIMEOUT = WebUIDriver.getWebUIDriver()
        .getExplicitWait();
    private final int sessionTimeout = WebUIDriver.getWebUIDriver()
        .getWebSessionTimeout();

    public BasePage() {
    }

    public void acceptAlert() throws NotCurrentPageException {
        final Alert alert = driver.switchTo().alert();
        alert.accept();
        driver.switchTo().defaultContent();
    }

    public void assertAlertPresent() {
        TestLogging.logWebStep("assert alert present.", false);

        try {
            driver.switchTo().alert();
        } catch (final Exception ex) {
            assertAlertHTML(false, "assert alert present.");
        }
    }

    public void assertAlertText(final String text) {
        TestLogging.logWebStep("assert alert text.", false);

        final Alert alert = driver.switchTo().alert();
        final String alertText = alert.getText();
        assertAlertHTML(alertText.contains(text), "assert alert text.");
    }

    /**
     * @param element
     * @param attributeName
     * @param value
     */
    public void assertAttribute(
        final HtmlElement element,
        final String attributeName, final String value
    ) {
        TestLogging.logWebStep(
            "assert " + element.toHTML() + " attribute = " + attributeName +
                ", expectedValue ={" + value + "}.", false
        );

        final String attributeValue = element.getAttribute(attributeName);

        assertHTML(
            (value != null) && value.equals(attributeValue),
            element.toString() + " attribute = " + attributeName +
                ", expectedValue = {" + value + "}" + ", attributeValue = {" +
                attributeValue + "}"
        );
    }

    public void assertAttributeContains(
        final HtmlElement element,
        final String attributeName, final String keyword
    ) {
        TestLogging.logWebStep(
            "assert " + element.toHTML() + " attribute=" + attributeName +
                ", contains keyword = {" + keyword + "}.", false
        );

        final String attributeValue = element.getAttribute(attributeName);

        assertHTML(
                (attributeValue != null) && (keyword != null) &&
                        attributeValue.contains(keyword),
                element.toString() + " attribute=" + attributeName +
                        ", expected to contains keyword {" + keyword + "}" +
                        ", attributeValue = {" + attributeValue + "}"
        );
    }

    public void assertAttributeMatches(
        final HtmlElement element,
        final String attributeName, final String regex
    ) {
        TestLogging.logWebStep(
            "assert " + element.toHTML() + " attribute=" + attributeName +
                ", matches regex = {" + regex + "}.", false
        );

        final String attributeValue = element.getAttribute(attributeName);

        assertHTML(
                (attributeValue != null) && (regex != null) &&
                        attributeValue.matches(regex),
                element.toString() + " attribute=" + attributeName +
                        " expected to match regex {" + regex + "}" +
                        ", attributeValue = {" + attributeValue + "}"
        );
    }

    public void assertConfirmationText(final String text) {
        TestLogging.logWebStep("assert confirmation text.", false);

        final Alert alert = driver.switchTo().alert();
        final String seenText = alert.getText();

        assertAlertHTML(seenText.contains(text), "assert confirmation text.");
    }

    protected void assertCurrentPage(final boolean log)
        throws NotCurrentPageException {
    }

    public void assertElementNotPresent(final HtmlElement element) {
        TestLogging.logWebStep("assert " + element.toHTML() + " is not present.", false);
        assertHTML(!element.isElementPresent(), element.toString() + " found.");
    }

    public void assertElementPresent(final HtmlElement element) {
        TestLogging.logWebStep("assert " + element.toHTML() + " is present.", false);
        assertHTML(
            element.isElementPresent(),
            element.toString() + " not found."
        );
    }

    public void assertElementEnabled(final HtmlElement element) {
        TestLogging.logWebStep(
            "assert " + element.toHTML() + " is enabled.", false
        );
        assertHTML(element.isEnabled(), element.toString() + " not found.");
    }

    public void assertElementNotEnabled(final HtmlElement element) {
        TestLogging.logWebStep(
            "assert " + element.toHTML() + " is not enabled.", false
        );
        assertHTML(!element.isEnabled(), element.toString() + " not found.");
    }

    public void assertElementDisplayed(final HtmlElement element) {
        TestLogging.logWebStep(
            "assert " + element.toHTML() + " is displayed.", false
        );
        assertHTML(element.isDisplayed(), element.toString() + " not found.");
    }

    public void assertElementSelected(final HtmlElement element) {
        TestLogging.logWebStep(
            "assert " + element.toHTML() + " is selected.", false
        );
        assertHTML(element.isSelected(), element.toString() + " not found.");
    }

    public void assertElementNotSelected(final HtmlElement element) {
        TestLogging.logWebStep(
            "assert " + element.toHTML() + " is NOT selected.", false
        );
        assertHTML(!element.isSelected(), element.toString() + " not found.");
    }

    public void assertCondition(final boolean condition, final String message) {
        TestLogging.logWebStep("assert that " + message, false);
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
        TestLogging.logWebStep("assert prompt text.", false);

        final Alert alert = driver.switchTo().alert();
        final String seenText = alert.getText();
        assertAlertHTML(seenText.contains(text), "assert prompt text.");
    }

    public void assertTable(
        final Table table, final int row, final int col,
        final String text
    ) {
        TestLogging.logWebStep(
            "assert text \"" + text + "\" equals " + table.toHTML() +
                " at (row, col) = (" + row + ", " + col + ").", false
        );

        final String content = table.getContent(row, col);
        assertHTML(
            (content != null) && content.equals(text),
            "Text= {" + text + "} not found on " + table.toString() +
                " at cell(row, col) = {" + row + "," + col + "}"
        );
    }

    public void assertTableContains(
        final Table table, final int row,
        final int col, final String text
    ) {
        TestLogging.logWebStep(
            "assert text \"" + text + "\" contains " + table.toHTML() +
                " at (row, col) = (" + row + ", " + col + ").", false
        );

        final String content = table.getContent(row, col);
        assertHTML(
            (content != null) && content.contains(text),
            "Text= {" + text + "} not found on " + table.toString() +
                " at cell(row, col) = {" + row + "," + col + "}"
        );
    }

    public void assertTableMatches(
        final Table table, final int row,
        final int col, final String text
    ) {
        TestLogging.logWebStep(
            "assert text \"" + text + "\" matches " + table.toHTML() +
                " at (row, col) = (" + row + ", " + col + ").", false
        );

        final String content = table.getContent(row, col);
        assertHTML(
            (content != null) && content.matches(text),
            "Text= {" + text + "} not found on " + table.toString() +
                " at cell(row, col) = {" + row + "," + col + "}"
        );
    }

    public void assertTextNotPresent(final String text) {
        TestLogging.logWebStep(
            "assert text \"" + text + "\" is not present.", false
        );
        assertHTML(!isTextPresent(text), "Text= {" + text + "} found.");
    }

    public void assertTextNotPresentIgnoreCase(final String text) {
        TestLogging.logWebStep(
            "assert text \"" + text + "\" is not present.(ignore case)", false
        );
        assertHTML(
            !getBodyText().toLowerCase().contains(text.toLowerCase()),
            "Text= {" + text + "} found."
        );
    }

    public void assertTextPresent(final String text) {
        TestLogging.logWebStep(
            "assert text \"" + text + "\" is present.",
            false
        );
        assertHTML(isTextPresent(text), "Text= {" + text + "} not found.");
    }

    public void assertTextPresentIgnoreCase(final String text) {
        TestLogging.logWebStep(
            "assert text \"" + text + "\" is present.(ignore case)", false
        );
        assertHTML(
            getBodyText().toLowerCase().contains(text.toLowerCase()),
            "Text= {" + text + "} not found."
        );
    }

    public String cancelConfirmation() throws NotCurrentPageException {
        final Alert alert = driver.switchTo().alert();
        final String seenText = alert.getText();
        alert.dismiss();
        driver.switchTo().defaultContent();

        return seenText;
    }

    protected abstract void capturePageSnapshot();

    public Alert getAlert() {
        final Alert alert = driver.switchTo().alert();

        return alert;
    }

    public String getAlertText() {
        final Alert alert = driver.switchTo().alert();
        final String seenText = alert.getText();

        return seenText;
    }

    private String getBodyText() {
        final WebElement body = driver.findElement(By.tagName("body"));

        return body.getText();
    }

    public String getConfirmation() {
        final Alert alert = driver.switchTo().alert();
        final String seenText = alert.getText();

        return seenText;
    }

    public WebDriver getDriver() {
        driver = WebUIDriver.getWebDriver();

        return driver;

    }

    public String getPrompt() {
        final Alert alert = driver.switchTo().alert();
        final String seenText = alert.getText();

        return seenText;
    }

    public boolean isElementPresent(final By by) {
        int count = 0;

        try {
            count = WebUIDriver.getWebDriver().findElements(by).size();
        } catch (final RuntimeException e) {

            if ((e instanceof InvalidSelectorException) ||
                (
                    (e.getMessage() != null) &&
                        e.getMessage().contains(
                            "TransformedEntriesMap cannot be cast to java.util.List"
                        )
                )) {
                TestLogging.log(
                    "InvalidSelectorException or CastException got, retry"
                );
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

    public static boolean isTextPresent(final String text) {
        CustomAssertion.assertNotNull(
                text,
                "isTextPresent: text should not be null!"
        );

        final WebElement body = WebUIDriver.getWebDriver().findElement(By.tagName("body"));
        return body.getText().contains(text);
    }

    public void selectFrame(final By by) {
        TestLogging.logWebStep(
            "select frame, locator={\"" + by.toString() + "\"}", false
        );
        driver.switchTo().frame(driver.findElement(by));
    }

    /**
     * If current window is closed then use driver.switchTo.window(handle).
     *
     * @param windowName
     *
     * @throws com.seleniumtests.customexception.NotCurrentPageException
     */
    public final void selectWindow(final String windowName)
        throws NotCurrentPageException {

        if (windowName == null) {
            try {
                driver.switchTo().window(windowName);
            } catch (final Exception e) {
                driver.switchTo().defaultContent();
            }
        }    
    }
    
    public void refreshPageTillTextAppears(final String text, final int waitPeriodInSec) throws InterruptedException {
        for(int i=0;i<=waitPeriodInSec;i++){
            if(!driver.getPageSource().contains(text)) {
                Thread.sleep(1000);
                driver.navigate().refresh();
            } else {
                break;
            }
        }
    }

    public static void waitForElementChecked(final HtmlElement element) {
        Assert.assertNotNull(element, "Element can't be null");
        TestLogging.logWebStep(
            "wait for " + element.toString() + " to be checked.", false
        );

        final WebDriverWait wait = new WebDriverWait(WebUIDriver.getWebDriver(), EXPLICT_WAIT_TIMEOUT);
        wait.until(ExpectedConditions.elementToBeSelected(element.getBy()));
    }

    public static void waitForElementEditable(final HtmlElement element) {
        Assert.assertNotNull(element, "Element can't be null");
        TestLogging.logWebStep(
            "wait for " + element.toString() + " to be editable.", false
        );

        final WebDriverWait wait = new WebDriverWait(WebUIDriver.getWebDriver(), EXPLICT_WAIT_TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(element.getBy()));
    }

    public static void waitForElementPresent(final By by) {
        TestLogging.logWebStep(
            "wait for " + by.toString() + " to be present.", false
        );

        final WebDriverWait wait = new WebDriverWait(WebUIDriver.getWebDriver(), EXPLICT_WAIT_TIMEOUT);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public static void waitForElementPresent(final By by, final int timeout) {
        TestLogging.logWebStep(
            "wait for " + by.toString() + " to be present.", false
        );

        final WebDriverWait wait = new WebDriverWait(WebUIDriver.getWebDriver(), timeout);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public static void waitForElementPresent(final HtmlElement element) {
        Assert.assertNotNull(element, "Element can't be null");
        TestLogging.logWebStep(
            "wait for " + element.toString() + " to be present.", false
        );

        final WebDriverWait wait = new WebDriverWait(WebUIDriver.getWebDriver(), EXPLICT_WAIT_TIMEOUT);
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                    element.getBy()
            )
        );
    }

    public static void waitForElementToBeVisible(final HtmlElement element) {
        Assert.assertNotNull(element, "Element can't be null");
        TestLogging.logWebStep(
            "wait for " + element.toString() + " to be visible.", false
        );

        final WebDriverWait wait = new WebDriverWait(WebUIDriver.getWebDriver(), EXPLICT_WAIT_TIMEOUT);
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                element.getBy()
            )
        );
    }

    public static void waitForURLToChange(
        final String match,
        final int waitSeconds,
        final boolean partialMatch
    ) {

        for (int i = 0; i < waitSeconds; i++) {
            if (partialMatch) {
                if (WebUIDriver.getWebDriver().getCurrentUrl().contains(match)) {
                    break;
                } else {
                    WaitHelper.waitForSeconds(1);
                }
            } else {
                if (WebUIDriver.getWebDriver().getCurrentUrl().equals(match)) {
                    break;
                } else {
                    WaitHelper.waitForSeconds(1);                }
            }
        }
    }

    public static void switchToWindow(
        final Set<String> allWindows,
        final String currentWindow
    ) {

        for (final String window : allWindows) {

            if (!window.equals(currentWindow)) {
                WebUIDriver.getWebDriver().switchTo().window(window);
            }
        }
    }

    public static void waitForElementToDisappear(final HtmlElement element) {
        Assert.assertNotNull(element, "Element can't be null");
        TestLogging.logWebStep(
            "wait for " + element.toString() + " to disappear.", false
        );

        final WebDriverWait wait = new WebDriverWait(WebUIDriver.getWebDriver(), EXPLICT_WAIT_TIMEOUT);
        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        element.getBy()
                )
        );
    }

    /**
     * Wait For seconds. Provide a value less than WebSessionTimeout i.e. 180 Seconds
     *
     * @param seconds
     */
    protected void waitForSeconds(final int seconds) {
        WaitHelper.waitForSeconds(seconds);
    }

    public static void waitForTextPresent(
        final HtmlElement element,
        final String text
    ) {
        Assert.assertNotNull(text, "Text can't be null");
        TestLogging.logWebStep(
            "wait for text \"" + text + "\" to be present.", false
        );

        final WebDriverWait wait = new WebDriverWait(WebUIDriver.getWebDriver(), EXPLICT_WAIT_TIMEOUT);
        wait.until(
            ExpectedConditions.textToBePresentInElementLocated(
                element.getBy(),
                text
            )
        );
    }

    public void waitForTextPresent(final String text) {
        Assert.assertNotNull(text, "Text can't be null");
        TestLogging.logWebStep(
            "wait for text \"" + text + "\" to be present.", false
        );

        boolean b = false;

        for (
            int millisec = 0; millisec < (EXPLICT_WAIT_TIMEOUT * 1000);
            millisec += 1000
            ) {

            try {

                if ((isTextPresent(text))) {
                    b = true;

                    break;
                }
            } catch (final Exception ignore) {
            }

            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        assertHTML(
            b,
            "Timed out waiting for text \"" + text + "\" to be there."
        );
    }

    public void waitForTextToDisappear(final String text) {
        Assert.assertNotNull(text, "Text can't be null");
        TestLogging.logWebStep(
            "wait for text \"" + text + "\" to disappear.", false
        );

        boolean textPresent = true;

        for (
            int millisec = 0; millisec < (EXPLICT_WAIT_TIMEOUT * 1000);
            millisec += 1000
            ) {

            try {

                if (!(isTextPresent(text))) {
                    textPresent = false;

                    break;
                }
            } catch (final Exception ignore) {
            }

            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        assertHTML(
            !textPresent,
            "Timed out waiting for text \"" + text + "\" to be gone."
        );
    }

    public void waitForTextToDisappear(
        final String text,
        final int explicitWaitTimeout
    ) {
        Assert.assertNotNull(text, "Text can't be null");
        TestLogging.logWebStep(
            "wait for text \"" + text + "\" to disappear.", false
        );

        boolean textPresent = true;

        for (
            int millisec = 0; millisec < (explicitWaitTimeout * 1000);
            millisec += 1000
            ) {

            try {

                if (!(isTextPresent(text))) {
                    textPresent = false;

                    break;
                }
            } catch (final Exception ignore) {
            }

            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        assertHTML(
            !textPresent,
            "Timed out waiting for text \"" + text + "\" to be gone."
        );
    }

    public void waitForURLToBeginWith(String regex, int waitCount) throws InterruptedException {
        for (int i = 0; i < waitCount; i++) {
            if (getDriver().getCurrentUrl().matches(regex)) {
                break;
            } else {
                Thread.sleep(1000);
            }
        }
    }

    /**
     * Closes all windows except the one supplied
     *
     * @param windowStream
     * @param windowToRemainOpen
     * @return BasePage
     */
    public BasePage closeOtherWindows(Stream<String>windowStream, String windowToRemainOpen) {
        windowStream
                .filter(windowHandle -> !windowHandle.equals(windowToRemainOpen))
                .forEach(windowHandle -> getDriver()
                        .switchTo()
                        .window(windowHandle)
                        .close()
                );
        getDriver().switchTo().window(windowToRemainOpen);
        return this;
    }

    public BasePage waitForGivenWindowCount(int windowCount, int... waitPeriodInSec) throws InterruptedException {
        int defaultWaitPeriod = 10;
        if(waitPeriodInSec.length>0) {
            defaultWaitPeriod = waitPeriodInSec[0];
        }
            for(int i=0; i<defaultWaitPeriod;i++) {
                if(getDriver().getWindowHandles().size() == windowCount){
                    break;
                } else {
                    Thread.sleep(100);
                }
            }
        return this;
    }
}
