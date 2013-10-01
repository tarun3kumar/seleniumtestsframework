package com.seleniumtests.controller;

import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.seleniumtests.helper.ClassContextHelper;

/**
 * Provides Keyword mechanism for Intl Localization
 * 
 */
@SuppressWarnings("rawtypes")
public class Keyword {

	private static Map<String, KeywordProvider> s_resourceBaseNameProviderMap = new Hashtable<String, KeywordProvider>();

	static {
		s_resourceBaseNameProviderMap
				.put(KeywordProvider.PropertiesKeywordProvider.DEFAULT_RESOURCE_BASE_NAME, new KeywordProvider.PropertiesKeywordProvider());
	}

	private static KeywordProvider _getProviderMapByResourceBaseName(String resourceBaseName) {
		KeywordProvider provider = s_resourceBaseNameProviderMap.get(resourceBaseName.toLowerCase());
		if (null == provider) {
			provider = new KeywordProvider.PropertiesKeywordProvider(resourceBaseName);
			s_resourceBaseNameProviderMap.put(resourceBaseName.toLowerCase(), provider);
		}
		return provider;
	}

	private static String _getValue(String input, Object[] values) {
		if (null == values)
			return input;
		Matcher matcher = Pattern.compile("\\{\\d+\\}").matcher(input);
		String swatch = input;
		String groupValue = null;
		Object tempValue = null;
		while (matcher.find()) {
			groupValue = matcher.group();
			int index = Integer.parseInt(groupValue.replaceAll("[{}]", ""));
			if (index >= 0 && index < values.length) {
				tempValue = values[index];
				swatch = swatch.replace(groupValue, (null == tempValue) ? "?NULL?" : tempValue.toString());
			}
		}
		return swatch;
	}

	public static String get(Class<?> callingClass, String key) {
		KeywordProvider provider = s_resourceBaseNameProviderMap.get(KeywordProvider.PropertiesKeywordProvider.DEFAULT_RESOURCE_BASE_NAME);
		return provider.getKeyword(callingClass, key);
	}

	public static String get(Class<?> callingClass, String key, Object[] values) {
		KeywordProvider provider = s_resourceBaseNameProviderMap.get(KeywordProvider.PropertiesKeywordProvider.DEFAULT_RESOURCE_BASE_NAME);
		String originalValue = provider.getKeyword(callingClass, key);
		return _getValue(originalValue, values);
	}

	public static String get(Class<?> callingClass, String key, String site) {
		KeywordProvider provider = s_resourceBaseNameProviderMap.get(KeywordProvider.PropertiesKeywordProvider.DEFAULT_RESOURCE_BASE_NAME);
		return provider.getKeyword(site, callingClass, key);
	}

	public static String get(Class<?> callingClass, String key, String site, Object[] values) {
		KeywordProvider provider = s_resourceBaseNameProviderMap.get(KeywordProvider.PropertiesKeywordProvider.DEFAULT_RESOURCE_BASE_NAME);
		String originalValue = provider.getKeyword(site, callingClass, key);
		return _getValue(originalValue, values);
	}

	public static String get(String key) {
		KeywordProvider provider = s_resourceBaseNameProviderMap.get(KeywordProvider.PropertiesKeywordProvider.DEFAULT_RESOURCE_BASE_NAME);
		Class callingClass = ClassContextHelper.getCallerEncloser();
		return provider.getKeyword(callingClass, key);
	}

	public static String get(String key, Object[] values) {
		KeywordProvider provider = s_resourceBaseNameProviderMap.get(KeywordProvider.PropertiesKeywordProvider.DEFAULT_RESOURCE_BASE_NAME);
		Class callingClass = ClassContextHelper.getCallerEncloser();
		String originalValue = provider.getKeyword(callingClass, key);
		return _getValue(originalValue, values);
	}

	public static String get(String key, String site) {
		KeywordProvider provider = s_resourceBaseNameProviderMap.get(KeywordProvider.PropertiesKeywordProvider.DEFAULT_RESOURCE_BASE_NAME);
		Class callingClass = ClassContextHelper.getCallerEncloser();
		return provider.getKeyword(site, callingClass, key);
	}

	public static String get(String key, String site, Object[] values) {
		KeywordProvider provider = s_resourceBaseNameProviderMap.get(KeywordProvider.PropertiesKeywordProvider.DEFAULT_RESOURCE_BASE_NAME);
		Class callingClass = ClassContextHelper.getCallerEncloser();
		String originalValue = provider.getKeyword(site, callingClass, key);
		return _getValue(originalValue, values);
	}

	public static String getByResourceBaseName(Class<?> callingClass, String resourceBaseName, String key) {
		KeywordProvider provider = _getProviderMapByResourceBaseName(resourceBaseName);
		return provider.getKeyword(callingClass, key);
	}

	public static String getByResourceBaseName(Class<?> callingClass, String resourceBaseName, String key, Object[] values) {
		KeywordProvider provider = _getProviderMapByResourceBaseName(resourceBaseName);
		String originalValue = provider.getKeyword(callingClass, key);
		return _getValue(originalValue, values);
	}

	public static String getByResourceBaseName(Class<?> callingClass, String resourceBaseName, String key, String site) {
		KeywordProvider provider = _getProviderMapByResourceBaseName(resourceBaseName);
		return provider.getKeyword(site, callingClass, key);
	}

	public static String getByResourceBaseName(Class<?> callingClass, String resourceBaseName, String key, String site, Object[] values) {
		KeywordProvider provider = _getProviderMapByResourceBaseName(resourceBaseName);
		String originalValue = provider.getKeyword(site, callingClass, key);
		return _getValue(originalValue, values);
	}

	public static String getByResourceBaseName(String resourceBaseName, String key) {
		KeywordProvider provider = _getProviderMapByResourceBaseName(resourceBaseName);
		Class callingClass = ClassContextHelper.getCallerEncloser();
		return provider.getKeyword(callingClass, key);
	}

	public static String getByResourceBaseName(String resourceBaseName, String key, Object[] values) {
		KeywordProvider provider = _getProviderMapByResourceBaseName(resourceBaseName);
		Class callingClass = ClassContextHelper.getCallerEncloser();
		String originalValue = provider.getKeyword(callingClass, key);
		return _getValue(originalValue, values);
	}

	public static String getByResourceBaseName(String resourceBaseName, String key, String site) {
		KeywordProvider provider = _getProviderMapByResourceBaseName(resourceBaseName);
		Class callingClass = ClassContextHelper.getCallerEncloser();
		return provider.getKeyword(site, callingClass, key);
	}

	public static String getByResourceBaseName(String resourceBaseName, String key, String site, Object[] values) {
		KeywordProvider provider = _getProviderMapByResourceBaseName(resourceBaseName);
		Class callingClass = ClassContextHelper.getCallerEncloser();
		String originalValue = provider.getKeyword(site, callingClass, key);
		return _getValue(originalValue, values);
	}

}
