package com.seleniumtests.browserfactory;

import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;

import com.seleniumtests.driver.DriverConfig;

/**
 * AndroidDriverFactory.
 */
public class AndroidDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory {

    public AndroidDriverFactory(final DriverConfig webDriverConfig) {
        super(webDriverConfig);
    }

    @Override
    // TO Do - update API to use Selendroid APIs
    public WebDriver createWebDriver() throws MalformedURLException {

        /*AndroidDriver driver = null;
         * DriverConfig cfg = this.getWebDriverConfig();
         *
         * driver = new AndroidDriver(new AndroidCapabilitiesFactory().createCapabilities(cfg));
         *
         *this.setWebDriver(driver);*/
        return null;
    }

}
