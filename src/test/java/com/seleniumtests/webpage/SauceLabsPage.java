
package com.seleniumtests.webpage;


import com.seleniumtests.core.SeleniumTestsContextManager;

import com.seleniumtests.webelements.PageObject;


public class SauceLabsPage extends PageObject {
    public SauceLabsPage() throws Exception {
    }

    public SauceLabsPage(final boolean openAPP) throws Exception {
        super(null,
            openAPP
                ? SeleniumTestsContextManager.getThreadContext()
                    .getSaucelabsURL() : null);
    }

    public void getMeToTheSauceLabs() {
        driver.get("https://saucelabs.com/test/guinea-pig");
        System.out.println("title of page is: " + driver.getTitle());
    }
}
