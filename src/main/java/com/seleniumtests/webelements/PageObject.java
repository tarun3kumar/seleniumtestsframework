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

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.seleniumtests.core.CustomAssertion;
import com.seleniumtests.core.SeleniumTestsContextManager;
import com.seleniumtests.core.SeleniumTestsPageListener;
import com.seleniumtests.core.TestLogging;
import com.seleniumtests.customexception.CustomSeleniumTestsException;
import com.seleniumtests.customexception.NotCurrentPageException;
import com.seleniumtests.driver.ScreenShot;
import com.seleniumtests.driver.ScreenshotUtil;
import com.seleniumtests.driver.WebUIDriver;
import com.seleniumtests.driver.WebUtility;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.testng.Assert;

import java.util.Calendar;
import java.util.Date;


public class PageObject extends BasePage implements IPage {

    private static final Logger logger = Logger.getLogger(PageObject.class);
    private static final int MAX_WAIT_TIME_FOR_REDIRECTION = 3;
    private boolean frameFlag = false;
    private HtmlElement pageIdentifierElement = null;
    private final String popupWindowName = null;
    private String windowHandle = null;
    private String title = null;
    private String url = null;
    private String bodyText = null;
    private String htmlSource = null;
    private String htmlSavedToPath = null;
    private String suiteName = null;
    private String outputDirectory = null;
    private String htmlFilePath = null;
    private String imageFilePath = null;

    /**
     * Constructor for non-entry point page. The control is supposed to have reached the page from other API call.
     *
     * @throws Exception
     */
    public PageObject() throws Exception {
        this(null, null);
    }

    /**
     * Constructor for non-entry point page. The control is supposed to have reached the page from other API call.
     *
     * @param pageIdentifierElement
     * @throws Exception
     */
    public PageObject(final HtmlElement pageIdentifierElement)
            throws Exception {
        this(pageIdentifierElement, null);
    }

    /**
     * Base Constructor.
     *
     * @param url
     * @throws Exception
     */
    public PageObject(final HtmlElement pageIdentifierElement, final String url)
            throws Exception {
        final Calendar start = Calendar.getInstance();
        start.setTime(new Date());

        if ((SeleniumTestsContextManager.getGlobalContext() != null) &&
                (SeleniumTestsContextManager.getGlobalContext()
                        .getTestNGContext() != null)) {
            suiteName = SeleniumTestsContextManager.getGlobalContext()
                    .getTestNGContext().getSuite().getName();
            outputDirectory = SeleniumTestsContextManager.getGlobalContext()
                    .getTestNGContext().getOutputDirectory();
        }

        this.pageIdentifierElement = pageIdentifierElement;
        driver = WebUIDriver.getWebDriver();

        if (url != null) {
            open(url);
        }

        // Wait for page load is applicable only for web test
        // When running tests on an iframe embedded site then test will fail if this command is not used
        if (SeleniumTestsContextManager.isWebTest()) {
            waitForPageToLoad();
        }

        assertCurrentPage(false);

        try {
            this.windowHandle = driver.getWindowHandle();
        } catch (final Exception ex) {
            // Ignore for OperaDriver
        }

        SeleniumTestsPageListener.informPageLoad(this);

        final Calendar end = Calendar.getInstance();
        start.setTime(new Date());

        final long startTime = start.getTimeInMillis();
        final long endTime = end.getTimeInMillis();

        if (((endTime - startTime) / 1000) > 0) {
            TestLogging.log("Open web page in :" +
                    ((endTime - startTime) / 1000) + "seconds");
        }
    }

    public void assertCookiePresent(final String name) {
        TestLogging.logWebStep("assert cookie " + name + " is present.",
                false);
        assertHTML(getCookieByName(name) != null,
                "Cookie: {" + name + "} not found.");
    }

