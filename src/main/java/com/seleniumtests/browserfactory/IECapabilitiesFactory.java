/*
 * Copyright 2015 www.seleniumtests.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seleniumtests.browserfactory;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.seleniumtests.driver.DriverConfig;
import com.seleniumtests.driver.DriverMode;

import com.seleniumtests.helper.FileUtility;
import com.seleniumtests.helper.OSUtility;

import com.seleniumtests.resources.WebDriverExternalResources;

public class IECapabilitiesFactory implements ICapabilitiesFactory {

    private void handleExtractResources() throws IOException {
        String dir = this.getClass().getResource("/").getPath();
        dir = FileUtility.decodePath(dir);
        if (!new File(dir).exists()) {
            FileUtility.extractJar(dir, WebDriverExternalResources.class);
        }

        if (!new File(dir + "\\IEDriverServer.exe").exists()) {
            if (OSUtility.getIEVersion() < 10) {
                FileUtility.copyFile(dir + "\\IEDriverServer_x64.exe", dir + "\\IEDriverServer.exe");
            } else {
                FileUtility.copyFile(dir + "\\IEDriverServer_Win32.exe", dir + "\\IEDriverServer.exe"); // Win32
            }
        }

        System.setProperty("webdriver.ie.driver", dir + "\\IEDriverServer.exe");
        System.out.println(dir + "\\IEDriverServer.exe");
    }

    public DesiredCapabilities createCapabilities(final DriverConfig cfg) {

        // Set IEDriver for Local Mode
        if (cfg.getMode() == DriverMode.LOCAL) {
            if (cfg.getIeDriverPath() != null) {
                System.setProperty("webdriver.ie.driver", cfg.getIeDriverPath());
            } else {
                if (System.getenv("webdriver.ie.driver") != null) {
                    System.out.println("Get IE Driver from property:" + System.getenv("webdriver.ie.driver"));
                    System.setProperty("webdriver.ie.driver", System.getenv("webdriver.ie.driver"));
                } else {
                    try {
                        handleExtractResources();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        DesiredCapabilities capability = DesiredCapabilities.internetExplorer();

        capability.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());

        if (cfg.isEnableJavascript()) {
            capability.setJavascriptEnabled(true);
        } else {
            capability.setJavascriptEnabled(false);
        }

        capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
        capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capability.setCapability("ignoreZoomSetting", true);

        if (cfg.getBrowserVersion() != null) {
            capability.setVersion(cfg.getBrowserVersion());
        }

        if (cfg.getWebPlatform() != null) {
            capability.setPlatform(cfg.getWebPlatform());
        }

        if (cfg.getProxyHost() != null) {
            Proxy proxy = cfg.getProxy();
            capability.setCapability(CapabilityType.PROXY, proxy);
        }

        capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        return capability;
    }
}
