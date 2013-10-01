package com.seleniumtests.webpage;
import static com.seleniumtests.controller.Assertion.assertEquals;
import static com.seleniumtests.controller.Assertion.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


import com.seleniumtests.controller.Assertion;
import com.seleniumtests.driver.web.WebUXDriver;
import com.seleniumtests.driver.web.element.Button;
import com.seleniumtests.driver.web.element.CheckBox;
import com.seleniumtests.driver.web.element.HtmlElement;
import com.seleniumtests.driver.web.element.Label;
import com.seleniumtests.driver.web.element.Link;
import com.seleniumtests.driver.web.element.TextField;
import com.seleniumtests.driver.web.element.WebPage;

/**
 * Exposes services provided by Review Order Page
 * <p/>
 * User: tbhadauria
 * Date: 4/15/13
 * Time: 11:53 AM
 */
public class XOReviewOrderPage extends WebPage {

    private static Button proceedToPayButton = new Button("Proceed To Pay", By.id("proceedtopay"));

    public XOReviewOrderPage() throws Exception {
        super(proceedToPayButton);
    }

    private static Link lnkChangeDeliveryAddress = new Link("Change Delivery Address Link", By.id("addressChange"));
    private static TextField txtCoupon = new TextField("Coupon Text box", By.id("enterCoupon"));
    private static Button btnCouponApply = new Button("Apply Coupon", By.id("couponApply"));
    private static Link lnkRemoveCoupon = new Link("Remove Coupon", By.id("couponRemove"));
    private static Label lblBuyerName = new Label("Buyer Name", By.cssSelector("#roAddressDetails>div:nth-child(1)"));
    private static Label lblAddress1And2 = new Label("Address1 and Address 2", By.cssSelector("#roAddressDetails>div:nth-child(2)"));
    private static Label lblCityStateZipCountry = new Label("City State Zip And Country", By.cssSelector("#roAddressDetails>div:nth-child(3)"));
    private static Label lblPhoneNumber = new Label("Phone Number", By.cssSelector("#roAddressDetails>div:nth-child(4)>.nu"));
    private static CheckBox chkCashOnDelivery = new CheckBox("Cash On Delivery", By.cssSelector("div[class ^= 'xo-com-cbx']>a"));
    private static CheckBox lblCouponApplicabilityMsg = new CheckBox("Coupon Applicability Message",
            By.xpath("//div[@id='roCouponTable']//p[contains(.,'Sorry, coupons are not applicable on COD.')]"));
    private final Label pageRefresh = new Label("Page Refresh", By.cssSelector("body[class='xo-oncall']"));
    private final Link lnkBack = new Link("Back Link", By.cssSelector(".xo-bkl>a"));
    private final Label lblCouponSuccessMsg = new Label("Coupon success message", "//div[@id='roPageErrorDiv']/div[@class='sm-co sm-su']");
    private final Label lblCouponFailureMsg = new Label("Coupon failure message", "//div[@id='roPageErrorDiv']/div[@class='sm-co sm-er']");
    private final TextField txtCouponCaptcha = new TextField("Coupon captcha text box", By.id("txtCouponCaptcha"));
    private final Link lnkRefreshCaptcha = new Link("refresh captcha", By.id("ropCaptcha"));
    private static TextField txtCaptchaCoupon = new TextField("Coupon Text box when captcha", By.xpath("//div[@id='coupCaptchaSection']//input[@id='enterCoupon']"));

    public static Link getChangeDeliveryAddressLink() {
        return lnkChangeDeliveryAddress;
    }

    public String getSellerNameForItem(String itemTitle) {
        return new Label("Seller Name", By.xpath("//div[@id='roCartGrp']" +
                "[descendant::div[contains(.,'" + itemTitle + "')]]/div[1]")).getText().trim();
    }

    public TextField getQuantityTextboxForItem(String itemTitle) {
        return new TextField("Item Quantity", By.xpath("//div[contains(@id, 'roItem')][descendant::div[contains(.,'" + itemTitle + "')]]//input"));
    }