    @Override
    protected void assertCurrentPage(final boolean log)
            throws NotCurrentPageException {

        if (pageIdentifierElement == null) {
        } else if (this.isElementPresent(pageIdentifierElement.getBy())) {
        } else {

            try {

                if (
                        !SeleniumTestsContextManager.getThreadContext()
                                .getCaptureSnapshot()) {
                    new ScreenshotUtil(driver).capturePageSnapshotOnException();
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }

            throw new NotCurrentPageException(getClass().getCanonicalName() +
                    " is not the current page.\nPageIdentifierElement " +
                    pageIdentifierElement.toString() + " is not found.");
        }

        if (log) {
            TestLogging.logWebStep(
                    "assert \"" + getClass().getSimpleName() +
                            "\" is the current page" +
                            ((pageIdentifierElement != null)
                                    ? (" (assert PageIdentifierElement " +
                                    pageIdentifierElement.toHTML() + " is present).")
                                    : "."), false);
        }
    }

    public void assertHtmlSource(final String text) {
        TestLogging.logWebStep(
                "assert text \"" + text + "\" is present in page source.", false);
        assertHTML(getHtmlSource().contains(text),
                "Text: {" + text + "} not found on page source.");
    }

    public void assertKeywordNotPresent(final String text) {
        TestLogging.logWebStep("assert text \"" + text + "\" is present in page source.", false);
        Assert.assertFalse(getHtmlSource().contains(text),
                "Text: {" + text + "} not found on page source.");
    }

    public void assertLocation(final String urlPattern) {
        TestLogging.logWebStep("assert location \"" + urlPattern + "\".",
                false);
        assertHTML(getLocation().contains(urlPattern),
                "Pattern: {" + urlPattern + "} not found on page location.");
    }

    public void assertPageSectionPresent(final WebPageSection pageSection) {
        TestLogging.logWebStep(
                "assert pagesection \"" + pageSection.getName() + "\"  is present.",
                false);
        assertElementPresent(new HtmlElement(pageSection.getName(),
                pageSection.getBy()));
    }

    public void assertTitle(final String text) {
        TestLogging.logWebStep(
                "assert text \"" + text + "\"  is present on title.", false);
        assertHTML(getTitle().contains(text),
                "Text: {" + text + "} not found on page title.");

    }

    public void capturePageSnapshot() {
        final ScreenShot screenShot = new ScreenshotUtil(driver)
                .captureWebPageSnapshot();
        this.title = screenShot.getTitle();

        if (screenShot.getHtmlSourcePath() != null) {
            htmlFilePath = screenShot.getHtmlSourcePath().replace(suiteName,
                    outputDirectory);
            htmlSavedToPath = screenShot.getHtmlSourcePath();
        }

        if (screenShot.getImagePath() != null) {
            imageFilePath = screenShot.getImagePath().replace(suiteName,
                    outputDirectory);
        }

        TestLogging.logWebOutput(url,
                title + " (" + TestLogging.buildScreenshotLog(screenShot) + ")",
                false);

    }

    public final void close() throws NotCurrentPageException {

        if (WebUIDriver.getWebDriver() == null) {
            return;
        }

        SeleniumTestsPageListener.informPageUnload(this);
        TestLogging.log("close web page");

        boolean isMultipleWindow = false;

        if (driver.getWindowHandles().size() > 1) {
            isMultipleWindow = true;
        }

        try {
            driver.close();
        } catch (final WebDriverException ignore) {
        }

        if (WebUIDriver.getWebUIDriver().getMode().equalsIgnoreCase("LOCAL")) {

            try {
                Thread.sleep(1000 * 2);
            } catch (final InterruptedException e) {
            }
        }

        try {

            if (isMultipleWindow) {
                this.selectWindow();
            } else {
                WebUIDriver.setWebDriver(null);
            }
        } catch (final UnreachableBrowserException ex) {
            WebUIDriver.setWebDriver(null);

        }
    }

    /**
     * Drags an element a certain distance and then drops it.
     *
     * @param element to dragAndDrop
     * @param offsetX in pixels from the current location to which the element should be moved, e.g., 70
     * @param offsetY in pixels from the current location to which the element should be moved, e.g., -300
     */
    public void dragAndDrop(final HtmlElement element, final int offsetX,
                            final int offsetY) {
        TestLogging.logWebStep(
                "dragAndDrop " + element.toHTML() + " to offset(x,y): (" + offsetX +
                        "," + offsetY + ")", false);
        element.captureSnapshot("before draging");

        new Actions(driver).dragAndDropBy((WebElement) element.getElement(),
                offsetX, offsetY).perform();
        element.captureSnapshot("after dropping");
    }

    public String getBodyText() {
        return bodyText;
    }

    public final String getCookieByName(final String name) {

        if (driver.manage().getCookieNamed(name) == null) {
            return null;
        }

        return driver.manage().getCookieNamed(name).getValue();
    }

    public final int getElementCount(final HtmlElement element)
            throws CustomSeleniumTestsException {
        return driver.findElements(element.getBy()).size();
    }

    public String getEval(final String expression) {
        CustomAssertion.assertTrue(false, "focus not implemented yet");

        return null;
    }

    public String getHtmlFilePath() {
        return htmlFilePath;
    }

    public String getHtmlSavedToPath() {
        return htmlSavedToPath;
    }

    public String getHtmlSource() {
        return htmlSource;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public String getLocation() {
        return driver.getCurrentUrl();
    }

    public String getPopupWindowName() {
        return popupWindowName;
    }

    public int getTimeout() {
        return SeleniumTestsContextManager.getThreadContext()
                .getWebSessionTimeout();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getCanonicalURL() {
        return new LinkElement("Canonical URL",
                By.cssSelector("link[rel=canonical]")).getAttribute("href");
    }

    public String getWindowHandle() {
        return windowHandle;
    }

    public final void goBack() {
        TestLogging.logWebStep("goBack", false);
        driver.navigate().back();
        frameFlag = false;
    }

    public final void goForward() {
        TestLogging.logWebStep("goForward", false);
        driver.navigate().forward();
        frameFlag = false;
    }

    public final boolean isCookiePresent(final String name) {
        return getCookieByName(name) != null;
    }

    public boolean isFrame() {
        return frameFlag;
    }

    public final void maximizeWindow() {
        new WebUtility(driver).maximizeWindow();
    }

    private void open(final String url) throws Exception {

        if (this.getDriver() == null) {
            TestLogging.logWebStep("Launch application", false);
            driver = webUXDriver.createWebDriver();
        }

        setUrl(url);

        try {

            // Navigate to app URL for browser test
            if (SeleniumTestsContextManager.isWebTest()) {
                driver.navigate().to(url);
            }
        } catch (final UnreachableBrowserException e) {

            // handle if the last window is closed
            TestLogging.logWebStep("Launch application", false);
            driver = webUXDriver.createWebDriver();
            maximizeWindow();
            driver.navigate().to(url);
        } catch (final UnsupportedCommandException e) {
            TestLogging.log("get UnsupportedCommandException, retry");
            driver = webUXDriver.createWebDriver();
            maximizeWindow();
            driver.navigate().to(url);
        } catch (final org.openqa.selenium.TimeoutException ex) {
            TestLogging.log("got time out when loading " + url + ", ignored");
        } catch (final org.openqa.selenium.UnhandledAlertException ex) {
            TestLogging.log("got UnhandledAlertException, retry");
            driver.navigate().to(url);
        } catch (final Throwable e) {
            e.printStackTrace();
            throw new CustomSeleniumTestsException(e);
        }

        // switchToDefaultContent();
    }

    private void populateAndCapturePageSnapshot() {

        try {
            setTitle(driver.getTitle());
            htmlSource = driver.getPageSource();

            try {
                bodyText = driver.findElement(By.tagName("body")).getText();
            } catch (final StaleElementReferenceException ignore) {
                logger.warn(
                        "StaleElementReferenceException got in populateAndCapturePageSnapshot");
                bodyText = driver.findElement(By.tagName("body")).getText();
            }

        } catch (final UnreachableBrowserException e) { // throw

            // UnreachableBrowserException
            throw new WebDriverException(e);
        } catch (final WebDriverException e) {
            throw e;
        }

        capturePageSnapshot();
    }

    public final void refresh() throws NotCurrentPageException {
        TestLogging.logWebStep("refresh", false);

        try {
            driver.navigate().refresh();
        } catch (final org.openqa.selenium.TimeoutException ex) {
            TestLogging.log("got time out customexception, ignore");
        }
    }

    public final void resizeTo(final int width, final int height) {
        new WebUtility(driver).resizeWindow(width, height);
    }

    public final void selectFrame(final int index) {
        TestLogging.logWebStep("select frame using index" + index, false);
        driver.switchTo().frame(index);
        frameFlag = true;
    }

    public final void selectFrame(final By by) {
        TestLogging.logWebStep("select frame, locator={\"" + by.toString() + "\"}", false);
        driver.switchTo().frame(driver.findElement(by));
        frameFlag = true;
    }

    public final void selectFrame(final String locator) {
        TestLogging.logWebStep(
                "select frame, locator={\"" + locator + "\"}", false);
        driver.switchTo().frame(locator);
        frameFlag = true;
    }

    public final void selectWindow() throws NotCurrentPageException {
        TestLogging.logWebStep(
                "select window, locator={\"" + getPopupWindowName() + "\"}", false);

        // selectWindow(getPopupWindowName());
        driver.switchTo().window((String)
                driver.getWindowHandles().toArray()[0]);
        waitForSeconds(1);

        // Check whether it's the expected page.
        assertCurrentPage(true);
    }

    public final void selectWindow(final int index)
            throws NotCurrentPageException {
        TestLogging.logWebStep(
                "select window, locator={\"" + index + "\"}", false);
        driver.switchTo().window((String)
                driver.getWindowHandles().toArray()[index]);
    }

    public final void selectNewWindow() throws NotCurrentPageException {
        TestLogging.logWebStep(
                "select new window", false);
        driver.switchTo().window((String)
                driver.getWindowHandles().toArray()[1]);
        waitForSeconds(1);
    }

    /**
     * Waits for given URL term to appear when there is URL redirect
     *
     * @param urlTerm
     * @param waitCount
     */
    public static void waitForGivenURLTermToAppear(final String urlTerm,
                                                   final int... waitCount) {
        int counter;
        if(waitCount.length == 0) {
            counter = 60;
        } else {
            counter = waitCount[0];
        }

        for (int index = 0; index < counter; index++) {

            if (WebUIDriver.getWebDriver().getCurrentUrl().contains(urlTerm)) {
                break;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                index = index + 1;
            }
        }
    }


    protected void setHtmlSavedToPath(final String htmlSavedToPath) {
        this.htmlSavedToPath = htmlSavedToPath;
    }

    protected void setTitle(final String title) {
        this.title = title;
    }

    protected void setUrl(final String openUrl) {
        this.url = openUrl;
    }

    public void switchToDefaultContent() {

        try {
            driver.switchTo().defaultContent();
        } catch (final UnhandledAlertException e) {
        }
    }

    private void waitForPageToLoad() throws Exception {
        try {
            populateAndCapturePageSnapshot();
        } catch (final Exception ex) {

            // ex.printStackTrace();
            throw ex;
        }
    }

    public WebElement getElement(final By by, final String elementName) {
        WebElement element = null;

        try {
            element = driver.findElement(by);
        } catch (final ElementNotFoundException e) {
            TestLogging.errorLogger(elementName +
                    " is not found with locator - " + by.toString());
            throw e;
        }

        return element;
    }

    public String getElementUrl(final By by, final String name) {
        return getElement(by, name).getAttribute("href");
    }

    public String getElementText(final By by, final String name) {
        return getElement(by, name).getText();
    }

    public String getElementSrc(final By by, final String name) {
        return getElement(by, name).getAttribute("src");
    }
}
