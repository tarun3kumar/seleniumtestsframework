package com.seleniumtests.driver.web.factory;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.seleniumtests.driver.web.WebDriverConfig;
import com.seleniumtests.driver.web.WebDriverMode;
import com.seleniumtests.helper.FileHelper;
import com.seleniumtests.helper.OSHelper;
import com.seleniumtests.resources.WebDriverExternalResources;

public class ChromeCapabilitiesFactory implements ICapabilitiesFactory {

	public DesiredCapabilities createCapabilities(WebDriverConfig cfg) {

		DesiredCapabilities capability = null;
		capability = DesiredCapabilities.chrome();
		capability
				.setBrowserName(DesiredCapabilities.chrome().getBrowserName());

		ChromeOptions options = new ChromeOptions();
		if (cfg.getUserAgentOverride() != null) {
			options.addArguments("--user-agent=" + cfg.getUserAgentOverride());
		}

		capability.setCapability(ChromeOptions.CAPABILITY, options);

		if (cfg.isEnableJavascript())
			capability.setJavascriptEnabled(true);
		else
			capability.setJavascriptEnabled(false);
		capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
		capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

		if (cfg.getBrowserVersion() != null) {
			capability.setVersion(cfg.getBrowserVersion());
		}

		if (cfg.getPlatform() != null) {
			capability.setPlatform(cfg.getPlatform());
		}

		if (cfg.getProxyHost() != null) {
			Proxy proxy = cfg.getProxy();
			capability.setCapability(CapabilityType.PROXY, proxy);
		}

		if (cfg.getChromeBinPath() != null) {
			capability.setCapability("chrome.binary", cfg.getChromeBinPath());
		}

		// Set ChromeDriver for local mode
		if (cfg.getMode() == WebDriverMode.LOCAL) {
			String chromeDriverPath = cfg.getChromeDriverPath();
			if (chromeDriverPath == null) {
				try {
					if (System.getenv("webdriver.chrome.driver") != null) {
						System.out.println("Chrome driver get from property:"
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
			System.out.println("handling chrome resources in " + dir);
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
