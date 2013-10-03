package com.seleniumtests.driver.web;

import com.seleniumtests.controller.Context;
import com.seleniumtests.controller.ContextManager;
import com.seleniumtests.driver.web.factory.*;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.events.WebDriverEventListener;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class provides factory to create webDriver session
 * 
*/
public class WebUXDriver {

	private static ThreadLocal<WebDriver> driverSession = new ThreadLocal<WebDriver>();
	private static ThreadLocal<WebUXDriver> uxDriverSession = new ThreadLocal<WebUXDriver>();
	private String node;
	

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public static void cleanUp() {
		IWebDriverFactory b = getWebUXDriver().webDriverBuilder;
		if (b != null)
			b.cleanUp();
		else {
			WebDriver driver = driverSession.get();
			if (driver != null) {
				try {
					driver.quit();
				} catch (WebDriverException ex) {
					ex.printStackTrace();
				}
				driver = null;
			}
		}
		driverSession.remove();
		uxDriverSession.remove();
	}

	/**
	 * Get native WebDriver which can be converted to RemoteWebDriver
	 * 
	 * @return webDriver
	 */
	public static WebDriver getNativeWebDriver() {
		return ((CustomEventFiringWebDriver) getWebDriver(false))
				.getWebDriver();
	}

	/**
	 * Get EventFiringWebDriver
	 * 
	 * @return webDriver
	 */
	public static WebDriver getWebDriver() {
		return getWebDriver(false);
	}

