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

package com.seleniumtests.reporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import java.lang.reflect.Method;

import java.net.URISyntaxException;
import java.net.URL;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;

import org.testng.internal.ResultMap;
import org.testng.internal.TestResult;
import org.testng.internal.Utils;

import org.testng.xml.XmlSuite;

import com.seleniumtests.core.CustomAssertion;
import com.seleniumtests.core.SeleniumTestsContext;
import com.seleniumtests.core.SeleniumTestsContextManager;
import com.seleniumtests.core.SeleniumTestsPageListener;
import com.seleniumtests.core.TestLogging;
import com.seleniumtests.core.TestRetryAnalyzer;

import com.seleniumtests.driver.ScreenShot;
import com.seleniumtests.driver.ScreenshotUtil;
import com.seleniumtests.driver.WebUIDriver;

import com.seleniumtests.helper.StringUtility;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.Type;

public class SeleniumTestsReporter implements IReporter, ITestListener, IInvokedMethodListener {

    private static Logger logger = TestLogging.getLogger(SeleniumTestsReporter.class);

    protected class TestMethodSorter<T extends ITestNGMethod> implements Comparator<T> {

        /**
         * Arrange methods by class and method name.
         */
        public int compare(final T o1, final T o2) {
            int r = ((T) o1).getTestClass().getName().compareTo(o2.getTestClass().getName());
            if (r == 0) {
                r = ((T) o1).getMethodName().compareTo(o2.getMethodName());
            }

            return r;
        }
    }

    protected class TestResultSorter<T extends ITestResult> implements Comparator<T> {

        /**
         * Arrange methods by class and method name.
         */
        public int compare(final T o1, final T o2) {
            String sig1 = StringUtility.constructMethodSignature(o1.getMethod().getMethod(), o1.getParameters());
            String sig2 = StringUtility.constructMethodSignature(o2.getMethod().getMethod(), o2.getParameters());
            return sig1.compareTo(sig2);
        }
    }

    protected static String escape(final String string) {
        if (null == string) {
            return string;
        }

        return string.replaceAll("\n", "<br/>");
    }

