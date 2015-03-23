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

package com.seleniumtests.core;

import org.apache.log4j.Logger;

import com.seleniumtests.reporter.PluginsHelper;

import com.seleniumtests.webelements.IPage;

/**
 * Plugin architecture for SEO, Java Script, Accessibility etc functional tests.
 */
public abstract class SeleniumTestsPageListener {
    protected static final Logger logger = TestLogging.getLogger(SeleniumTestsPageListener.class);

    /**
     * Informs all the page listeners on page Load.
     *
     * @param  page
     */
    public static void informPageLoad(final IPage page) {
        PluginsHelper.getInstance().invokePageListeners(SeleniumTestsContextManager.getThreadContext()
                .getTestMethodSignature(), page, true);
    }

    /**
     * Informs all the page listeners on page Unload.
     *
     * @param  page
     */
    public static void informPageUnload(final IPage page) {
        PluginsHelper.getInstance().invokePageListeners(SeleniumTestsContextManager.getThreadContext()
                .getTestMethodSignature(), page, false);
    }

    private String title;

    private boolean testResultEffected;

    public SeleniumTestsPageListener(final String title, final boolean testResultEffected) {
        this.title = title;
        this.testResultEffected = testResultEffected;
    }

    public SeleniumTestsPageListener() { }

    public String getTitle() {
        return title;
    }

    public boolean isTestResultEffected() {
        return testResultEffected;
    }

    public abstract void onPageLoad(IPage page);

    public abstract void onPageUnload(IPage page);
}
