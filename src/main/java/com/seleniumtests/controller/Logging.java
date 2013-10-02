package com.seleniumtests.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.google.common.collect.Lists;
import com.google.gdata.util.common.html.HtmlToText;
import com.seleniumtests.driver.web.IScreenshotListener;
import com.seleniumtests.driver.web.ScreenShot;
import com.seleniumtests.helper.FileHelper;
import com.seleniumtests.helper.StringHelper;
import com.seleniumtests.helper.URLHelper;
import com.seleniumtests.reporter.PluginsUtil;
import com.seleniumtests.xmldog.Config;
import com.seleniumtests.xmldog.Differences;
import com.seleniumtests.xmldog.XMLDog;

/**
 * Provides log methods for Web, API, DB, Email and SSH testing
 * 
 */
public class Logging {

	private static Map<String, Map<String, Map<String, List<String>>>> pageListenerLogMap = Collections
			.synchronizedMap(new HashMap<String, Map<String, Map<String, List<String>>>>());

	private static final Set<IScreenshotListener> screenshotListeners = new LinkedHashSet<IScreenshotListener>();

	/**
	 * Ability to register IScreenshotListeners.
	 */
	public static void registerScreenshotListener(IScreenshotListener screenshotListener) {
		screenshotListeners.add(screenshotListener);
	}

	public static void logScreenshot(final ScreenShot screenshot) {
		for (final IScreenshotListener screenshotListener : screenshotListeners) {
			new Thread() {
				public void run() {
					try {
						screenshotListener.doScreenCapture(
								screenshot.getPageId(), screenshot.getRlogId(),
								screenshot.getTitle(), "png",
								screenshot.getFullImagePath());
					} catch (Exception e) {
						// catching all listener impl exceptions to keep
						// continue with tests execution.
						// who ever implementing listener they must handle the
						// excetpions properly.
						System.err
								.println("Error in ScreenshotListener implementation "
										+ screenshotListener.getClass().getName()
										+ ". " + e.getMessage());
						// stacktrace will help to pin point issue.
						e.printStackTrace();
					}
				}
			}.start();
		}
	}
	
	/**
	 * Log method
	 * 
	 * @param message
	 */
	public static void error(String message) {
		message = "<li><b><font color='#FF0000'>" + message
				+ "</font></b></li>";
		log(null, message, false, false);
	}

	public static Logger getLogger(Class<?> clz) {
		boolean rootIsConfigured = Logger.getRootLogger().getAllAppenders()
				.hasMoreElements();
		if (!rootIsConfigured) {
			BasicConfigurator.configure();
			Logger.getRootLogger().setLevel(Level.INFO);
			Appender appender = (Appender) Logger.getRootLogger()
					.getAllAppenders().nextElement();
			appender.setLayout(new PatternLayout(" %-5p %d [%t] %C{1}: %m%n"));
		}
		return Logger.getLogger(clz);
	}

	public static Map<String, Map<String, List<String>>> getPageListenerLog(
			String pageListenerClassName) {
		return pageListenerLogMap.get(pageListenerClassName);
	}

	public static List<String> getPageListenerLogByMethodInstance(
			ITestResult testResult) {

		for (Entry<String, Map<String, Map<String, List<String>>>> listenerEntry : pageListenerLogMap
				.entrySet()) {
			if (!PluginsUtil.getInstance().isTestResultEffected(
					listenerEntry.getKey()))
				continue;

			Map<String, Map<String, List<String>>> pageMap = listenerEntry
					.getValue();
			for (Entry<String, Map<String, List<String>>> pageEntry : pageMap
					.entrySet()) {
				Map<String, List<String>> errorMap = pageEntry.getValue();
				String methodInstance = StringHelper.constructMethodSignature(
						testResult.getMethod().getConstructorOrMethod()
								.getMethod(), testResult.getParameters());
				return errorMap.get(methodInstance);
			}
		}

		return null;
	}

	/**
	 * Log method
	 * 
	 * @param message
	 */
	public static void info(String message) {
		message = "<li><font color='#008000'>" + message + "</font></li>";
		log(null, message, false, false);
	}

	/**
	 * Log method
	 * 
	 * @param message
	 */
	public static void log(String message) {
		log(null, message, false, false);
	}

	/**
	 * Log
	 * 
	 * @param message
	 * @param logToStandardOutput
	 */
	public static void log(String message, boolean logToStandardOutput) {
		log(null, message, false, logToStandardOutput);
	}

	public static void log(String url, String message, boolean failed,
			boolean logToStandardOutput) {

		if (message == null)
			message = "";
		message = message.replaceAll("\\n", "<br/>");

		if (failed) {
			message = "<span style=\"font-weight:bold;color:#FF0066;\">"
					+ message + "</span>";
		}
		// Added for debugging
		// System.out.println(Thread.currentThread()+":"+message);

		Reporter.log(escape(message), logToStandardOutput);// fix
																			// for
																			// testng6.7
	}
	
	public static String escape(String message){
		return message.replaceAll("\\n", "<br/>").replaceAll("<", "@@lt@@")
		.replaceAll(">", "^^gt^^");
	}
	
	public static String unEscape(String message){
		message = message.replaceAll("<br/>", "\\n").replaceAll("@@lt@@", "<")
		.replaceAll("\\^\\^gt\\^\\^", ">");
		
		message = HtmlToText.htmlToPlainText(message);
		return message;
	}

