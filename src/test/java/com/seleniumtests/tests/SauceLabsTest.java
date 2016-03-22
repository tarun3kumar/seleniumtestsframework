package com.seleniumtests.tests;


import com.seleniumtests.core.SeleniumTestPlan;

import com.seleniumtests.webpage.SauceLabsPage;

import org.testng.annotations.Test;


public class SauceLabsTest extends SeleniumTestPlan {

    public SauceLabsTest() throws Exception {
    }

    @Test(groups = "sauce")
    public void sauceLabsTest() throws Exception {
        SauceLabsPage sauceLabsPage = new SauceLabsPage(true);
        sauceLabsPage.getMeToTheSauceLabs();
    }
}
