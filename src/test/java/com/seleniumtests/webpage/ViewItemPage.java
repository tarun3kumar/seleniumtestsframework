package com.seleniumtests.webpage;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.seleniumtests.controller.Keyword;
import com.seleniumtests.controller.Logging;
import com.seleniumtests.driver.web.WebUXDriver;
import com.seleniumtests.driver.web.element.Button;
import com.seleniumtests.driver.web.element.Image;
import com.seleniumtests.driver.web.element.Label;
import com.seleniumtests.driver.web.element.Link;
import com.seleniumtests.driver.web.element.SelectList;
import com.seleniumtests.driver.web.element.Table;
import com.seleniumtests.driver.web.element.TextField;
import com.seleniumtests.driver.web.element.WebPage;
import com.seleniumtests.helper.ThreadHelper;

/**
 * Page: ViewItem Page
 * @Payload eBay Payload
 * @author Binfeng Shen, Sam Zhang
 * @date 2009/12/10
 */
public class ViewItemPage extends WebPage{

//    public static final String PAGE_URL = "http://cgi.qa.ebay.com/ws/eBayISAPI.dll?ViewItem&item=";
	public static final String PAGE_URL = "http://cgi.ebay.in/ws/eBayISAPI.dll?ViewItem&item=";

    private static final Link lnkPageIdentifier = new Link("ViewItemPageIdentifier", "//a[@id='payIdLink' or @id= 'a_payId' or @id='vi_tabs_ta_1' or @id='but_v4-4' or @id='but_v4-3' or contains(@href,'CheckoutProcessor')]");

    public final String SOJ_18_EPS = "18  -  EPS:ViewItemFlags";

    public final String SOJ_43_WINNER = "43  -  WINNER:ViewItemFlags";

    public final String SOJ_86_IS_RETURN_POLICY = "86  -  IS_RETURN_POLICY:ViewItemFlags";
    public final String SOJ_88_IS_QANDA = "88  -  IS_QANDA";
    public final String SOJ_131_ZOOM_DISPLAYED = "131  -  ZOOM_DISPLAYED:ViewItemFlags";
    public final Link lnkBackTo = new Link("BackTo", "//span[@id='ngviback']/a");
    public final Link lnkShippay = new Link("ShippingPaymentTab", "//a[@id='vi_tabs_ta_2']/h2");

    public final Link FBConnectSignIn = new Link("FBConnectSignInLink", "//form[@id='facebookConnect']/div/input");
    public final Link lnkShowMeHow = new Link("ShowMeHow", "//a[@id='couponshowmehow']"){
        @Override
        public void click()
        {
            super.captureSnapshot("before");
            super.fireEvent("mouseover");
            waitForSeconds(1);
            super.fireEvent("click");
            //session.clickAt(getLocator(), "1,1");
            waitForSeconds(2);
            super.captureSnapshot("after");
        }
    };

    public final SelectList selCountry = new SelectList("ChangeCountry", "//select[@id='shCountry']");

    public final Button btnINBIN = new Button("btnBuyItNow","//a[contains(text(),'Buy It Now')]");
    public final Button lnkBuyanother = new Button("lnkBuyanother","//a[contains(text(),'Buy another')]");

    public final TextField txtShipZipCode = new TextField("ShippingZipCode","//input[@id='shPostalCode']");

    public final TextField FBUserlogin = new TextField("FBuserlogin","//input[@id='email']");
    public final TextField FBUserloginpassword = new TextField("FBuserloginpassword","//input[@id='pass']");
    public final Button FBLoginButton = new Button("FBuserloginbutton","//input[@name='login'] or //div[@id='dialog_buttons']/label[1]/input");
    public final Button btnGetRates = new Button("ShippingRates","//input[@id='shGetRates']");
    public final Table tblShippingService = new Table("ShippingServiceTable", "//div[@id='ngviShipService']/table");
    public final Link lnkMakeSecondChanceOffer = new Link("MakeSecondChanceOffer","//a[contains(@href,'PersonalOfferLogin')]");

    public final Table tblItemInfo = new Table("ItemInfoTable", "//form[contains(@action, 'MakeBid')]/table");

    public final TextField txtQuantity = new TextField("Quantity","//input[@id='v4-25' or @id='v4-27' or @id='v4-28' or @class='vi-is1-mqtyTb' or @id='v4-32qtyId' or contains(@id, 'qtyId')]") ;

    public final Link lnkAskAQuestion = new Link("AskAQuestion", "//a[contains(@href,'ShowSellerFAQ') or contains(@href, 'ShowCoreAskSellerQuestion')]");

    public final Link lnkAnswerQuestion = new Link("AnswerQuestion", "//a[contains(@href,'RespondToQuestion')]");

    /**
     * See all Options link open up Payment layer on NGVI
     */
    public final Link lnkSeeAllOptions = new Link("PayIdLink", "//a[@id='payIdLink']");

    // page elements related to shipping layer on VI (PC 5224)
    /**
     * Read return policy detail link open up Return policy layer on NGVI
     */
    public final Link lnkReadDetails = new Link("ReadDetails", "//a[@id='readDetails']");
    /**
     * Link: Calculate Shipping Cost
     * calculate link shows up on initial page load, but once it is set, it no longer appears
     * @xpath //a[@id='shipCostCalLink']
     */
    public final Link lnkCalculate = new Link("Calculate","//a[@id='shipCostCalLink']") {
        @Override
        public void click() {
            super.click();
            ThreadHelper.waitForSeconds(2);
        }
    };

