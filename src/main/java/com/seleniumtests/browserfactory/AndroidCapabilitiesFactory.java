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

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.seleniumtests.driver.DriverConfig;

/**
 * Sets Android capabilities.
 */
public class AndroidCapabilitiesFactory implements ICapabilitiesFactory {

    public DesiredCapabilities createCapabilities(final DriverConfig cfg) {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("automationName", cfg.getAutomationName());
        capabilities.setCapability("platformName", cfg.getMobilePlatformName());

        // Set up version and device name else appium server would pick the only available emulator/device
        // Both of these are ignored for android for now
        capabilities.setCapability("platformVersion", cfg.getMobilePlatformVersion());
        capabilities.setCapability("deviceName", cfg.getDeviceName());

        // Use app browser when running test on emulator
        capabilities.setCapability("app", cfg.getApp());

        capabilities.setCapability(CapabilityType.BROWSER_NAME, cfg.getBrowserName());
        capabilities.setCapability("newCommandTimeout", cfg.getNewCommandTimeout());

        return capabilities;
    }
}
