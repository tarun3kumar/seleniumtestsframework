package com.seleniumtests.core;

import org.apache.log4j.Logger;

import com.seleniumtests.driver.web.element.IPage;
import com.seleniumtests.reporter.PluginsUtil;

/**
 * Plugin architecture for SEO, Java Script, Accessibility etc tests.
 */
public abstract class SeleniumTestsPageListener {
    protected static final Logger logger = TestLogging.getLogger(SeleniumTestsPageListener.class);

    /**
     * Informs all the page listeners on page Load
     *
     * @param page
     */
    public static void informPageLoad(IPage page) {
        PluginsUtil.getInstance().invokePageListeners(SeleniumTestsContextManager.getThreadContext().getTestMethodSignature(),
                page, true);
    }

    /**
     * Informs all the page listeners on page Unload
     *
     * @param page
     */
    public static void informPageUnload(IPage page) {
        PluginsUtil.getInstance().invokePageListeners(SeleniumTestsContextManager.getThreadContext().getTestMethodSignature(),
                page, false);
    }

    private String title;

    private boolean testResultEffected;

    public SeleniumTestsPageListener(String title, boolean testResultEffected) {
        this.title = title;
        this.testResultEffected = testResultEffected;
    }

    public SeleniumTestsPageListener() {
    }

    public String getTitle() {
        return title;
    }

    public boolean isTestResultEffected() {
        return testResultEffected;
    }

    public abstract void onPageLoad(IPage page);

    public abstract void onPageUnload(IPage page);
}
