package com.seleniumtests.browserfactory;

import java.io.File;
import java.io.IOException;

import com.seleniumtests.driver.DriverConfig;
import com.seleniumtests.driver.DriverMode;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.seleniumtests.helper.FileHelper;
import com.seleniumtests.helper.OSHelper;
import com.seleniumtests.resources.WebDriverExternalResources;

public class ChromeCapabilitiesFactory implements ICapabilitiesFactory {

	public DesiredCapabilities createCapabilities(DriverConfig webDriverConfig) {

		DesiredCapabilities capability = null;
		capability = DesiredCapabilities.chrome();
		capability
				.setBrowserName(DesiredCapabilities.chrome().getBrowserName());

		ChromeOptions options = new ChromeOptions();
		if (webDriverConfig.getUserAgentOverride() != null) {
			options.addArguments("--user-agent=" + webDriverConfig.getUserAgentOverride());
		}

		capability.setCapability(ChromeOptions.CAPABILITY, options);

		if (webDriverConfig.isEnableJavascript())
			capability.setJavascriptEnabled(true);
		else
			capability.setJavascriptEnabled(false);
		capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
		capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

		if (webDriverConfig.getBrowserVersion() != null) {
			capability.setVersion(webDriverConfig.getBrowserVersion());
		}

		if (webDriverConfig.getPlatform() != null) {
			capability.setPlatform(webDriverConfig.getPlatform());
		}

		if (webDriverConfig.getProxyHost() != null) {
			Proxy proxy = webDriverConfig.getProxy();
			capability.setCapability(CapabilityType.PROXY, proxy);
		}

		if (webDriverConfig.getChromeBinPath() != null) {
			capability.setCapability("chrome.binary", webDriverConfig.getChromeBinPath());
		}

		// Set ChromeDriver for local mode
		if (webDriverConfig.getMode() == DriverMode.LOCAL) {
			String chromeDriverPath = webDriverConfig.getChromeDriverPath();
			if (chromeDriverPath == null) {
				try {
					if (System.getenv("webdriver.chrome.driver") != null) {
						System.out.println("get Chrome driver from property:"
								+ System.getenv("webdriver.chrome.driver"));
						System.setProperty("webdriver.chrome.driver",
								System.getenv("webdriver.chrome.driver"));
					} else
						handleExtractResources();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} else
				System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		}
		return capability;
	}

	public void handleExtractResources() throws IOException {
		String dir = this.getClass().getResource("/").getPath();
		dir = FileHelper.decodePath(dir);

		if (!new File(dir).exists()) {
			System.out.println("extracting chrome resources in " + dir);
			FileHelper.extractJar(dir, WebDriverExternalResources.class);
		}
		if (!new File(dir + OSHelper.getSlash() + "chromedriver.exe").exists()) {
			FileHelper.extractJar(dir, WebDriverExternalResources.class);
		}

		if (OSHelper.isWindows())
			System.setProperty("webdriver.chrome.driver", dir
					+ "\\chromedriver.exe");
		else {
			System.setProperty("webdriver.chrome.driver", dir + "/chromedriver");
			new File(dir + "/chromedriver").setExecutable(true);
		}
	}

}
