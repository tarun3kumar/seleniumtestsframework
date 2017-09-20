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

import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.ITestContext;
import org.testng.ITestResult;

import com.seleniumtests.driver.ScreenShot;
import com.seleniumtests.driver.TestType;

import com.seleniumtests.reporter.PluginsHelper;

/**
 * Defines TestNG context used in STF.
 */
public class SeleniumTestsContext {

    /* configuration defined in testng.xml */
    public static final String TEST_CONFIGURATION = "testConfig";
    public static final String APP_URL = "appURL";
    public static final String WEB_SESSION_TIME_OUT = "webSessionTimeOut";
    public static final String IMPLICIT_WAIT_TIME_OUT = "implicitWaitTimeOut";
    public static final String EXPLICIT_WAIT_TIME_OUT = "explicitWaitTimeOut";
    public static final String PAGE_LOAD_TIME_OUT = "pageLoadTimeout";
    public static final String WEB_DRIVER_GRID = "webDriverGrid";
    public static final String RUN_MODE = "runMode";
    public static final String BROWSER = "browser";
    public static final String BROWSER_VERSION = "browserVersion";
    public static final String WEB_PLATFORM = "webPlatform";
    public static final String FIREFOX_USER_PROFILE_PATH = "firefoxUserProfilePath";
    public static final String USE_DEFAULT_FIREFOX_PROFILE = "useFirefoxDefaultProfile";
    public static final String OPERA_USER_PROFILE_PATH = "operaUserProfilePath";
    public static final String FIREFOX_BINARY_PATH = "firefoxBinaryPath";
    public static final String CHROME_DRIVER_PATH = "chromeDriverPath";
    public static final String CHROME_BINARY_PATH = "chromeBinaryPath";
    public static final String IE_DRIVER_PATH = "ieDriverPath";
    public static final String USER_AGENT = "userAgent";

    public static final String Set_Assume_Untrusted_Certificate_Issuer = "setAssumeUntrustedCertificateIssuer";
    public static final String Set_Accept_Untrusted_Certificates = "setAcceptUntrustedCertificates";
    public static final String ENABLE_JAVASCRIPT = "enableJavascript";
    public static final String EVENT_FIRING_WEB_DRIVER = "eventFiringWebDriver";
    public static final String NTLM_AUTH_TRUSTED_URIS = "ntlmAuthTrustedUris";
    public static final String BROWSER_DOWNLOAD_DIR = "browserDownloadDir";
    public static final String BROWSER_WINDOW_SIZE = "browserWindowSize";

    public static final String WEB_PROXY_ENABLED = "webProxyEnabled";
    public static final String WEB_PROXY_TYPE = "webProxyType";
    public static final String WEB_PROXY_ADDRESS = "webProxyAddress";

    public static final String TEST_ENTITY = "testEntity";

    public static final String REPORT_GENERATION_CONFIG = "reportGenerationConfig";
    public static final String OPEN_REPORT_IN_BROWSER = "openReportInBrowser";
    public static final String CAPTURE_SNAPSHOT = "captureSnapshot";
    public static final String ENABLE_EXCEPTION_LISTENER = "enableExceptionListener";

    public static final String DP_TAGS_INCLUDE = "dpTagsInclude";
    public static final String DP_TAGS_EXCLUDE = "dpTagsExclude";

    public static final String SSH_COMMAND_WAIT = "sshCommandWait";
    public static final String SOFT_ASSERT_ENABLED = "softAssertEnabled";

    public static final String OUTPUT_DIRECTORY = "outputDirectory";
    public static final String WEB_DRIVER_LISTENER = "webDriverListener";

    public static final String TEST_METHOD_SIGNATURE = "testMethodSignature";
    public static final String PLUGIN_CONFIG_PATH = "pluginConfigPath";

    public static final String TEST_DATA_FILE = "testDataFile";

    public static final String TEST_TYPE = "testType";

    // Appium specific properties
    public static final String APPIUM_SERVER_URL = "appiumServerURL";
    public static final String AUTOMATION_NAME = "automationName";
    public static final String MOBILE_PLATFORM_NAME = "platformName";
    public static final String MOBILE_PLATFORM_VERSION = "mobilePlatformVersion";
    public static final String DEVICE_NAME = "deviceName";
    public static final String APP = "app";

