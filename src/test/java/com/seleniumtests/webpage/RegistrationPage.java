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

import static com.seleniumtests.core.Locator.locateByCSSSelector;
import static com.seleniumtests.core.Locator.locateByName;

import com.seleniumtests.core.SeleniumTestsContextManager;

import com.seleniumtests.dataobject.User;

import com.seleniumtests.webelements.ButtonElement;
import com.seleniumtests.webelements.PageObject;
import com.seleniumtests.webelements.TextFieldElement;

/**
 * Provides services offered by Registration Page.
 *
 * <p/>Date: 10/2/13 Time: 6:26 PM
 */
public class RegistrationPage extends PageObject {

    private static final TextFieldElement firstNameTextbox = new TextFieldElement("First name text box",
            locateByName("firstname"));

    private TextFieldElement lastNameTextbox = new TextFieldElement("Last name text box", locateByName("lastname"));

    private TextFieldElement userNameTextbox = new TextFieldElement("user name text box", locateByName("username"));

    private TextFieldElement pwd1NameTextbox = new TextFieldElement("password 1 text box", locateByName("psw1"));

    private TextFieldElement pwd2NameTextbox = new TextFieldElement("password 2 text box", locateByName("psw2"));

    private ButtonElement submitButton = new ButtonElement("submit Button",
            locateByCSSSelector("input~input[value='Submit']"));

    public RegistrationPage(final boolean openPageURL) throws Exception {
        super(firstNameTextbox, openPageURL ? SeleniumTestsContextManager.getThreadContext().getAppURL() : null);
    }

    public RegistrationPage enterFirstName(final String firstName) {
        firstNameTextbox.clearAndType(firstName);

        return this;
    }

    public RegistrationPage enterLastName(final String lastName) {
        lastNameTextbox.clearAndType(lastName);

        return this;
    }

    public RegistrationPage enterUserName(final String userName) {
        userNameTextbox.clearAndType(userName);

        return this;
    }

    public RegistrationPage enterPassword(final String password) {
        pwd1NameTextbox.clearAndType(password);

        return this;
    }

    public RegistrationPage enterConfirmPassword(final String password) {
        pwd2NameTextbox.clearAndType(password);

        return this;
    }

    public LoginPage clickSubmitButton() throws Exception {
        submitButton.click();

        return new LoginPage();
    }

    public LoginPage submitValidRegistrationData(final User user) throws Exception {
        return enterFirstName(user.getFirstName()).enterLastName(user.getLastName()).enterUserName(user.getUserName())
                                                  .enterPassword(user.getPassword())
                                                  .enterConfirmPassword(user.getPassword()).clickSubmitButton();
    }

}
