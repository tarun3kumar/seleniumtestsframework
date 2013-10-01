package com.seleniumtests.webpage;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import com.seleniumtests.controller.Logging;
import com.seleniumtests.driver.web.element.Button;
import com.seleniumtests.driver.web.element.CheckBox;
import com.seleniumtests.driver.web.element.HtmlElement;
import com.seleniumtests.driver.web.element.Label;
import com.seleniumtests.driver.web.element.Link;
import com.seleniumtests.driver.web.element.TextField;
import com.seleniumtests.driver.web.element.WebPage;
import com.seleniumtests.dataobject.User;


/**
 * Page: Sign In and Sign Out Page
 *
 * @author Tenia, Li
 * @payload eBay Payload
 * @date 2010/04/11
 */
public class SignInPage extends WebPage {

    //public static final String PAGE_URL = "https://signin.qa.ebay.com/ws/eBayISAPI.dll?SignIn";
    public static final String PAGE_URL = "https://signin.ebay.in/ws/eBayISAPI.dll?SignIn&ru=http%3A%2F%2Fwww.ebay.in%2Fws%2FeBayISAPI.dll%3FSignIn";
    private static final TextField txtPageIdentifier = new TextField("SignInPageIdentifier", "//input[@id='userid' or @id='submit']");
    public final String CAPTCHA_URL =
            "http://signin.qa.ebay.com/ws/eBayISAPI.dll?LoadBotImage" +
                    "&tokenString=_TOKEN_STRING_&siteid=_SITEID_&co_brandId=_CO_BRANDID_";

    public final TextField txtUserId = new TextField("UserId", "//input[@id='userid']");
    public final TextField txtPassword = new TextField("Password", "//input[@id='pass' or @name='pass']");

    public final Button btnSignIn = new Button("SignIn", "//input[@class='SIActBtn' or @class='bfbt' or @class='btnMain' or @name='sgnBt' or contains(@id, 'sgnBt') or @id='but_motorsSignIn'" +
            "or @id='but_submit' or @id='sgnBt' or @id='submit']");
    public static final Link lnkSignOut = new Link("Sign out Link", By.linkText("Sign out"));

    public final Link lnkForgotYourPassword = new Link("ForgotYourPassword", "//a[contains(@href, 'FYPShow')]");

    public final Label lblWelcomeToeBay = new Label("WelcomeToEbay", "//div[@id='v4-0' or @id='AreaTitle']");

    public final TextField txtCaptcha = new TextField("TokenText", "//input[@id='tokenText']");

    public final HtmlElement heCaptcha = new HtmlElement("Tokenstring", "//input[@name='tokenstring']");

    public final CheckBox chkKeepSign = new CheckBox("keepSigned", "//input[@id='signed_in']");

    /**
     * Button Register
     *
     * @xpath //input[@id='but_regSub']
     */
    public final Button btnRegister = new Button("Register", "//input[contains(@id,'but_reg') or @id='register_signin']");

    //public final Link lnkfbBtn = new Link("SignInWithFaceBook", "//div[@class='txRgt']//a");
    public final Button btnfbSignIn = new Button("SingInWithFacebook", "//input[@id='fbBtn']");

    public final Button btnGuestCheckout = new Button("Guest Checkout", "//input[@id='but_gtChk' or @id='gtChk']");
    //public final Button btnGuestCheckout = new Button("Guest Checkout", "//input[@id='but1234_gtChk']");
    /**
     * Used if user don't want to open url,it's a redirected page
     *
     * @throws Exception
     */
    public SignInPage() throws Exception {
        super(txtPageIdentifier, true);
    }

    /**
     * Used if user want to open default url
     * new SignInPage(true);
     *
     * @param openPageUrl
     * @throws Exception
     */
    public SignInPage(boolean openPageUrl) throws Exception {
        super(txtPageIdentifier, openPageUrl ? PAGE_URL : null, true);
    }

    public SignInPage(HtmlElement elemPageIdentifier) throws Exception {
        super(elemPageIdentifier, true);


    }

