package com.seleniumtests.browserfactory;

import java.io.File;
import java.io.IOException;

import com.seleniumtests.driver.DriverConfig;
import com.seleniumtests.driver.DriverMode;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.seleniumtests.helper.FileHelper;
import com.seleniumtests.helper.OSHelper;
import com.seleniumtests.resources.WebDriverExternalResources;

public class IECapabilitiesFactory implements ICapabilitiesFactory {

	private void handleExtractResources() throws IOException {
		String dir = this.getClass().getResource("/").getPath();
		dir = FileHelper.decodePath(dir);
		if (!new File(dir).exists()) {
			FileHelper.extractJar(dir, WebDriverExternalResources.class);
		}
		if (!new File(dir + "\\IEDriverServer.exe").exists()) {
			if (OSHelper.getIEVersion() < 10) {
				FileHelper.copyFile(dir + "\\IEDriverServer_x64.exe", dir
						+ "\\IEDriverServer.exe");
			} else
				FileHelper.copyFile(dir + "\\IEDriverServer_Win32.exe", dir
						+ "\\IEDriverServer.exe"); // Win32
		}
		System.setProperty("webdriver.ie.driver", dir + "\\IEDriverServer.exe");
		System.out.println(dir + "\\IEDriverServer.exe");
	}

	public DesiredCapabilities createCapabilities(DriverConfig cfg) {
		// Set IEDriver for Local Mode
		if (cfg.getMode() == DriverMode.LOCAL) {
			if (cfg.getIeDriverPath() != null)
				System.setProperty("webdriver.ie.driver", cfg.getIeDriverPath());
			else {
				if (System.getenv("webdriver.ie.driver")!=null)
				{
					System.out.println("Get IE Driver from property:"+System.getenv("webdriver.ie.driver"));
					System.setProperty("webdriver.ie.driver",System.getenv("webdriver.ie.driver"));
				}
				else{
					try {
						handleExtractResources();
					} catch (IOException e) {
                        e.printStackTrace();
					}
				}
			}
			
		}
		DesiredCapabilities capability = DesiredCapabilities.internetExplorer();

		capability.setBrowserName(DesiredCapabilities.internetExplorer()
				.getBrowserName());

		if (cfg.isEnableJavascript())
			capability.setJavascriptEnabled(true);
		else
			capability.setJavascriptEnabled(false);
		capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
		capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		capability.setCapability("ignoreZoomSetting", true);

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
		capability
				.setCapability(
						InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
		return capability;
	}
}