package com.seleniumtests.driver;

import java.net.URL;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ScreenShotRemoteWebDriver extends RemoteWebDriver implements TakesScreenshot {
    public ScreenShotRemoteWebDriver(final DesiredCapabilities capabilities) {
        super(capabilities);
    }

    public ScreenShotRemoteWebDriver(final URL url, final DesiredCapabilities capabilities) {
        super(url, capabilities);
    }

    public ScreenShotRemoteWebDriver() { }

    public <X> X getScreenshotAs(final OutputType<X> target) throws WebDriverException {
        if ((Boolean) getCapabilities().getCapability(CapabilityType.TAKES_SCREENSHOT)) {
            String output = execute(DriverCommand.SCREENSHOT).getValue().toString();
            return target.convertFromBase64Png(output);
        }

        return null;
    }
}
