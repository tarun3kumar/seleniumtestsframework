package com.seleniumtests.reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.seleniumtests.reporter.pluginmodel.*;
import org.apache.log4j.Logger;

import com.seleniumtests.controller.AbstractPageListener;
import com.seleniumtests.driver.web.element.IPage;
import com.seleniumtests.reporter.pluginmodel.SeleniumTestsPlugins;

public class PluginsUtil {
	private static final Logger logger = Logger.getLogger(PluginsUtil.class);
	private static Map<String, AbstractPageListener> pageListenerMap = Collections
			.synchronizedMap(new HashMap<String, AbstractPageListener>());

	private static final PluginsUtil _instance = new PluginsUtil();

	public static synchronized PluginsUtil getInstance() {
		return _instance;
	}

	private SeleniumTestsPlugins _seleniumTestsPlugins = null;

	public SeleniumTestsPlugins getSeleniumTestsPlugins() {
		return _seleniumTestsPlugins;
	}

	public List<AbstractPageListener> getPageListeners() {
		List<AbstractPageListener> tempPageListenerList = Collections
				.synchronizedList(new ArrayList<AbstractPageListener>());
		tempPageListenerList.addAll(pageListenerMap.values());

		return tempPageListenerList;
	}

	public void invokePageListeners(String testMethodSignature, IPage page,
			boolean isPageLoad) {

		if (_seleniumTestsPlugins == null)
			return;

		List<AbstractPageListener> pageListenerList = new ArrayList<AbstractPageListener>();

		for (Plugin plugin : _seleniumTestsPlugins.getPlugin()) {
			if (isPageListenerApplicable(plugin, testMethodSignature, page
					.getClass().getCanonicalName()))
				pageListenerList.add(pageListenerMap.get(plugin.getClassName()
						.trim()));
		}

		for (AbstractPageListener listener : pageListenerList) {
			try {
				if (isPageLoad)
					listener.onPageLoad(page);
				else
					listener.onPageUnload(page);
			} catch (Throwable e) {
				logger.error(e);
			}
		}
	}

	public boolean isPageListenerApplicable(Plugin plugin,
			String testMethodSignature, String pageClassName) {
		if (testMethodSignature == null)
			return true;// for null context sinario
		boolean testFound = false;
		for (Test test : plugin.getTest()) {
			if (testMethodSignature.matches(test.getClassName() + "\\.\\w.*")) {
				// Take care of test class level pages
				for (Page page : test.getPage()) {
					if (pageClassName.matches(page.getClassName())) {
						// return true;
						testFound = true;
						break;
					}
				}

				if (testFound) {
					// Now let's look at the method level pages
					for (Method method : test.getMethod()) {
						if (testMethodSignature.matches(test.getClassName()
								+ "\\." + method.getName() + ".*")) {
							for (Page page : method.getPage()) {
								if (pageClassName.matches(page.getClassName()))
									return true;
							}
							return false;
						}
					}
					return true;
				}
			}
		}

		return false;
	}

	public boolean isTestResultEffected(String pageListenerClassName) {
		AbstractPageListener listener = pageListenerMap
				.get(pageListenerClassName);
		if (listener != null)
			return listener.isTestResultEffected();
		return false;
	}

	public void loadPlugins(File path) {
		logger.info("Loading Selenium Tests Plugins from " + path + " ...");

		InputStream is = null;
		try {
			is = new FileInputStream(path);
			JAXBContext jc = JAXBContext
					.newInstance("com.seleniumtests.reporter.pluginmodel");
			Unmarshaller u = jc.createUnmarshaller();
			_seleniumTestsPlugins = (SeleniumTestsPlugins) u.unmarshal(is);

			for (Plugin plugin : _seleniumTestsPlugins.getPlugin()) {
				try {
					pageListenerMap
							.put(plugin.getClassName().trim(),
									(AbstractPageListener) Class.forName(
											plugin.getClassName().trim())
											.newInstance());
				} catch (Exception e) {
					logger.error("Unable to load Plugins.", e);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
