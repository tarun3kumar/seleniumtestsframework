package com.seleniumtests.webpage;

import static com.seleniumtests.controller.Assertion.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import com.seleniumtests.webpage.PGIBillerRequestHandlerpage;
import org.openqa.selenium.By;
import org.testng.Reporter;

import com.seleniumtests.controller.Assertion;
import com.seleniumtests.driver.web.element.Button;
import com.seleniumtests.driver.web.element.HtmlElement;
import com.seleniumtests.driver.web.element.Label;
import com.seleniumtests.driver.web.element.Link;
import com.seleniumtests.driver.web.element.RadioButton;
import com.seleniumtests.driver.web.element.TextField;
import com.seleniumtests.driver.web.element.WebPage;

/**
 * Encapsulate operation of Payment page
 * <p/>
 * User: tbhadauria
 * Date: 7/10/13
 * Time: 4:42 PM
 */
public class XOPaymentPage extends WebPage {

    private static Link lnkReviewOrder = new Link("Review Order Link", By.xpath("//div[@id='paymentRoContent']//a[.='Review order']"));

    public XOPaymentPage() throws Exception {
        super(lnkReviewOrder);
    }

    private static Label lblPurchaseAmount = new Label("Purchase Amount", By.cssSelector(".xo-p-m"));

    // Debit card payment
    private static Link lnkDebitCard = new Link("Debit Card", By.xpath("//a[.='Debit Card']"));
    private static Button btnMasterAtmDebitCard = new Button("Master ATM Debit Card", By.xpath("//button[contains(.,' Maestro/ATM Debit Cards')]"));
    private static RadioButton lblSBIMaestroCard = new RadioButton("State Bank of India Maestro Label",
            By.xpath("//button//following-sibling::ul/li[contains(.,'State Bank of India')][1]"));
    private static RadioButton btnICICIDebitCard =
            new RadioButton("ICICI Debit Card Radio Button", By.cssSelector("ul[data-tabid = '1'] .xo-bicn[title='ICICI Bank']>span:nth-child(1)"));
    private static RadioButton btnPNBDebitCard =
            new RadioButton("PNB Debit Card Radio Button", By.cssSelector("ul[data-tabid = '1'] .xo-bicn[title='Punjab National Bank']>span:nth-child(1)"));
    private static RadioButton btnVisaDebitCard =
            new RadioButton("Visa Debit Card Radio Button", By.cssSelector("ul[data-tabid = '1'] .xo-bicn[title='Debit Card']>span:nth-child(1)"));
    private static RadioButton btnMaestroATMDebitCard =
            new RadioButton("MaestroATMDebitCard", By.xpath("//ul[@data-tabid = '1']/li[3]/span[1]"));



    // Net Banking payment
    private static Link lnkNetBanking = new Link("Net Banking", By.xpath("//a[.='Netbanking']"));
    private static RadioButton btnICICINetBanking =
            new RadioButton("ICICI Debit Card Radio Button", By.cssSelector("ul[data-tabid = '2'] .xo-bicn[title='ICICI Bank']>span:nth-child(1)"));
    private static RadioButton btnPNBNetBanking =
            new RadioButton("PNB Radio Button", By.cssSelector("ul[data-tabid = '2'] .xo-bicn[title='Punjab National Bank']>span:nth-child(1)"));

    // Credit card payment
    private static Link lnkCreditCard = new Link("Credit Card", By.xpath("//a[.='Credit Card']"));
    private static RadioButton btnVisaCreditCard = new RadioButton("Visa Credit Card", By.cssSelector("li[title='ICICI FD']>span:nth-child(1)"));
    private static RadioButton btnAmericanExpress = new RadioButton("American Express", By.cssSelector("li[title='American Express']>span:nth-child(1)"));
    private static RadioButton btnAmericanExpressEZEClick = new RadioButton("Amercian Express EZE Click", By.cssSelector("li[title='American Express ezeClick']>span:nth-child(1)"));
    private static TextField txtCreditCardNumber = new TextField("Credit Card Number", By.id("txtCName3"));
    private static TextField txtCardHolderName = new TextField("Credit Card Holder Name", By.id("txtCHName3"));
    private static TextField txtValidTillMonth = new TextField("Credit Card Valid Till Month", By.id("txtMnth3"));
    private static TextField txtValidTillYear = new TextField("Credit Card Valid Till Year", By.id("txtYear3"));
    private static TextField txtCVVNumber = new TextField("Credit Card CVV Number", By.id("txtVNo3"));
    private static Link lnkCashOnDelivery = new Link("Cash On Delivery", By.xpath("//a[.='Cash On Delivery']"));