    /**
     * Link: View More Shipping Options<br>
     * Shows up for items with flat shipping, but not calculated shipping items
     * @xpath //a[@id='vml']
     */
    public final Link lnkViewMoreShippingOptions = new Link("ViewMoreShippingOptions","//a[@id='vml']") {
        @Override
        public void click() {
            super.click();
            ThreadHelper.waitForSeconds(2);
        }
    };

    /**
     * Link: Change Shipping Location Qty<br>
     * change location link shows up only after user has set the location via the 'calculate shipping' link
     * @xpath //a[@id='chngLocQtyLink']
     */
    public final Link lnkChangeShippingLocationQty = new Link("ChangeShippingLocationQty","//a[@id='chngLocQtyLink']");

    /**
     * Enter quantity textfield.  Shows up on shipping layer
     */
    public final TextField txtShippingQuantity = new TextField("ShippingQuantity", "//input[@id='clquantity']");

    /**
     * Textfield: Change Shipping Zip Code<br>
     * where user types to change their shipping zip code.  Shows up on shipping layer
     * @xpath clPostalCode
     */
    public final TextField txtShippingZipCode = new TextField("ShippingZipCode","//input[@id='zipCode']");

    /**
     * Button: confirm zip code input on shipping layer
     * @xpath //input[@id='zipSub']
     */
    public final Button btnShippingUpdate = new Button("ShippingUpdate","//input[@id='zipSub']") {
//		@Override
//		public void click() {
//			// this command hands; clickAt gets around it
//			super.clickAt( "1,1");			
//			ThreadHelper.waitForSeconds(2);
//		}
    };

    /**
     * Button: confirm zip code input on shipping layer
     * @xpath zipSub
     */
    public Button btnConfirmShippingZipCode = new Button("submit button to confirm zip on shipping layer","//input[@id='zipSub']") {

//		@Override
//		public void click() {
//			// this command hands; clickAt gets around it
//			super.clickAt( "1,1");
//			
//			// may take some time to retrieve results
//			ThreadHelper.waitForSeconds(2);
//		}
    };

    /**
     * Button: Make Offer
     * Log in as Buyer(For Buying the Item)
     * @xpath //a[@id='but_v4-3']
     */

    public final Button btnMakeOffer = new Button("btnMakeOffer","//a[@id='but_v4-3'or @id='but_v4-2']");

    /**
     * Button: Review Offer
     * Log in as Seller(For Reviewing the offer)
     * @xpath //a[contains(text(),'Review offer')]
     */
    public final Button btnReviewOffer = new Button("btnReviewOffer","//a[contains(text(),'Review offer')]");

    public final Image imgPaymentLayerPayPalLogo = new Image("PaymentLayerPayPalLogo", "//*[@id='payDet1']/div/img");

    public final Table tblPaymentMethod = new Table("PaymentMethodTable", "//*[@id='payId-ol']/table");

    /**
     * Return policy table on layer on NGVI
     */
    public final Table tblReturnPolicy = new Table("ReturnPolicyTable", "//*[@id='rdPanel_olp_pad']/div[2]/table");

    public final String kwdPlaceBid = Keyword.get("PlaceBid");


    public final String kwdIncreaseMaxBid = Keyword.get("IncreaseMaxBid");

    public final String kwdYourItemSoldFor = Keyword.get("YourItemSoldFor");
    public final String kwdYouAreNowWatching = Keyword.get("YouAreNowWatching");
    public final String kwdYourMaxBid = Keyword.get("YourMaxBid");
    // keywords related to one-click bid dialog
    public final String kwdOneClickBidLink = Keyword.get("OneClickBidLink");
    public final String kwdOneClickBidDialogSubmitLink = Keyword.get("OneClickBidDialogSubmitLink");

    public final String kwdOneClickBidDialogGoodNews = Keyword.get("OneClickBidDialogGoodNews");
    // keywords related to Deep SKU
    public final String kwdLimitedQuantityAvailable = Keyword.get("LimitedQuantityAvailable");
    public final String kwdViewPurchasedItem = Keyword.get("ViewPurchasedItem");

    public final String kwdMouseHereToZoomIn = Keyword.get("MouseHereToZoomIn");

    public final String kwdNowWatching = Keyword.get("NowWatching");

    // ASQ related
    public final String kwdNoQuestionsOrAnswers = Keyword.get("NoQuestionsOrAnswers");

    public final String kwdQuestionsAndAnswersAboutThisItem = Keyword.get("QuestionsAndAnswersAboutThisItem");

    /**
     * Link: View purchased item link<br>
     * This is an ELI (edit live items) feature.  To trigger this link, have a buyer<br>
     * purchase an item, that the seller modifies after that time.  When the buyer<br>
     * logs in and goes to VI, they will see this link
     * @xpath link=View purchased item
     */
    public final Link lnkViewPurchasedItemLink = new Link("ViewPurchasedItem","//a[@target='purchasedItem']");
    public final Link lnkReviseYourItem = new Link("ReviseYourItem","//a[contains(@href,'UserItemVerification&item=')]");

