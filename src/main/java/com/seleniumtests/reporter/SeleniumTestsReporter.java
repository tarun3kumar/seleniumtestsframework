package com.seleniumtests.reporter;

import java.io.BufferedWriter;
import java.io.File;
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

import com.seleniumtests.controller.AbstractPageListener;
import com.seleniumtests.controller.CustomAssertion;
import com.seleniumtests.controller.Context;
import com.seleniumtests.controller.ContextManager;
import com.seleniumtests.controller.Logging;
import com.seleniumtests.controller.TestRetryAnalyzer;
import com.seleniumtests.driver.web.ScreenShot;
import com.seleniumtests.helper.StringHelper;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.Type;

@SuppressWarnings("deprecation")
public class SeleniumTestsReporter implements IReporter, ITestListener,IInvokedMethodListener {

	// ~ Inner Classes --------------------------------------------------------
	/** Arranges methods by classname and method name */
	protected class TestMethodSorter<T extends ITestNGMethod> implements Comparator<T> {// KEEPME
		// ~ Methods
		// -------------------------------------------------------------

		/** Arranges methods by classname and method name */
		public int compare(T o1, T o2) {
			int r = ((T) o1).getTestClass().getName().compareTo(o2.getTestClass().getName());
			if (r == 0) {
				r = ((T) o1).getMethodName().compareTo(o2.getMethodName());
			}
			return r;
		}
	}
	 
	/** Arranges methods by classname and method name */
	protected class TestResultSorter<T extends ITestResult> implements Comparator<T> {// KEEPME
		// ~ Methods
		// -------------------------------------------------------------

		/** Arranges methods by classname and method name */
		public int compare(T o1, T o2) {
			String sig1 = StringHelper.constructMethodSignature(o1.getMethod().getMethod(), o1.getParameters());
			String sig2 = StringHelper.constructMethodSignature(o2.getMethod().getMethod(), o2.getParameters());
			return sig1.compareTo(sig2);
		}
	}
	private static Logger logger = Logging.getLogger(SeleniumTestsReporter.class);

	protected static String escape(String string) {
		if (null == string)
			return string;
		//return string.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>");
		return string.replaceAll("\n", "<br/>");
	}

