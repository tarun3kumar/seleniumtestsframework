package com.seleniumtests.webpage;

import com.seleniumtests.core.CustomAssertion;
import com.seleniumtests.webelements.LinkElement;
import com.seleniumtests.webelements.SelectList;
import com.seleniumtests.webelements.PageObject;
import org.openqa.selenium.By;

/**
 * Provides services offered by TestLink login Admin Page
 *
 * Date: 10/2/13
 * Time: 6:26 PM
 */
public class AdminHomePage extends PageObject {

    private static SelectList documentationDropDown = new SelectList("Documentation drop down", By.name("docs"));

    public AdminHomePage() throws Exception{
        super(); // No check on page identification
    }

    private LinkElement testProjectManagementLink = new LinkElement("Test Project Management LinkElement", By.linkText("Test Project Management"));
    private SelectList testProjectDropdown = new SelectList("Test Project Dropdown", By.name("testproject"));

    public AdminHomePage switchToTestLinkFrame() {
        getDriver().switchTo().frame(getDriver().findElement(By.id("testlink")));
        return this;
    }

    public AdminHomePage switchToTitleBarFrame() {
        getDriver().switchTo().frame(getDriver().findElement(By.name("titlebar")));
        return this;
    }

    public AdminHomePage switchToMainFrame() {
        getDriver().switchTo().frame(getDriver().findElement(By.name("mainframe")));
        return this;
    }

    public AdminHomePage selectGivenTestProject(int index) {
        switchToTestLinkFrame();
        switchToTitleBarFrame();
        testProjectDropdown.selectByIndex(index);
        getDriver().switchTo().defaultContent();
        return this;
    }

    public AdminHomePage verifyDocumentationDropDown() {
        switchToTestLinkFrame();
        switchToMainFrame();
        CustomAssertion.assertTrue(documentationDropDown.isDisplayed(), "Documentation drop down is missing");
        getDriver().switchTo().defaultContent();
        return this;
    }

    public AdminHomePage verifyDocumentationDropDownFail() {
        switchToTestLinkFrame();
        switchToMainFrame();
        CustomAssertion.assertTrue(!documentationDropDown.isDisplayed(), "Documentation drop down is missing");
        getDriver().switchTo().defaultContent();
        return this;
    }

    public TestProjectManagementPage clickTestProjectManagementLink() throws Exception {
        switchToTestLinkFrame();
        switchToMainFrame();
        testProjectManagementLink.click();
        return new TestProjectManagementPage();
    }





}
