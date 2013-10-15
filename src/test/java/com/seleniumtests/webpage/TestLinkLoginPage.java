package com.seleniumtests.webpage;

import com.seleniumtests.core.CustomAssertion;
import com.seleniumtests.dataobject.User;
import com.seleniumtests.webelements.ButtonElement;
import com.seleniumtests.webelements.LinkElement;
import com.seleniumtests.webelements.TextFieldElement;
import com.seleniumtests.webelements.PageObject;
import org.openqa.selenium.By;

/**
 * Defines service for TestLink login page
 *
 * Date: 10/2/13
 * Time: 6:15 PM
 */
public class TestLinkLoginPage extends PageObject {

    private static final String PAGE_URL = "http://www.seleniumtests.com/2013/08/demo-test-link-site.html";

    // Page identifier field is set to seleniumtests.com as TestLink appears in iframe
    private static LinkElement seleniumTrainingLink = new LinkElement("Selenium Training LinkElement", By.linkText("Free Selenium Training"));
    private static TextFieldElement loginTextBox = new TextFieldElement("Login Text Box", By.id("login"));


    public TestLinkLoginPage() throws Exception {
        // verifies that loginTextBox appears on loginAsValidUser page else test would not make sense
        super(loginTextBox);
    }

    /**
     * Opens log in page
     *
     * @param openPageUrl
     * @throws Exception
     */
    public TestLinkLoginPage (boolean openPageUrl) throws Exception {
        super(seleniumTrainingLink, openPageUrl ? PAGE_URL : null, true);
        getDriver().switchTo().frame(getDriver().findElement(By.id("testlink"))); // Switch to test link frame
    }

    private TextFieldElement passwordTextBox = new TextFieldElement("Password Te  xt Box", By.name("tl_password"));
    private ButtonElement loginButton = new ButtonElement("Login ButtonElement", By.name("login_submit"));

    /**
     * Logging in with valid credentials direct user to home page
     *
     * @param user
     * @return
     * @throws Exception
     */
    public AdminHomePage loginAsValidUser(User user) throws Exception {
        loginTextBox.clear();
        loginTextBox.sendKeys(user.getUserID());
        passwordTextBox.clear();
        passwordTextBox.sendKeys(user.getPassword());
        loginButton.submit();
        return new AdminHomePage();
    }

    /**
     * Logging in with invalid credentials keeps user on login page
     *
     * @param user
     * @return
     * @throws Exception
     */
    public TestLinkLoginPage loginAsInvalidUser(User user) throws Exception {
        loginTextBox.clear();
        loginTextBox.sendKeys(user.getUserID());
        passwordTextBox.clear();
        passwordTextBox.sendKeys(user.getPassword());
        loginButton.submit();
        getDriver().switchTo().defaultContent();
        return this;
    }

    public TestLinkLoginPage verifyLoginBoxPresence() {
        getDriver().switchTo().frame(getDriver().findElement(By.id("testlink")));
        CustomAssertion.assertTrue(isElementPresent(loginTextBox.getBy()), "Invalid credentials don't block user from logging in");
        return this;
    }
}