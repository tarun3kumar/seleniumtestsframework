package com.seleniumtests.core;

import org.openqa.selenium.By;

/**
 * @author  tbhadauria <tarun.kumar.bhadauria@zalando.de>
 */
public class Locator {

    // To Do - should not return By object but hide it. https://sourcemaking.com/design_patterns/facade/java/1

    public static By locateByName(final String name) {
        return By.name(name);
    }

    public static By locateById(final String id) {
        return By.id(id);
    }

    public static By locateByCSSSelector(final String cssSelector) {
        return By.cssSelector(cssSelector);
    }

    public static By locateByXPath(final String xPath) {
        return By.xpath(xPath);
    }

    public static By locateByLinkText(final String linkText) {
        return By.linkText(linkText);
    }

    public static By locateByPartialLinkText(final String partialLinkText) {
        return By.partialLinkText(partialLinkText);
    }

    public static By locateByClassName(final String className) {
        return By.className(className);
    }

}