    public Link getUpdateLinkForItem(String itemTitle) {
        return new Link("Update Link", By.xpath("//ul[descendant::div[contains(.,'" + itemTitle + "')]]" +
                "[@class='xo-itm-grid']//a[@class='xo-upa']"));
    }

    public Link getRemoveLinkForItem(String itemTitle) {
        return new Link("Remove Link", By.xpath("//div[@class = 'xo-slritm']" +
                "[descendant::div[contains(.,'" + itemTitle + "')]]//a[contains(@id,'remItem')]"));
    }

    public double getItemPrice(String itemTitle) {
        return Double.valueOf(new Label("Item Price", By.xpath("//div[contains(@id, 'roItem')][descendant::div[contains(.,'" + itemTitle + "')]]" +
                "//div[contains(@class, 'xo-price')]/span[2]")).getText().replaceAll(",", ""));
    }

    public String getSingleShippingMethod(String itemTitle) {
        String shippingLabel = new Label("Shipping Label", By.xpath("//div[@id='roCartGrp']" +
                "[descendant::div[contains(.,'" + itemTitle + "')]]//div[@class='xo-itm-shpng']/div[2]/span")).getText();
        if (shippingLabel.contains(":")) {
            return shippingLabel.split(":")[1].trim();
        } else {
            return shippingLabel;
        }
    }

    public Button getShippingDropdownForItem(String itemTitle) {
        return new Button("Shipping drop down", By.xpath("//ul[descendant::div[contains(.,'" + itemTitle + "')]]" +
                "//button"));
    }

    public BigDecimal getOrderTotal() {
        return new BigDecimal(new Label("Order Total", By.cssSelector("#orderAmount div:nth-child(1)>div:nth-child(2)" +
                ">span:nth-child(2)")).getText().replaceAll(",", "").trim());
    }

    /**
     * @return total shipping cost displayed under order total section
     *         Total shipping amount could be either a number or free cost
     */
    public BigDecimal getTotalShippingCost() {
        // position of total shipping cost varies depending on whether total shipping cost is free or amount
        if (new Label("Shipping Cost", By.cssSelector("#orderAmount div:nth-child(2)>div:nth-child(2)>span:nth-child(2)")).isElementPresent()) {
            return new BigDecimal(new Label("Shipping Cost", By.cssSelector("#orderAmount div:nth-child(2)>div:nth-child(2)>span:nth-child(2)"))
                    .getText().replaceAll(",", "").trim());
        } else if (new Label("Shipping Cost", By.cssSelector("#orderAmount div:nth-child(2)>div:nth-child(2)>span:nth-child(1)")).isElementPresent()) {
            if (new Label("Shipping Cost", By.cssSelector("#orderAmount div:nth-child(2)>div:nth-child(2)>span:nth-child(1)"))
                    .getText().equalsIgnoreCase("Free")) {
                return new BigDecimal("0.0");
                // Not a valid total shipping cost
            } else {
                return null;
            }
        } else {
            // Not a valid total shipping cost
            return null;
        }
    }

    public double getGrandTotal() {
        return Double.valueOf(new Label("Grand Total", By.cssSelector("#orderAmount~div>div:nth-child(2)>span:nth-child(2)"))
                .getText().replaceAll(",", ""));
    }

    public BigDecimal getGrandTotalNew() {
        return new BigDecimal(new Label("Grand Total", By.cssSelector("#orderAmount~div>div:nth-child(2)>span:nth-child(2)"))
                .getText().replaceAll(",", ""));
    }

    /**
     * @return order total amount displayed towards RHS
     */
    public double getProceedToPayGrandTotalAmount() {
        return Double.valueOf(new Label("Grand Total", By.cssSelector("#roOrderTotal>div:nth-of-type(2)>div:nth-child(2)" +
                ">span:nth-child(2)")).getText().replaceAll(",", ""));
    }

