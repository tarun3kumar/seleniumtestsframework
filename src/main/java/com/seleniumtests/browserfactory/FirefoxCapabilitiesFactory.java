package com.seleniumtests.browserfactory;

import java.io.File;
import java.io.IOException;

import com.seleniumtests.core.TestLogging;
import com.seleniumtests.customFFprofile.FireFoxProfileMarker;
import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.seleniumtests.driver.WebDriverConfig;
import com.seleniumtests.helper.FileHelper;
import com.seleniumtests.helper.OSHelper;

public class FirefoxCapabilitiesFactory implements ICapabilitiesFactory {
	private static boolean isProfileCreated = false;
	private static Object lockProfile = new Object();

	protected void configProfile(FirefoxProfile profile, WebDriverConfig webDriverConfig) {
		profile.setAcceptUntrustedCertificates(webDriverConfig
				.isSetAcceptUntrustedCertificates());
		profile.setAssumeUntrustedCertificateIssuer(webDriverConfig
				.isSetAssumeUntrustedCertificateIssuer());

		if (webDriverConfig.getFfBinPath() != null)
			System.setProperty("webdriver.firefox.bin", webDriverConfig.getFfBinPath());

		if (webDriverConfig.getUserAgentOverride() != null) {
			profile.setPreference("general.useragent.override",
					webDriverConfig.getUserAgentOverride());
		}
		if (webDriverConfig.getNtlmAuthTrustedUris() != null)
			profile.setPreference("network.automatic-ntlm-auth.trusted-uris",
					webDriverConfig.getNtlmAuthTrustedUris());
		if (webDriverConfig.getBrowserDownloadDir() != null) {
			profile.setPreference("browser.download.dir",
					webDriverConfig.getBrowserDownloadDir());
			profile.setPreference("browser.download.folderList", 2);
			profile.setPreference("browser.download.manager.showWhenStarting",
					false);
			profile.setPreference(
					"browser.helperApps.neverAsk.saveToDisk",
					"application/octet-stream,text/plain,application/pdf,application/zip,text/csv,text/html");
		}

		if (!webDriverConfig.isEnableJavascript())
			profile.setPreference("javascript.enabled", false);
		else {
			// Add Firefox extension to collect JS Error
			if (webDriverConfig.isAddJSErrorCollectorExtension()) {
				try {
					JavaScriptError.addExtension(profile);
				} catch (IOException e) {
                    e.printStackTrace();
				}
			}
		}
		// fix permission denied issues
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
	public DesiredCapabilities createCapabilities(WebDriverConfig webDriverConfig) {
		DesiredCapabilities capability;
		capability = new DesiredCapabilities();
		capability.setBrowserName(DesiredCapabilities.firefox()
				.getBrowserName());

		FirefoxProfile profile = getFirefoxProfile(webDriverConfig);
		configProfile(profile, webDriverConfig);
		capability.setCapability(FirefoxDriver.PROFILE, profile);

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

		if (webDriverConfig.getProxyHost() != null)
			capability.setCapability(CapabilityType.PROXY, webDriverConfig.getProxy());

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
					FileHelper.extractJar(profilePath, FireFoxProfileMarker.class);
				}
			} catch (Exception ex) {
                ex.printStackTrace();
			}
		}

		isProfileCreated = true;
	}

	protected synchronized FirefoxProfile getFirefoxProfile(WebDriverConfig webDriverConfig) {
		String path = webDriverConfig.getFfProfilePath();
		FirefoxProfile profile;
		String realPath;
		if (webDriverConfig.isUseFirefoxDefaultProfile())
			realPath = getFirefoxProfilePath(path);
		else
			realPath = null;
		profile = createFirefoxProfile(realPath);
		return profile;
	}

	protected String getFirefoxProfilePath(String path) {
		String realPath = null;
		if (path != null && !new File(path).exists()) {
			TestLogging.log("Firefox profile path:" + path
                    + " not found, use default");
			path = null;
		}
		if (path != null) {

			realPath = path;
		} else {
			try {
				String profilePath = this.getClass().getResource("/").getPath() + "ffprofile";
				profilePath = FileHelper.decodePath(profilePath);

				extractDefaultProfile(profilePath);
				realPath = profilePath + OSHelper.getSlash() + "customFFprofile";

			} catch (Exception e) {
				e.printStackTrace();
				realPath = null;
			}
		}
		System.out.println("Firefox Profile: " + realPath);
		return realPath;
	}
}
