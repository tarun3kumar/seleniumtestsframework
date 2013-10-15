package com.seleniumtests.webpage;

import com.seleniumtests.webelements.ButtonElement;
import com.seleniumtests.webelements.SelectList;
import com.seleniumtests.webelements.TextFieldElement;
import com.seleniumtests.webelements.PageObject;
import org.openqa.selenium.By;

/**
 * Offers services provided by Test Project Management page
 *
 * Date: 10/4/13
 * Time: 9:03 AM
 */
public class TestProjectManagementPage extends PageObject {

    public TestProjectManagementPage() throws Exception {
        super();
    }

    private static ButtonElement createTestProjectButton = new ButtonElement("Create Test Project", By.id("create"));
    private static SelectList createFromExistingTestProject = new SelectList("Create from Existing", By.name("copy_from_tproject_id"));
    private static TextFieldElement name = new TextFieldElement("Name ", By.name("tprojectName"));
    private static TextFieldElement testCaseIdPrefix = new TextFieldElement("Text Case Prefix", By.name("tcasePrefix"));
    private static ButtonElement createButton = new ButtonElement("Create", By.name("doActionButton"));

    public TestProjectManagementPage switchToTestLinkFrame() {
        getDriver().switchTo().frame(getDriver().findElement(By.id("testlink")));
        return this;
    }

    public TestProjectManagementPage switchToTitleBarFrame() {
        getDriver().switchTo().frame(getDriver().findElement(By.name("titlebar")));
        return this;
    }

    public TestProjectManagementPage switchToMainFrame() {
        getDriver().switchTo().frame(getDriver().findElement(By.name("mainframe")));
        return this;
    }

    public TestProjectManagementPage clickCreateButton() {
        switchToTestLinkFrame();
        switchToMainFrame();
        createTestProjectButton.click();
        return this;
    }

    public TestProjectManagementPage selectCreateFromExistingTestProject(boolean createFromExisting) {
        if(! createFromExisting) {
            // do nothing
        }  else {
           // Select something
        }
        return this;
    }

    public TestProjectManagementPage enterProjectName(String projectName) {
        name.sendKeys(projectName);
        return this;
    }

    public TestProjectManagementPage enterPrefix(String prefix) {
        testCaseIdPrefix.sendKeys(prefix);
        return this;
    }

    public TestProjectManagementPage submitTestProject() {
        createButton.click();
        return this;
    }
}
