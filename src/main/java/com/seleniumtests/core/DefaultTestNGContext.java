package com.seleniumtests.core;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;

import org.testng.xml.XmlTest;

import com.google.inject.Injector;
import com.google.inject.Module;

public class DefaultTestNGContext implements ITestContext {

    private static final long serialVersionUID = 2710769637263878789L;
    ISuite suite;

    public DefaultTestNGContext() {
        this.suite = new SeleniumTestsDefaultSuite();
    }

    public Object getAttribute(final String name) {
        return null;
    }

    public void setAttribute(final String name, final Object value) { }

    public Set<String> getAttributeNames() {
        return null;
    }

    public Object removeAttribute(final String name) {
        return null;
    }

    public String getName() {
        return null;
    }

    public Date getStartDate() {
        return null;
    }

    public Date getEndDate() {
        return null;
    }

    public IResultMap getPassedTests() {
        return null;
    }

    public IResultMap getSkippedTests() {
        return null;
    }

    public IResultMap getFailedButWithinSuccessPercentageTests() {
        return null;
    }

    public IResultMap getFailedTests() {
        return null;
    }

    public String[] getIncludedGroups() {
        return null;
    }

    public String[] getExcludedGroups() {
        return null;
    }

    public String getOutputDirectory() {
        return this.getClass().getResource("/").getPath() + "../../test-output/defaultSuite";
    }

    public ISuite getSuite() {
        return suite;
    }

    public ITestNGMethod[] getAllTestMethods() {
        return null;
    }

    public String getHost() {
        return null;
    }

    public Collection<ITestNGMethod> getExcludedMethods() {
        return null;
    }

    public IResultMap getPassedConfigurations() {
        return null;
    }

    public IResultMap getSkippedConfigurations() {
        return null;
    }

    public IResultMap getFailedConfigurations() {
        return null;
    }

    public XmlTest getCurrentXmlTest() {
        return null;
    }

    public List<Module> getGuiceModules(final Class<? extends Module> cls) {
        return null;
    }

    public void addGuiceModule(final Class<? extends Module> cls, final Module module) { }

    public Injector getInjector(final List<Module> moduleInstances) {
        return null;
    }

    public void addInjector(final List<Module> moduleInstances, final Injector injector) { }

}
