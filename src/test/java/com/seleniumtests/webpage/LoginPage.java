package com.seleniumtests.webpage;

import com.seleniumtests.dataobject.User;

import com.seleniumtests.webelements.ButtonElement;
import com.seleniumtests.webelements.PageObject;
import com.seleniumtests.webelements.TextFieldElement;

import org.openqa.selenium.By;


/**
 * Created by tarun on 3/22/16.
 */
public class LoginPage extends PageObject {

    private static final TextFieldElement userNameTextBox =
        new TextFieldElement("Username Textbox", By.name("usernameLoginPage"));

    private static final TextFieldElement passwordTextBox =
        new TextFieldElement("Password Textbox", By.name("psw1"));

    private ButtonElement submitButton = new ButtonElement("submit Button",
            By.cssSelector("input~input[value='Submit']"));


    public LoginPage() throws Exception {
        super(userNameTextBox);
    }

    public LoginPage enterUserName(String userName) {
        userNameTextBox.clearAndType(userName);

        return this;
    }

    public LoginPage enterPassword(String password) {
        userNameTextBox.clearAndType(password);

        return this;
    }

    public static boolean isUserNameDisplayed() {
        return userNameTextBox.isDisplayed();
    }

    /**
     * Google Home Page is not part of seleniumtests.com but this how you can move from one page object to another
     * @return
     * @throws Exception
     */
    public GoogleHomePage clickSubmitButton() throws Exception {
        submitButton.click();

        return new GoogleHomePage();
    }

    public GoogleHomePage enterLoginData(User user) throws Exception {
        return enterUserName(user.getUserName()).enterPassword(
                user.getPassword()).clickSubmitButton();
    }
}
