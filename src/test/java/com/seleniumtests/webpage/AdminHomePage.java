package com.seleniumtests.webpage;

import com.seleniumtests.controller.Assertion;
import com.seleniumtests.driver.web.element.Link;
import com.seleniumtests.driver.web.element.SelectList;
import com.seleniumtests.driver.web.element.WebPage;
import org.openqa.selenium.By;

/**
 * Provides services offered by TestLink login Admin Page
 *
 * Date: 10/2/13
 * Time: 6:26 PM
 */
public class AdminHomePage extends WebPage {

    private static SelectList documentationDropDown = new SelectList("Documentation drop down", By.name("docs"));

    public AdminHomePage() throws Exception{
        super(); // No check on page identification
    }

    private Link testProjectManagementLink = new Link("Test Project Management Link", By.linkText("Test Project Management"));
    private SelectList testProjectDropdown = new SelectList("Test Project Dropdown", By.name("testproject"));

    public AdminHomePage selectGivenTestProject(int index) {
        getDriver().switchTo().frame(getDriver().findElement(By.id("testlink"))); // switch to test link frame
        getDriver().switchTo().frame(getDriver().findElement(By.name("titlebar"))); // switch to title bar frame
        testProjectDropdown.selectByIndex(index);
        getDriver().switchTo().defaultContent();
        return this;
    }

    public AdminHomePage verifyDocumentationDropDown() {
        getDriver().switchTo().frame(getDriver().findElement(By.id("testlink")));
        getDriver().switchTo().frame(getDriver().findElement(By.name("mainframe")));
        Assertion.assertTrue(documentationDropDown.isDisplayed(), "Documentation drop down is missing");
        getDriver().switchTo().defaultContent();
        return this;
    }

    /**
     * this is a false test and would always fail
     */
    public AdminHomePage verifyDocumentationDropDownFail() {
        getDriver().switchTo().frame(getDriver().findElement(By.id("testlink")));
        getDriver().switchTo().frame(getDriver().findElement(By.name("mainframe")));
        Assertion.assertTrue(!documentationDropDown.isDisplayed(), "Documentation drop down is missing");
        getDriver().switchTo().defaultContent();
        return this;
    }



}