    /**
     * Link: 1-click bid link<br>
     * This link is only available in the last hour or so of an auction,<br>
     * and only if a buyer has previously bid.  Use DCT tools to set the <br>
     * end time of an auction to < 60 minutes to see this link on ViewItem<br>
     * @xpath //a[text()='" +  kwdOneClickBidLink + "']
     */
    public final Link lnkOneClickBid = new Link("OneClickBid","//a[contains(text(), '" +  kwdOneClickBidLink + "')]") {

//		@Override
//		public void click() {			
//			super.clickAt( "1,1");
//			ThreadHelper.waitForSeconds(2);
//		}
    };

    /**
     * Link: One-click bid submit link<br>
     * This link refers to the submit button for the one-click bid dialog<br>
     * spawned when the '1-click bid' link is clicked on VI<br>	 *
     * @xpath //a[text()='" +  kwdOneClickBidLink + "']
     */
    public final Link lnkOneClickBidDialogSubmit = new Link("OneClickBidDialogSubmit","//a[text()='" +  kwdOneClickBidDialogSubmitLink + "']") {
//		@Override
//		public void click() {
//			super.clickAt( "1,1");
//			ThreadHelper.waitForSeconds(2);
//		}

    };

    /**
     * TextField: Max Bid
     * @xpath //input[@id='maxbid']
     */
    public final TextField txtMaxBid = new TextField("MaxBid","//input[@name='maxbid']");

    /**
     * Button: Place Bid
     * @xpath //input[@type='submit' and @value='Place bid']
     */
    public final Button btnPlaceBid = new Button("PlaceBid","//input[@id='but_v4-7' or (@type='submit' and @value='" +  kwdPlaceBid + "')]");

    /**
     * Link; Increase max bid
     * @xpath //input[@type='submit' and @value='Increase max bid']
     */
    public final Link lnkIncreaseMaxBid = new Link("lnkIncreaseMaxBid","//a[contains(@href,'MakeBid')]");

    /**
     * Link: View Bid History
     * @xpath //a[contains(@href,'eBayISAPI.dll?ViewBids&item=')]
     */
    public final Link lnkViewBidHistory = new Link("ViewBidHistory","//a[contains(@href,'eBayISAPI.dll?ViewBids&item=')]"){
        public String getText() {
            return element.findElement(By.xpath("//a[contains(@href,'eBayISAPI.dll?ViewBids&item=')]")).getText() ;
        }
    };

    /**
     * Link: Send invoice
     * @xpath //a[contains(text(),'Send invoice')]
     */
    public final Link lnkSendinvoice = new Link("Send invoice","//a[contains(text(),'Send invoice') or contains(@href,'UnifiedCheckoutSellerUpdateDetails')]");

    /**
     * Label: Your Item Sold For
     * @Xpath none
     */
    public final Label lblYourItemSoldFor = new Label("YourItemSoldFor",""){
        @Override
        public boolean isTextPresent(String s) {
            return super.isTextPresent( kwdYourItemSoldFor + s);
        }
    };
    /**
     * For ECP
     */
    public final Table tblItemDetails = new Table("Item Content on VI","//table[@class='sp1']");
    public final Link lnkRelist = new Link("Relist","//a[contains(text(),'Relist')]");
    /**
     * Button: Watch this item
     * @xpath //*[@id="watchbutton"]
     */
    public final Button btnWatchThisItem = new Button("WatchThisItem","//*[@id='watchbutton']") {

//		@Override
//		public void click() {
//			// overriding this message because using default click is displaying odd behavior
//			// this clicks 'at' a certain point (1,1), relative to the upper-left corner of the component
//			super.captureSnapshot("before click");
//			super.clickAt( "1,1");
//		}	
    };

    /**
     * Link: Picture Zoom Button<br>
     * @xpath //span[@class='ict-zm']
     */
    public final Link lnkPictureZoomButton = new Link("'Zoom' button/link under ViewItem picture","//span[@class='ict-zm']");

    /**
     * Link: Picture Enlarge Button
     * @xpath //span[@class='ict-enl']
     */
    public final Link lnkPictureEnlargeButton = new Link("'Enlarge' button/link under ViewItem picture","//span[@class='ict-enl']");





    /**
     * SelectList: First MSKU Dropdown<br>
     * This element is only valid/visible for MSKU items.  <br>
     * This drop-down shows the first variation option
     * @xpath 63
     */
    public final SelectList selFirstMSKUDropdown = new SelectList("FirstMSKUDropdown",By.id("63")) {
        @Override
        public void selectByText(String optionLocator) {
            super.selectByText(optionLocator);
        }
    };

    /**
     * SelectList: Second MSKU Dropdown<br>
     * This element is only valid/visible for MSKU items.  <br>
     * This drop-down shows the second variation option<br>
     * Must have at least 2 variations to appear.
     * @xpath 4032
     */
    public final SelectList selSecondMSKUDropdown = new SelectList("SecondMSKUDropdown",By.id("4032")) {

        @Override
        public void selectByText(String optionLocator) {
            super.selectByText(optionLocator);
        }
    };

    /**
     * SelectList: Third MUSK Dropdown<br>
     * This element is only valid/visible for MSKU items.<br>
     * This drop-down shows the third variation to appear <br>
     * Must have at least 3 variations to appear.
     * @xpath 258048
     */
    public final SelectList selThirdMSKUDropdown = new SelectList("ThirdMSKUDropdown",By.id("258048")) {

        @Override
        public void selectByText(String optionLocator) {
            super.selectByText(optionLocator);
        }
    };

