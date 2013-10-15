package com.seleniumtests.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.IInvokedMethod;
import org.testng.IObjectFactory;
import org.testng.IObjectFactory2;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestNGListener;
import org.testng.ITestNGMethod;
import org.testng.SuiteRunState;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlSuite;

public class SeleniumTestsDefaultSuite implements ISuite {

	private static final long serialVersionUID = -152933123712833411L;
	private XmlSuite xmlSuite;
	public SeleniumTestsDefaultSuite(){
		this.xmlSuite = new DefaultXmlSuite();
	}
	
	public Object getAttribute(String name) {
		return null;
	}

	
	public void setAttribute(String name, Object value) {
	}

	
	public Set<String> getAttributeNames() {
		return null;
	}

	
	public Object removeAttribute(String name) {
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

	
	public String getParameter(String parameterName) {
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

	
	public void run() {
	}

	
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

	
	public void addListener(ITestNGListener listener) {
	}

	
	public List<ITestNGMethod> getAllMethods() {
		return null;
	}

}
