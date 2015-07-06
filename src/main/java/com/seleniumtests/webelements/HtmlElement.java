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

package com.seleniumtests.webelements;

import java.util.List;

import org.apache.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.seleniumtests.core.TestLogging;

import com.seleniumtests.driver.BrowserType;
import com.seleniumtests.driver.ScreenshotUtil;
import com.seleniumtests.driver.WebUIDriver;

import com.seleniumtests.helper.ContextHelper;
import com.seleniumtests.helper.WaitHelper;

import com.thoughtworks.selenium.webdriven.JavascriptLibrary;

/**
 * Provides methods to interact with a web page. All HTML element (ButtonElement, LinkElement, TextFieldElement, etc.)
 * extends from this class.
 */
public class HtmlElement {

    private static enum LocatorType {
        ID,
        NAME,
        CLASS_NAME,
        LINK_TEXT,
        PARTIAL_LINK_TEXT,
        CSS_SELECTOR,
        TAG_NAME,
        XPATH,
    }

    private static final int EXPLICIT_WAIT_TIME_OUT = WebUIDriver.getWebUIDriver().getExplicitWait();
    protected static final Logger logger = TestLogging.getLogger(HtmlElement.class);
    protected WebDriver driver = WebUIDriver.getWebDriver();
    protected WebUIDriver webUXDriver = WebUIDriver.getWebUIDriver();
    protected WebElement element = null;
    private String label = null;
    private String locator = null;
    private By by = null;

    /**
     * Find element using BY locator. Make sure to initialize the driver before calling findElement()
     *
     * @param   label  - element name for logging
     * @param   by     - By type
     *
     * @sample  {@code new HtmlElement("UserId", By.id(userid))}
     */
    public HtmlElement(final String label, final By by) {
        this.label = label;
        this.by = by;
    }

    /**
     * This constructor locates the element using locator and locator type.
     *
     * @param  label
     * @param  locator  - locator
     */
    public HtmlElement(final String label, final String locator, final LocatorType locatorType) {
        this.label = label;
        this.locator = locator;
        this.by = getLocatorBy(locator, locatorType);
    }

    /**
     * Captures snapshot of the current browser window.
     */
    public void captureSnapshot() {
        captureSnapshot(ContextHelper.getCallerMethod() + " on ");
    }

    /**
     * Captures snapshot of the current browser window, and prefix the file name with the assigned string.
     *
     * @param  messagePrefix
     */
    protected void captureSnapshot(final String messagePrefix) {
        ScreenshotUtil.captureSnapshot(messagePrefix);
    }

    public void click() {
        findElement();
        element.click();
    }

    /**
     * Click element in native way by Actions.
     */
    public void clickAt() {
        clickAt("1,1");

    }

    /**
     * Click element in native way by Actions.
     *
     * <p/>
     * <pre class="code">
       clickAt(&quot;1, 1&quot;);
     * </pre>
     *
     * @param  value
     */
    public void clickAt(final String value) {
        TestLogging.logWebStep(null, "click on " + toHTML(), false);
        findElement();

        String[] parts = value.split(",");
        int xOffset = Integer.parseInt(parts[0]);
        int yOffset = Integer.parseInt(parts[1]);
        try {
            new Actions(driver).moveToElement(element, xOffset, yOffset).click().perform();
        } catch (InvalidElementStateException e) {
            e.printStackTrace();
            element.click();
        }

        try {
            BrowserType type = WebUIDriver.getWebUIDriver().getConfig().getBrowser();
            if ((type == BrowserType.Chrome || type == BrowserType.InternetExplore)
                    && this.getDriver().switchTo().alert().getText().contains("leave")) {
                this.getDriver().switchTo().alert().accept();
            }
        } catch (NoAlertPresentException e) {
            e.printStackTrace();
        }
    }

    public void simulateClick() {
        findElement();

        String mouseOverScript =
            "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(mouseOverScript, element);
        WaitHelper.waitForSeconds(2);

        String clickScript =
            "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('click', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onclick');}";
        js.executeScript(clickScript, element);
        WaitHelper.waitForSeconds(2);
    }

    public void simulateMoveToElement(final int x, final int y) {
        findElement();
        ((JavascriptExecutor) driver).executeScript(
            "function simulate(f,c,d,e){var b,a=null;for(b in eventMatchers)if(eventMatchers[b].test(c)){a=b;break}if(!a)return!1;document.createEvent?(b=document.createEvent(a),a==\"HTMLEvents\"?b.initEvent(c,!0,!0):b.initMouseEvent(c,!0,!0,document.defaultView,0,d,e,d,e,!1,!1,!1,!1,0,null),f.dispatchEvent(b)):(a=document.createEventObject(),a.detail=0,a.screenX=d,a.screenY=e,a.clientX=d,a.clientY=e,a.ctrlKey=!1,a.altKey=!1,a.shiftKey=!1,a.metaKey=!1,a.button=1,f.fireEvent(\"on\"+c,a));return!0} var eventMatchers={HTMLEvents:/^(?:load|unload|abort|errorLogger|select|change|submit|reset|focus|blur|resize|scroll)$/,MouseEvents:/^(?:click|dblclick|mouse(?:down|up|over|move|out))$/}; "
                + "simulate(arguments[0],\"mousemove\",arguments[1],arguments[2]);", element, x, y);

    }