    /**
     * Button: Pay Now
     * @xpath but_v4-4
     *
     * <br><br>
     * <img src = "doc-files/testimg.GIF" />
     * <br><br>
     */
    public final Link lnkPayNow = new Link("PayNow","//a[@id='but_v4-4' or @id='but_v4-3' or contains(@href,'CheckoutProcessor')]");


    /**
     * Button: MSKU Buy It Now
     * @xpath //input[@type='submit' and @value='Buy It Now']
     */
    public final Button btnMSKUBuyItNow = new Button("MSKUBuyItNow","//input[@type='button' and @value='Buy It Now' or @value='Sofort-Kaufen']");

    public final Link lnkAddToCart = new Link("Add to Cart button", "//a[contains(@href,'ShopCartProcessor')]");


    /**
     * Link: Buy It Now
     * @xpath //a[contains(@href,'BinConfirm')]
     */
    public final Link lnkBuyitNow = new Link("BuyItNow","//a[contains(@href,'BinConfirm')] | //a[contains(@href, 'BinController')]");

    /**
     * Link: View Order Details
     * @xpath //a[contains(@href,'ViewPaymentStatus')]
     */
    public final Link lnkViewOrderDetails = new Link("ViewOrderDetails","//a[contains(@href,'ViewPaymentStatus')]");

    /**
     * Link: 'See other items' Link
     * @xpath //a[text()='See other items']
     */
    public final Link lnkSOI = new Link("SOI","//a[text()='See other items ']");
    /**
     * Image: Item Center Large Picture<br>
     * The large picture of this item
     * @xpath //td[@class='ipics-cell']//center//img
     */
    public final Image imgItemLargePicture = new Image("ItemLargePicture","//td[@class='ipics-cell']//center//img");
    /**
     * Link: Report Item Link
     * @xpath link=Report item
     */
    public Link lnkReportItem = new Link("ReportItem","link=Report item");
    //public final Link lnkSellOneLikeThis= new Link("SellOneLikethis","//div[@id='vi-bottom']/div[2]/a[2]");
    public final Link lnkSellOneLikeThis= new Link("SellOneLikethis","//a[contains(@href,'SellLikeItem')]");

    /**
     * Link: Add to list
     * @xpath //a[@n='Watch List' or @id='-99_ttl_addToList']
     */
    public final Link lnkAddToList = new Link("AddToList","//a[@n='Watch List' or @id='-99_ttl_addToList']");

    /**
     * Link: Add To List dropdown panel
     * @xpath //a[@id='img_addToList']
     */
    public final Link lnkAddToListDropdownPanel = new Link("OpenDropdownPanel","//a[@id='img_addToList']") {

//		@Override
//		public void click() {
//			capturePageSnapshot();
//			
//			super.click();
//			
//			super.clickAt( "1,1");	
//			//super.fireEvent("click");
//		}
    };

    /**
     * Link: Add to Gift Ideas
     * @xpath //a[@n='Gift Ideas']
     */
    public final Link lnkAddToGiftIdeas = new Link("AddToGiftIdeas","//a[@n='Gift Ideas']") {

        @Override
        public void click() {
            super.captureSnapshot("before");
            super.mouseOver();
            super.fireEvent("click");
            //super.clickAt( "1,1");
            ThreadHelper.waitForSeconds(2);
            super.captureSnapshot("after");
        }

    };
    /**
     * Link: Add to Research
     * @xpath //a[@n='Research']
     */
    public final Link lnkAddToResearch = new Link("AddToResearch","//a[@n='Research']") {

        @Override
        public void click() {
            super.captureSnapshot("before");
            super.mouseOver();
            super.fireEvent("click");
            ThreadHelper.waitForSeconds(2);
            super.captureSnapshot("after");
        }

    };

    /**
     * Link: Add to Wish List
     * @xpath //a[@n='Wish List']
     */
    public final Link lnkAddToWishList = new Link("AddToWishList","//a[@n='Wish List']") {

        @Override
        public void click() {
            super.captureSnapshot("before");
            super.mouseOver();
            //super.fireEvent("click");
            super.fireEvent("click");
            //super.clickAt( "1,1");
            ThreadHelper.waitForSeconds(2);
            super.captureSnapshot("after");
        }

    };

    /**
     * Link: Add to Add to a new list
     * @xpath //a[@n='addNew' or @id='new_ita_addToList']
     */
    public final Link lnkAddToNewList = new Link("AddToNewList","//a[@n='addNew' or @id='new_ita_addToList']")  {

        @Override
        public void click() {
            super.captureSnapshot("before");
            super.mouseOver();
            super.click();
            //super.fireEvent("click");
            //super.clickAt( "1,1");

        }

    };

    // FIX trying out a better xpath
    //public final Table tblAddToList = new Table("AddToListTable", "//*[@id='v4-22' or @id='v4-23' or @id='v4-26']/table");
    public final Table tblAddToList = new Table("AddToList table", "//form[contains(@action, 'MakeBid')]/table");

    /**
     * Textfield: Type a new list name here<br>
     * where user types to new List Name.  Shows up on Add To List layer
     * @xpath nLstTxt
     */
    public final TextField txtNewListName = new TextField("NewListName","//*[@id='nLstTxt']") ;