    // EMI payment
    private static Link lnkEMICreditCard = new Link("EMI Credit Card", By.xpath("//a[.='EMI - Credit Card']"));

    //EMI ICICI Bank
    private static RadioButton btnICICIEMICreditCard = new RadioButton("ICICI EMI Credit Card",
            By.cssSelector("ul[data-onselect='emiSelected']>li[title='ICICI Bank']>span:nth-child(1)"));
    // When using this locator, fetch the collection of WebElements for 3/6/9/12 months EMI and use appropriate WebElement
    private static By btnICICIInstallmentPeriod = By.cssSelector("li[tabindex='3'][data-bank='ICICI Bank']>span:nth-child(1)");

    private static RadioButton btnICICI_3M = new RadioButton("ICICI_3M EMI Credit Card" ,By.xpath("//li[@tabindex='3'][@data-bank='ICICI Bank']/span[1][following::span[text()='3']]"));
    private static RadioButton btnICICI_6M = new RadioButton("ICICI_6M EMI Credit Card" ,By.xpath("//li[@tabindex='3'][@data-bank='ICICI Bank']/span[1][following::span[text()='6']]"));
    private static RadioButton btnICICI_9M = new RadioButton("ICICI_9M EMI Credit Card" ,By.xpath("//li[@tabindex='3'][@data-bank='ICICI Bank']/span[1][following::span[text()='9']]"));
    private static RadioButton btnICICI_12M = new RadioButton("ICICI_12M EMI Credit Card" ,By.xpath("//li[@tabindex='3'][@data-bank='ICICI Bank']/span[1][following::span[text()='12']]"));

    //EMI AXIS Bank
    private static RadioButton btnAXISEMICreditCard = new RadioButton("AXIS EMI Credit Card",
            By.cssSelector("ul[data-onselect='emiSelected']>li[title='AXIS']>span:nth-child(1)"));

    private static RadioButton btnAXIS_3M = new RadioButton("AXIS_3M EMI Credit Card", By.xpath("//li[@tabindex='3'][@data-bank='AXIS']/span[1][following::span[text()='3']]"));;
    private static RadioButton btnAXIS_6M = new RadioButton("AXIS_6M EMI Credit Card", By.xpath("//li[@tabindex='3'][@data-bank='AXIS']/span[1][following::span[text()='6']]"));;
    private static RadioButton btnAXIS_9M = new RadioButton("AXIS_9M EMI Credit Card", By.xpath("//li[@tabindex='3'][@data-bank='AXIS']/span[1][following::span[text()='9']]"));;
    private static RadioButton btnAXIS_12M = new RadioButton("AXIS_12M EMI Credit Card", By.xpath("//li[@tabindex='3'][@data-bank='AXIS']/span[1][following::span[text()='12']]"));;

    //EMI SCB Bank
    private static RadioButton btnSCBEMICreditCard = new RadioButton("SCB EMI Credit Card",
            By.cssSelector("ul[data-onselect='emiSelected']>li[title='SCB']>span:nth-child(1)"));

    private static RadioButton btnSCB_3M = new RadioButton("SCB_3M EMI Credit Card", By.xpath("//li[@tabindex='3'][@data-bank='SCB']/span[1][following::span[text()='3']]"));;
    private static RadioButton btnSCB_6M = new RadioButton("SCB_6M EMI Credit Card", By.xpath("//li[@tabindex='3'][@data-bank='SCB']/span[1][following::span[text()='6']]"));;
    private static RadioButton btnSCB_9M = new RadioButton("SCB_9M EMI Credit Card", By.xpath("//li[@tabindex='3'][@data-bank='SCB']/span[1][following::span[text()='9']]"));;
    private static RadioButton btnSCB_12M = new RadioButton("SCB_12M EMI Credit Card", By.xpath("//li[@tabindex='3'][@data-bank='SCB']/span[1][following::span[text()='12']]"));;

