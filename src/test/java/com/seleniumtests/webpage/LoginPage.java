package com.seleniumtests.webpage;

import static com.seleniumtests.core.Locator.locateByCSSSelector;
import static com.seleniumtests.core.Locator.locateByName;

import com.seleniumtests.dataobject.User;

import com.seleniumtests.webelements.ButtonElement;
import com.seleniumtests.webelements.PageObject;
import com.seleniumtests.webelements.TextFieldElement;

/**
 * Created by tarun on 3/22/16.
 */
public class LoginPage extends PageObject {

    private static final TextFieldElement userNameTextBox = new TextFieldElement("Username Textbox",
            locateByName("usernameLoginPage"));

    private static final TextFieldElement passwordTextBox = new TextFieldElement("Password Textbox",
            locateByName("psw1"));

    private ButtonElement submitButton = new ButtonElement("submit Button",
            locateByCSSSelector("input~input[value='Submit']"));

    public LoginPage() throws Exception {
        super(userNameTextBox);
    }

    public LoginPage enterUserName(final String userName) {
        userNameTextBox.clearAndType(userName);

        return this;
    }

    public LoginPage enterPassword(final String password) {
        userNameTextBox.clearAndType(password);

        return this;
    }

    public static boolean isUserNameDisplayed() {
        return userNameTextBox.isDisplayed();
    }

    /**
     * Google Home Page is not part of seleniumtests.com but this how you can move from one page object to another.
     *
     * @return
     *
     * @throws  Exception
     */
    public GoogleHomePage clickSubmitButton() throws Exception {
        submitButton.click();

        return new GoogleHomePage();
    }

    public GoogleHomePage enterLoginData(final User user) throws Exception {
        return enterUserName(user.getUserName()).enterPassword(user.getPassword()).clickSubmitButton();
    }
}
