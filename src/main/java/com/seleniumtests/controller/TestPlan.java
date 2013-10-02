package com.seleniumtests.controller;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.xml.XmlTest;
import com.seleniumtests.driver.web.WebUXDriver;

/**
 * TestPlan takes charge of setup and teardown including initialize context,
 * clean up drivers and deal with customized TearDownService. Al the test
 * plan should extend TestPlan class.
 *
 */
public abstract class TestPlan {
    private static final Logger logger = Logging.getLogger(TestPlan.class);
    private Date start;

    static {
    }

    /**
     * Implement an anonymous TearDownService and add it to the framework. The
     * framework will execute it once the test is complete whether it's passed
     * or failed does not matter.
     *
     * One example can be recycling of user. <code>
     * <pre>
     * 	addTearDownService(new TearDownService(){
     * 		public void tearDown(){
     * 			UserHelper.recycleUser(user);
     * 		}
     * 	});
     *  UserHelper.createUser(user);
     * </pre>
     * </code>
     *
     * @param service
     */
    public static void addTearDownService(TearDownService service) {
        ContextManager.getThreadContext().addTearDownService(service);
    }

    @AfterMethod(alwaysRun = true)
    public void afterTestMethod(Object[] parameters, Method method, ITestContext testContex, XmlTest xmlTest) {
        // Clean ups for test level services
        List<TearDownService> serviceList = ContextManager.getThreadContext().getTearDownServices();
        if (serviceList != null && !serviceList.isEmpty()) {
            for (TearDownService service : serviceList) {
                service.tearDown();
            }
        }
        // threadLocalAfterMethodTearDownServiceList.set(null);
        WebUXDriver.cleanUp();
        logger.info(Thread.currentThread() + " Finish method " + method.getName());
    }

    @AfterSuite(alwaysRun = true)
    public void afterTestSuite() {
        logger.info("Test Suite Execution Time: " + (new Date().getTime() - start.getTime()) / 1000 / 60 + " minutes.");
    }

    /**
     * Configure Test Params setting
     *
     * @param xmlTest
     */
    @BeforeTest(alwaysRun = true)
    public void beforeTest(ITestContext testContex, XmlTest xmlTest) {
        ContextManager.initTestLevelContext(testContex, xmlTest);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod(Object[] parameters, Method method, ITestContext testContex, XmlTest xmlTest) {
        logger.info(Thread.currentThread() + " Start method " + method.getName());
        ContextManager.initThreadContext(testContex, xmlTest);

        if (method != null) {
            ContextManager.getThreadContext().setAttribute(Context.TEST_METHOD_SIGNATURE, constructMethodSignature(method, parameters));
        }
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeTestSuite(ITestContext testContex) {
        start = new Date();
        ContextManager.initGlobalContext(testContex);
        ContextManager.initThreadContext(testContex, null);//Add this to support users want to call some functions in @beforeSuite
    }

    private String constructMethodSignature(Method method, Object[] parameters) {
        return method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "(" + constructParameterString(parameters) + ")";
    }

    /**
     * Remove name space from parameters
     *
     * @param parameters
     * @return
     */
    private String constructParameterString(Object[] parameters) {
        StringBuffer sbParam = new StringBuffer();

        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] == null) {
                    sbParam.append("null, ");
                } else if (parameters[i] instanceof java.lang.String) {
                    sbParam.append("\"").append(parameters[i]).append("\", ");
                } else {
                    sbParam.append(parameters[i]).append(", ");
                }
            }
        }

        if (sbParam.length() > 0)
            sbParam.delete(sbParam.length() - 2, sbParam.length() - 1);

        return sbParam.toString();
    }
}
