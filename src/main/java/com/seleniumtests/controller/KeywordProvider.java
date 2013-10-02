package com.seleniumtests.controller;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.log4j.Logger;

@SuppressWarnings("rawtypes")
public interface KeywordProvider {

	public final class PropertiesKeywordProvider implements KeywordProvider {
		private static Set<String> s_loadedKeywordsPackageSet = new HashSet<String>();
		private static Map<String, String> s_keywordsMap = new HashMap<String, String>();
		private static final String DEFAULT_KEYWORD_KEY_PREFIX = "default";
		private String m_resourceBaseName = null;

		public static final String DEFAULT_RESOURCE_BASE_NAME = "keywords";

		public PropertiesKeywordProvider() {
			this.m_resourceBaseName = DEFAULT_RESOURCE_BASE_NAME;
		}

		public PropertiesKeywordProvider(String resourceBaseName) {
			this.m_resourceBaseName = (null == resourceBaseName || "".equals(resourceBaseName)) ? DEFAULT_RESOURCE_BASE_NAME : resourceBaseName;
		}

		/**
		 * Load keywords for a domain flow.
		 * 
		 * @throws Exception
		 */
		private void _loadKeywords(String site, Class stepClass) {
			synchronized (PropertiesKeywordProvider.class) {
				// to load default properties
				String packagePath = stepClass.getPackage().getName();

				if (!s_loadedKeywordsPackageSet.contains(m_resourceBaseName + "." + DEFAULT_KEYWORD_KEY_PREFIX + "." + packagePath + ".keywords")) {
					ResourceBundle defaultBundle = _pickResourceBundle(stepClass);
					if (null == defaultBundle)
						logger.warn("WARN: resource \"" + packagePath + "." + m_resourceBaseName + ".properties\" not found.");
					else {
						Enumeration<String> keys = defaultBundle.getKeys();
						String identifier = null;
						String value = null;
						while (keys.hasMoreElements()) {
							identifier = keys.nextElement();
							value = defaultBundle.getString(identifier);

							if (!identifier.startsWith(packagePath))
								identifier = packagePath + "." + identifier;

							s_keywordsMap.put(m_resourceBaseName + "." + DEFAULT_KEYWORD_KEY_PREFIX + "." + identifier, value);
						}
					}
					s_loadedKeywordsPackageSet.add(m_resourceBaseName + "." + DEFAULT_KEYWORD_KEY_PREFIX + "." + packagePath + ".keywords");
				}

				// to load the particular site properties
				ResourceBundle localizationalBundle = _pickResourceBundle(stepClass, site);
				if (null == localizationalBundle)
					logger.warn("WARN: resource \"" + packagePath + "." + m_resourceBaseName + "_" + site.toLowerCase() + ".properties\" not found.");
				else {
					Enumeration<String> keys = localizationalBundle.getKeys();
					String identifier = null;
					String value = null;
					while (keys.hasMoreElements()) {
						identifier = keys.nextElement();
						value = localizationalBundle.getString(identifier);
//						if (site.equalsIgnoreCase("RU")||site.equalsIgnoreCase("HK")) {
							try {
								value = new String(value.getBytes("ISO8859-1"), "UTF-8");
							} catch (UnsupportedEncodingException e) {
							}
//						}

						if (!identifier.startsWith(packagePath))
							identifier = packagePath + "." + identifier;

						s_keywordsMap.put(m_resourceBaseName + "." + site + "." + identifier, value);
					}
				}
				s_loadedKeywordsPackageSet.add(m_resourceBaseName + "." + site + "." + packagePath + ".keywords");
			}
		}

		private ResourceBundle _pickResourceBundle(Class clz) {
			String packagePath = clz.getPackage().getName();
			try {
				return ResourceBundle.getBundle(packagePath + "." + m_resourceBaseName, new Locale("", "", ""));
			} catch (MissingResourceException e) {
				return null;
			}
		}

		private ResourceBundle _pickResourceBundle(Class clz, String site) {
			String packagePath = clz.getPackage().getName();
			try {
				return ResourceBundle.getBundle(packagePath + "." + m_resourceBaseName + ((null == site || "".equals(site)) ? "" : "_" + site.toLowerCase()));
			} catch (MissingResourceException e) {
				return null;
			}
		}

		private String getLocalizedSite() {
			String site = null;
				site = ContextManager.getThreadContext().getSite();
			return site;
		}

		public String getKeyword(Class stepClass, String identifier) {
			return getKeyword(getLocalizedSite(), stepClass, identifier);
		}

		public String getKeyword(String site, Class stepClass, String identifier) {
			if (!s_loadedKeywordsPackageSet.contains(m_resourceBaseName + "." + site + "." + stepClass.getPackage().getName() + ".keywords")) {
				_loadKeywords(site, stepClass);
			}

			if (s_keywordsMap.containsKey(m_resourceBaseName + "." + DEFAULT_KEYWORD_KEY_PREFIX + "." + stepClass.getCanonicalName() + "." + identifier))
				return s_keywordsMap.get(m_resourceBaseName + "." + DEFAULT_KEYWORD_KEY_PREFIX + "." + stepClass.getCanonicalName() + "." + identifier);

			return null;
		}

		public String getResourceBaseName() {
			return m_resourceBaseName;
		}

	}

	public Logger logger = Logging.getLogger(KeywordProvider.class);

	public String getKeyword(Class stepClass, String identifier);

	public String getKeyword(String site, Class<?> stepClass, String identifier);

}