	public static void main(String[] args)
	{
		String osName = System.getProperty("os.name");
		System.out.println(osName);
	}
	public static void writeResourceToFile(File file, String resourceName, Class<?> clasz) throws IOException {
		InputStream inputStream = clasz.getResourceAsStream("/" + resourceName);
		if (inputStream == null) {
			logger.error("Couldn't find resource on the class path: " + resourceName);
		}

		else {

			try {
				FileOutputStream outputStream = new FileOutputStream(file);
				try {
					int nread;
					byte[] buffer = new byte[4096];
					while (0 < (nread = inputStream.read(buffer))) {
						outputStream.write(buffer, 0, nread);
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
	// ~ Instance fields ------------------------------------------------------
	private String m_root = "resources/images/mktree/";
	protected PrintWriter m_out;

	private String uuid = new GregorianCalendar().getTime().toString();

	private int m_treeId = 0;

	private String outputDirectory;
	private String resources;
	private JavaDocBuilder builder = null;

	private File report;

	// static{
	// soaReporter = new SOAReporter();
	// }

	// ~ Methods --------------------------------------------------------------

	// Remove enforced hook. TO enable SOAReport, use <listener> in testng.xml
	// private static SOAReporter soaReporter = null;
	// private HashMap<String, List<CALEvent>> calEvent = new HashMap<String,
	// List<CALEvent>>();

	Map<String, ITestResult> methodsByGroup = null;
	
	private void addAllTestResults(Set<ITestResult> testResults, IResultMap resultMap) {
		if (resultMap != null) {
			testResults.addAll(resultMap.getAllResults());
		}
	}

	public void afterInvocation(IInvokedMethod method, ITestResult result) {
		Reporter.setCurrentTestResult(result);
		ScreenShot screenShot  = ContextManager.getThreadContext().getExceptionScreenShot(); 
		//Handle Last Exception only for failed test cases
		if(!result.isSuccess() && ContextManager.getThreadContext() != null && screenShot!=null)
		{
			Logging.log("<div><table><tr bgcolor=\"yellow\"><td><b>-- Screenshot of current web page with webdriver exception --</b><td></tr></table></div>");
			Logging.logWebOutput(screenShot.getTitle(), Logging.buildScreenshotLog(screenShot), true);
		}
		//Handle Soft CustomAssertion
		if (method.isTestMethod()) {
			List<Throwable> verificationFailures = CustomAssertion.getVerificationFailures();
			
			int size = verificationFailures.size();
				//CustomAssertion.fail("Test case faield with "+CustomAssertion.getVerificationFailures().size()+" errors!");
			if(size==0)return;
			else
				if(result.getStatus()==TestResult.FAILURE) return;
				result.setStatus(TestResult.FAILURE);
			
			if (size == 1) {
				result.setThrowable(verificationFailures.get(0));
			} else {
				//create a failure message with all failures and stack traces (except last failure)
				StringBuffer failureMessage = new StringBuffer("Multiple failures (").append(size).append("):nn");
				for (int i = 0; i < size-1; i++) {
					failureMessage.append("Failure ").append(i+1).append(" of ").append(size).append(":n");
					Throwable t = verificationFailures.get(i);
					String fullStackTrace = Utils.stackTrace(t, false)[1];
					failureMessage.append(fullStackTrace).append("nn");
				}
	
				//final failure
				Throwable last = verificationFailures.get(size-1);
				failureMessage.append("Failure ").append(size).append(" of ").append(size).append(":n");
				failureMessage.append(last.toString());
	
				//set merged throwable
				Throwable merged = new Throwable(failureMessage.toString());
				merged.setStackTrace(last.getStackTrace());
	
				result.setThrowable(merged);
			}
		}
	}

	public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {}

	protected void copyResources() throws Exception {

		new File(outputDirectory + File.separator + "resources").mkdir();
		new File(outputDirectory + File.separator + "resources" + File.separator + "css").mkdir();
		new File(outputDirectory + File.separator + "resources" + File.separator + "images").mkdir();
		new File(outputDirectory + File.separator + "resources" + File.separator + "images" + File.separator + "lightbox").mkdir();
		new File(outputDirectory + File.separator + "resources" + File.separator + "images" + File.separator + "mktree").mkdir();
		new File(outputDirectory + File.separator + "resources" + File.separator + "images" + File.separator + "yukontoolbox").mkdir();
		new File(outputDirectory + File.separator + "resources" + File.separator + "js").mkdir();

		List<String> resources = new ArrayList<String>();
		resources.add("reporter" + File.separator + "css" + File.separator + "report.css");
		resources.add("reporter" + File.separator + "css" + File.separator + "jquery.lightbox-0.5.css");
		resources.add("reporter" + File.separator + "css" + File.separator + "mktree.css");
		resources.add("reporter" + File.separator + "images" + File.separator + "lightbox" + File.separator + "seleniumtests_lightbox-blank.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "lightbox" + File.separator + "seleniumtests_lightbox-btn-close.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "lightbox" + File.separator + "seleniumtests_lightbox-btn-next.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "lightbox" + File.separator + "seleniumtests_lightbox-btn-prev.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "lightbox" + File.separator + "seleniumtests_lightbox-ico-loading.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "mktree" + File.separator + "seleniumtests_bullet.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "mktree" + File.separator + "seleniumtests_minus.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "mktree" + File.separator + "seleniumtests_plus.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "mktree" + File.separator + "seleniumtests_test1.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "mktree" + File.separator + "seleniumtests_test2.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "mktree" + File.separator + "seleniumtests_test3.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "mktree" + File.separator + "seleniumtests_test3.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator + "seleniumtests_footer_grad.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator + "seleniumtests_grey_bl.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator + "seleniumtests_grey_br.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator + "seleniumtests_hovertab_l.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator + "seleniumtests_hovertab_r.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator + "seleniumtests_tabbed_nav_goldgradbg.png");
		resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator + "seleniumtests_table_sep_left.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator + "seleniumtests_table_sep_right.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator + "seleniumtests_table_zebrastripe_left.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator + "seleniumtests_table_zebrastripe_right.gif");
		resources.add("reporter" + File.separator + "images" + File.separator + "yukontoolbox" + File.separator + "seleniumtests_yellow_tr.gif");
		resources.add("reporter" + File.separator + "js" + File.separator + "jquery-1.3.min.js");
		resources.add("reporter" + File.separator + "js" + File.separator + "jquery.lightbox-0.5.min.js");
		resources.add("reporter" + File.separator + "js" + File.separator + "mktree.js");
		resources.add("reporter" + File.separator + "js" + File.separator + "report.js");
		resources.add("reporter" + File.separator + "js" + File.separator + "browserdetect.js");

		for (String resourceName : resources) {
			File f = new File(outputDirectory, resourceName.replace("reporter", "resources"));
			resourceName = resourceName.replaceAll("\\\\", "/");
			logger.debug("about to write resource " + resourceName + " to the file " + f.getAbsolutePath());
			writeResourceToFile(f, resourceName, SeleniumTestsReporter.class);
		}
	}

	protected PrintWriter createWriter(String outdir) throws IOException {
		System.setProperty("file.encoding", "UTF8");
		uuid = uuid.replaceAll(" ", "-").replaceAll(":", "-"); 
		File f = new File(outdir, "SeleniumFrameworkTestReport-" + uuid + ".html");
		// ConfigLoader.getInstance().setProperty("report",
		// f.getAbsolutePath());
		logger.info("generating report " + f.getAbsolutePath());

		report = f;
		//return new PrintWriter(new BufferedWriter(new FileWriter(f)));
		// handle garbled code issue 
        OutputStream out = new FileOutputStream(f);
        Writer writer = new BufferedWriter(new OutputStreamWriter(out,"utf-8"));
        return new PrintWriter(writer);

	}

	/** Finishes HTML stream */
	protected void endHtml(PrintWriter out) {
		out.println("</body></html>");
	}

	public void executeCmd(String browserPath,String theUrl) {
		String cmdLine = null;
		String osName = System.getProperty("os.name");
		
		if (osName.startsWith("Windows")) {
			cmdLine = "start " + theUrl;
			// on NT, you need to start cmd.exe because start is not
			// an external command but internal, you need to start the
			// command interpreter
			// cmdLine = "cmd.exe /c " + cmdLine;
			cmdLine = "rundll32 SHELL32.DLL,ShellExec_RunDLL " + browserPath + " "  + theUrl;
		} else if(osName.startsWith("Mac"))
		{
			cmdLine =  "open " + theUrl;
		}
		else {
			//  Linux
			cmdLine =  "open " + browserPath + " "  + theUrl;
		}
		try {
			Runtime.getRuntime().exec(cmdLine);
		} catch (Exception e) {
			logger.info(e);
		}
	}

	protected void generateCalErrorReport(String exception, StringBuffer contentBuffer) {

		contentBuffer.append(" <div class='stContainer' ><a  href='javascript:void(0);'  class='exceptionlnk'>Detail</a>");
		contentBuffer.append("<div class='exception' style='display:none'>");
		contentBuffer.append(exception);
		contentBuffer.append("</div></div>");
	}

	/*
	 * protected String generateCALErrorHTML(ITestContext tc, ISuite suite,
	 * StringBuffer calcount) {
	 * 
	 * StringBuffer res = new StringBuffer(); try { VelocityEngine ve = new
	 * VelocityEngine(); ve.setProperty("resource.loader", "class");
	 * ve.setProperty("class.resource.loader.class",
	 * "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
	 * ve.init(); generateCALErrorPanel(ve, res, "failed", suite, tc, calcount);
	 * } catch (Exception e) { logger.error(e.getMessage()); }
	 * 
	 * return res.toString(); }
	 */

	/*
	 * private void generateCALErrorPanel(VelocityEngine ve, StringBuffer res,
	 * String style, ISuite suite, ITestContext ctx, StringBuffer sbCalcount) {
	 * 
	 * Set<ITestResult> testResults = new HashSet<ITestResult>();
	 * 
	 * addAllTestResults(testResults, ctx.getPassedTests());
	 * addAllTestResults(testResults, ctx.getFailedTests());
	 * addAllTestResults(testResults, ctx.getSkippedTests());
	 * addAllTestResults(testResults, ctx.getPassedConfigurations());
	 * addAllTestResults(testResults, ctx.getSkippedConfigurations());
	 * addAllTestResults(testResults, ctx.getFailedConfigurations());
	 * addAllTestResults(testResults,
	 * ctx.getFailedButWithinSuccessPercentageTests());
	 * 
	 * int cal = 0;
	 * 
	 * if (calEvent.isEmpty()) { res.append(
	 * "<div class='method passed'><div class='yuk_goldgrad_tl'><div class='yuk_goldgrad_tr'>"
	 * + "<div class='yuk_goldgrad_m'></div></div></div>" +
	 * "<h3 class='yuk_grad_ltitle_passed'>No CAL Errors found.</h3>" +
	 * "<div class='yuk_pnl_footerbar'></div>" +
	 * "<div class='yuk_grey_bm_footer'><div class='yuk_grey_br'>" +
	 * "<div class='yuk_grey_bl'></div></div></div></div>"); } else { try {
	 * StringBuffer contentBuffer = null;
	 * 
	 * Iterator<Entry<String, List<CALEvent>>> i =
	 * calEvent.entrySet().iterator(); while (i.hasNext()) {
	 * 
	 * Entry<String, List<CALEvent>> e = (Entry<String, List<CALEvent>>)
	 * i.next(); if (e.getKey() == null) continue;
	 * 
	 * contentBuffer = new StringBuffer(); contentBuffer.append(
	 * "<div class='leftContent' style='float: left; width: 100%;'>");
	 * 
	 * String cmd = (String) e.getKey(); List<CALEvent> events =
	 * (List<CALEvent>) e.getValue(); contentBuffer .append(
	 * "<table cellspacing=0 style='border-style:solid;border-width:1px'><thead><tr><th style='border-spacing:0px;border-style:solid;border-width:1px' >Event Type</th><th style='border-spacing:0px;border-style:solid;border-width:1px'>Event Name</th><th style='border-spacing:0px;border-style:solid;border-width:1px'>Test Case</th></tr></thead><tbody>"
	 * );
	 * 
	 * Set<String> eventNameSet = new TreeSet<String>(); for (CALEvent event :
	 * events) { if (eventNameSet.contains(event.getName())) continue;
	 * 
	 * eventNameSet.add(event.getName());
	 * 
	 * contentBuffer.append(
	 * "<tr><td style='border-spacing:0px;border-style:none;border-width:1px' valign='top'><a href='"
	 * + event.getCALLinkURL() + "' target=cal>");
	 * contentBuffer.append(event.getType()); contentBuffer.append("</a>");
	 * contentBuffer.append(
	 * "</td><td style='border-spacing:0px;border-style:none;border-width:1px' valign='top'>"
	 * ); contentBuffer.append(event.getName());
	 * generateCalErrorReport(event.getPayload().replaceAll("(\\\\r|\\\\n)+",
	 * "\n").replaceAll("(\\\\t)+", "     "), contentBuffer);
	 * 
	 * contentBuffer.append(
	 * "</td><td style='border-spacing:0px;border-style:solid;border-width:1px' valign='top'>"
	 * );
	 * 
	 * if (event.getTestCaseId() != null &&
	 * !"null".equalsIgnoreCase(event.getTestCaseId())) {
	 * contentBuffer.append(Context
	 * .getSignatureFromTestCaseId(event.getTestCaseId()));
	 * contentBuffer.append(" - "); contentBuffer.append(event.getTestCaseId());
	 * contentBuffer.append("</td></tr>"); } else
	 * contentBuffer.append("&nbsp;</td></tr>");
	 * 
	 * cal++; } contentBuffer.append("</tbody></table></div>"); // end of //
	 * leftContent contentBuffer.append("<div class='clear_both'></div>");
	 * 
	 * Template t = ve.getTemplate("/templates/report.part.singleTest.html");
	 * VelocityContext context = new VelocityContext(); context.put("status",
	 * style); context.put("desc", ""); context.put("time", "");
	 * context.put("methodName", cmd); context.put("content",
	 * contentBuffer.toString()); StringWriter writer = new StringWriter();
	 * t.merge(context, writer); res.append(writer.toString()); }
	 * 
	 * List<CALEvent> evt = calEvent.get(null); if (evt != null && evt.size() >
	 * 0) { contentBuffer = new StringBuffer(); contentBuffer.append(
	 * "<div class='leftContent' style='float: left; width: 100%;'>");
	 * contentBuffer .append(
	 * "<table cellspacing=0 style='border-style:solid;border-width:1px'><thead><tr><th style='border-spacing:0px;border-style:solid;border-width:1px' >Event Type</th><th style='border-spacing:0px;border-style:solid;border-width:1px'>Event Name</th><th style='border-spacing:0px;border-style:solid;border-width:1px'>Test Case</th></tr></thead><tbody>"
	 * );
	 * 
	 * Set<String> eventNameSet = new TreeSet<String>(); for (CALEvent event :
	 * evt) { if (eventNameSet.contains(event.getName())) continue;
	 * 
	 * eventNameSet.add(event.getName());
	 * 
	 * contentBuffer.append(
	 * "<tr><td style='border-spacing:0px;border-style:solid;border-width:1px' valign='top'><a href='"
	 * + event.getCALLinkURL() + "' target=cal>");
	 * contentBuffer.append(event.getType()); contentBuffer.append("</a>");
	 * contentBuffer.append(
	 * "</td><td style='border-spacing:0px;border-style:solid;border-width:1px' valign='top'>"
	 * ); contentBuffer.append(event.getName());
	 * generateCalErrorReport(event.getPayload().replaceAll("(\\\\r|\\\\n)+",
	 * "\n").replaceAll("(\\\\t)+", "     "), contentBuffer);
	 * 
	 * contentBuffer.append(
	 * "</td><td style='border-spacing:0px;border-style:solid;border-width:1px' valign='top'>"
	 * );
	 * 
	 * if (event.getTestCaseId() != null &&
	 * !"null".equalsIgnoreCase(event.getTestCaseId())) {
	 * contentBuffer.append(Context
	 * .getSignatureFromTestCaseId(event.getTestCaseId()));
	 * contentBuffer.append(" - "); contentBuffer.append(event.getTestCaseId());
	 * contentBuffer.append("</td></tr>"); } else
	 * contentBuffer.append("&nbsp;</td></tr>");
	 * 
	 * cal++; } contentBuffer.append("</tbody></table></div>"); // end of //
	 * leftContent contentBuffer.append("<div class='clear_both'></div>");
	 * 
	 * Template t = ve.getTemplate("/templates/report.part.singleTest.html");
	 * VelocityContext context = new VelocityContext(); context.put("status",
	 * style); context.put("desc", ""); context.put("time", "");
	 * context.put("methodName", ""); context.put("content",
	 * contentBuffer.toString()); StringWriter writer = new StringWriter();
	 * t.merge(context, writer); res.append(writer.toString()); }
	 * 
	 * } catch (Exception e) { e.printStackTrace();
	 * logger.error("error creating a cal log report for null test case id." +
	 * e.getMessage()); } } sbCalcount.append(cal); }
	 */

	protected void generateExceptionReport(Throwable exception, ITestNGMethod method, String title, StringBuffer contentBuffer, String lastline) {// Jerry
																																					// add
																																					// lastline
		generateTheStackTrace(exception, method, title, contentBuffer, lastline);
	}

	protected void generateExceptionReport(Throwable exception, ITestNGMethod method, StringBuffer contentBuffer, String lastline) {
		Throwable fortile = exception;
		/*
		 * if (exception instanceof VerificationException) { fortile =
		 * ((VerificationException)exception).getMaster(); }
		 */
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

	protected void generateGlobalErrorHTML(ITestContext tc, ISuite suite, StringBuffer errorCountTabs, StringBuffer errorCountHtmls) {
		try {
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty("resource.loader", "class");
			ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			ve.init();

			List<AbstractPageListener> pageListenersList = PluginsUtil.getInstance().getPageListeners();
			for (AbstractPageListener abstractPageListener : pageListenersList) {
				/* ========== to skip creating a tab according to "testResultEffected" property of an instance of com.seleniumtests.controller.AbstractPageListener, added by ziwu, 09/16/2011 ========== */
				if (!abstractPageListener.isTestResultEffected()) continue;
				/* ========== to skip creating a tab according to "testResultEffected" property of an instance of com.seleniumtests.controller.AbstractPageListener, added by ziwu, 09/16/2011 ========== */

				errorCountTabs.append("<li class='tab' id='" + abstractPageListener.getClass().getSimpleName() + "'><a href='#'><span>")
						.append(abstractPageListener.getTitle() != null ? abstractPageListener.getTitle() : abstractPageListener.getClass().getSimpleName())
						.append(" ( <font color='red'>");
				errorCountHtmls.append("<div class='" + abstractPageListener.getClass().getSimpleName() + "' style='width: 98%;margin-left:15px;'>");
				generateGlobalErrorsPanel(abstractPageListener, ve, errorCountHtmls, "failed", tc, errorCountTabs);
				errorCountHtmls.append("</div>");
				errorCountTabs.append("</font> )</span></a></li>");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private void generateGlobalErrorsPanel(AbstractPageListener abstractPageListener, VelocityEngine ve, StringBuffer res, String style, ITestContext tc,
			StringBuffer sbCalcount) {

		int pageCount = 0;

		Set<ITestResult> testResults = new HashSet<ITestResult>();

		addAllTestResults(testResults, tc.getPassedTests());
		addAllTestResults(testResults, failedTests.get(tc.getName()));
		addAllTestResults(testResults, tc.getFailedButWithinSuccessPercentageTests());

		Map<String, Map<String, List<String>>> pageListenerLogMap = Logging.getPageListenerLog(abstractPageListener.getClass().getCanonicalName());
		if (pageListenerLogMap == null || pageListenerLogMap.isEmpty()) {
			res.append("<div class='method passed'><div class='yuk_goldgrad_tl'><div class='yuk_goldgrad_tr'>"
					+ "<div class='yuk_goldgrad_m'></div></div></div>" + "<h3 class='yuk_grad_ltitle_passed'>No Errors found.</h3>"
					+ "<div class='yuk_pnl_footerbar'></div>" + "<div class='yuk_grey_bm_footer'><div class='yuk_grey_br'>"
					+ "<div class='yuk_grey_bl'></div></div></div></div>");
		} else {
			for (Entry<String, Map<String, List<String>>> pageEntry : pageListenerLogMap.entrySet()) {
				StringBuffer contentBuffer = new StringBuffer();
				contentBuffer.append("<table  class='ex' width='90%'><thead><tr><th>TestMethod</th><th>Errors</th></thead><tbody>");
				Map<String, List<String>> errorMap = pageEntry.getValue();

				boolean found = false;
				for (ITestResult testResult : testResults) {
					Method method = testResult.getMethod().getMethod();
					String methodInstance = StringHelper.constructMethodSignature(method, testResult.getParameters());
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
						logger.error("error creating a singlePageError." + e.getMessage());
					}
					pageCount++;
				}
			}
		}
		sbCalcount.append(pageCount);
	}

	public void generateGroupsArea(Collection<ITestNGMethod> methods) {
		Set<String> allGroups = new HashSet<String>();
		for (ITestNGMethod method : methods) {
			for (int j = 0; j < method.getGroups().length; j++) {
				allGroups.add(method.getGroups()[j]);
			}
		}
		m_out.print("Tags :<br/>");
		for (String group : allGroups) {
			m_out.print("<input type=\"checkbox\"  value=\"" + group.replace(' ', '_').replace('(', '_').replace(')', '_') + "\" checked='checked'> " + group
					+ " ");
		}

	}

	protected String generateHTML(ITestContext tc, boolean envt, ISuite suite, ITestContext ctx) {

		StringBuffer res = new StringBuffer();
		try {
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty("resource.loader", "class");
			ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			ve.init();

			if (envt) {
				if (tc.getFailedConfigurations().getAllResults().size() > 0)
					generatePanel(ve, tc.getFailedConfigurations(), res, "failed", suite, ctx, envt);
				generatePanel(ve, failedTests.get(tc.getName()), res, "failed", suite, ctx, envt);
				if (tc.getFailedConfigurations().getAllResults().size() > 0)
					generatePanel(ve, tc.getSkippedConfigurations(), res, "skipped", suite, ctx, envt);
				generatePanel(ve, skippedTests.get(tc.getName()), res, "skipped", suite, ctx, envt);
				generatePanel(ve, tc.getPassedTests(), res, "passed", suite, ctx, envt);
				// generatePanel(ve, tc.getPassedConfigurations(), res,
				// "passed", suite, ctx, envt);
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

	protected void generatePanel(VelocityEngine ve, IResultMap map, StringBuffer res, String style, ISuite suite, ITestContext ctx, boolean envt) {

		Collection<ITestNGMethod> methodSet = getMethodSet(map);

		for (ITestNGMethod method : methodSet) {
		
			boolean methodIsValid = true;
			if (envt) {
				methodIsValid = Arrays.asList(method.getGroups()).contains("envt");
			} else {
				methodIsValid = !Arrays.asList(method.getGroups()).contains("envt");
			}

			if (methodIsValid) {

				Collection<ITestResult> resultSet = getResultSet(map, method);
				//System.out.println(method.getMethodName()+":"+resultSet.size());
				String content = ctx.getName().replace(' ', '_').replace('(', '_').replace(')', '_');
				for (ITestResult ans : resultSet) {
					StringBuffer contentBuffer = new StringBuffer();
					String testName="";
					if(ans.getMethod().getXmlTest()!=null)
						testName = ans.getMethod().getXmlTest().getName();
					else
					{
						try{
							testName = ans.getTestContext().getCurrentXmlTest().getName();
						}catch(Exception ex)
						{
							ex.printStackTrace();
							continue;
						}catch(Error e)
						{
							e.printStackTrace();
							continue;
						}
					}
					Context testLevelContext = ContextManager.getTestLevelContext(testName);
					if (testLevelContext !=  null ) {
						String appURL = testLevelContext.getAppURL();
						String browser = (String)testLevelContext.getAttribute("browser");
						if (browser != null)
							browser = browser.replace("*", "");
						String browserVersion = (String)testLevelContext.getAttribute("browserVersion");
						if (browserVersion != null)
							browser = browser + browserVersion;
						contentBuffer.append("<div><i>App URL:  "+appURL+ ", Browser: " + browser + "</i></div>");
					}
					Object[] parameters = ans.getParameters();
					List<String> msgs = Reporter.getOutput(ans);

					boolean hasReporterOutput = msgs.size() > 0;
					Throwable exception = ans.getThrowable();
					boolean hasThrowable = exception != null;
					if (hasReporterOutput || hasThrowable) {
						contentBuffer.append("<div class='leftContent' style='float: left; width: 100%;'>");
						contentBuffer.append("<h4><a href='javascript:void(0);' class='testloglnk'>Test Steps " + (style.equals("passed") ? "[+]" : "[ - ]")
								+ "</a></h4>");
						contentBuffer.append("<div class='testlog' " + (style.equals("passed") ? "style='display:none'" : "") + ">");
						contentBuffer.append("<ol>");
						for (String line : msgs) {
							ElaborateLog logLine = new ElaborateLog(line, outputDirectory);
							String htmllog;
							if (logLine.getHref() != null) {
								htmllog = "<a href='" + logLine.getHref() + "' title='" + logLine.getLocation() + "' >" + logLine.getMsg() + "</a>";
							} else {
								htmllog = logLine.getMsg();
							}
							
							htmllog = htmllog.replaceAll("@@lt@@", "<").replace("^^gt^^", ">");//fix for testng 6.7
							contentBuffer.append(htmllog);
							if(!htmllog.contains("<br>"))contentBuffer.append("<br/>");//handle different in testng6.7
						}
						contentBuffer.append("</ol>");

						// Jerry
						String lastLine = "";
						for (int lastIdx = msgs.size() - 1; lastIdx >= 0; lastIdx--) {
							lastLine = msgs.get(lastIdx).replaceAll("@@lt@@", "<").replace("^^gt^^", ">"); //fix for testng 6.7
							if (lastLine.indexOf(">screenshot</a>") != -1) {
								break;
							}
						}
						if (hasThrowable) {
							generateExceptionReport(exception, method, contentBuffer, lastLine);
						}
						contentBuffer.append("</div></div>"); // end of
					}

					//int rq = 0;
					/* freynaud */
					String treeId = "tree" + m_treeId;
					m_treeId++;
					if (ans.getStatus() == 3) {
						contentBuffer.append("<br>This method has been skipped, because of its dependencies :<br>");
						takeCareOfDirectDependencies(suite, method, 0, ctx, treeId, contentBuffer);
					}

					//rq += 1;
					contentBuffer.append("<div class='clear_both'></div>");
					content = contentBuffer.toString();

					try {
						Template t = ve.getTemplate("/templates/report.part.singleTest.html");
						VelocityContext context = new VelocityContext();
						context.put("status", style);
						String javadoc = getJavadocComments(method);
						String desc = method.getDescription();

						String toDisplay = "no javadoc nor description for this method.";
						if (!"".equals(javadoc) && javadoc != null) {
							toDisplay = javadoc;
						} else if (!"".equals(desc) && desc != null) {
							toDisplay = desc;
						}
						String methodSignature = StringHelper.constructMethodSignature(method.getMethod(), parameters);
						if(methodSignature.length()>500)
							context.put("methodName", methodSignature.substring(0, 500)+"...");
						else
							context.put("methodName", methodSignature);
						context.put("desc", toDisplay.replaceAll("\r\n\r\n", "\r\n").replaceAll("\n\n", "\n"));
						context.put("content", content);
						context.put("time", "Time: " + ((ans.getEndMillis() - ans.getStartMillis()) / 1000) + "sec.");
						StringWriter writer = new StringWriter();
						t.merge(context, writer);
						res.append(writer.toString());
					} catch (Exception e) {
						logger.error("error creating a singleTest." + e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}

	}

	public void generateReport(List<XmlSuite> xml, List<ISuite> suites, String outdir) {
		ITestContext testCtx = ContextManager.getGlobalContext().getTestNGContext();
		if(testCtx == null) {
		   logger.error("Please check if your class extends from SeleniumTestPlan!");
		   return;
		}
		File f = new File(ContextManager.getGlobalContext().getOutputDirectory());
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
			logger.info("report generated.");
			//String browserPath = (String) testCtx.getSuite().getParameter(Context.OPEN_REPORT_IN_BROWSER);
			String browserPath = (String)ContextManager.getGlobalContext().getAttribute(Context.OPEN_REPORT_IN_BROWSER);
			if (browserPath != null && browserPath.trim().length() > 0) {
				executeCmd(browserPath , getReportLocation().getAbsolutePath());
			}
		} catch (Exception e) {
			logger.error("output file", e);
			return;
		}

	}

	protected void generateReportDetailsContainer(String name, int envtp, int envtf, int envts, int testp, int testf, int tests, String envthtml,
			String testhtml) {
		try {
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty("resource.loader", "class");
			ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
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
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			m_out.write(writer.toString());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	protected void generateReportDetailsContainer(String name, int envtp, int envtf, int envts, int testp, int testf, int tests, String envthtml,
			String testhtml, StringBuffer calCount, String globalErrorTabs, String globalErrorHtmls) {
		try {
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty("resource.loader", "class");
			ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
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
			context.put("calcount", calCount.toString());
			context.put("globalerrortabs", globalErrorTabs);
			context.put("globalerrorhtmls", globalErrorHtmls);

			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			m_out.write(writer.toString());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	protected void generateReportsSection(List<ISuite> suites) {

		m_out.println("<div id='reports'>");

		for (ISuite suite : suites) {
			Map<String, ISuiteResult> r = suite.getResults();
			for (ISuiteResult r2 : r.values()) {

				ITestContext tc = r2.getTestContext();

				int envtp = getNbInstanceForGroup(true, tc.getPassedTests());
				int envtf = getNbInstanceForGroup(true, failedTests.get(tc.getName()));
				int envts = getNbInstanceForGroup(true, skippedTests.get(tc.getName()));
				// envtp += getNbInstanceForGroup(true,
				// tc.getPassedConfigurations());
				envtf += getNbInstanceForGroup(true, tc.getFailedConfigurations());
				envts += getNbInstanceForGroup(true, tc.getSkippedConfigurations());

				int testp = getNbInstanceForGroup(false, tc.getPassedTests());
				int testf = getNbInstanceForGroup(false, failedTests.get(tc.getName()));
				int tests = getNbInstanceForGroup(false, skippedTests.get(tc.getName()));

				String envthtml = generateHTML(tc, true, suite, tc);
				String testhtml = generateHTML(tc, false, suite, tc);
				StringBuffer calcount = new StringBuffer();
				// String calhtml = generateCALErrorHTML(tc, suite, calcount);

				StringBuffer globalErrorTabs = new StringBuffer();
				StringBuffer globalErrorHtmls = new StringBuffer();

				generateGlobalErrorHTML(tc, suite, globalErrorTabs, globalErrorHtmls);

				generateReportDetailsContainer(tc.getName(), envtp, envtf, envts, testp, testf, tests, envthtml, testhtml, calcount,
						globalErrorTabs.toString(), globalErrorHtmls.toString());

			}

		}
		m_out.println("</div>");

	}

	public void generateSuiteSummaryReport(List<ISuite> suites, String suiteName) {
		NumberFormat formatter = new DecimalFormat("#,##0.0");
		// int qty_tests = 0;
		int qty_method = 0;
		//int qty_pass_m = 0;
		int qty_pass_s = 0;
		int qty_skip = 0;
		int qty_fail = 0;
		long time_start = Long.MAX_VALUE;
		long time_end = Long.MIN_VALUE;

		List<ShortTestResult> tests2 = new ArrayList<ShortTestResult>();
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> tests = suite.getResults();
			for (ISuiteResult r : tests.values()) {

				// qty_tests += 1;
				ITestContext overview = r.getTestContext();
				ShortTestResult mini = new ShortTestResult(overview.getName().replace(' ', '_').replace('(', '_').replace(')', '_'));
				int q = getMethodSet(overview.getPassedTests()).size();
				//qty_pass_m += q;
				q = overview.getAllTestMethods().length;
				qty_method += q;
				mini.setTotalMethod(q);
				q = overview.getPassedTests().size();
				qty_pass_s += q;
				mini.setInstancesPassed(q);
				q = skippedTests.get(overview.getName()).size();//getMethodSet(skippedTests.get(overview.getName())).size();
				qty_skip += q;
				mini.setInstancesSkipped(q);
				if(isRetryHandleNeeded.get(overview.getName()))
					q = failedTests.get(overview.getName()).size() ;
				else
					q = failedTests.get(overview.getName()).size() + getNbInstanceForGroup(true, overview.getFailedConfigurations());
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
			// BufferedWriter out = new BufferedWriter(new FileWriter(path));

			/* first, get and initialize an engine */
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty("resource.loader", "class");
			ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			ve.init();

			Template t = ve.getTemplate("/templates/report.part.summary.html");
			VelocityContext context = new VelocityContext();
			context.put("suiteName", suiteName);
			context.put("totalRunTime", formatter.format((time_end - time_start) / 1000.) + " sec");
			context.put("tests", tests2);
			context.put("total", total);

			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			m_out.write(writer.toString());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}
	
	protected void generateTheStackTrace(Throwable exception, ITestNGMethod method, String title, StringBuffer contentBuffer, String lastline) {
		//contentBuffer.append("<div><table><tr bgcolor=\"yellow\"><td>Last Step Screencapture " + lastline + "<td></tr></table></div>");// Jerry
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
			generateExceptionReport(t2, method, "Caused by " + t2.getLocalizedMessage(), contentBuffer, "");// jerry
		}
		contentBuffer.append("</div></div>");
	}

	protected Collection<ITestNGMethod> getAllMethods(ISuite suite) {
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

	public int getEnvConfigTestsCount(IResultMap map) {
		int count = 0;
		for (ITestNGMethod tm : map.getAllMethods()) {
			String[] groups = tm.getGroups();
			if (groups != null) {
				for (int i = 0; i < groups.length; i++) {
					if ("envt".equalsIgnoreCase(groups[i])) {
						count++;
						break;
					}
				}
			}
		}

		return count;
	}

	protected ITestResult getFailedOrSkippedResult(ITestContext ctx, ITestNGMethod method) {
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

	@SuppressWarnings("rawtypes")
	protected JavaDocBuilder getJavaDocBuilder(Class clz) throws URISyntaxException {
		String projectPath = new File("").getAbsolutePath();
		String packagePath = clz.getPackage().getName().replaceAll("\\.", "/");
		if (builder == null) {
			builder = new JavaDocBuilder();

			URL resource = Thread.currentThread().getContextClassLoader().getResource(packagePath);
			File src = new File(resource.toURI());
			builder.addSourceTree(src);

			// project source folder
			File realFolder = new File(projectPath + "/src/main/java/" + packagePath);
			if (realFolder.exists())
				builder.addSourceTree(realFolder);
		}
		return builder;
	}

	/**
	 * 
	 * @param method
	 * @return the java doc comment , or null.
	 */
	protected String getJavadocComments(ITestNGMethod method) {

		try {
			Method m = method.getMethod();
			String javaClass = m.getDeclaringClass().getName();
			String javaMethod = m.getName();
			JavaClass jc = getJavaDocBuilder(m.getDeclaringClass()).getClassByName(javaClass);
			Class<?>[] types = method.getMethod().getParameterTypes();
			Type[] qdoxTypes = new Type[types.length];
			for (int i = 0; i < types.length; i++) {
				// String s = types[i].getName();
				String type = getType(types[i]);
				int dim = getDim(types[i]);
				// System.out.println(s + " - " + type + " - " + dim);
				qdoxTypes[i] = new Type(type, dim);
			}
			JavaMethod jm = jc.getMethodBySignature(javaMethod, qdoxTypes);
			return jm.getComment();
		} catch (Throwable e) {
			logger.error("error loading the javadoc comments for : " + method.getMethodName() + e);
			return null;
		}

	}

	/**
	 * @param tests
	 * @return
	 */
	protected Collection<ITestNGMethod> getMethodSet(IResultMap tests) {
		Set<ITestNGMethod> r = new TreeSet<ITestNGMethod>(new TestMethodSorter<ITestNGMethod>());
		r.addAll(tests.getAllMethods());
		return r;
	}

	protected int getNbInstanceForGroup(boolean envt, IResultMap tests) {
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
	 * @param tests
	 * @return
	 */
	protected Collection<ITestResult> getResultSet(IResultMap tests, ITestNGMethod method) {
		Set<ITestResult> r = new TreeSet<ITestResult>(new TestResultSorter<ITestResult>());
		for(ITestResult result : tests.getAllResults())
		{
			if(result.getMethod().getMethodName().equals(method.getMethodName()))
			{
				//r.addAll(tests.getResults(method));
				r.add(result);
			}
		}
		return r;
	}

	protected ITestNGMethod getTestNGMethod(ITestContext ctx, String method) {
		Collection<ITestNGMethod> methods = new HashSet<ITestNGMethod>();
		
		//jliang
		//TestNG 6.3.  Skip method does not start with package name.  So strip off the package name before comparing.
		int index = method.substring(0, method.lastIndexOf(".")).lastIndexOf(".");
		String localMethod = method.substring(index+1);
		
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

	protected boolean hasDependencies(ITestNGMethod method) {
		return ((method.getGroupsDependedUpon().length + method.getMethodsDependedUpon().length) != 0);
	}

	protected Map<String, ITestResult> initMethodsByGroup() {
		methodsByGroup = new HashMap<String, ITestResult>();

		return null;
	}

	public void onFinish(final ITestContext arg0) {
		// runEnd = Calendar.getInstance().getTime();
		//
		// // Thread soaThread = new Thread(){@Override
		// // public void run() {
		// // if(Context.isCalCollectionEnabled()){
		// // soaReporter.onFinish(arg0);
		// // }
		// // }};
		// // soaThread.start();
		//
		// if (Context.isCalCollectionEnabled()) {
		// logger.info("Collecting CAL Events...");
		//
		// Calendar counter = Calendar.getInstance();
		// counter.setTime(runBegin);
		// CalEventCollector cal = new CalEventCollector(Context.getRunId());
		// ArrayList<CALEvent> events = new ArrayList<CALEvent>();
		//
		// try {
		// Thread.sleep(10000);
		// } catch (InterruptedException e1) {
		//
		// }
		//
		// while (counter.before(runEnd)) {
		//
		// events.addAll(cal.collect(Context.getPool().toLowerCase(), counter));
		// counter.add(Calendar.HOUR, 1);
		// }
		//
		// events.addAll(cal.collect(Context.getPool().toLowerCase(), counter));
		//
		// // Sort events into HashMap using cmd id as key
		// if (!events.isEmpty()) {
		// for (CALEvent e : events) {
		// if (calEvent.containsKey(e.getCmd())) {
		// calEvent.get(e.getCmd()).add(e);
		// } else {
		// ArrayList<CALEvent> a = new ArrayList<CALEvent>();
		// a.add(e);
		// calEvent.put(e.getCmd(), a);
		// }
		// }
		// }
		// logger.info("Completed Collecting CAL Events.");
		// }
		// // if(Context.isCalCollectionEnabled()){
		// // try {
		// // soaThread.join();
		// // } catch (InterruptedException e) {
		// // }
		// //
		// // }
		if(isRetryHandleNeeded.get(arg0.getName()))
		{
			removeIncorrectlySkippedTests(arg0, failedTests.get(arg0.getName()));
			removeFailedTestsInTestNG(arg0);
		}else
		{
			failedTests.put(arg0.getName(), arg0.getFailedTests());
			skippedTests.put(arg0.getName(), arg0.getSkippedTests());
		}
	}

	public void onStart(ITestContext arg0) {
		// runBegin = Calendar.getInstance().getTime();
		// if(Context.isCalCollectionEnabled()){
		// soaReporter.onStart(arg0);
		// }
		isRetryHandleNeeded.put(arg0.getName(), false);
		failedTests.put(arg0.getName(), new ResultMap());
		skippedTests.put(arg0.getName(), new ResultMap());
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// if(Context.isCalCollectionEnabled()){
		// soaReporter.onTestFailedButWithinSuccessPercentage(arg0);
		// }
	}

	public synchronized void onTestFailure(ITestResult arg0) {
		if (arg0.getMethod().getRetryAnalyzer() != null) {
			TestRetryAnalyzer testRetryAnalyzer = (TestRetryAnalyzer) arg0.getMethod().getRetryAnalyzer();

			if (testRetryAnalyzer.getCount() <= testRetryAnalyzer.getMaxCount()) {
				arg0.setStatus(ITestResult.SKIP);
				Reporter.setCurrentTestResult(null);
			}
			else {
				IResultMap rMap = failedTests.get(arg0.getTestContext().getName());
				rMap.addResult(arg0, arg0.getMethod());
				failedTests.put(arg0.getTestContext().getName(), rMap);
			}
			System.out.println(arg0.getMethod()+" Failed in "+testRetryAnalyzer.getCount()+" times");
			isRetryHandleNeeded.put(arg0.getTestContext().getName(), true);
		}
		// if(Context.isCalCollectionEnabled()){
		// soaReporter.onTestFailure(arg0);
		// }
	}

	public void onTestSkipped(ITestResult arg0) {
		// if(Context.isCalCollectionEnabled()){
		// soaReporter.onTestSkipped(arg0);
		// }

	}

	public void onTestStart(ITestResult arg0) {
		// if(Context.isCalCollectionEnabled()){
		// soaReporter.onTestStart(arg0);
		// }
	}

	public void onTestSuccess(ITestResult arg0) {
		// if(Context.isCalCollectionEnabled()){
		// soaReporter.onTestSuccess(arg0);
		// }
/*		if (arg0.getMethod().getRetryAnalyzer() != null) {
			TestRetryAnalyzer testRetryAnalyzer = (TestRetryAnalyzer) arg0.getMethod().getRetryAnalyzer();
			
			
			System.out.println(arg0.getMethod()+" Passed in "+testRetryAnalyzer.getCount()+" times");
			
			isRetryHandleNeeded = true;
		}*/
	}

	/**
	 * Remote failed test cases in TestNG
	 * @param tc
	 * @return
	 */
	private void removeFailedTestsInTestNG(ITestContext tc)
	{     
	  IResultMap returnValue = tc.getFailedTests();
	 
	  ResultMap removeMap = new ResultMap();
	  for(ITestResult result : returnValue.getAllResults())
	  {
	    boolean isFailed = false;	    
	    for(ITestResult resultToCheck : failedTests.get(tc.getName()).getAllResults())
	    {
	        if(result.getMethod().equals(resultToCheck.getMethod()) && result.getEndMillis()==resultToCheck.getEndMillis())
	        {
	        	//logger.info("Keep failed cases:"+result.getMethod().getMethodName());
	        	isFailed = true;
	            break;
	        }
	    }
	    if(!isFailed)
	    {
	    	//logger.info("Removed failed cases:"+result.getMethod().getMethodName());
	    	System.out.println("Removed failed cases:"+result.getMethod().getMethodName());
	    	//test.getFailedTests().getAllResults().remove(result);
	    	removeMap.addResult(result, result.getMethod());
	    	//test.getFailedTests().removeResult(result.getMethod());
	    }
	  }
	  for(ITestResult result : removeMap.getAllResults())
	  {
		  ITestResult removeResult = null;
		  for(ITestResult resultToCheck : returnValue.getAllResults())
		  {
			  if(result.getMethod().equals(resultToCheck.getMethod()) && result.getEndMillis()==resultToCheck.getEndMillis())
			  {
				  removeResult = resultToCheck;
				  break;
			  }
		  }
		  if(removeResult!=null) returnValue.getAllResults().remove(removeResult);
	  }
	}
	
	/**
	 * Remove retrying failed test cases from skipped test cases
	 * @param tc
	 * @param map
	 * @return
	 */
	private void removeIncorrectlySkippedTests(ITestContext tc, IResultMap map)
	{     
	  List<ITestNGMethod> failsToRemove = new ArrayList<ITestNGMethod>();
	  IResultMap returnValue = tc.getSkippedTests();

	  for(ITestResult result : returnValue.getAllResults())
	  { 
	    for(ITestResult resultToCheck : map.getAllResults())
	    {
	        if(resultToCheck.getMethod().equals(result.getMethod()))
	        {
	            failsToRemove.add(resultToCheck.getMethod());
	            break;
	        }
	    }  

	    for(ITestResult resultToCheck : tc.getPassedTests().getAllResults())
	    {
	        if(resultToCheck.getMethod().equals(result.getMethod()))
	        {
	            failsToRemove.add(resultToCheck.getMethod());
	            break;
	        }
	    }     
	  }
	  
	  for(ITestNGMethod method : failsToRemove)
	  {
	      returnValue.removeResult(method);
	  }  

	  skippedTests.put(tc.getName(), tc.getSkippedTests());
	  
	}
	

	public void setOutputDirectory(String outtimestamped) {
		this.outputDirectory = outtimestamped;
	}

	public void setReportId(String uuid) {
		this.uuid = uuid;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	/** Starts HTML stream */
	protected void startHtml(ITestContext ctx, PrintWriter out) {
		try {
			// BufferedWriter out = new BufferedWriter(new FileWriter(path));

			/* first, get and initialize an engine */
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty("resource.loader", "class");
			ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			ve.init();

			Template t = ve.getTemplate("/templates/report.part.header.html");
			VelocityContext context = new VelocityContext();

			String userName = System.getProperty("user.name");
			context.put("userName", userName);
			context.put("currentDate", new Date().toString());
			// context.put("runId", (String)
			// ContextManager.getGlobalContext().getAttribute(Context.RUN_ID));
			// context.put("pool", poolInfo);
//			context.put("pool", (String) ContextManager.getGlobalContext().getPool());
			// context.put("apipool", (String)
			// ContextManager.getGlobalContext().getAttribute(Context.API_POOL));
			// context.put("buildTag", build);
			String mode = ContextManager.getGlobalContext().getWebRunMode();
			String hubUrl = ContextManager.getGlobalContext().getWebDriverGrid();
			//context.put("gridHub", "<a href='" + hubUrl + "' target=hub>" + (null == hubUrl? null : new URL(hubUrl).getHost()) + "</a>");
			context.put("gridHub", "<a href='" + hubUrl + "' target=hub>" + hubUrl + "</a>");
			
			context.put("mode",mode);

			StringBuffer sbGroups = new StringBuffer();
			sbGroups.append("envt,test,cal");
			List<AbstractPageListener> pageListenerList = PluginsUtil.getInstance().getPageListeners();
			if (pageListenerList != null && !pageListenerList.isEmpty()) {
				for (AbstractPageListener abstractPageListener : pageListenerList) {
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
	
	protected void takeCareOfDirectDependencies(ISuite suite, ITestNGMethod method, int indent, ITestContext ctx, String treeId, StringBuffer res) {

		if (indent == 0) {
			res.append("<a href=\"#\" onclick=\"expandTree('" + treeId + "'); return false;\">Expand All</a>&nbsp;");
			res.append("<a href=\"#\" onclick=\"collapseTree('" + treeId + "'); return false;\">Collapse All</a>");
			res.append("<ul class=\"mktree\" id=\"" + treeId + "\">");
		}

		String[] methStr = method.getMethodsDependedUpon();
		if (methStr.length != 0) {
			// Set<ITestNGMethod> methSet = new LinkedHashSet<ITestNGMethod>();
			for (int i = 0; i < methStr.length; i++) {
				ITestNGMethod m = getTestNGMethod(ctx, methStr[i]);
				String intendstr = "";
				for (int j = 0; j < indent; j++) {
					intendstr += "\t";
				}
				String img = "<img src=\"";
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
				// Collection<ITestNGMethod> all = suite.getInvokedMethods();
				methodsByGroup = initMethodsByGroup();
				// System.out.println(all);

			}
			String dependentGroup = method.getGroupsDependedUpon()[i];

			Set<ITestNGMethod> methods = new LinkedHashSet<ITestNGMethod>();
			Collection<ITestNGMethod> c = suite.getMethodsByGroups().get(dependentGroup);

			if (c != null)
				methods.addAll(c);
			res.append("<li><u>Group " + dependentGroup + "</u>");
			res.append("<ul>");

			for (ITestNGMethod m : methods) {
				String intendstr = "";
				for (int j = 0; j < indent; j++) {
					intendstr += "\t";
				}
				String img = "<img src=\"";
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