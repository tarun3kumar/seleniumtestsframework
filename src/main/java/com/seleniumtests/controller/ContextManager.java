package com.seleniumtests.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.ITestContext;
import org.testng.xml.XmlTest;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.seleniumtests.helper.XMLHelper;


/**
 * ContextManager provides ways to manage global context, thread context and
 * test level context
 * 
 */
public class ContextManager {

	// Customized Contex Attribute
	private static List<IContexAttributeListener> contexAttributeListenerList = Collections.synchronizedList(new ArrayList<IContexAttributeListener>());

	// define the global level context
	private static Context globalContext;

	// define the test level context
	private static Map<String, Context> testLevelContext = Collections.synchronizedMap(new HashMap<String, Context>());

	// define the thread level Context
	private static ThreadLocal<Context> threadLocalContext = new ThreadLocal<Context>();

	public static void addContexAttributeListener(IContexAttributeListener listener) {
		contexAttributeListenerList.add(listener);
	}

	public static Context getGlobalContext() {
		if (globalContext == null) {
			System.out.println("Initialize default GlobalContext");
			initGlobalContext(new DefaultTestNGContext());
		}
		return globalContext;
	}

	public static Context getTestLevelContext(ITestContext testContext) {
		if (testContext != null && testContext.getCurrentXmlTest() != null) {
			if (testLevelContext.get(testContext.getCurrentXmlTest().getName()) == null) {
				// sometimes getTestLevelContext is called before @BeforeTest in
				// TestPlan
				initTestLevelContext(testContext, testContext.getCurrentXmlTest());
			}
			return testLevelContext.get(testContext.getCurrentXmlTest().getName());
		} else {
			return null;
		}
	}

	public static Context getTestLevelContext(String testName) {
		return testLevelContext.get(testName);
	}

	public static Context getThreadContext() {
		if (threadLocalContext.get() == null) {
			System.out.println("Initialize default ThreadContext");
			initThreadContext(null, null);
		}
		return threadLocalContext.get();
	}

	public static void initGlobalContext(ITestContext testNGCtx) {
		testNGCtx = getContextFromConfigFile(testNGCtx);
		globalContext = new Context(testNGCtx);
		loadCustomizedContextAttribute(testNGCtx, globalContext);
	}

	private static ITestContext getContextFromConfigFile(ITestContext testContex) {
		if (testContex != null) {
			if (testContex.getSuite().getParameter(Context.TEST_CONFIG) != null) {
				File suiteFile = new File(testContex.getSuite().getXmlSuite().getFileName());
				String configFile = suiteFile.getPath().replace(suiteFile.getName(), "") + testContex.getSuite().getParameter("testConfig");
				NodeList nList = XMLHelper.getXMLNodes(configFile, "parameter");
				Map<String, String> parameters = testContex.getSuite().getXmlSuite().getParameters();
				for (int i = 0; i < nList.getLength(); i++) {
					Node nNode = nList.item(i);
					parameters.put(nNode.getAttributes().getNamedItem("name").getNodeValue(), nNode.getAttributes().getNamedItem("value").getNodeValue());
				}
				testContex.getSuite().getXmlSuite().setParameters(parameters);
			}
		}
		return testContex;
	}

	public static void initTestLevelContext(ITestContext testNGCtx, XmlTest xmlTest) {
		Context mauiCtx = new Context(testNGCtx);
		if (xmlTest != null) {
			Map<String, String> testParameters = xmlTest.getTestParameters();
			// parse the test level parameters
			for (Entry<String, String> entry : testParameters.entrySet()) {
				mauiCtx.setAttribute(entry.getKey(), entry.getValue());
			}

		}
		testLevelContext.put(xmlTest.getName(), mauiCtx);
	}

	public static void initTestLevelContext(XmlTest xmlTest) {
		initTestLevelContext(globalContext.getTestNGContext(), xmlTest);
	}

	public static void initThreadContext() {
		initThreadContext(globalContext.getTestNGContext(), null);
	}

	public static void initThreadContext(ITestContext testNGCtx) {
		initThreadContext(testNGCtx, null);
	}

	public static void initThreadContext(ITestContext testNGCtx, XmlTest xmlTest) {
		Context mauiCtx = new Context(testNGCtx);

		loadCustomizedContextAttribute(testNGCtx, mauiCtx);

		if (xmlTest != null) {
			Map<String, String> testParameters = xmlTest.getTestParameters();
			// parse the test level parameters
			for (Entry<String, String> entry : testParameters.entrySet()) {

				if (System.getProperty(entry.getKey()) == null)
					mauiCtx.setAttribute(entry.getKey(), entry.getValue());

			}

		}

		threadLocalContext.set(mauiCtx);
	}

	public static void initThreadContext(XmlTest xmlTest) {
		initThreadContext(globalContext.getTestNGContext(), xmlTest);
	}

	private static void loadCustomizedContextAttribute(ITestContext testNGCtx, Context mauiCtx) {
		for (int i = 0; i < contexAttributeListenerList.size(); i++) {
			contexAttributeListenerList.get(i).load(testNGCtx, mauiCtx);
		}
	}

	public static void setGlobalContext(Context ctx) {
		globalContext = (ctx);
	}

	public static void setThreadContext(Context ctx) {
		threadLocalContext.set(ctx);
	}
}