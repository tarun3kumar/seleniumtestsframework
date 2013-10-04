package com.seleniumtests.driver.web.factory;

import java.io.File;
import java.io.IOException;

import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.seleniumtests.controller.Logging;
import com.seleniumtests.customProfileDirCUSTFF.FFProfileMarker;
import com.seleniumtests.driver.web.WebDriverConfig;
import com.seleniumtests.helper.FileHelper;
import com.seleniumtests.helper.OSHelper;

public class FirefoxCapabilitiesFactory implements ICapabilitiesFactory {
	private static boolean isProfileCreated = false;
	private static Object lockProfile = new Object();

	protected void configProfile(FirefoxProfile profile, WebDriverConfig cfg) {
		profile.setAcceptUntrustedCertificates(cfg
				.isSetAcceptUntrustedCertificates());
		profile.setAssumeUntrustedCertificateIssuer(cfg
				.isSetAssumeUntrustedCertificateIssuer());

		if (cfg.getFfBinPath() != null)
			System.setProperty("webdriver.firefox.bin", cfg.getFfBinPath());

		if (cfg.getUserAgentOverride() != null) {
			profile.setPreference("general.useragent.override",
					cfg.getUserAgentOverride());
		}
		if (cfg.getNtlmAuthTrustedUris() != null)
			profile.setPreference("network.automatic-ntlm-auth.trusted-uris",
					cfg.getNtlmAuthTrustedUris());
		if (cfg.getBrowserDownloadDir() != null) {
			profile.setPreference("browser.download.dir",
					cfg.getBrowserDownloadDir());
			profile.setPreference("browser.download.folderList", 2);
			profile.setPreference("browser.download.manager.showWhenStarting",
					false);
			profile.setPreference(
					"browser.helperApps.neverAsk.saveToDisk",
					"application/octet-stream,text/plain,application/pdf,application/zip,text/csv,text/html");

		}

		if (!cfg.isEnableJavascript())
			profile.setPreference("javascript.enabled", false);
		else {
			// Add Firefox extension to collect JS Error
			if (cfg.isAddJSErrorCollectorExtension()) {
				try {
					JavaScriptError.addExtension(profile);
				} catch (IOException e) {
				}
			}
		}
		// fix permission denied problem
		profile.setPreference(
				"capability.policy.default.Window.QueryInterface", "allAccess");
		profile.setPreference(
				"capability.policy.default.Window.frameElement.get",
				"allAccess");
		profile.setPreference(
				"capability.policy.default.HTMLDocument.compatMode.get",
				"allAccess");
		profile.setPreference(
				"capability.policy.default.Document.compatMode.get",
				"allAccess");
		profile.setEnableNativeEvents(false);
		profile.setPreference("dom.max_chrome_script_run_time", 0);
		profile.setPreference("dom.max_script_run_time", 0);
	}

	/**
	 * Create firefox capabilities
	 */

	public DesiredCapabilities createCapabilities(WebDriverConfig cfg) {
		DesiredCapabilities capability = null;
		capability = new DesiredCapabilities();
		capability.setBrowserName(DesiredCapabilities.firefox()
				.getBrowserName());

		FirefoxProfile profile = getFirefoxProfile(cfg);
		configProfile(profile, cfg);
		capability.setCapability(FirefoxDriver.PROFILE, profile);

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

		if (cfg.getProxyHost() != null)
			capability.setCapability(CapabilityType.PROXY, cfg.getProxy());

		return capability;
	}

	protected FirefoxProfile createFirefoxProfile(String path) {
		if (path != null)
			return new FirefoxProfile(new File(path));
		else
			return new FirefoxProfile();
	}

	/**
	 * extractDefaultProfile to a folder
	 * 
	 * @param profilePath
	 *            The folder to store the profile
	 * @throws IOException
	 */
	protected void extractDefaultProfile(String profilePath) throws IOException {
		synchronized (lockProfile) {
			try {
				if (!isProfileCreated) {
					System.out.println("start create profile");
					FileHelper.deleteDirectory(profilePath);
					FileHelper.extractJar(profilePath, FFProfileMarker.class);
				}
			} catch (Exception ex) {
			}
		}

		isProfileCreated = true;
	}

	protected synchronized FirefoxProfile getFirefoxProfile(WebDriverConfig cfg) {
		String path = cfg.getFfProfilePath();
		FirefoxProfile profile = null;
		String realPath = null;
		if (cfg.isUseFirefoxDefaultProfile())
			realPath = getFirefoxProfilePath(path);
		else
			realPath = null;
		profile = createFirefoxProfile(realPath);
		return profile;
	}

	protected String getFirefoxProfilePath(String path) {
		String realPath = null;
		if (path != null && !new File(path).exists()) {
			Logging.log("Firefox profile path:" + path
					+ " can't be found, use default");
			path = null;
		}
		if (path != null) {

			realPath = path;
		} else {
			try {

				//String slash = "\\";
				//String profilePath = "C:\\grid\\profile";
				String profilePath = this.getClass().getResource("/").getPath() + "ffprofile";
				profilePath = FileHelper.decodePath(profilePath);

				extractDefaultProfile(profilePath);
				realPath = profilePath + OSHelper.getSlash() + "customProfileDirCUSTFF";

			} catch (Exception e) {
				e.printStackTrace();
				realPath = null;
			}
		}
		System.out.println("FirefoxProfile: " + realPath);
		return realPath;
	}
}
