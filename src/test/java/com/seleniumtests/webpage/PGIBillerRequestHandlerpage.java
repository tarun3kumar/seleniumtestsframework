package com.seleniumtests.webpage;

import com.seleniumtests.driver.web.element.Button;
import com.seleniumtests.driver.web.element.TextField;
import com.seleniumtests.driver.web.element.WebPage;

public class PGIBillerRequestHandlerpage extends WebPage {
    private static final Button pageidentifier = new Button("continue", "//input[@value='submit']");

    public	PGIBillerRequestHandlerpage()throws Exception{
        super(pageidentifier);
    }

    public  final  Button btnsubmit = new Button("submit", "//input[@value='submit']");

    public final TextField txtRV = new TextField("return value","//input[@id='RU']");

    public void submitBillDeskPayment() throws Exception {
        btnsubmit.click();
    }
}
