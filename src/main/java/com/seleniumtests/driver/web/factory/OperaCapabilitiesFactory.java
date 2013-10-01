package com.seleniumtests.driver.web.factory;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.opera.core.systems.OperaProfile;
import com.seleniumtests.controller.Logging;
import com.seleniumtests.driver.web.WebDriverConfig;
import com.seleniumtests.helper.FileHelper;
import com.seleniumtests.helper.OSHelper;
import com.seleniumtests.operaprofile.OperaProfileMarker;


public class OperaCapabilitiesFactory implements ICapabilitiesFactory {
	private static Object lockProfile = new Object();
	private static boolean isProfileCreated = false;
	
	public DesiredCapabilities createCapabilities(WebDriverConfig cfg) {
		DesiredCapabilities capability = DesiredCapabilities.opera();

		capability.setBrowserName(DesiredCapabilities.opera().getBrowserName());

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

		OperaProfile profile = getOperaProfile(cfg);
		capability.setCapability("opera.profile", profile);

		if (cfg.getProxyHost() != null) {
			Proxy proxy = cfg.getProxy();
			capability.setCapability(CapabilityType.PROXY, proxy);
		}

		return capability;
	}
	
	protected synchronized OperaProfile getOperaProfile(WebDriverConfig cfg) {
		// For example Opera profile path is "/Users/junjshi/Library/Opera/" in
		// Mac machine.
		// This doesn't work in Windows so far.
		// in about:config to find Opera Directory
		OperaProfile profile = null;
		synchronized(OperaCapabilitiesFactory.class){
			String path = cfg.getOperaProfilePath();
			path = getOperaProfilePath(path);
			if (path != null) {
				profile = new OperaProfile(path);
			} else
				profile = new OperaProfile(); // fresh, random profile
		}
		return profile;
	}
	
	protected String getOperaProfilePath(String path) {
		String realPath = null;
		if (path != null && !new File(path).exists()) {
			Logging.log("Opera profile path:" + path
					+ " can't be found, use default");
			path = null;
		}
		if (path != null) {

			realPath = path;
		} else {
			try {

				String slash = "\\";
				String profilePath = "C:\\grid\\profile";
				if (!OSHelper.isWindows()) {
					slash = "/";
					profilePath = "/tmp/maui";
				}
				//2.23.0, opera doesn't create Temporary profile folder, so we create it, a mvn clean command will clear these folders
				profilePath = this.getClass().getResource("/").getFile()+slash+"defaultOperaProfile";
				extractDefaultProfile(profilePath);
				String tempProfilePath = this.getClass().getResource("/").getFile()+slash+new Date().hashCode()+Thread.currentThread().getId();
				FileUtils.copyDirectory(new File(profilePath), new File(tempProfilePath));
				realPath = tempProfilePath + slash + "operaProfile";

			} catch (Exception e) {
				e.printStackTrace();
				realPath = null;
			}
		}
		System.out.println("OperaProfile: " + realPath);
		return realPath;
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
					FileHelper.extractJar(profilePath, OperaProfileMarker.class);
				}
			} catch (Exception ex) {
			}
		}

		isProfileCreated = true;
	}
}
