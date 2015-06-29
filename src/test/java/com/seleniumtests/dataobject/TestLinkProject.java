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

package com.seleniumtests.dataobject;

/**
 * Attributes of a test link project.
 *
 * <p/>Date: 10/4/13 Time: 8:28 AM
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

    public void setCreateFromExisting(final boolean createFromExisting) {
        this.createFromExisting = createFromExisting;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getTestCaseIDPrefix() {
        return testCaseIDPrefix;
    }

    public void setTestCaseIDPrefix(final String testCaseIDPrefix) {
        this.testCaseIDPrefix = testCaseIDPrefix;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(final String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public boolean isEnableRequirementFeature() {
        return enableRequirementFeature;
    }

    public void setEnableRequirementFeature(final boolean enableRequirementFeature) {
        this.enableRequirementFeature = enableRequirementFeature;
    }

    public boolean isEnableTestingPriority() {
        return enableTestingPriority;
    }

    public void setEnableTestingPriority(final boolean enableTestingPriority) {
        this.enableTestingPriority = enableTestingPriority;
    }

    public boolean isEnableTestAutomation() {
        return enableTestAutomation;
    }

    public void setEnableTestAutomation(final boolean enableTestAutomation) {
        this.enableTestAutomation = enableTestAutomation;
    }

    public boolean isEnableInventory() {
        return enableInventory;
    }

    public void setEnableInventory(final boolean enableInventory) {
        this.enableInventory = enableInventory;
    }

    public boolean isIssueTrackerNavigation() {
        return issueTrackerNavigation;
    }

    public void setIssueTrackerNavigation(final boolean issueTrackerNavigation) {
        this.issueTrackerNavigation = issueTrackerNavigation;
    }

    public boolean isActivelyAvailable() {
        return activelyAvailable;
    }

    public void setActivelyAvailable(final boolean activelyAvailable) {
        this.activelyAvailable = activelyAvailable;
    }

    public boolean isPubliclyAvailable() {
        return publiclyAvailable;
    }

    public void setPubliclyAvailable(final boolean publiclyAvailable) {
        this.publiclyAvailable = publiclyAvailable;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("TestLinkProject [Create From Existing = " + createFromExisting + ", ")
                            .append("Project Name =" + name + ", ")
                            .append("Test case ID Prefix = " + testCaseIDPrefix + ", ")
                            .append("Project Description =" + projectDescription + "]").toString();
    }
}
