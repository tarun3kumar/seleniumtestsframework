package com.seleniumtests.webpage;

import com.seleniumtests.driver.web.WebUXDriver;
import com.seleniumtests.driver.web.element.*;
import com.seleniumtests.driver.web.element.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.seleniumtests.controller.Assertion.assertEquals;
import static com.seleniumtests.controller.Assertion.assertTrue;

/**
 * Exposes services provided by Shopping Cart page
 * <p/>
 * User: tbhadauria
 * Date: 4/10/13
 * Time: 11:05 AM
 */
public class XOShoppingCartPage extends WebPage {

    private static Button placeOrderButton = new Button("Place Order Button", By.id("ptc_shopcart_pc_top"));

    public XOShoppingCartPage() throws Exception {
        super();
    }

    private final Label lblOrderTotal = new Label("Order Total Amount", By.xpath("//span[contains(.,'Order total')]/following-sibling::span"));
    private final Link lnkShopMore = new Link("Shop More Link", By.xpath("//a[contains(.,'Shop More')]"));
    private final CheckBox chkItem = new CheckBox("Item Check Box", By.cssSelector("span[class $= 'chk-med']"));
    private final TextField txtItemQuantity = new TextField("Item Quantity", By.cssSelector("input[id ^= 'qty']"));
    private final Link lnkRemoveLink = new Link("Remove Link", By.cssSelector("a[id ^= 'rmlnk']"));
    private final Link lnkItemTitle = new Link("Item Title Link", By.cssSelector("ul[class='xo-itm-grid']>li:nth-child(3) a[href *= 'ViewItem&item=']"));
    private final Label lblEmptyCart = new Label("Empty Cart Message", By.cssSelector(".xo-no-cart"));
    private final Link lnkStartShopping = new Link("Start Shopping Link", By.xpath("//div[@class='xo-no-cart']/a[.='Start Shopping']"));
    private final Label lblItemAddRemoveMessage = new Label("New Item Addition Message", By.cssSelector("#sc_usr_action .sm-md:nth-child(3)"));
    private final Label lblNoItemSelected = new Label("No Item selected msg", By.xpath("//p[@class='sm-md'][contains(.,'Please select at least one item to place order.')]"));

    // Page Refresh element appears when updating item quantity
    private final Label pageRefresh = new Label("Page Refresh", By.cssSelector("body[class='xo-oncall']"));

    public CheckBox getItemCheckBox(String itemTitle) {
        return new CheckBox("Item Checkbox", By.xpath("//ul[descendant::div[@title = '" + itemTitle + "']]//span[contains(@class, 'chk')]"));
    }

    public TextField getQuantityTextboxForItem(String itemTitle) {
        return new TextField("Item Quantity Text box", By.xpath("//ul[descendant::a[contains(text(),'" + itemTitle + "')]]//input[contains(@id, 'qty')]"));
    }

    public Link getUpdateLinkForItem(String itemTitle) {
        return new Link("Quantity Update Link", "//ul[descendant::a[contains(text(),'" + itemTitle + "')]]//a[@class='xo-upa']");
    }

    public Link getRemoveItemLink(String itemTitle) {
        return new Link("Remove Link", By.xpath("//div[contains(@class, 'xo-slritm')]" +
                "[descendant::a[contains(text(),'" + itemTitle + "')]]//a[contains(.,'Remove')]"));
    }

    public String getItemPrice(String itemTitle) {
        return new Label("Item Price", By.xpath("//ul[descendant::a[contains(text(),'" + itemTitle + "')]]" +
                "//div[contains(@class,'xo-price')]/span[2]")).getText();
    }

    public double getItemShippingCharges(String itemTitle) {
        return Double.valueOf(new Label("Shipping Charges", By.xpath("//ul[descendant::a[contains(text(),'" + itemTitle + "')]]" +
                "//div[contains(@class,'itm-shcst')]/span[2]")).getText());
    }

    public String getFreeShippingValueForItem(String itemTitle) {
        return new Label("Shipping Charges", By.xpath("//ul[descendant::a[contains(text(),'" + itemTitle + "')]]" +
                "//div[contains(@class,'itm-shcst')]/span")).getText();
    }

    public String getSellerNameForItem(String itemTitle) {
        return new Label("Seller", By.xpath("//div[@class='xo-slr1'][descendant::a[contains(.,'"+itemTitle+"')]]" +
                "/preceding-sibling::div[1]//a")).getText().split("\\(")[0].trim();
    }