    /**
     * Finds the element using By type. Implicit Waits is built in createWebDriver() in WebUIDriver to handle dynamic
     * element problem. This method is invoked before all the basic operations like click, sendKeys, getText, etc. Use
     * waitForPresent to use Explicit Waits to deal with special element which needs long time to present.
     */
    protected void findElement() {
        driver = WebUIDriver.getWebDriver();
        element = driver.findElement(by);
    }

    /**
     * Fires a Javascript event on the underlying element.
     *
     * @param  eventName
     */
    public void fireEvent(final String eventName) {
        findElement();
        try {
            JavascriptLibrary jsLib = new JavascriptLibrary();
            jsLib.callEmbeddedSelenium(driver, "doFireEvent", element, eventName);
        } catch (Exception ex) {
            // Handle OperaDriver
        }
    }

    /**
     * Get all elements in the current page with same locator.
     *
     * @return
     */
    public List<WebElement> getAllElements() {
        findElement();
        return driver.findElements(by);
    }

    /**
     * Gets an attribute (using standard key-value pair) from the underlying attribute.
     *
     * @param   name
     *
     * @return
     */
    public String getAttribute(final String name) {
        findElement();
        return element.getAttribute(name);
    }

    /**
     * Returns the BY locator stored in the HtmlElement.
     *
     * @return
     */
    public By getBy() {
        return by;
    }

    /**
     * Returns the value for the specified CSS key.
     *
     * @param   propertyName
     *
     * @return
     */
    public String getCssValue(final String propertyName) {
        findElement();
        return element.getCssValue(propertyName);
    }

    /**
     * Get and refresh underlying WebDriver.
     */
    protected WebDriver getDriver() {
        return WebUIDriver.getWebDriver();
    }

    /**
     * Returns the underlying WebDriver WebElement.
     *
     * @return
     */
    public WebElement getElement() {
        element = driver.findElement(by);
        return element;
    }

    /**
     * Executes the given JavaScript against the underlying WebElement.
     *
     * @param   script
     *
     * @return
     */
    public String getEval(final String script) {
        findElement();

        String name = (String) ((JavascriptExecutor) driver).executeScript(script, element);
        return name;
    }

    /**
     * Returns the 'height' property of the underlying WebElement's Dimension.
     *
     * @return
     */
    public int getHeight() {
        findElement();
        return element.getSize().getHeight();
    }

    /**
     * Returns the label used during initialization.
     *
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the Point location of the underlying WebElement.
     *
     * @return
     */
    public Point getLocation() {
        findElement();
        return element.getLocation();
    }

    /**
     * Returns the locator used to find the underlying WebElement.
     *
     * @return
     */
    public String getLocator() {
        return locator;
    }

    private By getLocatorBy(final String locator, final LocatorType locatorType) {
        switch (locatorType) {

            case ID :
                return By.id(locator);

            case NAME :
                return By.name(locator);

            case CLASS_NAME :
                return By.className(locator);

            case LINK_TEXT :
                return By.linkText(locator);

            case PARTIAL_LINK_TEXT :
                return By.partialLinkText(locator);

            case CSS_SELECTOR :
                return By.cssSelector(locator);

            case TAG_NAME :
                return By.tagName(locator);

            default :
                return By.xpath(locator);
        }
    }

    /**
     * Returns the Dimension property of the underlying WebElement.
     *
     * @return
     */
    public Dimension getSize() {
        findElement();
        return element.getSize();
    }

    /**
     * Returns the HTML Tag for the underlying WebElement (div, a, input, etc).
     *
     * @return
     */
    public String getTagName() {
        findElement();
        return element.getTagName();
    }

    /**
     * Returns the text body of the underlying WebElement.
     *
     * @return
     */
    public String getText() {
        findElement();
        return element.getText();
    }

    /**
     * Returns the 'value' attribute of the underlying WebElement.
     *
     * @return
     */
    public String getValue() {
        findElement();
        return element.getAttribute("value");
    }

    /**
     * Returns the 'width' property of the underlying WebElement's Dimension.
     *
     * @return
     */
    public int getWidth() {
        findElement();
        return element.getSize().getWidth();
    }

    /**
     * Refreshes the WebUIDriver before locating the element, to ensure we have the current version (useful for when the
     * state of an element has changed via an AJAX or non-page-turn action).
     */
    public void init() {
        driver = WebUIDriver.getWebDriver();
        element = driver.findElement(by);
    }

