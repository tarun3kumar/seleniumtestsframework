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

package com.seleniumtests.driver;

import java.io.IOException;

import org.apache.log4j.Logger;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.seleniumtests.core.SeleniumTestsContext;
import com.seleniumtests.core.SeleniumTestsContextManager;
import com.seleniumtests.core.TestLogging;

import com.seleniumtests.customexception.WebSessionEndedException;

import com.seleniumtests.helper.FileUtility;
import com.seleniumtests.helper.HashCodeGenerator;

public class ScreenshotUtil {
    private static final Logger logger = Logger.getLogger(ScreenshotUtil.class);

    public static String captureEntirePageScreenshotToString(final WebDriver driver, final String arg0) {
        if (driver == null) {
            return "";
        }

        try {

            // Don't capture snapshot for htmlunit
            if (WebUIDriver.getWebUIDriver().getBrowser().equalsIgnoreCase(BrowserType.HtmlUnit.getBrowserType())) {
                return null;
            }

            if (WebUIDriver.getWebUIDriver().getBrowser().equalsIgnoreCase(BrowserType.Android.getBrowserType())) {
                return null;
            }

            TakesScreenshot screenShot = (TakesScreenshot) driver;
            return screenShot.getScreenshotAs(OutputType.BASE64);
        } catch (Exception ex) {

            // Ignore all exceptions
            ex.printStackTrace();
        }

        return "";
    }

    private String suiteName;
    private String outputDirectory;
    private WebDriver driver;
    private String filename;

    public ScreenshotUtil() {
        suiteName = getSuiteName();
        outputDirectory = getOutputDirectory();
        this.driver = WebUIDriver.getWebDriver();
    }

    public ScreenshotUtil(final WebDriver driver) {
        suiteName = getSuiteName();
        outputDirectory = getOutputDirectory();
        this.driver = driver;
    }

    private static String getSuiteName() {
        String suiteName = null;

        suiteName = SeleniumTestsContextManager.getGlobalContext().getTestNGContext().getSuite().getName();

        return suiteName;
    }

    private static String getOutputDirectory() {
        String outputDirectory = null;
        outputDirectory = SeleniumTestsContextManager.getGlobalContext().getTestNGContext().getOutputDirectory();

        return outputDirectory;
    }

    private void handleSource(String htmlSource, final ScreenShot screenShot) {
        if (htmlSource == null) {

            // driver.switchTo().defaultContent();
            htmlSource = driver.getPageSource();
        }

        if (htmlSource != null) {
            try {
                FileUtility.writeToFile(outputDirectory + "/htmls/" + filename + ".html", htmlSource);
                screenShot.setHtmlSourcePath(suiteName + "/htmls/" + filename + ".html");
            } catch (IOException e) {
                logger.warn("Ex", e);
            }

        }
    }

    private void handleImage(final ScreenShot screenShot) {
        try {
            String screenshotString = captureEntirePageScreenshotToString(WebUIDriver.getWebDriver(), "");

            if (screenshotString != null && !screenshotString.equalsIgnoreCase("")) {
                byte[] byteArray = screenshotString.getBytes();
                FileUtility.writeImage(outputDirectory + "/screenshots/" + filename + ".png", byteArray);
                screenShot.setImagePath(suiteName + "/screenshots/" + filename + ".png");

            }
        } catch (Throwable e) {
            logger.warn("Ex", e);
            e.printStackTrace();
        }
    }

    private void handleTitle(String title, final ScreenShot screenShot) {
        if (title == null) {

            // driver.switchTo().defaultContent();
            title = driver.getTitle();
        }

        if (title == null) {
            title = "";
        }

        screenShot.setTitle(title);
    }

    public ScreenShot captureWebPageSnapshot() {

        ScreenShot screenShot = new ScreenShot();

        if (SeleniumTestsContextManager.getThreadContext() == null || outputDirectory == null) {
            return screenShot;
        }

        screenShot.setSuiteName(this.suiteName);

        try {
            String url = null;
            try {
                url = driver.getCurrentUrl();
            } catch (org.openqa.selenium.UnhandledAlertException ex) {

                // ignore alert customexception
                ex.printStackTrace();
                url = driver.getCurrentUrl();
            }

            String title = driver.getTitle();
            String pageSource = driver.getPageSource();

            String filename = HashCodeGenerator.getRandomHashCode("web");
            this.filename = filename;
            screenShot.setLocation(url);

            handleTitle(title, screenShot);
            handleSource(pageSource, screenShot);
            if (SeleniumTestsContextManager.getThreadContext().getCaptureSnapshot()) {
                handleImage(screenShot);
            }
        } catch (WebSessionEndedException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (SeleniumTestsContextManager.getThreadContext().getCaptureSnapshot()) {
            SeleniumTestsContextManager.getThreadContext().addScreenShot(screenShot);
        }

        return screenShot;
    }

    /**
     * Used by DriverExceptionListener, don't log the customexception but put it into context.
     */
    public void capturePageSnapshotOnException() {
        Boolean capture = SeleniumTestsContextManager.getThreadContext().getCaptureSnapshot();
        SeleniumTestsContextManager.getThreadContext().setAttribute(SeleniumTestsContext.CAPTURE_SNAPSHOT, "true");
        captureWebPageSnapshot();
        SeleniumTestsContextManager.getThreadContext().setAttribute(SeleniumTestsContext.CAPTURE_SNAPSHOT,
            Boolean.toString(capture));

        // SeleniumTestsContextManager.getThreadContext().setScreenshotName(filename);
        // SeleniumTestsContextManager.getThreadContext().setWebExceptionURL(location);
        // SeleniumTestsContextManager.getThreadContext().setWebExceptionMessage(title + " ("
        // + sbMessage.toString() + ")");
        // screenShot.setException(true);
        if (SeleniumTestsContextManager.getThreadContext().getScreenshots().size() > 0) {
            SeleniumTestsContextManager.getThreadContext().getScreenshots().getLast().setException(true);
        }
    }

    public static void captureSnapshot(final String messagePrefix) {
        if (SeleniumTestsContextManager.getThreadContext() != null
                && SeleniumTestsContextManager.getThreadContext().getCaptureSnapshot()
                && getOutputDirectory() != null) {
            String filename = HashCodeGenerator.getRandomHashCode("HtmlElement");
            StringBuffer sbMessage = new StringBuffer();
            try {
                String img = ScreenshotUtil.captureEntirePageScreenshotToString(WebUIDriver.getWebDriver(), "");
                if (img == null) {
                    return;
                }

                byte[] byteArray = img.getBytes();
                if (byteArray != null && byteArray.length > 0) {
                    String imgFile = "/screenshots/" + filename + ".png";
                    FileUtility.writeImage(getOutputDirectory() + imgFile, byteArray);

                    ScreenShot screenShot = new ScreenShot();
                    String imagePath = getSuiteName() + imgFile;
                    screenShot.setImagePath(imagePath);
                    SeleniumTestsContextManager.getThreadContext().addScreenShot(screenShot);
                    sbMessage.append(messagePrefix + ": <a href='" + imagePath
                            + "' class='lightbox'>Application Snapshot</a>");
                    TestLogging.logWebOutput(null, sbMessage.toString(), false);
                    sbMessage = null;
                }
            } catch (WebSessionEndedException ex) {
                throw ex;
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }
}