    public BigDecimal getOrderTotalAmount() {
        return new BigDecimal(lblOrderTotal.getText().split("Rs.")[1].replaceAll(",", "").trim())
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public XOShoppingCartPage selectAllItems() {
        List<WebElement> itemCheckBoxes = driver.findElements(By.cssSelector("span[class *= 'chk-off']"));
        for (WebElement itemCheckBox : itemCheckBoxes) {
            itemCheckBox.click();
        }
        return this;
    }

    public XOShoppingCartPage unselectAllItems() {
        List<WebElement> itemCheckBoxes = driver.findElements(By.cssSelector("span[class *= 'chk-on']"));
        for (WebElement itemCheckBox : itemCheckBoxes) {
            itemCheckBox.click();
        }
        return this;
    }

    public XOShoppingCartPage updateItemQuantity(String itemTitle, int itemQuantity) {
        getQuantityTextboxForItem(itemTitle).clear();
        getQuantityTextboxForItem(itemTitle).sendKeys(String.valueOf(itemQuantity));
        getUpdateLinkForItem(itemTitle).click();
        waitForElementToDisappear(pageRefresh);
        return this;
    }

    public XOShoppingCartPage removeItem(String itemTitle) {
        getRemoveItemLink(itemTitle).click();
        waitForElementToDisappear(pageRefresh);
        return this;
    }

    public XOShoppingCartPage removeAllItems() {
        int removeLinkCount = WebUXDriver.getWebDriver().findElements(By.linkText("Remove")).size();
        for (int i = 1; i <= removeLinkCount; i++) {
            WebUXDriver.getWebDriver().findElement(By.linkText("Remove")).click();
        }
        return this;
    }

    public XOReviewOrderPage placeOrder() throws Exception {
        placeOrderButton.click();
        waitForElementToBeVisible(XOReviewOrderPage.getChangeDeliveryAddressLink());
        return new XOReviewOrderPage();
    }

    public XOShoppingCartPage verifyItemTitle(String expectedItemTitle) {
        assertEquals(lnkItemTitle.getText(), expectedItemTitle, "Item title is wrong");
        return this;
    }

    /**
     * Verifies cart order total
     * This price does not include shipping cost
     *
     * @param expectedOrderTotal
     * @return
     */
    public XOShoppingCartPage verifyCartOrderTotal(BigDecimal expectedOrderTotal) {
        assertTrue(getOrderTotalAmount().compareTo(expectedOrderTotal) == 0, "Order Total is incorrect");
        return this;
    }

    public XOShoppingCartPage verifyEmptyCartMessage() {
        assertEquals(lblEmptyCart.getText(), "Your shopping cart is empty, but it doesn't have to be.\n" +
                "There are a lot of great deals waiting for you. Start shopping and look for the \"add to cart\" button.\n" +
                "You can add several items to your cart from different seller and pay for them at once.\n" +
                "Start Shopping", "Incorrect empty shopping cart message");
        return this;
    }

    public XOShoppingCartPage verifyStartShoppingLink() {
        assertTrue(lnkStartShopping.isElementPresent(), "Start Shopping Link is missing on empty cart page");
        return this;
    }

    /**
     * Verifies total number of items available in cart
     *
     * @param expectedItemCount
     * @return
     */
    public XOShoppingCartPage verifyTotalItemCount(int expectedItemCount) {
        assertEquals(WebUXDriver.getWebDriver().findElements(By.cssSelector("ul[class *='xo-itm']")).size(), expectedItemCount,
                "Item count is wrong");
        return this;
    }

    public XOShoppingCartPage verifyItemAdditionMessage(String itemTitle) {
        assertEquals(lblItemAddRemoveMessage.getText(), itemTitle + " was just added to your cart",
                "Wrong item addition message");
        return this;
    }

    public XOShoppingCartPage verifyItemRemovalMessage(String itemTitle) {
        assertEquals(lblItemAddRemoveMessage.getText(), itemTitle + " was just removed from your cart",
                "Wrong item removal message");
        return this;
    }

    public XOShoppingCartPage clickItemCheckBox(String itemTitle) {
        getItemCheckBox(itemTitle).click();
        waitForElementToDisappear(pageRefresh);
        return this;
    }

    public XOShoppingCartPage verifyNoItemSelectionMessage() {
        assertTrue(lblNoItemSelected.isDisplayed(), "No Item selected message is missing after unchecking all items");
        return this;
    }

    public XOShoppingCartPage waitForPageRefresh() {
        waitForElementToDisappear(pageRefresh);
        return this;
    }

    public static void main(String[] args) {
        System.out.println(new BigDecimal(Double.valueOf("7000.026")).setScale(2, BigDecimal.ROUND_HALF_UP));
        System.out.println(new BigDecimal(7000.001).setScale(2, BigDecimal.ROUND_HALF_UP));
        System.out.println(new BigDecimal(7000.289).setScale(2, BigDecimal.ROUND_HALF_UP));
        System.out.println(new BigDecimal("7000.00"));




    }



}