    //EMI CITI Bank
    private static RadioButton btnCITIEMICreditCard = new RadioButton("CITI EMI Credit Card",
            By.cssSelector("ul[data-onselect='emiSelected']>li[title='Citibank']>span:nth-child(1)"));

    private static RadioButton btnCITI_3M = new RadioButton("CITI_3M EMI Credit Card", By.xpath("//li[@tabindex='3'][@data-bank='Citibank']/span[1][following::span[text()='3']]"));;
    private static RadioButton btnCITI_6M = new RadioButton("CITI_6M EMI Credit Card", By.xpath("//li[@tabindex='3'][@data-bank='Citibank']/span[1][following::span[text()='6']]"));;


    //EMI HDFC Bank
    private static RadioButton btnHDFCEMICreditCard = new RadioButton("HDFC EMI Credit Card",
            By.cssSelector("ul[data-onselect='emiSelected']>li[title='HDFC']>span:nth-child(1)"));

    private static RadioButton btnHDFC_3M = new RadioButton("HDFC_3M EMI Credit Card", By.xpath("//li[@tabindex='3'][@data-bank='HDFC']/span[1][following::span[text()='3']]"));;
    private static RadioButton btnHDFC_6M = new RadioButton("HDFC_6M EMI Credit Card", By.xpath("//li[@tabindex='3'][@data-bank='HDFC']/span[1][following::span[text()='6']]"));;
    private static RadioButton btnHDFC_9M = new RadioButton("HDFC_9M EMI Credit Card", By.xpath("//li[@tabindex='3'][@data-bank='HDFC']/span[1][following::span[text()='9']]"));;
    private static RadioButton btnHDFC_12M = new RadioButton("HDFC_12M EMI Credit Card", By.xpath("//li[@tabindex='3'][@data-bank='HDFC']/span[1][following::span[text()='12']]"));;

    // Other payment
    private static Link lnkOthers = new Link("Others Payment", By.xpath("//a[.='Others']"));
    private static RadioButton btnAirtelMoney = new RadioButton("Airtel Money", By.xpath("//li[descendant::span[.='Airtel Money']]/span[1]"));

    private static Button btnPayNow = new Button("Pay now", By.xpath("//button[.='Pay now'][not(@disabled)]"));
    private static Button btnConfirmCODPayment = new Button("Confirm COD", By.xpath("//button[.='Confirm']"));

    private static Label lblEMICharge = new Label("EMI Charge", By.id("spnEmiCharge"));
    private static Label lblEMIMonths = new Label("EMI Months", By.id("spnMnth"));
    private static Label lblEMIconvenienceCharge  = new Label("EMI convenience charge", By.id("spnEmiFee"));

    private String EMIchargedMessage = "You will charged an EMI of Rs. %s for next %s months";
    private String EMIconvenienceChargeMessage = "An additional Rs.%s will be charged as bank convenience charges ";

    public static Label lblPaymentErrorMessage = new Label("Error message", By.xpath("//div[@class='xo-err-sdiv']//p[@class='xo-err']"));


    public double getPurchaseAmount() {
        return Double.valueOf(lblPurchaseAmount.getText().replaceAll(",", ""));
    }

    public XOPaymentPage clickDebitCardTab() {
        lnkDebitCard.click();
        return this;
    }

    public XOPaymentPage selectStateBankOfIndiaDebitCardRadioButton() {
        clickDebitCardTab();
        lblSBIMaestroCard.click();
        return this;
    }

    /**
     * Pay Now would be enabled only after selecting a payment method
     *
     * @return
     */
    public PGIBillerRequestHandlerpage payNow() throws Exception {
        waitForElementToBeVisible(btnPayNow);
        btnPayNow.click();
        try {
            acceptAlert();
        } catch (Exception  e) {
            e.printStackTrace();
            Reporter.log("No alert is present on Bill Desk Emulator");
        }
        return new PGIBillerRequestHandlerpage();
    }

    public XOPaymentPage selectDebitCard() {
        lnkDebitCard.click();
        return this;
    }

    public XOPaymentPage selectMaestroATMCard() {
        btnMasterAtmDebitCard.click();
        return this;
    }

    public XOPaymentPage selectNetBanking() {
        lnkNetBanking.click();
        return this;
    }

    public XOPaymentPage selectCreditCard() {
        lnkCreditCard.click();
        return this;
    }

