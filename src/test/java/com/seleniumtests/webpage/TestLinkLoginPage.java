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

package com.seleniumtests.webpage;

import org.openqa.selenium.By;

import com.seleniumtests.core.SeleniumTestsContextManager;

import com.seleniumtests.dataobject.User;

import com.seleniumtests.webelements.ButtonElement;
import com.seleniumtests.webelements.LinkElement;
import com.seleniumtests.webelements.PageObject;
import com.seleniumtests.webelements.TextFieldElement;

/**
 * Defines service for TestLink login page.
 *
 * <p/>Date: 10/2/13 Time: 6:15 PM
 */
public class TestLinkLoginPage extends PageObject {

    // Page identifier field is set to seleniumtests.com as TestLink appears in iframe
    private static LinkElement pageHeader = new LinkElement("Page Header",
            By.cssSelector("img[alt='No Automated Testing']"));
    private static TextFieldElement loginTextBox = new TextFieldElement("Login Text Box", By.id("login"));

    public TestLinkLoginPage() throws Exception {

        // verifies that loginTextBox appears on loginAsValidUser page else test would not make sense
        super(loginTextBox);
    }

    /**
     * Opens log in page.
     *
     * @param   openPageUrl
     *
     * @throws  Exception
     */
    public TestLinkLoginPage(final boolean openPageUrl) throws Exception {
        super(pageHeader, openPageUrl ? SeleniumTestsContextManager.getThreadContext().getAppURL() : null);
        getDriver().switchTo().frame(getDriver().findElement(By.id("testlink"))); // Switch to test link frame
    }

    private TextFieldElement passwordTextBox = new TextFieldElement("Password Te  xt Box", By.name("tl_password"));
    private ButtonElement loginButton = new ButtonElement("Login ButtonElement", By.name("login_submit"));

    /**
     * Logging in with valid credentials direct user to home page.
     *
     * @param   user
     *
     * @return
     *
     * @throws  Exception
     */
    public AdminHomePage loginAsValidUser(final User user) throws Exception {
        loginTextBox.clear();
        loginTextBox.sendKeys(user.getUserID());
        passwordTextBox.clear();
        passwordTextBox.sendKeys(user.getPassword());
        loginButton.submit();
        return new AdminHomePage();
    }

    /**
     * Logging in with invalid credentials keeps user on login page.
     *
     * @param   user
     *
     * @return
     *
     * @throws  Exception
     */
    public TestLinkLoginPage loginAsInvalidUser(final User user) throws Exception {
        loginTextBox.clear();
        loginTextBox.sendKeys(user.getUserID());
        passwordTextBox.clear();
        passwordTextBox.sendKeys(user.getPassword());
        loginButton.submit();
        getDriver().switchTo().defaultContent();
        return this;
    }

    public boolean isLoginBoxDisplayed() {
        getDriver().switchTo().frame(getDriver().findElement(By.id("testlink")));

        boolean isDisplayed = loginTextBox.isDisplayed();
        return isDisplayed;
    }
}