    /**
     * @return order total amount displayed towards bottom of page
     */
    public double getOrderDetailGrandTotalAmount() {
        return Double.valueOf(new Label("Grand Total", By.cssSelector(".xo-content .xo-gt>div:nth-child(2)>span:nth-child(2)"))
                .getText().replaceAll(",", ""));
    }

    public XOReviewOrderPage clickCashOnDelivery() {
        chkCashOnDelivery.click();
        waitForElementToDisappear(pageRefresh);
        return this;
    }

    public String getFreeShippingValueForItem(String itemTitle) {
        return new Label("Shipping Charges", By.xpath("//div[@class='xo-slritm'][descendant::div[contains(.,'" + itemTitle + "')]]" +
                "//div[@class='xo-itm-shcst']/span")).getText();
    }

    public double getItemShippingCharges(String itemTitle) {
        return Double.valueOf(new Label("Shipping Charges", By.xpath("//ul[@class='xo-itm-grid']" +
                "[descendant::div[contains(.,'" + itemTitle + "')]]//div[@class='xo-itm-shcst']/span[2]")).getText().replaceAll(",", ""));
    }

    public XOReviewOrderPage verifyCouponApplicabilityMessage() {
        assertElementPresent(lblCouponApplicabilityMsg);
        return this;
    }

    public XOReviewOrderPage verifyTotalItemCount(int itemCount) {
        assertElementPresent(new Label("Item Count Message", By.xpath("//div[@id='roOrderDetails']" +
                "/h3[contains(.,'Your order details - " + itemCount + " item(s)')]")));
        return this;
    }

    public XOReviewOrderPage verifyOrderTotal(BigDecimal expectedOrderTotal) {
        assertTrue(getOrderTotal().compareTo(expectedOrderTotal) == 0, "Order Total is incorrect");
        return this;
    }

    public XOReviewOrderPage verifyTotalShippingCost(BigDecimal expectedShippingCost) {
        assertTrue(getTotalShippingCost().compareTo(expectedShippingCost) == 0, "Shipping Total is incorrect");
        return this;
    }

    /**
     * Overall item Price and shipping cost
     *
     * @param expectedGrandTotal
     * @return
     */
    public XOReviewOrderPage verifyROPGrandTotal(BigDecimal expectedGrandTotal) {
        assertTrue(getGrandTotalNew().compareTo(expectedGrandTotal) == 0, "Grand Total is incorrect");
        return this;
    }

    public XOReviewOrderPage verifyUnavailabilityOfCashOnDelivery() {
        assertTrue(!chkCashOnDelivery.isElementPresent(), "Cash on Delivery options is available in review order page, " +
                "despite all items don't support cash on delivery");
        return this;
    }

    public XOReviewOrderPage verifyAvailabilityOfCashOnDelivery() {
        assertTrue(chkCashOnDelivery.isElementPresent(), "Cash on Delivery options is not available in review order page, " +
                "despite all items support cash on delivery");
        return this;
    }

    /**
     * Returns Map of all available shipping options from shipping drop down
     * Map has shipping name and corresponding value
     *
     * @param itemTitle
     * @return
     */
    public Map<String, Double> getShippingDropdownOptions(String itemTitle) {
        Map<String, Double> shippingServiceOptions = new TreeMap<String, Double>();
        List<WebElement> shippingDropDown =
                WebUXDriver.getWebDriver().findElements(By.xpath("//div[contains(@id, 'roItem')]" +
                        "[descendant::div[contains(.,'" + itemTitle + "')]]//li/span"));
        for (int i = 0; i < shippingDropDown.size(); i++) {
            shippingServiceOptions.put(shippingDropDown.get(i + i).getAttribute("innerHTML").split("Shipping: ")[1],
                    Double.valueOf(shippingDropDown.get(i + i + 1).getAttribute("innerHTML").replaceAll(",", "").split("Rs.")[1]));
            if (i + i + 2 == shippingDropDown.size()) {
                break;
            }
        }
        return shippingServiceOptions;
    }

