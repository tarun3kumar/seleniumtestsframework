package com.seleniumtests.driver.web.factory;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.seleniumtests.controller.TestLogging;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.seleniumtests.driver.web.BrowserType;
import com.seleniumtests.driver.web.ScreenShotRemoteWebDriver;
import com.seleniumtests.driver.web.WebDriverConfig;
import com.seleniumtests.helper.ThreadHelper;

public class RemoteDriverFactory extends AbstractWebDriverFactory implements
		IWebDriverFactory {

	public RemoteDriverFactory(WebDriverConfig cfg) {
		super(cfg);
	}

	@Override
	public WebDriver createWebDriver() throws MalformedURLException,
			IllegalArgumentException, SecurityException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException {
		WebDriverConfig cfg = this.getWebDriverConfig();
		DesiredCapabilities capability = null;
		URL url;

		url = new URL(cfg.getHubUrl());

		switch (cfg.getBrowser()) {
		case FireFox:
			capability = new FirefoxCapabilitiesFactory()
					.createCapabilities(cfg);
			break;
		case InternetExplore:
			capability = new IECapabilitiesFactory().createCapabilities(cfg);
			break;
		case Chrome:
			capability = new ChromeCapabilitiesFactory()
					.createCapabilities(cfg);
			break;
		case HtmlUnit:
			capability = new HtmlUnitCapabilitiesFactory()
					.createCapabilities(cfg);
			break;
		case Safari:
			capability = new SafariCapabilitiesFactory()
					.createCapabilities(cfg);
			break;
		case Android:
			capability = new AndroidCapabilitiesFactory()
					.createCapabilities(cfg);
			break;
		case IPhone:
			capability = ((ICapabilitiesFactory) Class
					.forName(
							"com.seleniumtests.driver.web.factory.IPhoneCapabilitiesFactory")
					.getConstructor().newInstance()).createCapabilities(cfg);
			break;
		case IPad:
			capability = ((ICapabilitiesFactory) Class
					.forName(
							"com.seleniumtests.driver.web.factory.IPadCapabilitiesFactory")
					.getConstructor().newInstance()).createCapabilities(cfg);
			break;
		case Opera:
			capability = new OperaCapabilitiesFactory().createCapabilities(cfg);
			break;
		case PhantomJS:
			capability = new PhantomJSCapabilitiesFactory()
					.createCapabilities(cfg);
			break;
		default:
			break;
		}

		switch (cfg.getBrowser()) {
		case IPhone:
		case IPad:
			driver = (WebDriver) Class
					.forName(
							"com.seleniumtests.driver.web.factory.RemoteIOSBaseDriver")
					.getConstructor(URL.class, DesiredCapabilities.class)
					.newInstance(url, capability);
			break;
		case FireFox:
			try {
				driver = new ScreenShotRemoteWebDriver(url, capability);
			} catch (RuntimeException ex) {
				if (ex.getMessage()
						.contains(
								"Unable to connect to host 127.0.0.1 on port 7062 after 45000 ms. Firefox console output")) {
					TestLogging.log("Firefox Driver creation got port 7062 exception, retry after 5 seconds");
					ThreadHelper.waitForSeconds(5);
					driver = new ScreenShotRemoteWebDriver(url, capability);
				} else
					throw ex;
			}
			break;
		default:
			driver = new ScreenShotRemoteWebDriver(url, capability);
		}

		setImplicitWaitTimeout(cfg.getImplicitWaitTimeout());
		if (cfg.getPageLoadTimeout() >= 0) {
			setPageLoadTimeout(cfg.getPageLoadTimeout(), cfg.getBrowser());
		}
		this.setWebDriver(driver);
		String hub = url.getHost();
		int port = url.getPort();
		// logging node ip address:
		try {
			HttpHost host = new HttpHost(hub, port);
			DefaultHttpClient client = new DefaultHttpClient();
			String sessionUrl = "http://" + hub + ":" + port
					+ "/grid/api/testsession?session=";
			URL session = new URL(sessionUrl
					+ ((RemoteWebDriver) driver).getSessionId());
			BasicHttpEntityEnclosingRequest req;
			req = new BasicHttpEntityEnclosingRequest("POST",
					session.toExternalForm());
			org.apache.http.HttpResponse response = client.execute(host, req);
			String responseContent = EntityUtils.toString(response.getEntity());
			try {
				JSONObject object = new JSONObject(responseContent);
				String proxyId = (String) object.get("proxyId");
				String node = (proxyId.split("//")[1].split(":")[0]);
				String browserName = ((RemoteWebDriver) driver)
						.getCapabilities().getBrowserName();
				String version = ((RemoteWebDriver) driver).getCapabilities()
						.getVersion();
				System.out.println("WebDriver is running on node " + node
						+ ", " + browserName + version + ", session "
						+ ((RemoteWebDriver) driver).getSessionId());
				TestLogging.log("WebDriver is running on node " + node + ", "
                        + browserName + version + ", session "
                        + ((RemoteWebDriver) driver).getSessionId());
			} catch (org.json.JSONException e) {
			}
		} catch (Exception ex) {
		}
		return driver;
	}

	protected void setPageLoadTimeout(long timeout, BrowserType type) {
		switch (type) {
		case Chrome:
			try {
				driver.manage().timeouts()
						.pageLoadTimeout(timeout, TimeUnit.SECONDS);
			} catch (UnsupportedCommandException e) {
				// chromedriver1 does not support pageLoadTimeout
			}
			break;
		case FireFox:
		case InternetExplore:
			driver.manage().timeouts()
					.pageLoadTimeout(timeout, TimeUnit.SECONDS);
			break;
		default:
			// Safari: java.lang.RuntimeException:
			// org.openqa.selenium.WebDriverException: Unknown command:
			// setTimeout (WARNING: The server did not provide any stacktrace
			// information)
		}
	}
}
