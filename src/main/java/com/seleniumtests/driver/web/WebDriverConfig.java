package com.seleniumtests.driver.web;

import java.net.URISyntaxException;
import java.util.ArrayList;

import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class WebDriverConfig {

	private boolean setAssumeUntrustedCertificateIssuer = true;
	private boolean setAcceptUntrustedCertificates = true;
	private boolean enableJavascript = true;

	private WebDriver driver;
	private BrowserType browser = BrowserType.FireFox;
	private WebDriverMode mode = WebDriverMode.LOCAL;
	private String hubUrl;
	private String ffProfilePath;
	private String operaProfilePath;
	private String ffBinPath;
	private String ieDriverPath;
	private String chromeDriverPath;
	private String chromeBinPath;
	private int webSessionTimeout = 90 * 1000;
	final static public int DEFAULT_IMPLICIT_WAIT_TIMEOUT = 5;
	final static public int DEFAULT_EXPLICIT_WAIT_TIME_OUT = 15;
	final static public int DEFAULT_PAGE_LOAD_TIMEOUT = 90;
	private double implicitWaitTimeout = DEFAULT_IMPLICIT_WAIT_TIMEOUT;
	private int explicitWaitTimeout = DEFAULT_EXPLICIT_WAIT_TIME_OUT;
	private int pageLoadTimeout = DEFAULT_PAGE_LOAD_TIMEOUT;
	private String outputDirectory;
	private String browserVersion;
	private Platform platform;
	private String userAgentOverride;
	private String ntlmAuthTrustedUris;
	private String browserDownloadDir;
	private boolean addJSErrorCollectorExtension = false;
	private ArrayList<WebDriverEventListener> webDriverListeners;
	private boolean useFirefoxDefaultProfile = true;
	private int browserWindowWidth = -1;
	private int browserWindowHeight = -1;

	private String proxyHost;
	private String appName;
	private String appVersion;

	public ArrayList<WebDriverEventListener> getWebDriverListeners() {
		return webDriverListeners;
	}

	public void setWebDriverListeners(
			ArrayList<WebDriverEventListener> webDriverListeners) {
		this.webDriverListeners = webDriverListeners;
	}

	public void setWebDriverListeners(String listeners) {
		ArrayList<WebDriverEventListener> listenerList = new ArrayList<WebDriverEventListener>();
		String[] list = listeners.split(",");
		for (int i = 0; i < list.length; i++) {

			WebDriverEventListener listener = null;
			try {
				if (list[i] != "") {
					listener = (WebDriverEventListener) (Class.forName(list[i]))
							.newInstance();
					listenerList.add(listener);
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
		this.webDriverListeners = listenerList;
	}

	public BrowserType getBrowser() {
		return browser;
	}

	public String getBrowserDownloadDir() {
		return browserDownloadDir;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public String getChromeBinPath() {
		return chromeBinPath;
	}

	public String getChromeDriverPath() {
		return chromeDriverPath;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public int getExplicitWaitTimeout() {
		if (explicitWaitTimeout < getImplicitWaitTimeout()) {
			return (int)getImplicitWaitTimeout();
		} else
			return explicitWaitTimeout;
	}

	public String getFfBinPath() {
		return ffBinPath;
	}

	public String getFfProfilePath() {
		if (ffProfilePath == null
				&& getClass().getResource("/profiles/customProfileDirCUSTFF") != null) {

			try {
				return getClass()
						.getResource("/profiles/customProfileDirCUSTFF")
						.toURI().getPath();
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		} else
			return ffProfilePath;
	}

	public String getHubUrl() {
		return hubUrl;
	}

	public String getIeDriverPath() {
		return ieDriverPath;
	}

	public double getImplicitWaitTimeout() {
		return implicitWaitTimeout;
	}

	public WebDriverMode getMode() {
		return mode;
	}

	public String getNtlmAuthTrustedUris() {
		return ntlmAuthTrustedUris;
	}

	public String getOperaProfilePath() {
		if (operaProfilePath == null
				&& getClass().getResource("/profiles/operaProfile") != null) {

			try {
				return getClass().getResource("/profiles/operaProfile").toURI()
						.getPath();
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}
		return operaProfilePath;
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public int getPageLoadTimeout() {
		return pageLoadTimeout;
	}

	public Platform getPlatform() {
		return platform;
	}

	public Proxy getProxy() {
		Proxy proxy = null;
		if (proxyHost != null) {
			proxy = new Proxy();
			proxy.setProxyType(ProxyType.MANUAL);
			proxy.setHttpProxy(proxyHost);
			proxy.setFtpProxy(proxyHost);
			proxy.setSslProxy(proxyHost);

		}
		return proxy;

	}

	public String getProxyHost() {
		return proxyHost;
	}

	public String getUserAgentOverride() {
		return this.userAgentOverride;
	}

	public int getWebSessionTimeout() {
		return webSessionTimeout;
	}

	public boolean isAddJSErrorCollectorExtension() {
		return addJSErrorCollectorExtension;
	}

	public boolean isUseFirefoxDefaultProfile() {
		return this.useFirefoxDefaultProfile;
	}

	public void setUseFirefoxDefaultProfile(boolean useFirefoxDefaultProfile) {
		this.useFirefoxDefaultProfile = useFirefoxDefaultProfile;
	}

	public boolean isEnableJavascript() {
		return enableJavascript;
	}

	public boolean isSetAcceptUntrustedCertificates() {
		return setAcceptUntrustedCertificates;
	}

	public boolean isSetAssumeUntrustedCertificateIssuer() {
		return setAssumeUntrustedCertificateIssuer;
	}

	public void setAddJSErrorCollectorExtension(
			boolean addJSErrorCollectorExtension) {
		this.addJSErrorCollectorExtension = addJSErrorCollectorExtension;
	}

	public void setBrowser(BrowserType browser) {
		this.browser = browser;
	}

	public void setBrowserDownloadDir(String browserDownloadDir) {
		this.browserDownloadDir = browserDownloadDir;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public void setChromeBinPath(String chromeBinPath) {
		this.chromeBinPath = chromeBinPath;
	}

	public void setChromeDriverPath(String chromeDriverPath) {
		this.chromeDriverPath = chromeDriverPath;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public void setEnableJavascript(boolean enableJavascript) {
		this.enableJavascript = enableJavascript;
	}

	public void setExplicitWaitTimeout(int explicitWaitTimeout) {
		this.explicitWaitTimeout = explicitWaitTimeout;
	}

	public void setFfBinPath(String ffBinPath) {
		this.ffBinPath = ffBinPath;
	}

	public void setFfProfilePath(String ffProfilePath) {
		this.ffProfilePath = ffProfilePath;
	}

	public void setHubUrl(String hubUrl) {
		this.hubUrl = hubUrl;
	}

	public void setIeDriverPath(String ieDriverPath) {
		this.ieDriverPath = ieDriverPath;
	}

	public void setImplicitWaitTimeout(double implicitWaitTimeout) {
		this.implicitWaitTimeout = implicitWaitTimeout;
	}

	public void setMode(WebDriverMode mode) {
		this.mode = mode;
	}

	public void setNtlmAuthTrustedUris(String ntlmAuthTrustedUris) {
		this.ntlmAuthTrustedUris = ntlmAuthTrustedUris;
	}

	public void setOperaProfilePath(String operaProfilePath) {
		this.operaProfilePath = operaProfilePath;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public void setPageLoadTimeout(int pageLoadTimeout) {
		this.pageLoadTimeout = pageLoadTimeout;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public void setSetAcceptUntrustedCertificates(
			boolean setAcceptUntrustedCertificates) {
		this.setAcceptUntrustedCertificates = setAcceptUntrustedCertificates;
	}

	public void setSetAssumeUntrustedCertificateIssuer(
			boolean setAssumeUntrustedCertificateIssuer) {
		this.setAssumeUntrustedCertificateIssuer = setAssumeUntrustedCertificateIssuer;
	}

	public void setUserAgentOverride(String userAgentOverride) {
		this.userAgentOverride = userAgentOverride;
	}

	public void setWebSessionTimeout(int webSessionTimeout) {
		this.webSessionTimeout = webSessionTimeout;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppName() {
		return appName;
	}
	
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public int getBrowserWindowWidth() {
		return browserWindowWidth;
	}

	public void setBrowserWindowWidth(int browserWindowWidth) {
		this.browserWindowWidth = browserWindowWidth;
	}

	public int getBrowserWindowHeight() {
		return browserWindowHeight;
	}

	public void setBrowserWindowHeight(int browserWindowHeight) {
		this.browserWindowHeight = browserWindowHeight;
	}

	
}