    public static final String BROWSER_NAME = "browserName";
    public static final String APP_PACKAGE = "appPackage";
    public static final String APP_ACTIVITY = "appActivity";
    public static final String NEW_COMMAND_TIMEOUT = "newCommandTimeout";

    // SauceLabs specific properties
    public static final String VERSION = "version";
    public static final String PLATFORM = "platform";
    public static final String SAUCELABS_URL = "sauceLabsURL";

    // Zalenium specific properties
    public static final String ZALENIUM_URL= "zaleniumURL";

    private LinkedList<TearDownService> tearDownServices = new LinkedList<TearDownService>();
    private Map<ITestResult, List<Throwable>> verificationFailuresMap = new HashMap<ITestResult, List<Throwable>>();

    /* Data object to store all context data */
    private Map<String, Object> contextDataMap = Collections.synchronizedMap(new HashMap<String, Object>());

    private ITestContext testNGContext = null;

    private LinkedList<ScreenShot> screenshots = new LinkedList<ScreenShot>();

    public LinkedList<ScreenShot> getScreenshots() {
        return screenshots;
    }

    public void addScreenShot(final ScreenShot screenShot) {
        deleteExceptionSnapshots();
        screenshots.addLast(screenShot);
    }

    private void deleteExceptionSnapshots() {
        try {
            int size = screenshots.size();
            if (size == 0) {
                return;
            }

            ScreenShot screenShot = screenshots.get(size - 1);
            if (screenShot.isException() && screenShot.getFullImagePath() != null) {
                new File(screenShot.getFullImagePath()).delete();
                screenshots.remove(size - 1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ScreenShot getExceptionScreenShot() {
        if (screenshots.size() > 0 && screenshots.getLast().isException()) {
            return screenshots.getLast();
        } else {
            return null;
        }
    }

    public SeleniumTestsContext(final ITestContext context) {
        this.testNGContext = context;

        setContextAttribute(context, TEST_DATA_FILE, System.getProperty(TEST_DATA_FILE), "testCase");
        setContextAttribute(context, TEST_TYPE, System.getProperty(TEST_TYPE), TestType.WEB.toString());

        setContextAttribute(context, WEB_SESSION_TIME_OUT, System.getProperty(WEB_SESSION_TIME_OUT), "90000");
        setContextAttribute(context, IMPLICIT_WAIT_TIME_OUT, System.getProperty(IMPLICIT_WAIT_TIME_OUT), "5");
        setContextAttribute(context, EXPLICIT_WAIT_TIME_OUT, System.getProperty(EXPLICIT_WAIT_TIME_OUT), "15");
        setContextAttribute(context, PAGE_LOAD_TIME_OUT, System.getProperty(PAGE_LOAD_TIME_OUT), "90");

        setContextAttribute(context, WEB_DRIVER_GRID, System.getProperty(WEB_DRIVER_GRID), null);
        setContextAttribute(context, RUN_MODE, System.getProperty(RUN_MODE), "LOCAL");
        setContextAttribute(context, BROWSER, System.getProperty(BROWSER), "*firefox");
        setContextAttribute(context, BROWSER_VERSION, System.getProperty(BROWSER_VERSION), null);
        setContextAttribute(context, WEB_PLATFORM, System.getProperty(WEB_PLATFORM), null);

        setContextAttribute(context, FIREFOX_USER_PROFILE_PATH, System.getProperty(FIREFOX_USER_PROFILE_PATH), null);
        setContextAttribute(context, USE_DEFAULT_FIREFOX_PROFILE, System.getProperty(USE_DEFAULT_FIREFOX_PROFILE),
            "true");

        setContextAttribute(context, OPERA_USER_PROFILE_PATH, System.getProperty(OPERA_USER_PROFILE_PATH), null);
        setContextAttribute(context, FIREFOX_BINARY_PATH, System.getProperty(FIREFOX_BINARY_PATH), null);
        setContextAttribute(context, CHROME_DRIVER_PATH, System.getProperty(CHROME_DRIVER_PATH), null);
        setContextAttribute(context, IE_DRIVER_PATH, System.getProperty(IE_DRIVER_PATH), null);
        setContextAttribute(context, USER_AGENT, System.getProperty(USER_AGENT), null);
        setContextAttribute(context, Set_Assume_Untrusted_Certificate_Issuer,
            System.getProperty(Set_Assume_Untrusted_Certificate_Issuer), null);
        setContextAttribute(context, Set_Accept_Untrusted_Certificates,
            System.getProperty(Set_Accept_Untrusted_Certificates), null);
        setContextAttribute(context, ENABLE_JAVASCRIPT, System.getProperty(ENABLE_JAVASCRIPT), null);
        setContextAttribute(context, NTLM_AUTH_TRUSTED_URIS, System.getProperty(NTLM_AUTH_TRUSTED_URIS), null);
        setContextAttribute(context, BROWSER_DOWNLOAD_DIR, System.getProperty(BROWSER_DOWNLOAD_DIR), null);
        setContextAttribute(context, BROWSER_WINDOW_SIZE, System.getProperty(BROWSER_WINDOW_SIZE), null);
        setContextAttribute(context, WEB_PROXY_ENABLED, System.getProperty(WEB_PROXY_ENABLED), "false");
        setContextAttribute(context, WEB_PROXY_TYPE, System.getProperty(WEB_PROXY_TYPE), null);
        setContextAttribute(context, WEB_PROXY_ADDRESS, System.getProperty(WEB_PROXY_ADDRESS), null);

        // Set default to summaryPerSuite, by default it would generate a summary report per suite for tests in SeleniumTestReport.html
        // if set to summaryAllSuites, only one summary report section would be generated.
        setContextAttribute(context, REPORT_GENERATION_CONFIG, System.getProperty(REPORT_GENERATION_CONFIG), "summaryPerSuite");

        setContextAttribute(context, OPEN_REPORT_IN_BROWSER, System.getProperty(OPEN_REPORT_IN_BROWSER), null);

        setContextAttribute(context, CAPTURE_SNAPSHOT, System.getProperty(CAPTURE_SNAPSHOT), null);
        setContextAttribute(context, ENABLE_EXCEPTION_LISTENER, System.getProperty(ENABLE_EXCEPTION_LISTENER), "true");

        setContextAttribute(context, DP_TAGS_INCLUDE, System.getProperty(DP_TAGS_INCLUDE), null);
        setContextAttribute(context, DP_TAGS_EXCLUDE, System.getProperty(DP_TAGS_EXCLUDE), null);

        setContextAttribute(context, SSH_COMMAND_WAIT, System.getProperty(SSH_COMMAND_WAIT), "5000");
        setContextAttribute(context, SOFT_ASSERT_ENABLED, System.getProperty(SOFT_ASSERT_ENABLED), "false");

        setContextAttribute(context, WEB_DRIVER_LISTENER, System.getProperty(WEB_DRIVER_LISTENER), null);

        setContextAttribute(context, APPIUM_SERVER_URL, System.getProperty(APPIUM_SERVER_URL), null);
        setContextAttribute(context, AUTOMATION_NAME, System.getProperty(AUTOMATION_NAME), "Appium");
        setContextAttribute(context, MOBILE_PLATFORM_NAME, System.getProperty(MOBILE_PLATFORM_NAME), "Android");
        setContextAttribute(context, MOBILE_PLATFORM_VERSION, System.getProperty(MOBILE_PLATFORM_VERSION), null);
        setContextAttribute(context, DEVICE_NAME, System.getProperty(DEVICE_NAME), null);

        setContextAttribute(context, APP, System.getProperty(APP), "");

        // By default test is assumed to be executed on default browser on android emulator
        setContextAttribute(context, BROWSER_NAME, System.getProperty(BROWSER_NAME), "Browser");
        setContextAttribute(context, APP_PACKAGE, System.getProperty(APP_PACKAGE), null);
        setContextAttribute(context, APP_ACTIVITY, System.getProperty(APP_ACTIVITY), null);
        setContextAttribute(context, NEW_COMMAND_TIMEOUT, System.getProperty(NEW_COMMAND_TIMEOUT), "120");

        setContextAttribute(context, VERSION, System.getProperty(VERSION), null);
        setContextAttribute(context, PLATFORM, System.getProperty(PLATFORM), null);
        setContextAttribute(context, SAUCELABS_URL, System.getProperty(SAUCELABS_URL), null);

        if (context != null) {
            setContextAttribute(OUTPUT_DIRECTORY, null, context.getOutputDirectory(), null);

            // parse other parameters that are defined in testng xml but not defined
            // in this context
            setContextAttribute(context);

            new File(context.getOutputDirectory() + "/screenshots").mkdirs();
            new File(context.getOutputDirectory() + "/htmls").mkdirs();
            new File(context.getOutputDirectory() + "/xmls").mkdirs();
            new File(context.getOutputDirectory() + "/textfiles/").mkdirs();

            String path = (String) getAttribute(PLUGIN_CONFIG_PATH);

            if (path != null && path.trim().length() > 0) {
                File configFile = new File(path);
                if (configFile.exists()) {
                    PluginsHelper.getInstance().loadPlugins(configFile);
                }
            }
        }
    }

    /**
     * Adds the given tear down.
     */
    public void addTearDownService(final TearDownService tearDown) {
        tearDownServices.add(tearDown);
    }

    public void addVerificationFailures(final ITestResult result, final List<Throwable> failures) {

        this.verificationFailuresMap.put(result, failures);
    }

    public void addVerificationFailures(final ITestResult result, final Throwable failure) {

        if (verificationFailuresMap.get(result) != null) {
            this.verificationFailuresMap.get(result).add(failure);
        } else {
            ArrayList<Throwable> failures = new ArrayList<Throwable>();
            failures.add(failure);
            this.addVerificationFailures(result, failures);
        }
    }

    public Object getAttribute(final String name) {
        Object obj = contextDataMap.get(name);
        return obj == null ? null : obj;
    }

    public String getBrowserDownloadDir() {
        if (getAttribute(BROWSER_DOWNLOAD_DIR) != null) {
            return (String) getAttribute(BROWSER_DOWNLOAD_DIR);
        } else {
            return this.getOutputDirectory() + "\\downloads\\";
        }
    }

    public String getBrowserWindowSize() {
        return (String) getAttribute(BROWSER_WINDOW_SIZE);
    }

    public boolean getCaptureSnapshot() {
        if (getAttribute(CAPTURE_SNAPSHOT) == null) {

            // IE grid default value set to false
            if (this.getWebRunMode().equalsIgnoreCase("ExistingGrid")
                    && (this.getWebRunBrowser().contains("iexplore") || this.getWebRunBrowser().contains("safari"))) {
                this.setAttribute(CAPTURE_SNAPSHOT, "false");
            } else {
                this.setAttribute(CAPTURE_SNAPSHOT, "true");
            }
        }

        return Boolean.parseBoolean((String) getAttribute(CAPTURE_SNAPSHOT));
    }

    public boolean getEnableExceptionListener() {
        return Boolean.parseBoolean((String) getAttribute(ENABLE_EXCEPTION_LISTENER));
    }

    public String getChromeBinPath() {
        return (String) getAttribute(CHROME_BINARY_PATH);
    }

    public String getChromeDriverPath() {
        return (String) getAttribute(CHROME_DRIVER_PATH);
    }

    public String getDPTagsExclude() {
        return (String) getAttribute(DP_TAGS_EXCLUDE);
    }

    public String getDPTagsInclude() {
        return (String) getAttribute(DP_TAGS_INCLUDE);
    }

    public int getExplicitWaitTimeout() {
        Integer timeout;
        try {
            timeout = Integer.parseInt((String) getAttribute(EXPLICIT_WAIT_TIME_OUT));
        } catch (Exception e) {
            timeout = 15;
        }

        if (timeout < getImplicitWaitTimeout()) {
            return (int) getImplicitWaitTimeout();
        } else {
            return timeout;
        }
    }

    public String getFirefoxBinPath() {
        return (String) getAttribute(FIREFOX_BINARY_PATH);
    }

    public String getFirefoxUserProfilePath() {
        return (String) getAttribute(FIREFOX_USER_PROFILE_PATH);
    }

    public String getIEDriverPath() {
        return (String) getAttribute(IE_DRIVER_PATH);
    }

    public double getImplicitWaitTimeout() {
        try {
            return Double.parseDouble((String) getAttribute(IMPLICIT_WAIT_TIME_OUT));
        } catch (Exception e) {
            return 5;
        }
    }

    public String getNtlmAuthTrustedUris() {
        return (String) getAttribute(NTLM_AUTH_TRUSTED_URIS);
    }

    public String getReportGenerationConfig() {
        return (String) getAttribute(REPORT_GENERATION_CONFIG);
    }

    public String getOpenReportInBrowser() {
        return (String) getAttribute(OPEN_REPORT_IN_BROWSER);
    }

    public String getOperaUserProfilePath() {
        return (String) getAttribute(OPERA_USER_PROFILE_PATH);
    }

    public String getOutputDirectory() {
        return (String) getAttribute(OUTPUT_DIRECTORY);
    }

    public int getPageLoadTimeout() {
        try {
            return Integer.parseInt((String) getAttribute(PAGE_LOAD_TIME_OUT));
        } catch (Exception e) {
            return 90;
        }
    }

    public String getWebPlatform() {
        return (String) getAttribute(WEB_PLATFORM);
    }

    public String getAppURL() {
        return (String) getAttribute(APP_URL);
    }

    public int getSshCommandWait() {
        try {
            return Integer.parseInt((String) getAttribute(SSH_COMMAND_WAIT));
        } catch (Exception e) {
            return 5000; // Default
        }
    }

    /**
     * Get TestNG suite parameter from testng.xml. Return System value for CI job.
     *
     * @param   key
     *
     * @return
     */
    public String getSuiteParameter(final String key) {
        if (System.getProperty(key) != null) {
            return System.getProperty(key);
        } else {
            return getTestNGContext().getSuite().getParameter(key);
        }
    }

    /**
     * Returns the tear down list.
     */
    public LinkedList<TearDownService> getTearDownServices() {
        return tearDownServices;
    }

    public String getTestDataFile() {
        return (String) getAttribute(TEST_DATA_FILE);
    }

    public String getTestType() {
        return (String) getAttribute(TEST_TYPE);
    }

    public String getTestMethodSignature() {
        return (String) getAttribute(TEST_METHOD_SIGNATURE);
    }

    public ITestContext getTestNGContext() {
        return testNGContext;
    }

    public Object getTestEntity() {
        return getAttribute(TEST_ENTITY);
    }

    public String getWebDriverListener() {
        return (String) getAttribute(WEB_DRIVER_LISTENER);
    }

    public String getUserAgent() {
        return (String) getAttribute(USER_AGENT);
    }

    public List<Throwable> getVerificationFailures(final ITestResult result) {
        List<Throwable> verificationFailures = verificationFailuresMap.get(result);
        return verificationFailures == null ? new ArrayList<Throwable>() : verificationFailures;

    }

    public String getWebBrowserVersion() {
        return (String) getAttribute(BROWSER_VERSION);
    }

    public String getWebDriverGrid() {
        return (String) getAttribute(WEB_DRIVER_GRID);
    }

    public String getWebProxyAddress() {
        return (String) getAttribute(WEB_PROXY_ADDRESS);
    }

    public String getWebProxyType() {
        return (String) getAttribute(WEB_PROXY_TYPE);
    }

    public String getWebRunBrowser() {
        return (String) getAttribute(BROWSER);
    }

    public String getWebRunMode() {
        return (String) getAttribute(RUN_MODE);
    }

    public int getWebSessionTimeout() {
        try {
            return Integer.parseInt((String) getAttribute(WEB_SESSION_TIME_OUT));
        } catch (Exception e) {
            return 90000; // Default
        }
    }

    public String getAppiumServerURL() {
        return (String) getAttribute(APPIUM_SERVER_URL);
    }

    public String getAutomationName() {
        return (String) getAttribute(AUTOMATION_NAME);
    }

    public String getMobilePlatformName() {
        return (String) getAttribute(MOBILE_PLATFORM_NAME);
    }

    public String getMobilePlatformVersion() {
        return (String) getAttribute(MOBILE_PLATFORM_VERSION);
    }

    public String getDeviceName() {
        return (String) getAttribute(DEVICE_NAME);
    }

    public String getApp() {
        return (String) getAttribute(APP);
    }

    public String getBrowserName() {
        return (String) getAttribute(BROWSER_NAME);
    }

    public String getAppPackage() {
        return (String) getAttribute(APP_PACKAGE);
    }

    public String getAppActivity() {
        return (String) getAttribute(APP_ACTIVITY);
    }

    public String getNewCommandTimeout() {
        return (String) getAttribute(NEW_COMMAND_TIMEOUT);
    }

    public String getVersion() {
        return (String) getAttribute(VERSION);
    }

    public String getPlatform() {
        return (String) getAttribute(PLATFORM);
    }

    public String getSaucelabsURL() {
        return (String) getAttribute(SAUCELABS_URL);
    }

    public String getZaleniumUrl() { return (String) getAttribute(ZALENIUM_URL); }

    public boolean isUseFirefoxDefaultProfile() {
        try {
            return Boolean.parseBoolean((String) getAttribute(USE_DEFAULT_FIREFOX_PROFILE));
        } catch (Exception e) {
            return true; // Default
        }

    }

    public boolean isSoftAssertEnabled() {
        try {
            return Boolean.parseBoolean((String) getAttribute(SOFT_ASSERT_ENABLED));
        } catch (Exception e) {
            return false; // Default
        }
    }

    public boolean isWebProxyEnabled() {
        try {
            return Boolean.parseBoolean((String) getAttribute(WEB_PROXY_ENABLED));
        } catch (Exception e) {
            return false; // Default
        }
    }

    public void setAttribute(final String name, final Object value) {
        contextDataMap.put(name, value);
    }

    private void setContextAttribute(final ITestContext context) {
        if (context != null) {
            Map<String, String> testParameters = context.getSuite().getXmlSuite().getParameters();

            for (Entry<String, String> entry : testParameters.entrySet()) {
                String attributeName = entry.getKey();

                if (contextDataMap.get(entry.getKey()) == null) {
                    String sysPropertyValue = System.getProperty(entry.getKey());
                    String suiteValue = entry.getValue();
                    setContextAttribute(attributeName, sysPropertyValue, suiteValue, null);
                }

            }
        }

    }

    /**
     * Set Suite SeleniumTestsContext Attributes.
     *
     * @param  context
     * @param  attributeName
     * @param  sysPropertyValue
     * @param  defaultValue
     */

    private void setContextAttribute(final ITestContext context, final String attributeName,
            final String sysPropertyValue, final String defaultValue) {
        String suiteValue = null;
        if (context != null) {
            suiteValue = context.getSuite().getParameter(attributeName);
        }

        contextDataMap.put(attributeName,
            (sysPropertyValue != null ? sysPropertyValue : (suiteValue != null ? suiteValue : defaultValue)));
    }

    private void setContextAttribute(final String attributeName, final String sysPropertyValue, final String suiteValue,
            final String defaultValue) {

        contextDataMap.put(attributeName,
            (sysPropertyValue != null ? sysPropertyValue : (suiteValue != null ? suiteValue : defaultValue)));

    }

    public void setExplicitWaitTimeout(final double timeout) {
        setAttribute(EXPLICIT_WAIT_TIME_OUT, timeout);
    }

    public void setImplicitWaitTimeout(final double timeout) {
        setAttribute(IMPLICIT_WAIT_TIME_OUT, timeout);
    }

    public void setPageLoadTimeout(final int timeout) {
        setAttribute(PAGE_LOAD_TIME_OUT, timeout);
    }

    public void setTestDataFile(final String testDataFile) {
        setAttribute(TEST_DATA_FILE, testDataFile);
    }

    public void setTestType(final String testType) {
        setAttribute(TEST_TYPE, testType);
    }

}
