package com.seleniumtests.browserfactory;

import com.seleniumtests.customexception.DriverExceptions;
import com.seleniumtests.driver.DriverConfig;
import com.seleniumtests.driver.TestType;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SauceLabsDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory {


    public SauceLabsDriverFactory(final DriverConfig cfg) {
        super(cfg);
    }

    protected WebDriver createNativeDriver() throws MalformedURLException {

        if(webDriverConfig.getTestType().equals(TestType.APPIUM_WEB_ANDROID.getTestType())){
            return new AndroidDriver(new URL(webDriverConfig.getSauceLabsURL()), new AndroidCapabilitiesFactory()
                    .createCapabilities(webDriverConfig));
        } else if (webDriverConfig.getTestType().equals(TestType.APPIUM_WEB_IOS.getTestType())){
            return new IOSDriver(new URL(webDriverConfig.getSauceLabsURL()), new IOsCapabilitiesFactory()
                    .createCapabilities(webDriverConfig));
        }

        return new RemoteWebDriver(new URL(webDriverConfig.getSauceLabsURL()), new SauceLabsCapabilitiesFactory()
                    .createCapabilities(webDriverConfig));

    }

    @Override
    public WebDriver createWebDriver() {
        final DriverConfig cfg = this.getWebDriverConfig();

        try {
            driver = createNativeDriver();
        } catch (final MalformedURLException me){
            throw new DriverExceptions("Problem with creating driver", me);
        }

        setImplicitWaitTimeout(cfg.getImplicitWaitTimeout());
        if (cfg.getPageLoadTimeout() >= 0) {
            setPageLoadTimeout(cfg.getPageLoadTimeout());
        }

        this.setWebDriver(driver);
        return driver;
    }

    protected void setPageLoadTimeout(final long timeout) {
        try {
            driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
        } catch (UnsupportedCommandException e) {
            // chromedriver does not support pageLoadTimeout
        }
    }

}