    public XOPaymentPage selectCODPayment() {
        lnkCashOnDelivery.click();
        return this;
    }

    public XOPaymentPage selectEMICreditCard() {
        lnkEMICreditCard.click();
        return this;
    }

    public XOPaymentPage selectOthersPayment() {
        lnkOthers.click();
        return this;
    }

    public XOPaymentPage selectSBIDebitCard() {
        lblSBIMaestroCard.click();
        return this;
    }

    public XOPaymentPage selectPNBNetBanking() {
        btnPNBNetBanking.click();
        return this;
    }

    public XOPaymentPage selectVISACreditCard() {
        btnVisaCreditCard.click();
        return this;
    }

    public XOPaymentPage selectAmericanExpressCreditCard() {
        btnAmericanExpress.click();
        return this;
    }

    private XOPaymentPage selectAmericanExpressEZEClick() {
        btnAmericanExpressEZEClick.click();
        return this;
    }

    public XOPaymentPage enterCreditCardNumber(String creditCardNumber) {
        waitForElementToBeVisible(txtCreditCardNumber);
        txtCreditCardNumber.sendKeys(creditCardNumber);
        return this;
    }

    public XOPaymentPage enterCardHolderName(String creditCardHolderName) {
        txtCardHolderName.sendKeys(creditCardHolderName);
        return this;
    }

    public XOPaymentPage enterValidTillMonth(String validTillMonth) {
        txtValidTillMonth.sendKeys(validTillMonth);
        return this;
    }

    public XOPaymentPage enterValidTillYear(String validTillYear) {
        txtValidTillYear.sendKeys(validTillYear);
        return this;
    }

    public XOPaymentPage enterCVVNumber(String cvvNumber) {
        txtCVVNumber.sendKeys(cvvNumber);
        return this;
    }

    public XOPaymentPage selectICICIEMIRadioButton() {
        btnICICIEMICreditCard.click();
        return this;
    }

    public XOPaymentPage select3MonthsICICIInstallmentPeriod() {
        getDriver().findElements(btnICICIInstallmentPeriod).get(0).click();
        return this;
    }

    public XOPaymentPage selectAirtelMoney() {
        btnAirtelMoney.click();
        return this;
    }

    public PGIBillerRequestHandlerpage makeSBIDebitCardPayment() throws Exception {
        return selectDebitCard()
                .selectMaestroATMCard()
                .selectSBIDebitCard()
                .payNow();
    }

    public PGIBillerRequestHandlerpage makePNBNetBankingPayment() throws Exception {
        return selectNetBanking()
                .selectPNBNetBanking()
                .payNow();
    }

    public XOPaymentPage makePNBNetBankingPaymentExceedingAllowableAmount() throws Exception {
        selectNetBanking()
                .selectPNBNetBanking();
        btnPayNow.click();
        return this;
    }

    public PGIBillerRequestHandlerpage makeVisaCreditCardPayment(String creditCardNumber, String cardHolderName,
                                                                 String validTillMonth, String validTillYear,
                                                                 String cvvNumber) throws Exception {
        return  selectCreditCard()
                .selectVISACreditCard()
                .enterCreditCardNumber(creditCardNumber)
                .enterCardHolderName(cardHolderName)
                .enterValidTillMonth(validTillMonth)
                .enterValidTillYear(validTillYear)
                .enterCVVNumber(cvvNumber)
                .payNow();
    }

    public PGIBillerRequestHandlerpage makeAmericanExpressCreditCardPayment() throws Exception {
        return  selectCreditCard()
                .selectAmericanExpressCreditCard()
                .payNow();
    }

    public PGIBillerRequestHandlerpage makeAmericanExpressEZEClickPayment() throws Exception {
        return selectCreditCard()
                .selectAmericanExpressEZEClick()
                .payNow();
    }

    public PGIBillerRequestHandlerpage makeICICI3MonthsCreditCardPayment() throws Exception {
        selectEMICreditCard()
                .selectICICIEMIRadioButton();
        getDriver().findElements(btnICICIInstallmentPeriod).get(0).click();
        return  select3MonthsICICIInstallmentPeriod()
                .payNow();
    }

    public PGIBillerRequestHandlerpage makeAirtelMoneyPayment() throws Exception {
        return selectOthersPayment()
                .selectAirtelMoney()
                .payNow();
    }

