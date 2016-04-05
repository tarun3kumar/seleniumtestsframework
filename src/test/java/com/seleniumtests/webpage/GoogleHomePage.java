package com.seleniumtests.webpage;

import com.seleniumtests.core.Locator;

import com.seleniumtests.webelements.PageObject;
import com.seleniumtests.webelements.TextFieldElement;

/**
 * Created by tarun on 3/22/16.
 */
public class GoogleHomePage extends PageObject {

    private static final TextFieldElement searchTextBox = new TextFieldElement("search Text Box",
            Locator.locateByName("q"));

    public GoogleHomePage() throws Exception {
        super(searchTextBox);
    }

    public boolean isSearchBoxDisplayed() {
        return searchTextBox.isDisplayed();
    }
}
