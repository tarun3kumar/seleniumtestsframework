package com.seleniumtests.controller;

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
	/**
	 * 
	 */
	private static final long serialVersionUID = 3809658526152767666L;
	ISuite suite;

	
	public DefaultTestNGContext() {
		this.suite = new DefaultSuite();
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
		// TODO Auto-generated method stub
		return null;
	}

	
	public Date getStartDate() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Date getEndDate() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IResultMap getPassedTests() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IResultMap getSkippedTests() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IResultMap getFailedButWithinSuccessPercentageTests() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IResultMap getFailedTests() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String[] getIncludedGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String[] getExcludedGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getOutputDirectory() {
		return this.getClass().getResource("/").getPath()
		+ "../../test-output/defaultSuite";
	}

	
	public ISuite getSuite() {
		return suite;
	}

	
	public ITestNGMethod[] getAllTestMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getHost() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Collection<ITestNGMethod> getExcludedMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IResultMap getPassedConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IResultMap getSkippedConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IResultMap getFailedConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public XmlTest getCurrentXmlTest() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<Module> getGuiceModules(Class<? extends Module> cls) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void addGuiceModule(Class<? extends Module> cls, Module module) {
		// TODO Auto-generated method stub
		
	}

	
	public Injector getInjector(List<Module> moduleInstances) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void addInjector(List<Module> moduleInstances, Injector injector) {
		// TODO Auto-generated method stub
		
	}

}
