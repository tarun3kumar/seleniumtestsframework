package com.seleniumtests.browserfactory;

import com.seleniumtests.customexception.DriverExceptions;
import com.seleniumtests.driver.DriverConfig;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

    import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class ZaleniumDriverFactory extends AbstractWebDriverFactory implements IWebDriverFactory {

    public ZaleniumDriverFactory(final DriverConfig cfg) {
        super(cfg);
    }

    protected WebDriver createNativeDriver() throws MalformedURLException{
        return new RemoteWebDriver(new URL(webDriverConfig.getZaleniumURL()), new ZaleniumCapabilitiesFactory().createCapabilities(webDriverConfig));
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
