package com.seleniumtests.driver;

/**
 * @author  tbhadauria <tarun.kumar.bhadauria@zalando.de>
 */
public enum TestType {

    WEB("web"),
    APP("app"),
    NON_GUI("NonGUI");

    String testType;

    TestType(final String testType) {
        this.testType = testType;
    }

    public String getTestType() {
        return testType;
    }

}