    public XOReviewOrderPage selectShippingMethod(String itemTitle, String shippingMethod) {
        getShippingDropdownForItem(itemTitle).mouseOver();
        Label label = new Label("Shipping Dropdown value", "//div[contains(@id, 'roItem')][descendant::div[contains(.,'" + itemTitle + "')]]" +
                "//li/span[contains(.,'" + shippingMethod + "')]");
        label.waitForPresent();
        label.click();
        waitForElementToDisappear(pageRefresh);
        return this;
    }

    public XOReviewOrderPage removeItem(String itemTitle) {
        getRemoveLinkForItem(itemTitle).click();
        waitForElementToDisappear(pageRefresh);
        return this;
    }

    public XOShoppingCartPage goBackToShoppingCart() throws Exception {
        WebUXDriver.getWebDriver().navigate().back();
        return new XOShoppingCartPage();
    }

    public XOReviewOrderPage updateItemQuantity(String itemTitle, int itemQuantity) {
        getQuantityTextboxForItem(itemTitle).clear();
        getQuantityTextboxForItem(itemTitle).sendKeys(String.valueOf(itemQuantity));
        getUpdateLinkForItem(itemTitle).click();
        waitForElementToDisappear(pageRefresh);
        return this;
    }

    public XOPaymentPage proceedToPay() throws Exception {
        proceedToPayButton.click();
        return new XOPaymentPage();
    }

    public XOReviewOrderPage enterCouponCode(String couponCode) {
        txtCoupon.sendKeys(couponCode);
        return this;
    }

    public XOReviewOrderPage clickApplyCoupon() {
        btnCouponApply.click();
        waitForPageToRefresh();
        return this;
    }

    public XOReviewOrderPage enterCouponAndApply(String couponCode) {
        enterCouponCode(couponCode)
                .clickApplyCoupon();
        return this;
    }

    public XOReviewOrderPage removeCoupon() {
        lnkRemoveCoupon.click();
        return this;
    }

    public String getCouponCodeFromCouponSection() {
        return new Label("Coupon code from coupon section", "//div[@id='roCouponTable']//div[@class='xo-r-cpn']/span").getText();
    }

    public BigDecimal getCouponDiscountFromCouponSection() {
        return new BigDecimal(
                new Label("Coupon amount from coupon section", "//div[@id='roCouponTable']//div[@class='xo-r-cpn']/span[3]")
                        .getText().trim().replaceAll(",", ""));
    }

    public BigDecimal getCouponDiscountAppliedOnItem(String itemTitle) {
        return new BigDecimal(new Label("Shipping Charges",
                By.xpath("//ul[@class='xo-itm-grid']" +
                        "[descendant::div[contains(.,'" + itemTitle + "')]]//div[@class='xo-itm-shcst']/span[3]"))
                .getText().trim().replaceAll(",", ""));
    }

    public BigDecimal getCouponDiscountFromOrderTotalSection() {
        return new BigDecimal(new Label("Grand Total", By.cssSelector("#orderAmount~div>div:nth-child(3)>span:nth-child(3)"))
                .getText().trim().replaceAll(",", ""));
    }


    public XOReviewOrderPage verifyCouponSuccessMsgFromCouponSection() {
        assertEquals(
                new Label("Coupon success message from coupon section", "//div[@id='roCouponTable']//span[@class='xo-r-grn']").getText(),
                "Coupon Successfully Applied",
                "Invalid coupon success message in coupon section");
        return this;
    }

    public XOReviewOrderPage verifyCouponSuccessMessage(BigDecimal couponAmount) {
        assertEquals(lblCouponSuccessMsg.getText(),
                "Smart Move! You saved Rs. " + couponAmount + " with this eBay coupon.",
                "Wrong success message after applying coupon");
        return this;
    }