    /**
     * Used if you want to open other sign in page, such as motor signin.
     * It used in motor sigin constructor method.
     *
     * @param elemPageIdentifier
     * @param PageUrl
     * @param openPageUrl
     * @throws Exception
     */
    public SignInPage(HtmlElement elemPageIdentifier, String PageUrl, boolean openPageUrl) throws Exception {
        super(elemPageIdentifier, openPageUrl ? PageUrl : null, true);

    }

    /**
     * Used if you want to open your own url instead of default PAGE_URL
     *
     * @param url
     * @throws Exception
     */
    public SignInPage(String url) throws Exception {
        super(txtPageIdentifier, url);
    }
    //public final Link lnkfbBtn = new Link("SignInWithFaceBook", "//a[contains(@href,'javascript')]");


    /**
     * Page flow description: fillUserCredentials
     *
     */
    public void fillUserCredentials(String user, String passwd) {
        txtUserId.type(user);
        txtPassword.type(passwd);
    }

    /**
     * Page flow description: SignIn
     *
     */
    public void fillUserCredentials(User user) {
        txtUserId.type(user.getUserID());
        if (user.getPassword() != null)
            txtPassword.type(user.getPassword());
        else
            txtPassword.type("");
    }

    /**
     * Page flow description: SignIn
     *
     */
    public void signIn(String user, String password) {
        fillUserCredentials(user, password);
        try {
            //when captcha shows up, enter key
//            SignInOutFlows.readandWriteCaptcha(heCaptcha, txtCaptcha, CAPTCHA_URL);
        } catch (Exception e) {
            //e.printStackTrace();
            Logging.log(e.getMessage());
        }
        btnSignIn.click();
    }

    public void signOutAndSignIn(String userId, String password) throws Exception {
        // logout and login
        try {
            signOut();
            btnSignIn.click();
            fillUserCredentials(userId, password);
            btnSignIn.click();
        } catch (Exception e) {
            // logout from intermediate page i.e. User signing in during Buy It Now flow
            new SignInPage(true);
            btnSignIn.click();
            fillUserCredentials(userId, password);
            btnSignIn.click();
        }
    }

    /**
     * Page flow description: SignIn
     *
     */
    public void signIn(User user) {
        fillUserCredentials(user);
        try {
            //when captcha shows up, enter key
            //SignInOutFlows.readandWriteCaptcha(heCaptcha, txtCaptcha, CAPTCHA_URL);
        } catch (Exception e) {
            //e.printStackTrace();
            Logging.log(e.getMessage());
        }
        btnSignIn.click();
        try {
            //return new MyeBaySummaryPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return null;
    }

    /**
     * Page flow description: keepMeSignIn
     *
     */
    public void signIn(User user, boolean keepMeSignedIn) {
        fillUserCredentials(user);
        if (keepMeSignedIn)
            chkKeepSign.check();
        try {
            //when captcha shows up, enter key
            //SignInOutFlows.readandWriteCaptcha(heCaptcha, txtCaptcha, CAPTCHA_URL);
        } catch (Exception e) {
            //e.printStackTrace();
            Logging.log(e.getMessage());
        }
        btnSignIn.click();

    }

    /**
     * Use for test a wrong user info, will wait for the error message
     *
     * @param user
     * @param password
     * @throws Exception
     */
    public void signinWithWrongInfo(String user, String password) {

        txtUserId.type(user);
        txtPassword.type(password);
        btnSignIn.click();
        this.capturePageSnapshot();
        waitForElementPresent(new Button("errorText", "//span[text()='Your user ID or password is incorrect.']"));
    }
    public void signOut(){

        String signoutXptah="//*[@id='gh-eb-u']";
        Link signoutLink=new Link("Signout", signoutXptah);

        if(this.isElementPresent(By.xpath(signoutXptah))){
            Actions builder = new Actions(this.driver);
            builder.moveToElement(signoutLink.getElement()).build().perform();
            lnkSignOut.click();
        }else{
            lnkSignOut.click();
        }

    }

}