    public XOPaymentPage verifyExceedingAllowableAmountErrorMessage() {
        Assertion.assertTrue(isElementPresent(By.xpath("//p[contains(.,'Sorry, your payment was not successful. Please try again.')]")),
                "No Error message appears for checkout exceeding 5L amount");
        return this;
    }

    public XOPaymentPage verifySelectedPaymentMethodIsAmericanExpress() {
        Assertion.assertTrue(isElementPresent(By.xpath("//li[@title='American Express'][descendant::span[@class='gspr radio-med radio-on']]")),
                "American express payment not selected");
        return this;
    }

    public String getEMIchargedMessage() {
        return String.format(EMIchargedMessage, lblEMICharge.getText().replaceAll(",", "").trim(),
                lblEMIMonths.getText().replaceAll(",", "").trim());
    }

    public String getEMIconvenienceMessage() {
        return String.format(EMIconvenienceChargeMessage, lblEMIconvenienceCharge.getText().replaceAll(",", "").trim());
    }

    public XOPaymentPage verifyEMIChargedMessage(BigDecimal EMIcharge, String months) {
        assertEquals(getEMIchargedMessage(),
                String.format(EMIchargedMessage, EMIcharge, months),
                "EMI amount and Months message is incorrect");
        return this;
    }

    public XOPaymentPage verifyEMIconvenienceChargeMessageText(BigDecimal EMIconvenienceCharge) {
        assertEquals(getEMIconvenienceMessage(),
                String.format(EMIconvenienceChargeMessage, EMIconvenienceCharge),
                "EMI convenience charge message is incorrect");
        return this;
    }

    public XOPaymentPage verifyPaymentMethodSelection(HtmlElement paymentMethod) {
        Assertion.assertTrue(paymentMethod.getAttribute("class").equals("gspr radio-med radio-on"),
                "Wrong payment method selected") ;
        return this;

    }

    public XOPaymentPage verifyPaymentMethodIsDisabled(HtmlElement paymentMethod) {
        Assertion.assertTrue(paymentMethod.getAttribute("class").equals("gspr radio-med radio-off"),
                "Payment method is enabled") ;
        return this;

    }

    public XOPaymentPage verifyMaestroATMDebitCardIsDisabled() {
        HtmlElement eleMaestroATMDebitCard = new HtmlElement("MaestroATMDebitCard", By.xpath("//li[descendant::span[text()='Maestro/ATM Debit Cards']]"));
        Assertion.assertEquals(eleMaestroATMDebitCard.getAttribute("class"), "xo-bicn xo-inactive", "MaestroATMDebitCard is not disabled");
        return this;
    }

    public XOPaymentPage verifyVISADebitCardIsDisabled() {
        HtmlElement eleVISADebitCard = new HtmlElement("Visa debit card", By.xpath("//ul[@data-tabid='1']/li[1]"));
        Assertion.assertEquals(eleVISADebitCard.getAttribute("class"), "xo-bicn xo-inactive", "VISADebitCard is not disabled");
        return this;
    }

    public XOPaymentPage verifySelectedDebitCard(String bankName) {
        HtmlElement eleDebitcard = new HtmlElement("debit card", By.xpath("//ul[@data-tabid='1']/li[3]/span[2]//span"));
        Assertion.assertEquals(eleDebitcard.getText().trim(), bankName, "Payment method : " + bankName + " is not selected");
        return this;
    }

   public static BigDecimal calculateEMI(BigDecimal totalOrderAmount, String months) {
        return totalOrderAmount.divide(new BigDecimal(months), 2, BigDecimal.ROUND_HALF_UP);
    }

   public String getPaymentErrorMessage() {
        return lblPaymentErrorMessage.getText().trim();
    }

    public int getSelectedBanksCount() {
        return driver.findElements(By.xpath("//span[@class='gspr radio-med radio-on']")).size();

    }

    public XOPaymentPage verifyTotalBanksSelectedCount(int expectedCount) {
        Assertion.assertEquals(getSelectedBanksCount(), expectedCount, "Total banks selected  mismacth");
        return this;
    }

    public XOPaymentPage verifyPayNowButtonIsDisabled() {
        Assertion.assertFalse(btnPayNow.isElementPresent(), "Pay Now Button is not disabled");
        return this;
    }

}