    public static void writeResourceToFile(final File file, final String resourceName, final Class<?> aClass)
        throws IOException {
        InputStream inputStream = aClass.getResourceAsStream("/" + resourceName);
        if (inputStream == null) {
            logger.error("can not find resource on the class path: " + resourceName);
        } else {

            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                try {
                    int i;
                    byte[] buffer = new byte[4096];
                    while (0 < (i = inputStream.read(buffer))) {
                        outputStream.write(buffer, 0, i);
                    }
                } finally {
                    outputStream.close();
                }
            } finally {
                inputStream.close();
            }
        }
    }

    private Map<String, Boolean> isRetryHandleNeeded = new HashMap<String, Boolean>();

    private Map<String, IResultMap> failedTests = new HashMap<String, IResultMap>();

    private Map<String, IResultMap> skippedTests = new HashMap<String, IResultMap>();
    protected PrintWriter m_out;

    private String uuid = new GregorianCalendar().getTime().toString();

    private int m_treeId = 0;

    private String outputDirectory;
    private String resources;
    private JavaDocBuilder builder = null;

    private File report;

    Map<String, ITestResult> methodsByGroup = null;

    private void addAllTestResults(final Set<ITestResult> testResults, final IResultMap resultMap) {
        if (resultMap != null) {
            testResults.addAll(resultMap.getAllResults());
        }
    }

    public void afterInvocation(final IInvokedMethod method, final ITestResult result) {
        Reporter.setCurrentTestResult(result);

        // Handle Soft CustomAssertion
        if (method.isTestMethod()) {
            List<Throwable> verificationFailures = CustomAssertion.getVerificationFailures();

            int size = verificationFailures.size();
            if (size == 0) {
                return;
            } else if (result.getStatus() == TestResult.FAILURE) {
                return;
            }

            result.setStatus(TestResult.FAILURE);

            if (size == 1) {
                result.setThrowable(verificationFailures.get(0));
            } else {

                // create failure message with all failures and stack traces barring last failure)
                StringBuilder failureMessage = new StringBuilder("!!! Many Test Failures (").append(size).append(
                        "):nn");
                for (int i = 0; i < size - 1; i++) {
                    failureMessage.append("Failure ").append(i + 1).append(" of ").append(size).append(":n");

                    Throwable t = verificationFailures.get(i);
                    String fullStackTrace = Utils.stackTrace(t, false)[1];
                    failureMessage.append(fullStackTrace).append("nn");
                }

                // final failure
                Throwable last = verificationFailures.get(size - 1);
                failureMessage.append("Failure ").append(size).append(" of ").append(size).append(":n");
                failureMessage.append(last.toString());

                // set merged throwable
                Throwable merged = new Throwable(failureMessage.toString());
                merged.setStackTrace(last.getStackTrace());

                result.setThrowable(merged);
            }
        }
    }

    public void beforeInvocation(final IInvokedMethod arg0, final ITestResult arg1) { }

    protected void copyResources() throws Exception {

        new File(outputDirectory + File.separator + "resources").mkdir();
        new File(outputDirectory + File.separator + "resources" + File.separator + "css").mkdir();
        new File(outputDirectory + File.separator + "resources" + File.separator + "images").mkdir();
        new File(outputDirectory + File.separator + "resources" + File.separator + "images" + File.separator
                + "lightbox").mkdir();
        new File(outputDirectory + File.separator + "resources" + File.separator + "images" + File.separator + "mktree")
            .mkdir();
        new File(outputDirectory + File.separator + "resources" + File.separator + "images" + File.separator
                + "yukontoolbox").mkdir();
        new File(outputDirectory + File.separator + "resources" + File.separator + "js").mkdir();

        List<String> resources = new ArrayList<String>();
        resources.add("reporter" + File.separator + "css" + File.separator + "report.css");
        resources.add("reporter" + File.separator + "css" + File.separator + "jquery.lightbox-0.5.css");
        resources.add("reporter" + File.separator + "css" + File.separator + "mktree.css");
        resources.add("reporter" + File.separator + "images" + File.separator + "lightbox" + File.separator
                + "seleniumtests_lightbox-blank.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "lightbox" + File.separator
                + "seleniumtests_lightbox-btn-close.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "lightbox" + File.separator
                + "seleniumtests_lightbox-btn-next.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "lightbox" + File.separator
                + "seleniumtests_lightbox-btn-prev.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "lightbox" + File.separator
                + "seleniumtests_lightbox-ico-loading.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "mktree" + File.separator
                + "seleniumtests_bullet.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "mktree" + File.separator
                + "seleniumtests_minus.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "mktree" + File.separator
                + "seleniumtests_plus.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "mktree" + File.separator
                + "seleniumtests_test1.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "mktree" + File.separator
                + "seleniumtests_test2.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "mktree" + File.separator
                + "seleniumtests_test3.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "mktree" + File.separator
                + "seleniumtests_test3.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator
                + "seleniumtests_footer_grad.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator
                + "seleniumtests_grey_bl.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator
                + "seleniumtests_grey_br.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator
                + "seleniumtests_hovertab_l.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator
                + "seleniumtests_hovertab_r.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator
                + "seleniumtests_tabbed_nav_goldgradbg.png");
        resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator
                + "seleniumtests_table_sep_left.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator
                + "seleniumtests_table_sep_right.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator
                + "seleniumtests_table_zebrastripe_left.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator
                + "seleniumtests_table_zebrastripe_right.gif");
        resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator
                + "seleniumtests_yellow_tr.gif");
        resources.add("reporter" + File.separator + "js" + File.separator + "jquery-1.10.2.min.js");
        resources.add("reporter" + File.separator + "js" + File.separator + "jquery.lightbox-0.5.min.js");
        resources.add("reporter" + File.separator + "js" + File.separator + "mktree.js");
        resources.add("reporter" + File.separator + "js" + File.separator + "report.js");
        resources.add("reporter" + File.separator + "js" + File.separator + "browserdetect.js");

        for (String resourceName : resources) {
            File f = new File(outputDirectory, resourceName.replace("reporter", "resources"));
            resourceName = resourceName.replaceAll("\\\\", "/");
            logger.debug("Begin to write resource " + resourceName + " to file " + f.getAbsolutePath());
            writeResourceToFile(f, resourceName, SeleniumTestsReporter.class);
        }
    }

    protected PrintWriter createWriter(final String outDir) throws IOException, FileNotFoundException {
        System.setProperty("file.encoding", "UTF8");
        uuid = uuid.replaceAll(" ", "-").replaceAll(":", "-");

        File f = new File(outDir, "SeleniumTestReport.html");
        logger.info("generating report " + f.getAbsolutePath());
        report = f;

        OutputStream out = new FileOutputStream(f);
        Writer writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
        return new PrintWriter(writer);

    }

    /**
     * Completes HTML stream.
     *
     * @param  out
     */
    protected void endHtml(final PrintWriter out) {
        out.println("</body></html>");
    }

    public void executeCmd(final String browserPath, final String theUrl) {
        String cmdLine;
        String osName = System.getProperty("os.name");

        if (osName.startsWith("Windows")) {
            cmdLine = "rundll32 SHELL32.DLL,ShellExec_RunDLL " + browserPath + " " + theUrl;
        } else if (osName.startsWith("Mac")) {
            cmdLine = "open " + theUrl;
        }

        // For Linux
        else {
            cmdLine = "open " + browserPath + " " + theUrl;
        }

        try {
            Runtime.getRuntime().exec(cmdLine);
        } catch (Exception e) {
            logger.info(e);
        }
    }

    protected void generateExceptionReport(final Throwable exception, final ITestNGMethod method, final String title,
            final StringBuffer contentBuffer, final String lastLine) {
        generateTheStackTrace(exception, method, title, contentBuffer, lastLine);
    }

    protected void generateExceptionReport(final Throwable exception, final ITestNGMethod method,
            final StringBuffer contentBuffer, final String lastline) {
        Throwable fortile = exception;
        String title = fortile.getMessage();
        if (title == null) {
            try {
                title = fortile.getCause().getMessage();
            } catch (Throwable e) {
                title = e.getMessage();
            }
        }

        generateExceptionReport(exception, method, title, contentBuffer, lastline);
    }

    protected void generateGlobalErrorHTML(final ITestContext testContext, final StringBuffer errorCountTabs,
            final StringBuffer errorCountHtmls) {
        try {
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty("resource.loader", "class");
            ve.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            ve.init();

            List<SeleniumTestsPageListener> pageListenersList = PluginsHelper.getInstance().getPageListeners();
            for (SeleniumTestsPageListener abstractPageListener : pageListenersList) {

                // Skip creating a tab according to "testResultEffected" property of an instance of
                // com.seleniumtests.core.SeleniumTestsPageListener
                if (!abstractPageListener.isTestResultEffected()) {
                    continue;
                }

                // Skip creating a tab according to "testResultEffected" property of an instance of
                // com.seleniumtests.core.SeleniumTestsPageListener
                errorCountTabs.append("<li class='tab' id='" + abstractPageListener.getClass().getSimpleName()
                                      + "'><a href='#'><span>")
                              .append(abstractPageListener.getTitle() != null
                                      ? abstractPageListener.getTitle()
                                      : abstractPageListener.getClass().getSimpleName()).append(
                                  " ( <font color='red'>");
                errorCountHtmls.append("<div class='" + abstractPageListener.getClass().getSimpleName()
                        + "' style='width: 98%;margin-left:15px;'>");
                generateGlobalErrorsPanel(abstractPageListener, ve, errorCountHtmls, "failed", testContext,
                    errorCountTabs);
                errorCountHtmls.append("</div>");
                errorCountTabs.append("</font> )</span></a></li>");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void generateGlobalErrorsPanel(final SeleniumTestsPageListener abstractPageListener,
            final VelocityEngine ve, final StringBuffer res, final String style, final ITestContext tc,
            final StringBuffer sbCalcount) {
        int pageCount = 0;

        Set<ITestResult> testResults = new HashSet<ITestResult>();

        addAllTestResults(testResults, tc.getPassedTests());
        addAllTestResults(testResults, failedTests.get(tc.getName()));
        addAllTestResults(testResults, tc.getFailedButWithinSuccessPercentageTests());

        Map<String, Map<String, List<String>>> pageListenerLogMap = TestLogging.getPageListenerLog(
                abstractPageListener.getClass().getCanonicalName());
        if (pageListenerLogMap == null || pageListenerLogMap.isEmpty()) {
            res.append("<div class='method passed'><div class='yuk_goldgrad_tl'><div class='yuk_goldgrad_tr'>"
                    + "<div class='yuk_goldgrad_m'></div></div></div>"
                    + "<h3 class='yuk_grad_ltitle_passed'>No Errors found.</h3>"
                    + "<div class='yuk_pnl_footerbar'></div>"
                    + "<div class='yuk_grey_bm_footer'><div class='yuk_grey_br'>"
                    + "<div class='yuk_grey_bl'></div></div></div></div>");
        } else {
            for (Entry<String, Map<String, List<String>>> pageEntry : pageListenerLogMap.entrySet()) {
                StringBuilder contentBuffer = new StringBuilder();
                contentBuffer.append(
                    "<table  class='ex' width='90%'><thead><tr><th>TestMethod</th><th>Errors</th></thead><tbody>");

                Map<String, List<String>> errorMap = pageEntry.getValue();

                boolean found = false;
                for (ITestResult testResult : testResults) {
                    Method method = testResult.getMethod().getMethod();
                    String methodInstance = StringUtility.constructMethodSignature(method, testResult.getParameters());
                    if (errorMap.containsKey(methodInstance)) {
                        found = true;
                        contentBuffer.append("<tr><td>" + methodInstance + "</td><td>");
                        for (String message : errorMap.get(methodInstance)) {
                            contentBuffer.append(message);
                            contentBuffer.append("<br>");
                        }

                        contentBuffer.append("</td><tr>");
                    }
                }

                if (found) {
                    contentBuffer.append("</tbody></table>");

                    try {
                        Template t = ve.getTemplate("/templates/report.part.singlePageError.html");
                        VelocityContext context = new VelocityContext();
                        context.put("status", style);
                        context.put("pageName", pageEntry.getKey());
                        context.put("content", contentBuffer.toString());

                        StringWriter writer = new StringWriter();
                        t.merge(context, writer);
                        res.append(writer.toString());
                    } catch (Exception e) {
                        logger.error("errorLogger creating a singlePageError." + e.getMessage());
                    }

                    pageCount++;
                }
            }
        }

        sbCalcount.append(pageCount);
    }

    protected String generateHTML(final ITestContext tc, final boolean envt, final ISuite suite,
            final ITestContext ctx) {

        StringBuffer res = new StringBuffer();
        try {
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty("resource.loader", "class");
            ve.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            ve.init();

            if (envt) {
                if (tc.getFailedConfigurations().getAllResults().size() > 0) {
                    generatePanel(ve, tc.getFailedConfigurations(), res, "failed", suite, ctx, envt);
                }

                generatePanel(ve, failedTests.get(tc.getName()), res, "failed", suite, ctx, envt);
                if (tc.getFailedConfigurations().getAllResults().size() > 0) {
                    generatePanel(ve, tc.getSkippedConfigurations(), res, "skipped", suite, ctx, envt);
                }

                generatePanel(ve, skippedTests.get(tc.getName()), res, "skipped", suite, ctx, envt);
                generatePanel(ve, tc.getPassedTests(), res, "passed", suite, ctx, envt);
            } else {
                generatePanel(ve, failedTests.get(tc.getName()), res, "failed", suite, ctx, envt);
                generatePanel(ve, skippedTests.get(tc.getName()), res, "skipped", suite, ctx, envt);
                generatePanel(ve, tc.getPassedTests(), res, "passed", suite, ctx, envt);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return res.toString();
    }

    protected void generatePanel(final VelocityEngine ve, final IResultMap map, final StringBuffer res,
            final String style, final ISuite suite, final ITestContext ctx, final boolean envt) {

        Collection<ITestNGMethod> methodSet = getMethodSet(map);

        for (ITestNGMethod method : methodSet) {

            boolean methodIsValid;
            if (envt) {
                methodIsValid = Arrays.asList(method.getGroups()).contains("envt");
            } else {
                methodIsValid = !Arrays.asList(method.getGroups()).contains("envt");
            }

            if (methodIsValid) {

                Collection<ITestResult> resultSet = getResultSet(map, method);
                String content;
                for (ITestResult ans : resultSet) {
                    StringBuffer contentBuffer = new StringBuffer();
                    String testName = "";
                    if (ans.getMethod().getXmlTest() != null) {
                        testName = ans.getMethod().getXmlTest().getName();
                    } else {
                        try {
                            testName = ans.getTestContext().getCurrentXmlTest().getName();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            continue;
                        } catch (Error e) {
                            e.printStackTrace();
                            continue;
                        }
                    }

                    SeleniumTestsContext testLevelContext = SeleniumTestsContextManager.getTestLevelContext(testName);
                    if (testLevelContext != null) {
                        String appURL = testLevelContext.getAppURL();
                        String browser = (String) testLevelContext.getAttribute("browser");
                        if (browser != null) {
                            browser = browser.replace("*", "");
                        }

                        String browserVersion = (String) testLevelContext.getAttribute("browserVersion");
                        if (browserVersion != null) {
                            browser = browser + browserVersion;
                        }

                        contentBuffer.append("<div><i>App URL:  <b>" + appURL + "</b>, Browser: <b>" + browser
                                + "</b></i></div>");
                    }

                    Object[] parameters = ans.getParameters();
                    List<String> msgs = Reporter.getOutput(ans);

                    boolean hasReporterOutput = msgs.size() > 0;
                    Throwable exception = ans.getThrowable();
                    boolean hasThrowable = exception != null;
                    if (hasReporterOutput || hasThrowable) {
                        contentBuffer.append("<div class='leftContent' style='float: left; width: 100%;'>");
                        contentBuffer.append("<h4><a href='javascript:void(0);' class='testloglnk'>Test Steps "
                                + (style.equals("passed") ? "[+]" : "[ - ]") + "</a></h4>");
                        contentBuffer.append("<div class='testlog' "
                                + (style.equals("passed") ? "style='display:none'" : "") + ">");
                        contentBuffer.append("<ol>");
                        for (String line : msgs) {
                            ElaborateLog logLine = new ElaborateLog(line, outputDirectory);
                            String htmllog;
                            if (logLine.getHref() != null) {
                                htmllog = "<a href='" + logLine.getHref() + "' title='" + logLine.getLocation() + "' >"
                                        + logLine.getMsg() + "</a>";
                            } else {
                                htmllog = logLine.getMsg();
                            }

                            htmllog = htmllog.replaceAll("@@lt@@", "<").replace("^^greaterThan^^", ">");
                            contentBuffer.append(htmllog);
                            if (!htmllog.contains("<br>")) {
                                contentBuffer.append("<br/>");
                            }
                        }

                        contentBuffer.append("</ol>");

                        String lastLine = "";
                        for (int lastIdx = msgs.size() - 1; lastIdx >= 0; lastIdx--) {
                            lastLine = msgs.get(lastIdx).replaceAll("@@lt@@", "<").replace("^^greaterThan^^", ">");
                            if (lastLine.indexOf(">screenshot</a>") != -1) {
                                break;
                            }
                        }

                        if (hasThrowable) {
                            generateExceptionReport(exception, method, contentBuffer, lastLine);
                        }

                        contentBuffer.append("</div></div>");
                    }

                    String treeId = "tree" + m_treeId;
                    m_treeId++;
                    if (ans.getStatus() == 3) {
                        contentBuffer.append("<br>method skipped, because of its dependencies :<br>");
                        takeCareOfDirectDependencies(suite, method, 0, ctx, treeId, contentBuffer);
                    }

                    contentBuffer.append("<div class='clear_both'></div>");
                    content = contentBuffer.toString();

                    try {
                        Template t = ve.getTemplate("/templates/report.part.singleTest.html");
                        VelocityContext context = new VelocityContext();
                        context.put("status", style);

                        String javadoc = getJavadocComments(method);
                        String desc = method.getDescription();

                        String toDisplay = "neither javadoc nor description for this method.";
                        if (!"".equals(javadoc) && javadoc != null) {
                            toDisplay = javadoc;
                        } else if (!"".equals(desc) && desc != null) {
                            toDisplay = desc;
                        }

                        String methodSignature = StringUtility.constructMethodSignature(method.getMethod(), parameters);
                        if (methodSignature.length() > 500) {
                            context.put("methodName", methodSignature.substring(0, 500) + "...");
                        } else {
                            context.put("methodName", methodSignature);
                        }

                        context.put("desc", toDisplay.replaceAll("\r\n\r\n", "\r\n").replaceAll("\n\n", "\n"));
                        context.put("content", content);
                        context.put("time", "Time: " + ((ans.getEndMillis() - ans.getStartMillis()) / 1000) + "sec.");

                        StringWriter writer = new StringWriter();
                        t.merge(context, writer);
                        res.append(writer.toString());
                    } catch (Exception e) {
                        logger.error("Exception creating a singleTest." + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public void generateReport(final List<XmlSuite> xml, final List<ISuite> suites, final String outdir) {
        ITestContext testCtx = SeleniumTestsContextManager.getGlobalContext().getTestNGContext();
        if (testCtx == null) {
            logger.error("Looks like your class does not extend from SeleniumTestPlan!");
            return;
        }

        File f = new File(SeleniumTestsContextManager.getGlobalContext().getOutputDirectory());
        setOutputDirectory(f.getParentFile().getAbsolutePath());
        setResources(getOutputDirectory() + "\\resources");
        try {

            m_out = createWriter(getOutputDirectory());
            startHtml(testCtx, m_out);
            generateSuiteSummaryReport(suites, xml.get(0).getName());
            generateReportsSection(suites);
            endHtml(m_out);
            m_out.flush();
            m_out.close();
            copyResources();
            logger.info("Completed Report Generation.");

            String browserPath = (String) SeleniumTestsContextManager.getGlobalContext().getAttribute(
                    SeleniumTestsContext.OPEN_REPORT_IN_BROWSER);
            if (browserPath != null && browserPath.trim().length() > 0) {
                executeCmd(browserPath, getReportLocation().getAbsolutePath());
            }
        } catch (Exception e) {
            logger.error("output file", e);
        }

    }

    protected void generateReportDetailsContainer(final String name, final int envtp, final int envtf, final int envts,
            final int testp, final int testf, final int tests, final String envthtml, final String testhtml,
            final String globalErrorTabs, final String globalErrorHtmls) {
        try {
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty("resource.loader", "class");
            ve.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            ve.init();

            Template t = ve.getTemplate("/templates/report.part.testDetail.html");
            VelocityContext context = new VelocityContext();
            context.put("testId", name.toLowerCase().replace(' ', '_').replace('(', '_').replace(')', '_'));
            context.put("testName", name);
            context.put("envtp", envtp);
            context.put("envtf", envtf);
            context.put("envts", envts);
            context.put("testp", testp);
            context.put("testf", testf);
            context.put("tests", tests);
            context.put("envthtml", envthtml);
            context.put("testhtml", testhtml);
            context.put("globalerrortabs", globalErrorTabs);
            context.put("globalerrorhtmls", globalErrorHtmls);

            StringWriter writer = new StringWriter();
            t.merge(context, writer);
            m_out.write(writer.toString());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    protected void generateReportsSection(final List<ISuite> suites) {

        m_out.println("<div id='reports'>");

        for (ISuite suite : suites) {
            Map<String, ISuiteResult> r = suite.getResults();
            for (ISuiteResult r2 : r.values()) {

                ITestContext tc = r2.getTestContext();

                int envtp = getNbInstanceForGroup(true, tc.getPassedTests());
                int envtf = getNbInstanceForGroup(true, failedTests.get(tc.getName()));
                int envts = getNbInstanceForGroup(true, skippedTests.get(tc.getName()));
                envtf += getNbInstanceForGroup(true, tc.getFailedConfigurations());
                envts += getNbInstanceForGroup(true, tc.getSkippedConfigurations());

                int testp = getNbInstanceForGroup(false, tc.getPassedTests());
                int testf = getNbInstanceForGroup(false, failedTests.get(tc.getName()));
                int tests = getNbInstanceForGroup(false, skippedTests.get(tc.getName()));

                String envthtml = generateHTML(tc, true, suite, tc);
                String testhtml = generateHTML(tc, false, suite, tc);

                StringBuffer globalErrorTabs = new StringBuffer();
                StringBuffer globalErrorHtmls = new StringBuffer();

                generateGlobalErrorHTML(tc, globalErrorTabs, globalErrorHtmls);

                generateReportDetailsContainer(tc.getName(), envtp, envtf, envts, testp, testf, tests, envthtml,
                    testhtml, globalErrorTabs.toString(), globalErrorHtmls.toString());

            }

        }

        m_out.println("</div>");

    }

    public void generateSuiteSummaryReport(final List<ISuite> suites, final String suiteName) {
        NumberFormat formatter = new DecimalFormat("#,##0.0");
        int qty_method = 0;
        int qty_pass_s = 0;
        int qty_skip = 0;
        int qty_fail = 0;
        long time_start = Long.MAX_VALUE;
        long time_end = Long.MIN_VALUE;

        List<ShortTestResult> tests2 = new ArrayList<ShortTestResult>();
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> tests = suite.getResults();
            for (ISuiteResult r : tests.values()) {

                ITestContext overview = r.getTestContext();
                ShortTestResult mini = new ShortTestResult(overview.getName().replace(' ', '_').replace('(', '_')
                            .replace(')', '_'));
                int q;
                q = overview.getAllTestMethods().length;
                qty_method += q;
                mini.setTotalMethod(q);
                q = overview.getPassedTests().size();
                qty_pass_s += q;
                mini.setInstancesPassed(q);
                q = skippedTests.get(overview.getName()).size();
                qty_skip += q;
                mini.setInstancesSkipped(q);
                if (isRetryHandleNeeded.get(overview.getName())) {
                    q = failedTests.get(overview.getName()).size();
                } else {
                    q = failedTests.get(overview.getName()).size()
                            + getNbInstanceForGroup(true, overview.getFailedConfigurations());
                }

                qty_fail += q;
                mini.setInstancesFailed(q);
                time_start = Math.min(overview.getStartDate().getTime(), time_start);
                time_end = Math.max(overview.getEndDate().getTime(), time_end);
                tests2.add(mini);
            }
        }

        ShortTestResult total = new ShortTestResult("total");
        total.setTotalMethod(qty_method);
        total.setInstancesPassed(qty_pass_s);
        total.setInstancesFailed(qty_fail);
        total.setInstancesSkipped(qty_skip);

        try {
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty("resource.loader", "class");
            ve.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            ve.init();

            Template t = ve.getTemplate("/templates/report.part.summary.html");
            VelocityContext context = new VelocityContext();
            context.put("suiteName", suiteName);
            context.put("totalRunTime", formatter.format((time_end - time_start) / 1000.) + " sec");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE dd MMM HH:mm:ss zzz yyyy");
            context.put("TimeStamp", simpleDateFormat.format(new GregorianCalendar().getTime()));
            context.put("tests", tests2);
            context.put("total", total);

            StringWriter writer = new StringWriter();
            t.merge(context, writer);
            m_out.write(writer.toString());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    protected void generateTheStackTrace(final Throwable exception, final ITestNGMethod method, final String title,
            final StringBuffer contentBuffer, final String lastline) {
        contentBuffer.append(" <div class='stContainer' >" + exception.getClass() + ":" + escape(title)
                + "(<a  href='javascript:void(0);'  class='exceptionlnk'>stacktrace</a>)");

        contentBuffer.append("<div class='exception' style='display:none'>");

        StackTraceElement[] s1 = exception.getStackTrace();
        Throwable t2 = exception.getCause();
        if (t2 == exception) {
            t2 = null;
        }

        for (int x = 0; x < s1.length; x++) {
            contentBuffer.append((x > 0 ? "<br/>at " : "") + escape(s1[x].toString()));
        }

        if (t2 != null) {
            generateExceptionReport(t2, method, "Caused by " + t2.getLocalizedMessage(), contentBuffer, ""); // jerry
        }

        contentBuffer.append("</div></div>");
    }

    protected Collection<ITestNGMethod> getAllMethods(final ISuite suite) {
        Set<ITestNGMethod> all = new LinkedHashSet<ITestNGMethod>();
        Map<String, Collection<ITestNGMethod>> methods = suite.getMethodsByGroups();
        for (Entry<String, Collection<ITestNGMethod>> group : methods.entrySet()) {
            all.addAll(methods.get(group.getKey()));
        }

        return all;
    }

    protected int getDim(Class<?> cls) {
        int dim = 0;

        while (cls.isArray()) {
            dim++;
            cls = cls.getComponentType();
        }

        return dim;
    }

    protected ITestResult getFailedOrSkippedResult(final ITestContext ctx, final ITestNGMethod method) {
        List<ITestResult> res = new LinkedList<ITestResult>();
        res.addAll(failedTests.get(ctx.getName()).getResults(method));
        if (res.size() != 0) {
            return res.get(0);
        }

        res.addAll(ctx.getPassedTests().getResults(method));
        if (res.size() != 0) {
            return res.get(0);
        }

        res.addAll(skippedTests.get(ctx.getName()).getResults(method));
        if (res.size() != 0) {
            return res.get(0);
        }

        return null;
    }

    protected JavaDocBuilder getJavaDocBuilder(final Class clz) throws URISyntaxException {
        String projectPath = new File("").getAbsolutePath();
        String packagePath = clz.getPackage().getName().replaceAll("\\.", "/");
        if (builder == null) {
            builder = new JavaDocBuilder();

            URL resource = Thread.currentThread().getContextClassLoader().getResource(packagePath);
            File src = new File(resource.toURI());
            builder.addSourceTree(src);

            // project source folder
            File realFolder = new File(projectPath + "/src/main/java/" + packagePath);
            if (realFolder.exists()) {
                builder.addSourceTree(realFolder);
            }
        }

        return builder;
    }

    protected String getJavadocComments(final ITestNGMethod method) {

        try {
            Method m = method.getMethod();
            String javaClass = m.getDeclaringClass().getName();
            String javaMethod = m.getName();
            JavaClass jc = getJavaDocBuilder(m.getDeclaringClass()).getClassByName(javaClass);
            Class<?>[] types = method.getMethod().getParameterTypes();
            Type[] qdoxTypes = new Type[types.length];
            for (int i = 0; i < types.length; i++) {
                String type = getType(types[i]);
                int dim = getDim(types[i]);
                qdoxTypes[i] = new Type(type, dim);
            }

            JavaMethod jm = jc.getMethodBySignature(javaMethod, qdoxTypes);
            return jm.getComment();
        } catch (Throwable e) {
            logger.error("Exception loading the javadoc comments for : " + method.getMethodName() + e);
            return null;
        }

    }

    /**
     * @param   tests
     *
     * @return
     */
    protected Collection<ITestNGMethod> getMethodSet(final IResultMap tests) {
        Set<ITestNGMethod> r = new TreeSet<ITestNGMethod>(new TestMethodSorter<ITestNGMethod>());
        r.addAll(tests.getAllMethods());
        return r;
    }

    protected int getNbInstanceForGroup(final boolean envt, final IResultMap tests) {
        int res = 0;

        for (ITestResult result : tests.getAllResults()) {

            boolean resultIsAnEnvtRes = Arrays.asList(result.getMethod().getGroups()).contains("envt");

            if (resultIsAnEnvtRes) {
                if (envt) {
                    res++;
                }
            } else {
                if (!envt) {
                    res++;
                }
            }
        }

        return res;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public File getReportLocation() {
        return report;
    }

    public String getResources() {
        return resources;
    }

    /**
     * @param   tests
     *
     * @return
     */
    protected Collection<ITestResult> getResultSet(final IResultMap tests, final ITestNGMethod method) {
        Set<ITestResult> r = new TreeSet<ITestResult>(new TestResultSorter<ITestResult>());
        for (ITestResult result : tests.getAllResults()) {
            if (result.getMethod().getMethodName().equals(method.getMethodName())) {
                r.add(result);
            }
        }

        return r;
    }

    protected ITestNGMethod getTestNGMethod(final ITestContext ctx, final String method) {
        Collection<ITestNGMethod> methods = new HashSet<ITestNGMethod>();

        int index = method.substring(0, method.lastIndexOf(".")).lastIndexOf(".");
        String localMethod = method.substring(index + 1);

        ITestNGMethod[] all = ctx.getAllTestMethods();
        for (int i = 0; i < all.length; i++) {
            methods.add(all[i]);
        }

        for (ITestNGMethod m : methods) {

            if (m.toString().startsWith(localMethod)) {
                return m;
            }
        }

        throw new RuntimeException("method " + method + " not found. " + "Should not happen. Suite " + ctx.getName());
    }

    protected String getType(Class<?> cls) {

        while (cls.isArray()) {
            cls = cls.getComponentType();
        }

        return cls.getName();
    }

    protected boolean hasDependencies(final ITestNGMethod method) {
        return ((method.getGroupsDependedUpon().length + method.getMethodsDependedUpon().length) != 0);
    }

    protected Map<String, ITestResult> initMethodsByGroup() {
        methodsByGroup = new HashMap<String, ITestResult>();

        return null;
    }

    public void onFinish(final ITestContext arg0) {
        if (isRetryHandleNeeded.get(arg0.getName())) {
            removeIncorrectlySkippedTests(arg0, failedTests.get(arg0.getName()));
            removeFailedTestsInTestNG(arg0);
        } else {
            failedTests.put(arg0.getName(), arg0.getFailedTests());
            skippedTests.put(arg0.getName(), arg0.getSkippedTests());
        }
    }

    public void onStart(final ITestContext arg0) {
        isRetryHandleNeeded.put(arg0.getName(), false);
        failedTests.put(arg0.getName(), new ResultMap());
        skippedTests.put(arg0.getName(), new ResultMap());
    }

    public void onTestFailedButWithinSuccessPercentage(final ITestResult arg0) { }

    public synchronized void onTestFailure(final ITestResult arg0) {
        if (arg0.getMethod().getRetryAnalyzer() != null) {
            TestRetryAnalyzer testRetryAnalyzer = (TestRetryAnalyzer) arg0.getMethod().getRetryAnalyzer();

            if (testRetryAnalyzer.getCount() <= testRetryAnalyzer.getMaxCount()) {
                arg0.setStatus(ITestResult.SKIP);
                Reporter.setCurrentTestResult(null);
            } else {
                IResultMap rMap = failedTests.get(arg0.getTestContext().getName());
                rMap.addResult(arg0, arg0.getMethod());
                failedTests.put(arg0.getTestContext().getName(), rMap);
            }

            System.out.println(arg0.getMethod() + " Failed in " + testRetryAnalyzer.getCount() + " times");
            isRetryHandleNeeded.put(arg0.getTestContext().getName(), true);
        }

        // capture snap shot only for the failed web tests
        if (WebUIDriver.getWebDriver() != null) {
            ScreenShot screenShot = new ScreenshotUtil().captureWebPageSnapshot();
            TestLogging.logWebOutput(screenShot.getTitle(), TestLogging.buildScreenshotLog(screenShot), true);
        }
    }

    public void onTestSkipped(final ITestResult arg0) { }

    public void onTestStart(final ITestResult arg0) { }

    public void onTestSuccess(final ITestResult arg0) { }

    /**
     * Remote failed test cases in TestNG.
     *
     * @param   tc
     *
     * @return
     */
    private void removeFailedTestsInTestNG(final ITestContext tc) {
        IResultMap returnValue = tc.getFailedTests();

        ResultMap removeMap = new ResultMap();
        for (ITestResult result : returnValue.getAllResults()) {
            boolean isFailed = false;
            for (ITestResult resultToCheck : failedTests.get(tc.getName()).getAllResults()) {
                if (result.getMethod().equals(resultToCheck.getMethod())
                        && result.getEndMillis() == resultToCheck.getEndMillis()) {
                    isFailed = true;
                    break;
                }
            }

            if (!isFailed) {
                System.out.println("Removed failed cases:" + result.getMethod().getMethodName());
                removeMap.addResult(result, result.getMethod());
            }
        }

        for (ITestResult result : removeMap.getAllResults()) {
            ITestResult removeResult = null;
            for (ITestResult resultToCheck : returnValue.getAllResults()) {
                if (result.getMethod().equals(resultToCheck.getMethod())
                        && result.getEndMillis() == resultToCheck.getEndMillis()) {
                    removeResult = resultToCheck;
                    break;
                }
            }

            if (removeResult != null) {
                returnValue.getAllResults().remove(removeResult);
            }
        }
    }

    /**
     * Remove retrying failed test cases from skipped test cases.
     *
     * @param   tc
     * @param   map
     *
     * @return
     */
    private void removeIncorrectlySkippedTests(final ITestContext tc, final IResultMap map) {
        List<ITestNGMethod> failsToRemove = new ArrayList<ITestNGMethod>();
        IResultMap returnValue = tc.getSkippedTests();

        for (ITestResult result : returnValue.getAllResults()) {
            for (ITestResult resultToCheck : map.getAllResults()) {
                if (resultToCheck.getMethod().equals(result.getMethod())) {
                    failsToRemove.add(resultToCheck.getMethod());
                    break;
                }
            }

            for (ITestResult resultToCheck : tc.getPassedTests().getAllResults()) {
                if (resultToCheck.getMethod().equals(result.getMethod())) {
                    failsToRemove.add(resultToCheck.getMethod());
                    break;
                }
            }
        }

        for (ITestNGMethod method : failsToRemove) {
            returnValue.removeResult(method);
        }

        skippedTests.put(tc.getName(), tc.getSkippedTests());

    }

    public void setOutputDirectory(final String outtimestamped) {
        this.outputDirectory = outtimestamped;
    }

    public void setReportId(final String uuid) {
        this.uuid = uuid;
    }

    public void setResources(final String resources) {
        this.resources = resources;
    }

    /**
     * Begin HTML stream.
     */
    protected void startHtml(final ITestContext ctx, final PrintWriter out) {
        try {
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty("resource.loader", "class");
            ve.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            ve.init();

            Template t = ve.getTemplate("/templates/report.part.header.html");
            VelocityContext context = new VelocityContext();

            String userName = System.getProperty("user.name");
            context.put("userName", userName);
            context.put("currentDate", new Date().toString());

            String mode = SeleniumTestsContextManager.getGlobalContext().getWebRunMode();
            String hubUrl = SeleniumTestsContextManager.getGlobalContext().getWebDriverGrid();
            context.put("gridHub", "<a href='" + hubUrl + "' target=hub>" + hubUrl + "</a>");

            context.put("mode", mode);

            StringBuilder sbGroups = new StringBuilder();
            sbGroups.append("envt,test");

            List<SeleniumTestsPageListener> pageListenerList = PluginsHelper.getInstance().getPageListeners();
            if (pageListenerList != null && !pageListenerList.isEmpty()) {
                for (SeleniumTestsPageListener abstractPageListener : pageListenerList) {
                    sbGroups.append(",").append(abstractPageListener.getClass().getSimpleName());
                }
            }

            context.put("groups", sbGroups.toString());

            StringWriter writer = new StringWriter();
            t.merge(context, writer);
            out.write(writer.toString());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    protected void takeCareOfDirectDependencies(final ISuite suite, final ITestNGMethod method, final int indent,
            final ITestContext ctx, final String treeId, final StringBuffer res) {

        if (indent == 0) {
            res.append("<a href=\"#\" onclick=\"expandTree('" + treeId + "'); return false;\">Expand All</a>&nbsp;");
            res.append("<a href=\"#\" onclick=\"collapseTree('" + treeId + "'); return false;\">Collapse All</a>");
            res.append("<ul class=\"mktree\" id=\"" + treeId + "\">");
        }

        String[] methStr = method.getMethodsDependedUpon();
        if (methStr.length != 0) {
            for (int i = 0; i < methStr.length; i++) {
                ITestNGMethod m = getTestNGMethod(ctx, methStr[i]);
                String intendstr = "";
                for (int j = 0; j < indent; j++) {
                    intendstr += "\t";
                }

                String img = "<img src=\"";
                String m_root = "resources/images/mktree/";
                img += m_root + "/test" + getFailedOrSkippedResult(ctx, m).getStatus() + ".gif";
                img += "\"/>";

                res.append(intendstr + "<li>" + img + m);
                if (hasDependencies(m)) {
                    res.append(intendstr + "<ul>");
                    takeCareOfDirectDependencies(suite, m, indent + 1, ctx, treeId, res);
                    res.append(intendstr + "</ul>");
                }

                res.append("</li>");
            }

        }

        for (int i = 0; i < method.getGroupsDependedUpon().length; i++) {
            if (methodsByGroup == null) {
                methodsByGroup = initMethodsByGroup();

            }

            String dependentGroup = method.getGroupsDependedUpon()[i];

            Set<ITestNGMethod> methods = new LinkedHashSet<ITestNGMethod>();
            Collection<ITestNGMethod> c = suite.getMethodsByGroups().get(dependentGroup);

            if (c != null) {
                methods.addAll(c);
            }

            res.append("<li><u>Group " + dependentGroup + "</u>");
            res.append("<ul>");

            for (ITestNGMethod m : methods) {
                String intendstr = "";
                for (int j = 0; j < indent; j++) {
                    intendstr += "\t";
                }

                String img = "<img src=\"";
                String m_root = "resources/images/mktree/";
                img += m_root + "/test" + getFailedOrSkippedResult(ctx, m).getStatus() + ".gif";
                img += "\"/>";
                res.append(intendstr + "<li>" + img + m);
                if (hasDependencies(m)) {
                    res.append(intendstr + "<ul>");
                    takeCareOfDirectDependencies(suite, m, indent + 1, ctx, treeId, res);
                    res.append(intendstr + "</ul>");
                }

                res.append("</li>");
            }

            res.append("</ul>");
            res.append("</li>");
        }

        if (indent == 0) {
            res.append("</ul>");
        }

    }
}