    /**
     * Button: Add To New List button on Add to New List layer
     * @xpath ad_btn
     */
    public final Button btnAddNewList = new Button("submit button to Add on Add to New List layer","//*[@id='ad_btn']") {
//		@Override
//		public void click() {
//			super.captureSnapshot();
//			// this command hands; clickAt gets around it
//			super.clickAt( "1,1");	
//			//super.fireEvent("click");
//			// may take some time to retrieve results
//			ThreadHelper.waitForSeconds(2);
//		}
    };

    /**
     * Link: Add to list Buyer panel
     * @xpath //a[@id='ldLink']
     */
    public final Link lnkAddToListBuyerPanel = new Link("AddToListBuyerPanel","//a[@id='ldLink']");

    /**
     * Link: Add to other list Buyer panel
     * @xpath //a[@id='otherLink']
     */
    public final Link lnkAddToOtherListBuyerPanel = new Link("AddToOtherListBuyerPanel","//a[@id='otherLink']");
    public final Link lnkSigninForMoreLists = new Link("lnkSigninForMoreLists","//a[@n='addNew' or @id='s_ita_addToList']");

    public final Link lnkWatchListBuyerPanelDropdown = new Link("WatchListBuyerPanelDropdown","//a[@id='id_-99']");

    /**
     * Refers to the 'Confirm Bid' button for the bidding layer that shows up on ViewItem
     * This bidding layer is controlled by PC 2966
     */
    public final Link lnkBidLayerConfirmBid = new Link("ConfirmBid", "//a[contains(text(), 'Confirm Bid')] | //input[@value='Confirm bid']") {

//		@Override
//		public void click() {
//			super.captureSnapshot();
//			// this command hangs; clickAt gets around it
//			super.clickAt( "1,1");	
//			// may take some time to retrieve results
//			ThreadHelper.waitForSeconds(2);
//		}
    };

    /**
     * Link: Send Invoice
     * @xpath //a[contains(text(),'Send invoice')] or //a[contains(text(),'Send information')]
     */
    public final Link lnkSendInvoice = new Link("Send Invoice", "//a[contains(text(),'Send invoice') or contains(text(),'Send information') or contains(text(),'Zahlungsinformationen senden')]");

    /**
     * Button: Buy It Now
     * Log in as Buyer(For Buying the Item)
     * @xpath //a[contains(text(),'Buy It Now')]
     */
    public final Button btnBIN = new Button("btnBuyItNow","//a[contains(text(),'Buy It Now') or contains(text(),'Sofort-Kaufen')]");

    public final Button btnConfirmBid = new Button("btnConfirmBid","//input[@id='BIN_button' or @name='BIN_button' or @value='Confirm Bid' or @value='Place bid']");
    public final Link lnkMarkAsShipped = new Link("MarkAsShipped", "//a[contains(text(),'Mark as shipped') or contains(@href,'Action%3AMSHP') or contains(text(),'Markieren als „verschickt“')]");


    public final Link lnkMarkAsPaymentReceived = new Link("MarkAsPaymentReceived", "//a[contains(text(),'Mark as payment received') or contains(@href,'Action%3AMPR') or contains(text(),'Als „Zahlung erhalten“ markieren')]");


    public ViewItemPage() throws Exception{
        super();//remove page identifier by Jojo
    }

    public ViewItemPage(String itemId) throws Exception{
        super(PAGE_URL + itemId);//remove page identifier by Jojo
    }
    public final Image imgEmilogo = new Image("Emi logo","//img[@alt='EMI Logo']");
    public final Image imgpaisapay = new Image("paisapay logo","//img[@alt='PaisaPay']");
    /**
     * Image: Item Small picture<br>
     * small picture of this item<br>
     * index is 1,2,3...
     * @param index
     * @xpath //table[@class='tg-tb tg-clp']//td["+index+"]//img
     * @sample ViewItemPage.imgItemPicture(1);
     */
    public final Image imgItemSmallPicture(int index){
        if(index <= 6){
            return new Image("ItemSmallPicture"+index,"//table[contains(@class,'tg-tb')]//tr[1]//td["+index+"]//img"){
                @Override
                public void click()
                {
                    super.mouseOver();
                    ThreadHelper.waitForSeconds(2);
                    super.captureSnapshot();
                }
            };
        }
        else {
            int picIndex = index - 6;
            return new Image("ItemSmallPicture"+index,"//table[contains(@class,'tg-tb')]//tr[2]//td["+picIndex+"]//img"){
                @Override
                public void click()
                {
                    super.mouseOver();
                    ThreadHelper.waitForSeconds(2);
                    super.captureSnapshot();
                }
            };
        }
    }

    /**
     * Page flow description: bid item steps
     * @param    amount:bid amount
     * @sample {@code ViewItemPage.placeBidSteps("20");}
     */
    public void placeBidSteps(String amount) {
        txtMaxBid.type(amount);
        btnPlaceBid.click();
    }

    /**
     * Page Flow Description: verify image change when you click different image
     * @param ImageNumber1
     * @param ImageNumber2
     */
    public void verifyImageChange(int ImageNumber1, int ImageNumber2) {

        imgItemSmallPicture(ImageNumber1).click();
        String imgSrcPreviewBefore = imgItemLargePicture.getAttribute("src");

        imgItemSmallPicture(ImageNumber2).click();
        String imgSrcPreviewAfter = imgItemLargePicture.getAttribute("src");

        Assert.assertFalse(imgSrcPreviewBefore.equals(imgSrcPreviewAfter),
                "Image Does Not Display Correctly When You Click An Image");

    }

