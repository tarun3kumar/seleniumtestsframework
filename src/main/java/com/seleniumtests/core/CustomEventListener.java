package com.seleniumtests.core;

import com.seleniumtests.webelements.HtmlElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import java.util.logging.Level;

/**
 * Created by tarun on 12.07.16.
 */
public class CustomEventListener extends AbstractWebDriverEventListener {

    boolean isJSErrorFound;


    private void logErrors(String url, LogEntries logEntries) {
        if (logEntries.getAll().size() == 0) {
            TestLogging.log("********* No Severe Error on Browser Console *********", true);
        } else {
            for (LogEntry logEntry : logEntries) {
                if (logEntry.getLevel().equals(Level.SEVERE)) {
                    TestLogging.log("URL: "+url);
                    TestLogging.logWebStep("Time stamp: " + logEntry.getTimestamp() + ", " +
                            "Log level: " + logEntry
                            .getLevel() + ", Log message: " + logEntry.getMessage(), true);
                    isJSErrorFound = true;
                }
            }
            assert !isJSErrorFound;
        }
    }

    private void logErrors(String event, WebElement element, LogEntries logEntries) {
        if (logEntries.getAll().size() == 0) {
            TestLogging.log("********* No Severe Error on Browser Console *********", true);
        } else {
            for (LogEntry logEntry : logEntries) {
                if (logEntry.getLevel().equals(Level.SEVERE)) {
                    TestLogging.log("Sever Console Error on Browser "+event+" clicking " +
                            "element: " +((HtmlElement)element).getBy());
                    TestLogging.logWebStep("Time stamp: " + logEntry.getTimestamp() + ", " +
                            "Log level: " + logEntry
                            .getLevel() + ", Log message: " + logEntry.getMessage(), true);
                    isJSErrorFound = true;
                }
            }
            assert !isJSErrorFound;
        }
    }

    private LogEntries getBrowserLogs(WebDriver webDriver) {
        return webDriver.manage().logs().get(LogType.BROWSER);
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver webDriver) {
        logErrors(url, getBrowserLogs(webDriver));

    }

    @Override
    public void afterNavigateTo(String url, WebDriver webDriver) {
        logErrors(url, getBrowserLogs(webDriver));
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        logErrors("before", element, getBrowserLogs(driver));
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        logErrors("after", element, getBrowserLogs(driver));
    }
}
