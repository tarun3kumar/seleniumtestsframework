package com.seleniumtests.browserfactory;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.seleniumtests.driver.DriverConfig;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.opera.core.systems.OperaProfile;
import com.seleniumtests.core.TestLogging;
import com.seleniumtests.helper.FileHelper;
import com.seleniumtests.helper.OSHelper;
import com.seleniumtests.operaprofile.OperaProfileMarker;


public class OperaCapabilitiesFactory implements ICapabilitiesFactory {
	private static Object lockProfile = new Object();
	private static boolean isProfileCreated = false;
	
	public DesiredCapabilities createCapabilities(DriverConfig cfg) {
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
	
	protected synchronized OperaProfile getOperaProfile(DriverConfig cfg) {
		OperaProfile profile = null;
		synchronized(OperaCapabilitiesFactory.class){
			String path = cfg.getOperaProfilePath();
			path = getOperaProfilePath(path);
			if (path != null) {
				profile = new OperaProfile(path);
			} else
				profile = new OperaProfile();
		}
		return profile;
	}
	
	protected String getOperaProfilePath(String path) {
		String realPath = null;
		if (path != null && !new File(path).exists()) {
			TestLogging.log("Profile path of Opera Browser:" + path
                    + " not found, use default");
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
				}
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