	public static WebDriver getWebDriver(Boolean isCreate) {
		if (driverSession.get() == null && isCreate) {
			try {
				getWebUXDriver().createWebDriver();
			} catch (Exception e) {
				System.out.println("Capture exception when create web driver");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return driverSession.get();
	}

	public static WebUXDriver getWebUXDriver() {
		if (uxDriverSession.get() == null) {
			uxDriverSession.set(new WebUXDriver());
		}
		return uxDriverSession.get();
	}

	public static void setWebDriver(WebDriver driver) {
		if (driver == null)
			driverSession.remove();
		else {
			if (getWebUXDriver() == null)
				new WebUXDriver();
			driverSession.set(driver);
		}
	}

	private WebDriverConfig config = new WebDriverConfig();
	private WebDriver driver;
	private IWebDriverFactory webDriverBuilder;

	public WebUXDriver() {
		init();
		uxDriverSession.set(this);
	}

	public WebUXDriver(String browser, String mode) {
		init();
		this.setBrowser(browser);
		this.setMode(mode);
		uxDriverSession.set(this);
	}

	public WebDriver createRemoteWebDriver(String browser, String mode)
			throws Exception {
		WebDriver driver = null;
		config.setBrowser(BrowserType.getBrowserType(browser));
		config.setMode(WebDriverMode.valueOf(mode));

		if (config.getMode() == WebDriverMode.ExistingGrid) {
			webDriverBuilder = new RemoteDriverFactory(this.config);
		} else {
			if (config.getBrowser() == BrowserType.FireFox) {
				webDriverBuilder = new FirefoxDriverFactory(this.config);
			} else if (config.getBrowser() == BrowserType.InternetExplore) {
				webDriverBuilder = new IEDriverFactory(this.config);
			} else if (config.getBrowser() == BrowserType.Chrome) {
				webDriverBuilder = new ChromeDriverFactory(this.config);
			} else if (config.getBrowser() == BrowserType.HtmlUnit) {
				webDriverBuilder = new HtmlUnitDriverFactory(this.config);
			} else if (config.getBrowser() == BrowserType.Safari) {
				webDriverBuilder = new SafariDriverFactory(this.config);
			} else if (config.getBrowser() == BrowserType.Android) {
				webDriverBuilder = new AndroidDriverFactory(this.config);
			} else if (config.getBrowser() == BrowserType.IPhone) {
				//webDriverBuilder = new IPhoneDriverFactory(this.config);
				webDriverBuilder = (IWebDriverFactory) Class.forName("com.seleniumtests.driver.web.factory.IPhoneDriverFactory").getConstructor(WebDriverConfig.class).newInstance(this.config);
			} else if (config.getBrowser() == BrowserType.IPad) {
				//webDriverBuilder = new IPadDriverFactory(this.config);
				webDriverBuilder = (IWebDriverFactory) Class.forName("com.seleniumtests.driver.web.factory.IPadDriverFactory").getConstructor(WebDriverConfig.class).newInstance(this.config);
			} else if (config.getBrowser() == BrowserType.Opera)
				webDriverBuilder = new OperaDriverFactory(this.config);
			else {
				throw new RuntimeException("Unsupported browser" + browser);
			}
		}
		synchronized(this.getClass()){
			driver = webDriverBuilder.createWebDriver();
		}
		if (config.getBrowserWindowWidth() > 0 && config.getBrowserWindowHeight() > 0){
			new BaseWebUtil(driver).resizeWindow(config.getBrowserWindowWidth(), config.getBrowserWindowHeight());
		} else {
			new BaseWebUtil(driver).maximizeWindow();
		}
		driver = handleListeners(driver);

		return driver;
	}
	
	

	protected WebDriver handleListeners(WebDriver driver) {
		// driver = new EventFiringWebDriver(driver).register(new
		// WebDriverExceptionListener());
		// Support customized listeners
		ArrayList<WebDriverEventListener> listeners = config
				.getWebDriverListeners();
		if (listeners != null && listeners.size() > 0) {
			for (int i = 0; i < config.getWebDriverListeners().size(); i++) {
				driver = new CustomEventFiringWebDriver(driver)
						.register(listeners.get(i));
			}
		}
		return driver;
	}

	public WebDriver createWebDriver() throws Exception {
        System.out.println(Thread.currentThread() + ":" + new Date()
				+ "Start creating web driver instance: " +this.getBrowser());
		driver = createRemoteWebDriver(config.getBrowser().getType(), config
				.getMode().name());

		System.out.println(Thread.currentThread() + ":" + new Date()
				+ "Finish creating web driver instance: " +this.getBrowser());

		driverSession.set(driver);
		return driver;
	}

	public String getBrowser() {
		return config.getBrowser().getType();
	}

	public String getPlatform() {
		return config.getPlatform().name();
	}

	public String getBrowserVersion() {
		return config.getBrowserVersion();
	}

	public String getChromeBinPath() {
		return config.getChromeBinPath();
	}

	public String getChromeDriverPath() {
		return config.getChromeDriverPath();
	}

	public WebDriverConfig getConfig() {
		return config;
	}

	public int getExplicitWait() {
		return config.getExplicitWaitTimeout();
	}

	public String getFfBinPath() {
		return config.getFfBinPath();
	}

	public String getFfProfilePath() throws URISyntaxException {
		return config.getFfProfilePath();
	}

	public String getOperaProfilePath() throws URISyntaxException {
		return config.getOperaProfilePath();
	}

	public void setOperaProfilePath(String operaProfilePath) {
		config.setOperaProfilePath(operaProfilePath);
	}

	public String getHubUrl() {
		return config.getHubUrl();
	}

	public String getIEDriverPath() {
		return config.getIeDriverPath();
	}

	public double getImplicitWait() {
		return config.getImplicitWaitTimeout();
	}

	public String getMode() {
		return config.getMode().name();
	}

	public String getOutputDirectory() {
		return config.getOutputDirectory();
	}

	public String getNtlmAuthTrustedUris() {
		return config.getNtlmAuthTrustedUris();
	}

	public void setNtlmAuthTrustedUris(String url) {
		config.setNtlmAuthTrustedUris(url);
	}

	public int getPageLoadTimeout() {
		return config.getPageLoadTimeout();
	}

	public String getProxyHost() {
		return config.getProxyHost();
	}

	public void setUserAgentOverride(String userAgentOverride) {
		config.setUserAgentOverride(userAgentOverride);
	}

	public String getUserAgentOverride() {
		return config.getUserAgentOverride();
	}

	public IWebDriverFactory getWebDriverBuilder() {
		return webDriverBuilder;
	}

	public int getWebSessionTimeout() {
		return config.getWebSessionTimeout();
	}

	private void init() {
		if (ContextManager.getThreadContext() == null)
			return;

		String browser = ContextManager.getThreadContext().getWebRunBrowser();
		config.setBrowser(BrowserType.getBrowserType(browser));
		String mode = ContextManager.getThreadContext().getWebRunMode();
		config.setMode(WebDriverMode.valueOf(mode));
		String hubUrl = ContextManager.getThreadContext().getWebDriverGrid();
		config.setHubUrl(hubUrl);
		String ffProfilePath = ContextManager.getThreadContext()
				.getFirefoxUserProfilePath();
		config.setFfProfilePath(ffProfilePath);
		String operaProfilePath = ContextManager.getThreadContext()
				.getOperaUserProfilePath();
		config.setOperaProfilePath(operaProfilePath);
		String ffBinPath = ContextManager.getThreadContext()
				.getFirefoxBinPath();
		config.setFfBinPath(ffBinPath);
		String chromeBinPath = ContextManager.getThreadContext()
				.getChromeBinPath();
		config.setChromeBinPath(chromeBinPath);
		String chromeDriverPath = ContextManager.getThreadContext()
				.getChromeDriverPath();
		config.setChromeDriverPath(chromeDriverPath);
		String ieDriverPath = ContextManager.getThreadContext()
				.getIEDriverPath();
		config.setIeDriverPath(ieDriverPath);
		int webSessionTimeout = ContextManager.getThreadContext()
				.getWebSessionTimeout();
		config.setWebSessionTimeout(webSessionTimeout);
		double implicitWaitTimeout = ContextManager.getThreadContext()
				.getImplicitWaitTimeout();
		config.setImplicitWaitTimeout(implicitWaitTimeout);
		int explicitWaitTimeout = ContextManager.getThreadContext()
				.getExplicitWaitTimeout();
		config.setExplicitWaitTimeout(explicitWaitTimeout);
		config.setPageLoadTimeout(ContextManager.getThreadContext()
				.getPageLoadTimeout());

		String outputDirectory = ContextManager.getGlobalContext()
				.getOutputDirectory();
		config.setOutputDirectory(outputDirectory);

		if (ContextManager.getThreadContext().isWebProxyEnabled()) {
			String proxyHost = ContextManager.getThreadContext()
					.getWebProxyAddress();
			config.setProxyHost(proxyHost);
		}
		String browserVersion = ContextManager.getThreadContext()
				.getWebBrowserVersion();
		config.setBrowserVersion(browserVersion);
		String platform = ContextManager.getThreadContext().getPlatform();
		if (platform != null)
			config.setPlatform(Platform.valueOf(platform));
		if ("false".equalsIgnoreCase((String) ContextManager.getThreadContext()
				.getAttribute(Context.Set_Assume_Untrusted_Certificate_Issuer))) {
			config.setSetAssumeUntrustedCertificateIssuer(false);
		}
		if ("false".equalsIgnoreCase((String) ContextManager.getThreadContext()
				.getAttribute(Context.Set_Accept_Untrusted_Certificates))) {
			config.setSetAcceptUntrustedCertificates(false);
		}
		if ("false".equalsIgnoreCase((String) ContextManager.getThreadContext()
				.getAttribute(Context.ENABLE_JAVASCRIPT))) {
			config.setEnableJavascript(false);
		}
		if (ContextManager.getThreadContext().getNtlmAuthTrustedUris() != null)
			config.setNtlmAuthTrustedUris(ContextManager.getThreadContext()
					.getNtlmAuthTrustedUris());
		if (ContextManager.getThreadContext().getBrowserDownloadDir() != null)
			config.setBrowserDownloadDir(ContextManager.getThreadContext()
					.getBrowserDownloadDir());
		if (ContextManager.getThreadContext().getAddJSErrorCollectorExtension() != null)
			config.setAddJSErrorCollectorExtension(Boolean
					.parseBoolean(ContextManager.getThreadContext()
							.getAddJSErrorCollectorExtension()));
		String ua = null;
		if (ContextManager.getThreadContext().getPCSettings() != null) {
			if (ContextManager.getThreadContext().getUserAgent() != null) {
				ua = ContextManager.getThreadContext().getPCSettings() + " "
						+ ContextManager.getThreadContext().getUserAgent();
			} else {
				ua = ContextManager.getThreadContext().getPCSettings()
						+ " Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101 Firefox/16.0";
			}
		} else {
			if (ContextManager.getThreadContext().getUserAgent() != null) {
				ua = ContextManager.getThreadContext().getUserAgent();
			} else {
				ua = null;
			}
		}
		config.setUserAgentOverride(ua);
		String listeners = ContextManager.getThreadContext()
				.getWebDriverListener();
		if (ContextManager.getThreadContext().getEnableExceptionListener()) {
			if (listeners != null) {
				listeners = listeners + ",";
			} else
				listeners = "";
			listeners = listeners + WebDriverExceptionListener.class.getName();
		}
		if (listeners != null && listeners != "")
			config.setWebDriverListeners(listeners);
		else
			config.setWebDriverListeners("");
		config.setUseFirefoxDefaultProfile(ContextManager.getThreadContext()
				.isUseFirefoxDefaultProfile());
		config.setAppName(ContextManager.getThreadContext().getAppName());
		config.setAppVersion(ContextManager.getThreadContext().getAppVersion());
		String size = ContextManager.getThreadContext().getBrowserWindowSize();
		if (size != null) {
			int width = -1;
			int height = -1;
			try {
				width = Integer.parseInt(size.split(",")[0].trim());
				height = Integer.parseInt(size.split(",")[1].trim());
			} catch (Exception ex) {
			}
			config.setBrowserWindowWidth(width);
			config.setBrowserWindowHeight(height);
		}
	}

	public static void main(String[] args) {
		System.out.println(WebDriverExceptionListener.class.getName());
	}

	public boolean isSetAcceptUntrustedCertificates() {
		return config.isSetAcceptUntrustedCertificates();
	}

	public boolean isAddJSErrorCollectorExtension() {
		return config.isAddJSErrorCollectorExtension();
	}

	public void setAddJSErrorCollectorExtension(
			Boolean addJSErrorCollectorExtension) {
		config.setAddJSErrorCollectorExtension(addJSErrorCollectorExtension);
	}

	public boolean isSetAssumeUntrustedCertificateIssuer() {
		return config.isSetAssumeUntrustedCertificateIssuer();
	}

	public boolean isEnableJavascript() {
		return config.isEnableJavascript();
	}

	public void setEnableJavascript(Boolean enableJavascript) {
		config.setEnableJavascript(enableJavascript);
	}

	public void setBrowser(String browser) {
		config.setBrowser(BrowserType.getBrowserType(browser));

	}

	public void setBrowserVersion(String browserVersion) {
		config.setBrowserVersion(browserVersion);
	}

	public void setPlatform(String platform) {
		config.setPlatform(Platform.valueOf(platform));
	}

	public void setChromeBinPath(String chromeBinPath) {
		config.setChromeBinPath(chromeBinPath);
	}

	public void setBrowserDownloadDir(String browserDownloadDir) {
		config.setBrowserDownloadDir(browserDownloadDir);
	}

	public String getBrowserDownloadDir() {
		return config.getBrowserDownloadDir();
	}

	public void setChromeDriverPath(String chromeDriverPath) {
		config.setChromeDriverPath(chromeDriverPath);
	}

	public void setConfig(WebDriverConfig config) {
		this.config = config;
	}

	public void setExplicitTimeout(int explicitWaitTimeout) {
		config.setExplicitWaitTimeout(explicitWaitTimeout);
	}

	public void setFfBinPath(String ffBinPath) {
		config.setFfBinPath(ffBinPath);
	}

	public void setFfProfilePath(String ffProfilePath) {
		config.setFfProfilePath(ffProfilePath);
	}

	public void setHubUrl(String hubUrl) {
		config.setHubUrl(hubUrl);
	}

	public void setIEDriverPath(String ieDriverPath) {
		config.setIeDriverPath(ieDriverPath);
	}

	public void setImplicitlyWaitTimeout(double implicitTimeout) {
		config.setImplicitWaitTimeout(implicitTimeout);
	}

	public void setMode(String mode) {
		config.setMode(WebDriverMode.valueOf(mode));
	}

	public void setOutputDirectory(String outputDirectory) {
		config.setOutputDirectory(outputDirectory);
	}

	public void setPageLoadTimeout(int pageLoadTimeout) {
		config.setPageLoadTimeout(pageLoadTimeout);
	}

	public void setProxyHost(String proxyHost) {
		config.setProxyHost(proxyHost);
	}

	public void setSetAcceptUntrustedCertificates(
			boolean setAcceptUntrustedCertificates) {
		config.setSetAcceptUntrustedCertificates(setAcceptUntrustedCertificates);
	}

	public void setSetAssumeUntrustedCertificateIssuer(
			boolean setAssumeUntrustedCertificateIssuer) {
		config.setSetAssumeUntrustedCertificateIssuer(setAssumeUntrustedCertificateIssuer);
	}

	public void setWebDriverBuilder(IWebDriverFactory builder) {
		this.webDriverBuilder = builder;
	}

	public void setWebSessionTimeout(int webSessionTimeout) {
		config.setWebSessionTimeout(webSessionTimeout);
	}

}
