package com.seleniumtests.webpage;

import com.seleniumtests.dataobject.User;
import com.seleniumtests.driver.web.WebUXDriver;
import com.seleniumtests.driver.web.element.Button;
import com.seleniumtests.driver.web.element.Link;
import com.seleniumtests.driver.web.element.TextField;
import com.seleniumtests.driver.web.element.WebPage;
import org.openqa.selenium.By;

/**
 * User: tbhadauria
 * Date: 10/2/13
 * Time: 6:15 PM
 */
public class TestLinkLoginPage extends WebPage {

    private static final String PAGE_URL = "http://www.seleniumtests.com/2013/08/demo-test-link-site.html";

    // Page identifier field is set to seleniumtests.com as TestLink appears in iframe
    private static Link seleniumTrainingLink = new Link("Selenium Training Link", By.linkText("Free Selenium Training"));
    private static TextField loginTextBox = new TextField("Login Text Box", By.id("login"));


    public TestLinkLoginPage() throws Exception {
        // verifies that loginTextBox appears on login page else test would not make sense
        super(loginTextBox);
    }

    /**
     * Opens log in page
     * @param openPageUrl
     * @throws Exception
     */
    public TestLinkLoginPage (boolean openPageUrl) throws Exception {
        super(seleniumTrainingLink, openPageUrl ? PAGE_URL : null, true);
        getDriver().switchTo().frame(getDriver().findElement(By.id("testlink"))); // Switch to test link frame
    }

    private TextField passwordTextBox = new TextField("Password Te  xt Box", By.name("tl_password"));
    private Button loginButton = new Button("Login Button", By.name("login_submit"));


    public AdminHomePage login(User user) throws Exception {
        loginTextBox.clear();
        loginTextBox.sendKeys(user.getUserID());
        passwordTextBox.clear();
        passwordTextBox.sendKeys(user.getPassword());
        loginButton.submit();
        return new AdminHomePage();
    }
}