    public XOReviewOrderPage verifyCouponSuccessMessage(BigDecimal couponAmount, boolean isFullyFunded) {
        if (isFullyFunded) {
            verifyCouponSuccessMessageForFullyFundedCoupon();
            return this;
        }

        assertEquals(lblCouponSuccessMsg.getText(),
                "Smart Move! You saved Rs. " + couponAmount + " with this eBay coupon.",
                "Wrong success message after applying coupon");
        return this;
    }


    public XOReviewOrderPage verifyCouponDiscountFromCouponSection(BigDecimal expectedCouponDiscount) {
        assertEquals(getCouponDiscountFromCouponSection(), expectedCouponDiscount, "Coupon discount in Coupon section mismatch");
        return this;
    }

    public XOReviewOrderPage verifyCouponCodeFromCouponSection(String expectedCouponCode) {
        assertEquals(getCouponCodeFromCouponSection(), expectedCouponCode, "Coupon code in coupon section mismatch");
        return this;
    }

    public void waitForPageToRefresh() {
        HtmlElement htmlEle = new HtmlElement("PageRefresh", "//body[@class='xo-onloading']");
        if (htmlEle.isElementPresent())
            waitForElementToDisappear(new HtmlElement("PageRefresh", "//body[@class='xo-onloading']"));
    }

    public XOReviewOrderPage verifyCouponSuccessMessageForMUC(BigDecimal couponAmount, int remainingAttempt) {
        Assertion.assertEquals(lblCouponSuccessMsg.getText(),
                "Smart Move! You saved Rs. " + couponAmount + " with this eBay coupon. You now have " + remainingAttempt + " remaining attempt(s) for this coupon.",
                "Wrong success message after applying MUC coupon");
        return this;
    }

    public XOReviewOrderPage verifyCouponSuccessMessageForMUC(BigDecimal couponAmount, int remainingAttempt, boolean isFullyFunded) {
        if (isFullyFunded) {
            verifyCouponSuccessMessageForFullyFundedMUC(remainingAttempt);
            return this;
        }
        Assertion.assertEquals(lblCouponSuccessMsg.getText(),
                "Smart Move! You saved Rs. " + couponAmount + " with this eBay coupon. You now have " + remainingAttempt + " remaining attempt(s) for this coupon.",
                "Wrong success message after applying MUC coupon");
        return this;
    }


    public XOReviewOrderPage verifyCouponSuccessMessageForFullyFundedMUC(int remainingAttempt) {
        String successMessage = "Super! Your eBay coupon covers the full purchase amount!. You now have " + remainingAttempt + " remaining attempt(s) for this coupon.";
        Assertion.assertTrue(lblCouponSuccessMsg.getText().contains(successMessage), "Wrong success message after applying MUC coupon");
        lblCouponSuccessMsg.captureSnapshot();
        return this;

    }

    public XOReviewOrderPage verifyCouponSuccessMessageForFullyFundedCoupon() {
        String successMessage = "Super! Your eBay coupon covers the full purchase amount!";
        Assertion.assertTrue(lblCouponSuccessMsg.getText().contains(successMessage), "Wrong success message after applying coupon");
        lblCouponSuccessMsg.captureSnapshot();
        return this;

    }

   public XOReviewOrderPage verifyCouponCaptchaTextBox() {
        waitForElementPresent(txtCouponCaptcha);
        Assertion.assertTrue(txtCouponCaptcha.isElementPresent(), "Coupon captcha textbox not available");
        return this;
    }

    public XOReviewOrderPage verifyCouponCaptchaRefresh() {
        Assertion.assertTrue(lnkRefreshCaptcha.isElementPresent(), "Coupon captcha refresh not available");
        return this;
    }

    public XOReviewOrderPage verifyCouponTextBoxIsEnabled() {
        this.waitForElementEditable(txtCaptchaCoupon);
        Assertion.assertTrue(txtCaptchaCoupon.isEnabled(), "Coupon text box is not enabled");
        return this;
    }

    public XOReviewOrderPage verifyCouponApplyButtonIsEnabled() {
        Assertion.assertTrue(btnCouponApply.isEnabled(), "Coupon apply button is not enabled");
        return this;
    }

}