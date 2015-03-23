package com.seleniumtests.driver;

public enum BrowserType {
    FireFox("*firefox"),
    InternetExplore("*iexplore"),
    Chrome("*chrome"),
    HtmlUnit("*htmlunit"),
    Safari("*safari"),
    Opera("*opera"),
    Android("*android"),
    IPhone("*iphone"),
    IPad("*ipad"),
    PhantomJS("*phantomjs");

    public static BrowserType getBrowserType(final String browserType) {
        if (browserType.equalsIgnoreCase("*firefox") || browserType.equalsIgnoreCase("firefox")) {
            return BrowserType.FireFox;
        } else if (browserType.equalsIgnoreCase("*iexplore") || browserType.equalsIgnoreCase("iexplore")) {
            return BrowserType.InternetExplore;
        } else if (browserType.equalsIgnoreCase("*chrome") || browserType.equalsIgnoreCase("chrome")) {
            return BrowserType.Chrome;
        } else if (browserType.equalsIgnoreCase("*htmlunit") || browserType.equalsIgnoreCase("htmlunit")) {
            return BrowserType.HtmlUnit;
        } else if (browserType.equalsIgnoreCase("*safari") || browserType.equalsIgnoreCase("safari")) {
            return BrowserType.Safari;
        } else if (browserType.equalsIgnoreCase("*android") || browserType.equalsIgnoreCase("android")) {
            return BrowserType.Android;
        } else if (browserType.equalsIgnoreCase("*iphone") || browserType.equalsIgnoreCase("iphone")) {
            return BrowserType.IPhone;
        } else if (browserType.equalsIgnoreCase("*ipad") || browserType.equalsIgnoreCase("ipad")) {
            return BrowserType.IPad;
        } else if (browserType.equalsIgnoreCase("*opera") || browserType.equalsIgnoreCase("opera")) {
            return BrowserType.Opera;
        } else if (browserType.equalsIgnoreCase("*phantomjs") || browserType.equalsIgnoreCase("phantomjs")) {
            return BrowserType.PhantomJS;
        } else {
            return BrowserType.FireFox;
        }
    }

    private String browserType;

    BrowserType(final String type) {
        this.browserType = type;
    }

    public String getBrowserType() {
        return this.browserType;
    }

}
