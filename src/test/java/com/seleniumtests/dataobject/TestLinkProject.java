package com.seleniumtests.dataobject;

/**
 * Attributes of a test link project
 *
 * Date: 10/4/13
 * Time: 8:28 AM
 */
public class TestLinkProject {

    boolean createFromExisting;
    String name;
    String testCaseIDPrefix;
    String projectDescription;
    boolean enableRequirementFeature;
    boolean enableTestingPriority;
    boolean enableTestAutomation;
    boolean enableInventory;
    boolean issueTrackerNavigation;
    boolean activelyAvailable;
    boolean publiclyAvailable;

    public boolean isCreateFromExisting() {
        return createFromExisting;
    }

    public void setCreateFromExisting(boolean createFromExisting) {
        this.createFromExisting = createFromExisting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTestCaseIDPrefix() {
        return testCaseIDPrefix;
    }

    public void setTestCaseIDPrefix(String testCaseIDPrefix) {
        this.testCaseIDPrefix = testCaseIDPrefix;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public boolean isEnableRequirementFeature() {
        return enableRequirementFeature;
    }

    public void setEnableRequirementFeature(boolean enableRequirementFeature) {
        this.enableRequirementFeature = enableRequirementFeature;
    }

    public boolean isEnableTestingPriority() {
        return enableTestingPriority;
    }

    public void setEnableTestingPriority(boolean enableTestingPriority) {
        this.enableTestingPriority = enableTestingPriority;
    }

    public boolean isEnableTestAutomation() {
        return enableTestAutomation;
    }

    public void setEnableTestAutomation(boolean enableTestAutomation) {
        this.enableTestAutomation = enableTestAutomation;
    }

    public boolean isEnableInventory() {
        return enableInventory;
    }

    public void setEnableInventory(boolean enableInventory) {
        this.enableInventory = enableInventory;
    }

    public boolean isIssueTrackerNavigation() {
        return issueTrackerNavigation;
    }

    public void setIssueTrackerNavigation(boolean issueTrackerNavigation) {
        this.issueTrackerNavigation = issueTrackerNavigation;
    }

    public boolean isActivelyAvailable() {
        return activelyAvailable;
    }

    public void setActivelyAvailable(boolean activelyAvailable) {
        this.activelyAvailable = activelyAvailable;
    }

    public boolean isPubliclyAvailable() {
        return publiclyAvailable;
    }

    public void setPubliclyAvailable(boolean publiclyAvailable) {
        this.publiclyAvailable = publiclyAvailable;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("TestLinkProject [Create From Existing = "+createFromExisting+", ")
                .append("Project Name ="+name+", ")
                .append("Test case ID Prefix = "+testCaseIDPrefix+"]").toString();
    }
}