	public static void logAPICall(String url, String message, boolean failed) {
		log(url, "<li>" + (failed ? "<b>FailedAPICall</b>: " : "APICall: ")
				+ message + "</li>", failed, false);
	}

	public static void logDBCall(String message, boolean failed) {
		log(null, "<li>" + (failed ? "<b>FailedDBCall</b>: " : "DBCall: ")
				+ message + "</li>", failed, false);
	}

	public static void logEmailStep(String message, boolean failed) {
		log(null, "<li>"
				+ (failed ? "<b>FailedEmailStep</b>: " : "EmailStep: ")
				+ message + "</li>", failed, false);
	}

	public static void logSSHCall(String message, boolean failed) {
		log(null, "<li>" + (failed ? "<b>FailedSSHStep</b>: " : "SSHStep: ")
				+ message + "</li>", failed, false);
	}

	public static void logWebOutput(String url, String message, boolean failed) {
		log(url, "Output: " + message + "<br/>", failed, false);
	}

	public static void logWebStep(String url, String message, boolean failed) {
		log(url, "<li>" + (failed ? "<b>FailedStep</b>: " : "Step: ") + message
				+ "</li>", failed, false);
	}
	
	public static String buildScreenshotLog(ScreenShot screenShot){
		StringBuffer sbMessage = new StringBuffer("");
		if(screenShot.getLocation()!=null)
			sbMessage.append("<a href='" + screenShot.getLocation() + "' target=url>location</a>");
		if(screenShot.getHtmlSourcePath()!=null)
			sbMessage.append(" | <a href='" + screenShot.getHtmlSourcePath() + "' target=html>html</a>");
		if(screenShot.getImagePath()!=null)
			sbMessage.append(" | <a href='" + screenShot.getImagePath()+"' class='lightbox'>screenshot</a>");
		if(screenShot.getCalLog()!=null)
			sbMessage.append(" | <a href='"+screenShot.getCalLog() + "' target=cal>RlogId</a>");	
		if(screenShot.getPageId()!=null)sbMessage.append(" | PageId="+screenShot.getPageId());
		return sbMessage.toString();
	}

	/**
	 * Log method
	 * 
	 * @param message
	 */
	public static void warning(String message) {
		message = "<li><font color='#FFFF00'>" + message + "</font></li>";
		log(null, message, false, false);
	}
	
	public static ArrayList<String> getRawLog(ITestResult result){
		ArrayList<String> messages = Lists.newArrayList();
		for(String line: Reporter.getOutput(result)){
			line = unEscape(line);
			messages.add(line);
		}
		return messages;
	}
	
	private static String getXMLTitle(String xmlString) {
		String xmlTitle="";

	       String startPattern = "<S:Body>";
	       String endPattern = "xmlns=";
	       xmlTitle = xmlString;

	       int startIndex = xmlString.indexOf(startPattern) + 13;
	       int endIndex = xmlString.indexOf(endPattern, startIndex);
	       
	       xmlTitle = xmlTitle.substring(startIndex, endIndex);
	       xmlTitle = xmlTitle.replace('<', ' ');
	       xmlTitle = xmlTitle.replace('>', ' ');
	       xmlTitle= xmlTitle.trim();

	       System.out.println("XML title : " + xmlTitle);
	    return xmlTitle;
		
	}
	

	public static Differences compareGoldenFile(String goldenFile,String responseXML,String outputDir,String outputfilename, StringBuffer sbMessage, Config xmlCompareConfig)
	{
		if (goldenFile != null && !goldenFile.isEmpty()) {
			
		// Save API response to gold fold
			goldenFile = goldenFile.replaceAll("\\\\", "/");

			if (!new File(goldenFile).getParentFile().exists()) {
				new File(goldenFile).getParentFile().mkdirs();
			}

			if (!new File(goldenFile).exists()) {
				try {
					FileHelper.writeToFile(goldenFile, responseXML);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			String xmlOutputFile = outputDir + "/xmls/" + outputfilename + ".xml";
			xmlOutputFile = xmlOutputFile.replaceAll("\\\\", "/");

			sbMessage.append(" | <a href='file:///" + goldenFile + "' target=golden>golden</a>");
			sbMessage.append(" | <a href=\"javascript:copyFile('" + xmlOutputFile + "','" + goldenFile + "');\">save as golden</a>");

			Differences diff = null;
			XMLDog dog = null;
			try {
				if (xmlCompareConfig != null) {
//					xmlCompareConfig.setCustomDifference(false);
//					xmlCompareConfig.setApplyEListToSiblings(true);
//					xmlCompareConfig.setIgnoringOrder(false);
					dog = new XMLDog(xmlCompareConfig);
				} else {
					dog = new XMLDog();
				}
				diff = dog.compare(goldenFile, xmlOutputFile);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			sbMessage.append(" | <a href=\"javascript:toggle('" + outputfilename + "');\" class='xmldifflnk'>xml diff (" + ((diff != null) ? diff.getDiffCount() : "UNKNOWN") + ") [+]</a>");
			sbMessage.append(" <div id='" + outputfilename + "' class='xmldiff'> " + (diff == null ? "" : diff.getHTML()) + "</div>");
			return diff;
			
		}
		else
			return null;
	}
		
	
}