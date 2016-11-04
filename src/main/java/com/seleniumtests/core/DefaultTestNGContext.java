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

package com.seleniumtests.core;

import com.google.inject.Injector;
import com.google.inject.Module;
import org.testng.*;
import org.testng.xml.XmlTest;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    @Override
    public Injector getInjector(final IClass iClass) {
        return null;
    }

    public void addInjector(final List<Module> moduleInstances, final Injector injector) { }

}
