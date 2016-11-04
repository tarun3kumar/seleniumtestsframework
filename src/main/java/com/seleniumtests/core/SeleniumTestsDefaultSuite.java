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
import org.testng.*;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlSuite;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SeleniumTestsDefaultSuite implements ISuite {

    private static final long serialVersionUID = -152933123712833411L;
    private final XmlSuite xmlSuite;

    public SeleniumTestsDefaultSuite() {
        this.xmlSuite = new DefaultXmlSuite();
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
        return "Default suite";
    }

    public Map<String, ISuiteResult> getResults() {
        return null;
    }

    public IObjectFactory getObjectFactory() {
        return null;
    }

    public IObjectFactory2 getObjectFactory2() {
        return null;
    }

    public String getOutputDirectory() {
        return null;
    }

    public String getParallel() {
        return null;
    }

    @Override
    public String getParentModule() {
        return null;
    }

    @Override
    public String getGuiceStage() {
        return null;
    }

    public String getParameter(final String parameterName) {
        return null;
    }

    public Map<String, Collection<ITestNGMethod>> getMethodsByGroups() {
        return null;
    }

    public Collection<ITestNGMethod> getInvokedMethods() {
        return null;
    }

    public List<IInvokedMethod> getAllInvokedMethods() {
        return null;
    }

    public Collection<ITestNGMethod> getExcludedMethods() {
        return null;
    }

    public void run() { }

    public String getHost() {
        return null;
    }

    public SuiteRunState getSuiteState() {
        return null;
    }

    public IAnnotationFinder getAnnotationFinder() {
        return null;
    }

    public XmlSuite getXmlSuite() {
        return xmlSuite;
    }

    public void addListener(final ITestNGListener listener) { }

    @Override
    public Injector getParentInjector() {
        return null;
    }

    @Override
    public void setParentInjector(final Injector injector) {

    }

    public List<ITestNGMethod> getAllMethods() {
        return null;
    }

}
