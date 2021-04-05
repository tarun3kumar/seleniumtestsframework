package com.seleniumtests.tests;

import org.testng.annotations.Test;

import com.seleniumtests.core.SeleniumTestPlan;
import com.seleniumtests.webpage.RegistrationPage;

public class AndroidWebTest extends SeleniumTestPlan {

    @Test()
    public void testMobileWeb() throws Exception {
        RegistrationPage registrationPage = new RegistrationPage(true);
        assert registrationPage.getTitle().equals("Test Registration Page") : "unable to launch Webdriver forum";
    }

}