    //Bucks image on VIP
    public final Image imgBucks = new Image("Bucks image", "//img[contains(@src,'/aw/pics/rewards/ebaybucks/imgBucks_35x13.gif')]");
    //BML image on VIP
    public final Image imgBml = new Image("BML image", "//img[contains(@src,'/aw/pics/payments/bml/logoBML_77x18.gif')]");

    public final Link btnAddtoCart = new Link("Add to Cart button ", "//a[contains(text(),'Add to Cart')]");

    public final Link btnAddtoCart1 = new Link("Add to Cart button ", "//a[contains(text(),'Add to Cart')]");
    /*Incentives Redesign Project*/
    //Link "Get coupon code" in gray area
    public Link lnkGetCouponCode = new Link("Get coupon code", "//a[@id='gcclid']");
    //Link "Learn more"
    public Link lnkLearnMore = new Link("Learn more", "//a[@id='lmlid']");
    //Link "Click to close"
    public Link lnkCloseOverlay = new Link("Close overlay", "//a[@id='incOvyId_CB']");
    //Coupon image on VIP
    public final Image imgCouponVip = new Image("Coupon image on Vip", "//img[contains(@src,'/aw/pics/buy/incentives/iconCoupon_70x16.gif')]");
    //New text in Gray area
    public final Label lblCouponVip = new Label("coupon text", "//div[@id='inc_coupon_gr']");
    //Link "See terms" for coupon on overlay
    public final Link lnkCouponSeeTerms = new Link("Coupon See terms", "//a[@id='Inc_st1' or contains(@href,'MyEbay&CurrentPage=MyeBayIncentives')]");
    //Link "See terms" for bucks on overlay
    public final Link lnkBucksSeeTerms = new Link("Bucks See terms", "//a[contains(@href,'ssPageName=STRK:VI:LNLK:TCS')]");
    //Link "See terms" for bml on vip and overlay
    public final Link lnkBmlSeeTerms = new Link("Bml See Terms", "//a[@id='Bml_st1']");
    //Coupon image on overlay
    public final Image imgCouponOverlay = new Image("Coupon image on overlay", "//img[contains(@src,'/aw/pics/buy/incentives/iconCoupon_50x39.gif')]");
    //Bucks image on vip
    public final Image imgBucksVip = new Image("Bucks image on Vip", "//img[contains(@src,'/aw/pics/buy/incentives/iconBucks.gif')]");
    //Bucks image on overlay
    public final Image imgBucksOverlay = new Image("Bucks image on overlay", "//img[contains(@src,'/aw/pics/buy/incentives/imgBucks.gif')]");
    //BML image on vip
    public final Image imgBmlVip = new Image("Bml image on Vip", "//img[contains(@src,'/aw/pics/buy/incentives/iconBML.gif')]");
    //BML image on overlay
    public final Image imgBmlOverlay = new Image("Bml image on overlay", "//img[contains(@src,'/aw/pics/buy/incentives/imgBMLOL.gif')]");
    //Bonus Bucks Image
    public final Image imgBonusBucksVip = new Image("Bonus Bucks image on vip", "//img[contains(@src,'/aw/pics/buy/incentives/iconBonusBucks.gif')]");
    //Bonus Bucks Image on overlay
    public final Image imgBonusBucksOverlay = new Image("Bonus Bucks image on overlay", "//img[contains(@src,'/aw/pics/buy/incentives/imgBonusBucks_50x50.gif')]");
    //Image "Special"
    public final Image imgSpecial = new Image("Special image", "//img[contains(@src,'/aw/pics/buy/incentives/imgSpecialOffers.gif')]");
    //Image Special arrow
    public final Image imgSpecialArrow = new Image("Special arrow", "//img[contains(@src,'/aw/pics/buy/incentives/imgSOArrow.gif')]");

    public final Label lblItemState = new Label("Item State","//div[@id='blueStripComp]/span/span']");

    //Lines and "And" between incentives description on overlay
    ///[@id='v4-EIO']/div[2]/div[2]/hr
    //*[@id='v4-EIO']/div[2]/div[3]
    //*[@id='v4-EIO']/div[2]/div[4]/hr

    public final Button btnINPlaceBid = new Button("PlaceBid","//b/span/input");

    public final Button btnINPayNOw = new Button("paynow","//a[contains(text(),'Pay now')]");

    /**
     * Link: Sell Similiar Item
     * @xpath //a[contains(@href,'SellSimilarItem')]
     */

    public final Link lnkSellSimiliarItm = new Link("SellSimiliarItem", "//a[contains(text(),'Sell a similar item')]");

    /**
     * Label: Item BIN Price
     * @xpath //span[@id='v4-27']
     */
    public final Label lblItemBINPrice = new Label("ItemBINPrice", "//span[@id='v4-27']");

    /**
     * Label: Item Title
     * @xpath //b[@id='mainContent']/h1
     */
    public final Label lblItemTitle = new Label("ItemTitle", "//h1[@class='vi-is1-titleH1']");

