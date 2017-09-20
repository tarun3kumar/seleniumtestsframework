package com.seleniumtests.browserfactory;

import com.seleniumtests.core.SeleniumTestsContext;
import com.seleniumtests.core.SeleniumTestsContextManager;
import com.seleniumtests.driver.DriverConfig;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ZaleniumCapabilitiesFactory implements ICapabilitiesFactory {

    @Override
    public DesiredCapabilities createCapabilities(final DriverConfig cfg) {

        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", cfg.getBrowser());
        capabilities.setCapability("platform", cfg.getPlatform());
        capabilities.setCapability("name", SeleniumTestsContextManager.getThreadContext().getAttribute(SeleniumTestsContext.TEST_METHOD_SIGNATURE));

        return capabilities;
    }
}