    /**
     * Indicates whether or not the web element is currently displayed in the browser.
     *
     * @return
     */
    public boolean isDisplayed() {
        findElement();
        try {
            return element.isDisplayed();
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Searches for the element using the BY locator, and indicates whether or not it exists in the page. This can be
     * used to look for hidden objects, whereas isDisplayed() only looks for things that are visible to the user
     *
     * @return
     */
    public boolean isElementPresent() {
        if (WebUIDriver.getWebDriver() == null) {
            TestLogging.log("Web Driver is terminated! Exception might caught in last action.");
            throw new RuntimeException("Web Driver is terminated! Exception might caught in last action.");
        }

        int count = 0;
        try {

            count = WebUIDriver.getWebDriver().findElements(by).size();
        } catch (RuntimeException e) {
            if (e instanceof InvalidSelectorException) {
                TestLogging.log("Got InvalidSelectorException, retry");
                WaitHelper.waitForSeconds(2);
                count = WebUIDriver.getWebDriver().findElements(by).size();
            } else if (e.getMessage() != null
                    && e.getMessage().contains("TransformedEntriesMap cannot be cast to java.util.List")) {
                TestLogging.log("Got CastException, retry");
                WaitHelper.waitForSeconds(2);
                count = WebUIDriver.getWebDriver().findElements(by).size();
            } else {
                throw e;
            }
        }

        if (count == 0) {
            return false;
        }

        return true;
    }

    /**
     * Indicates whether or not the element is enabled in the browser.
     *
     * @return
     */
    public boolean isEnabled() {
        findElement();
        return element.isEnabled();
    }

    /**
     * Indicates whether or not the element is selected in the browser.
     *
     * @return
     */
    public boolean isSelected() {
        findElement();
        return element.isSelected();
    }

    /**
     * Whether or not the indicated text is contained in the element's getText() attribute.
     *
     * @param   text
     *
     * @return
     */
    public boolean isTextPresent(final String text) {
        findElement();
        return element.getText().contains(text);
    }

    /**
     * Forces a mouseDown event on the WebElement.
     */
    public void mouseDown() {
        TestLogging.log("MouseDown " + this.toString());
        findElement();

        Mouse mouse = ((HasInputDevices) driver).getMouse();
        mouse.mouseDown(null);
    }

    /**
     * Forces a mouseOver event on the WebElement.
     */
    public void mouseOver() {
        TestLogging.log("MouseOver " + this.toString());
        findElement();

        // build and perform the mouseOver with Advanced User Interactions API
        // Actions builder = new Actions(driver);
        // builder.moveToElement(element).build().perform();
        Locatable hoverItem = (Locatable) element;
        Mouse mouse = ((HasInputDevices) driver).getMouse();
        mouse.mouseMove(hoverItem.getCoordinates());
    }

    /**
     * Forces a mouseOver event on the WebElement using simulate by JavaScript way for some dynamic menu.
     */
    public void simulateMouseOver() {
        findElement();

        String mouseOverScript =
            "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(mouseOverScript, element);
    }

    /**
     * Forces a mouseUp event on the WebElement.
     */
    public void mouseUp() {
        TestLogging.log("MouseUp " + this.toString());
        findElement();

        Mouse mouse = ((HasInputDevices) driver).getMouse();
        mouse.mouseUp(null);
    }

    /**
     * Sends the indicated CharSequence to the WebElement.
     *
     * @param  arg0
     */
    public void sendKeys(final CharSequence arg0) {
        findElement();
        element.sendKeys(arg0);
    }

    /**
     * Method, which should never be used.
     */
    protected void sleep(final int waitTime) throws InterruptedException {
        Thread.sleep(waitTime);
    }

    /**
     * Converts the Type, Locator and LabelElement attributes of the HtmlElement into a readable and report-friendly
     * string.
     *
     * @return
     */
    public String toHTML() {
        return getClass().getSimpleName().toLowerCase()
                + " <a style=\"font-style:normal;color:#8C8984;text-decoration:none;\" href=# \">" + getLabel() + ",: "
                + getBy().toString() + "</a>";
    }

    /**
     * Returns a friendly string, representing the HtmlElement's Type, LabelElement and Locator.
     */
    public String toString() {
        return getClass().getSimpleName().toLowerCase() + " " + getLabel() + ", by={" + getBy().toString() + "}";
    }

    /**
     * Wait element to present using Explicit Waits with default EXPLICIT_WAIT_TIME_OUT = 15 seconds.
     */
    public void waitForPresent() {
        waitForPresent(EXPLICIT_WAIT_TIME_OUT);
    }

    /**
     * Wait element to present using Explicit Waits with timeout in seconds. This method is used for special element
     * which needs long time to present.
     */
    public void waitForPresent(final int timeout) {
        TestLogging.logWebStep(null, "wait for " + this.toString() + " to present.", false);

        Wait<WebDriver> wait = new WebDriverWait(driver, timeout);
        wait.until(new ExpectedCondition<WebElement>() {
                public WebElement apply(final WebDriver driver) {
                    return driver.findElement(by);
                }
            });
    }
}
