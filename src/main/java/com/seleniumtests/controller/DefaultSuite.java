package com.seleniumtests.controller;

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

public class DefaultSuite implements ISuite {

	private static final long serialVersionUID = -1778730636766300291L;
	private XmlSuite xmlSuite;
	public DefaultSuite(){
		this.xmlSuite = new DefaultXmlSuite();
	}
	
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void setAttribute(String name, Object value) {
		// TODO Auto-generated method stub
		
	}

	
	public Set<String> getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Object removeAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getName() {
		return "Default suite";
	}

	
	public Map<String, ISuiteResult> getResults() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IObjectFactory getObjectFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IObjectFactory2 getObjectFactory2() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getOutputDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getParallel() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getParameter(String parameterName) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Map<String, Collection<ITestNGMethod>> getMethodsByGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Collection<ITestNGMethod> getInvokedMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<IInvokedMethod> getAllInvokedMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Collection<ITestNGMethod> getExcludedMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void run() {
		// TODO Auto-generated method stub
		
	}

	
	public String getHost() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public SuiteRunState getSuiteState() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IAnnotationFinder getAnnotationFinder() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public XmlSuite getXmlSuite() {
		// TODO Auto-generated method stub
		return xmlSuite;
	}

	
	public void addListener(ITestNGListener listener) {
		// TODO Auto-generated method stub
		
	}

	
	public List<ITestNGMethod> getAllMethods() {
		// TODO Auto-generated method stub
		return null;
	}

}