    /**
     * Label: Item Description
     * @xpath //div[@id='EBdescription']
     */
    public final Label lblItemDescription = new Label("ItemDescription", "//div[@id='EBdescription']") {
        public String getText() {
            String itemDescription;
            WebDriver driver = WebUXDriver.getWebDriver();
            try {
                driver.switchTo().frame("b");
            } catch (NoSuchFrameException e) {
                Logging.log("No Frame exists with id : b");
            }
            try {
                driver.switchTo().frame("d");
            }catch (NoSuchFrameException e) {
                Logging.log("No Frame exists with id : d");
            }
            itemDescription=  driver.findElement(By.id("EBdescription")).getText();
            driver.switchTo().defaultContent();
            return itemDescription;
        }
    };

    public String getItemBrand () {
        return driver.findElement(By.xpath("//th[contains(text(),'Brand: ')]/following-sibling::td")).getText();
    }


    /**
     * Return all available sizes for MSKU item
     * @return String[]
     */
    public String[] getItemSizes() {
        List<WebElement> itemSizes = selFirstMSKUDropdown.getOptions();
        int sizeCount = 0;
        String MSKUitemSizes[] = new String[itemSizes.size() - 1];
        for (WebElement itemSize : itemSizes) {
            if (!itemSize.getText().equalsIgnoreCase("- Select -")) {
                MSKUitemSizes[sizeCount] = itemSize.getText();
                sizeCount++;
            }
        }
        return MSKUitemSizes;
    }

    public final Label lblMSKU_BIN_Price = new Label("MSKU Item BIN Price", "//span[@id='v4-30']");

    public final Button btnMSKUBIN_IN = new Button("MSKU BIN button", "//input[@value='Buy It Now']");

    public final SelectList selSize = new SelectList("Size", "//select[@name='Size']");

    public final SelectList selColor = new SelectList("Color", "//select[@name='Color']");

    public final SelectList selSleeveStyle = new SelectList("SleeveStyle", "//select[@name='SleeveStyle']");

    private final Link lnkShippingAndPaymentTab = new Link("Shipping and Payment tab", "//a[contains(.,'Shipping and payments')]");
    private final Label lblFlateRateCourier = new Label("Free shipping Label","//div[contains(text(),'Flat Rate Courier - Delivery anywhere in India')]");
    private final Label lblFreeShipping = new Label("Free shipping Label","//div[contains(text(),'Free shipping')]");
    private final Label lblNoReturnPolicy = new Label("No Return Policy", "//td[contains(text(),'Return policy not specified by the seller.')]");
    private final Label lblReturnPolicy = new Label("Return Policy on view item", "//h3[contains(text(),'Return policy & Warranty')]");
    private final Label lblReturnwithinDays = new Label("Return Policy on view item", "//div[contains(text(),'Item must be returned within')]");
    private final Label lblReturnwithinDaysValue = new Label("Return Policy on view item", "//div[contains(text(),'7 Days')]");
    private final Label lblRefundtype = new Label("Return Policy on view item", "//div[contains(text(),'Refund will be given as')]");
    private final Label lblRefundtypeValue = new Label("Return Policy on view item", "//div[contains(text(),'Money Back')]");
    private final Label lblShippingcost = new Label("Return Policy on view item", "//div[contains(text(),'Shipping cost for returns paid by')]");
    private final Label lblShippingcostValue = new Label("Return Policy on view item", "//div[contains(text(),'Buyer')]");
    private final Label lblWarrantytype = new Label("Return Policy on view item", "//div[contains(text(),'Warranty type')]");
    private final Label lblWarrantytypeValues = new Label("Return Policy on view item", "//div[contains(text(),'Manufacturer Warranty')]");
    private final Label lblWarrantyDuration = new Label("Return Policy on view item", "//div[contains(text(),'Warranty duration')]");
    private final Label lblWarrantydurationValue = new Label("Return Policy on view item", "//div[contains(text(),'Item must be returned within')]");


    /**
     * Return all available sizes for MSKU item
     * @return String[]
     */
    public String[] getSizes() {
        List<WebElement> itemSizes = selSize.getOptions();
        int sizeCount = 0;
        String MSKUitemSizes[] = new String[itemSizes.size() - 1];
        for (WebElement itemSize : itemSizes) {
            if (!itemSize.getText().equalsIgnoreCase("- Select -")) {
                MSKUitemSizes[sizeCount] = itemSize.getText();
                sizeCount++;
            }
        }
        return MSKUitemSizes;
    }

    /**
     * Return all available colors for MSKU item
     * @return String[]
     */
    public String[] getColors() {
        List<WebElement> itemColors = selColor.getOptions();
        int colorCount = 0;
        String MSKUitemColors[] = new String[itemColors.size() - 1];
        for (WebElement itemColor : itemColors) {
            if (!itemColor.getText().equalsIgnoreCase("- Select -")) {
                MSKUitemColors[colorCount] = itemColor.getText();
                colorCount++;
            }
        }
        return MSKUitemColors;
    }

    /**
     * Return all available sleeve styles for MSKU item
     * @return String[]
     */
    public String[] getSleeveStyles() {
        List<WebElement> itemSleeveStyles = selSleeveStyle.getOptions();
        int sleeveCount = 0;
        String MSKUitemSleeveStyles[] = new String[itemSleeveStyles.size() - 1];
        for (WebElement itemSleeveStyle: itemSleeveStyles) {
            if (!itemSleeveStyle.getText().equalsIgnoreCase("- Select -")) {
                MSKUitemSleeveStyles[sleeveCount] = itemSleeveStyle.getText();
                sleeveCount++;
            }
        }
        return MSKUitemSleeveStyles;
    }

