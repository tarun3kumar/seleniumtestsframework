package com.seleniumtests.util.internal.entity;

import org.apache.commons.lang3.StringUtils;

public class TestEntity {
    public static final String TEST_CASE_ID = "TestEntity.TestCaseId";
    public static final String TEST_METHOD = "TestEntity.TestMethod";
    public static final String TEST_TITLE = "TestEntity.TestTitle";
    public static final String TEST_DP_TAGS = "TestEntity.TestTags";
    public static final String TEST_IS_ACTIVE = "TestEntity.IsActive";

    private String testCaseId = "";
    private String testMethod = "";
    private String testTitle = "";
    private String testSite = "";
    private boolean isActive = true;

    public String getTestCaseId() {
        return testCaseId;
    }

    public String getTestMethod() {
        return testMethod;
    }

    public String getTestSite() {
        return testSite;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

    public void setTestCaseId(final String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public void setTestMethod(final String testMethod) {
        this.testMethod = testMethod;
    }

    public void setTestSite(final String testSite) {
        this.testSite = testSite;
    }

    public void setTestTitle(final String testTitle) {
        this.testTitle = testTitle;
    }

    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("Test Attributes: [ TestCaseId: " + testCaseId);
        if (StringUtils.isNotEmpty(testMethod)) {
            ret.append("|| TestMethod: " + testMethod);
        }

        if (StringUtils.isNotEmpty(testTitle)) {
            ret.append("||TestTitle: " + testTitle);
        }

        if (!isActive) {
            ret.append("||IsActive: " + isActive);
        }

        ret.append(" ]");
        return ret.toString();
    }
}
