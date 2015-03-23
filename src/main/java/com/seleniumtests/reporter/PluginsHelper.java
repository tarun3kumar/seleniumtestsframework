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

import org.apache.log4j.Logger;

import com.seleniumtests.core.SeleniumTestsPageListener;

import com.seleniumtests.reporter.pluginmodel.Method;
import com.seleniumtests.reporter.pluginmodel.Page;
import com.seleniumtests.reporter.pluginmodel.Plugin;
import com.seleniumtests.reporter.pluginmodel.SeleniumTestsPlugins;
import com.seleniumtests.reporter.pluginmodel.Test;

import com.seleniumtests.webelements.IPage;

public class PluginsHelper {
    private static final Logger logger = Logger.getLogger(PluginsHelper.class);
    private static Map<String, SeleniumTestsPageListener> pageListenerMap = Collections.synchronizedMap(
            new HashMap<String, SeleniumTestsPageListener>());

    private static final PluginsHelper instance = new PluginsHelper();

    public static synchronized PluginsHelper getInstance() {
        return instance;
    }

    private SeleniumTestsPlugins _seleniumTestsPlugins = null;

    public List<SeleniumTestsPageListener> getPageListeners() {
        List<SeleniumTestsPageListener> tempPageListenerList = Collections.synchronizedList(
                new ArrayList<SeleniumTestsPageListener>());
        tempPageListenerList.addAll(pageListenerMap.values());

        return tempPageListenerList;
    }

    public void invokePageListeners(final String testMethodSignature, final IPage page, final boolean isPageLoad) {

        if (_seleniumTestsPlugins == null) {
            return;
        }

        List<SeleniumTestsPageListener> pageListenerList = new ArrayList<SeleniumTestsPageListener>();

        for (Plugin plugin : _seleniumTestsPlugins.getPlugin()) {
            if (isPageListenerApplicable(plugin, testMethodSignature, page.getClass().getCanonicalName())) {
                pageListenerList.add(pageListenerMap.get(plugin.getClassName().trim()));
            }
        }

        for (SeleniumTestsPageListener listener : pageListenerList) {
            try {
                if (isPageLoad) {
                    listener.onPageLoad(page);
                } else {
                    listener.onPageUnload(page);
                }
            } catch (Throwable e) {
                logger.error(e);
            }
        }
    }

    public boolean isPageListenerApplicable(final Plugin plugin, final String testMethodSignature,
            final String pageClassName) {
        if (testMethodSignature == null) {
            return true;
        }

        boolean testFound = false;
        for (Test test : plugin.getTest()) {
            if (testMethodSignature.matches(test.getClassName() + "\\.\\w.*")) {
                for (Page page : test.getPage()) {
                    if (pageClassName.matches(page.getClassName())) {
                        testFound = true;
                        break;
                    }
                }

                if (testFound) {
                    for (Method method : test.getMethod()) {
                        if (testMethodSignature.matches(test.getClassName() + "\\." + method.getName() + ".*")) {
                            for (Page page : method.getPage()) {
                                if (pageClassName.matches(page.getClassName())) {
                                    return true;
                                }
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

    public boolean isTestResultEffected(final String pageListenerClassName) {
        SeleniumTestsPageListener listener = pageListenerMap.get(pageListenerClassName);
        if (listener != null) {
            return listener.isTestResultEffected();
        }

        return false;
    }

    public void loadPlugins(final File path) {
        logger.info("Loading Selenium Tests Plugins from path: " + path + " ...");

        InputStream is = null;
        try {
            is = new FileInputStream(path);

            JAXBContext jc = JAXBContext.newInstance("com.seleniumtests.reporter.pluginmodel");
            Unmarshaller u = jc.createUnmarshaller();
            _seleniumTestsPlugins = (SeleniumTestsPlugins) u.unmarshal(is);

            for (Plugin plugin : _seleniumTestsPlugins.getPlugin()) {
                try {
                    pageListenerMap.put(plugin.getClassName().trim(),
                        (SeleniumTestsPageListener) Class.forName(plugin.getClassName().trim()).newInstance());
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
                    e.printStackTrace();
                }
            }
        }
    }
}