    public ViewItemPage enterQuantity(String quantity) {
        txtQuantity.sendKeys(quantity);
        return this;
    }

    public void enterBID(String amt){
        txtMaxBid.type(amt);
        btnPlaceBid.click();
        btnConfirmBid.click();
    }

    public void verifyShippingPolicy(){
        lnkShippingAndPaymentTab.isElementPresent();
        lblFlateRateCourier.isElementPresent();
        lblFreeShipping.isElementPresent();
    }
    public void verifyNoReturnPolicyVI(){
        lblNoReturnPolicy.isElementPresent();
    }
    public void verifyReturnPolicyVI(){
        lnkShippingAndPaymentTab.click();
        Assert.assertTrue(lblReturnPolicy.isElementPresent());
        Assert.assertTrue(lblReturnwithinDays.isElementPresent());
        Assert.assertTrue(lblReturnwithinDaysValue.isElementPresent());
        Assert.assertTrue(lblRefundtype.isElementPresent());
        Assert.assertTrue(lblRefundtypeValue.isElementPresent());
        Assert.assertTrue(lblShippingcost.isElementPresent());
        Assert.assertTrue(lblShippingcostValue.isElementPresent());
        Assert.assertTrue(lblWarrantytype.isElementPresent());
        Assert.assertTrue(lblWarrantytypeValues.isElementPresent());
        Assert.assertTrue(lblWarrantyDuration.isElementPresent());
        Assert.assertTrue(lblWarrantydurationValue.isElementPresent());
    }

    /**
     * Add item with specified quantity to cart
     *
     * @param item collection of item id and quantity
     * @return
     * @throws Exception
     */
    public XOShoppingCartPage addBINItemToCart(Map<String, Integer> item) throws Exception {
        for(String itemID:item.keySet()) {
            int quantity = item.get(itemID);
            new ViewItemPage(itemID);
            if(quantity > 1) {
                txtQuantity.clear();
                txtQuantity.sendKeys(String.valueOf(quantity));
            }
            btnAddtoCart.click();
        }
        return new XOShoppingCartPage();
    }

    /**
     * Dumb service to select any one value of variation for each variation type
     *
     * @param mskuItemVariation item id and name, value pair collection of item variation
     * @param  itemQuantity
     * @return
     * @throws Exception
     */
    public XOShoppingCartPage addAnyMSKUItemVariationToCart(Map<String, Map<String, List<String>>> mskuItemVariation, int itemQuantity) throws Exception {
        for(String itemID: mskuItemVariation.keySet())  {
            new ViewItemPage(itemID);
            for(String variationName:mskuItemVariation.get(itemID).keySet()) {
                new SelectList("Variation List", By.name(variationName)).selectByText(mskuItemVariation.get(itemID).get(variationName).get(0));
            }
            if(itemQuantity > 1) {
                txtQuantity.clear();
                txtQuantity.sendKeys(String.valueOf(itemQuantity));
            }
        }
        btnAddtoCart.click();
        return new XOShoppingCartPage();
    }


    /**
     * Selects specified value of variation for each variation type
     *
     * @param mskuItemVariation item id and name, value pair collection of item variation
     * @param  itemQuantity
     * @return
     * @throws Exception
     */
    public XOShoppingCartPage addSpecifiedMSKUItemVariationToCart(Map<String, Map<String, String>> mskuItemVariation, int itemQuantity) throws Exception {
        for(String itemID: mskuItemVariation.keySet())  {
            new ViewItemPage(itemID);
            for(String variationName:mskuItemVariation.get(itemID).keySet()) {
                new SelectList("Variation List", By.name(variationName)).selectByText(mskuItemVariation.get(itemID).get(variationName));
            }
            if(itemQuantity > 1) {
                txtQuantity.clear();
                txtQuantity.sendKeys(String.valueOf(itemQuantity));
            }
        }
        btnAddtoCart.click();
        return new XOShoppingCartPage();
    }

    /**
     * Enters given quantity and clicks Buy It Now
     *
     * @param itemID
     * @param itemQuantity
     * @return
     * @throws Exception
     */
    public XOReviewOrderPage buyNonMSKUItemNow(String itemID, int itemQuantity) throws Exception {
        new ViewItemPage(itemID);
        if(itemQuantity > 1) {
            txtQuantity.clear();
            txtQuantity.sendKeys(String.valueOf(itemQuantity));
        }
        btnBIN.click();
        return new XOReviewOrderPage();
    }

    /**
     * Selects variations, enters given quantity and clicks Buy It Now for MSKU item
     *
     * @param mskuItemVariation
     * @param itemQuantity
     * @return
     * @throws Exception
     */
    public XOReviewOrderPage buyMSKUItemNow(Map<String, Map<String, String>> mskuItemVariation, int itemQuantity) throws Exception {
        for(String itemID: mskuItemVariation.keySet())  {
            new ViewItemPage(itemID);
            for(String variationName:mskuItemVariation.get(itemID).keySet()) {
                new SelectList("Variation List", By.name(variationName)).selectByText(mskuItemVariation.get(itemID).get(variationName));
            }
            if(itemQuantity > 1) {
                txtQuantity.clear();
                txtQuantity.sendKeys(String.valueOf(itemQuantity));
            }
        }
        btnMSKUBuyItNow.click();
        return new XOReviewOrderPage();
    }
}