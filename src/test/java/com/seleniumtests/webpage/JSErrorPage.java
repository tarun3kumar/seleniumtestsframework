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

package com.seleniumtests.webpage;

import com.seleniumtests.core.SeleniumTestsContextManager;
import com.seleniumtests.dataobject.User;
import com.seleniumtests.webelements.ButtonElement;
import com.seleniumtests.webelements.PageObject;
import com.seleniumtests.webelements.TextFieldElement;

import static com.seleniumtests.core.Locator.locateByCSSSelector;
import static com.seleniumtests.core.Locator.locateByName;

/**
 * Provides services offered by Registration Page.
 *
 * <p/>Date: 10/2/13 Time: 6:26 PM
 */
public class JSErrorPage extends PageObject {

    public JSErrorPage(final boolean openPageURL) throws Exception {
        super(null, openPageURL ? SeleniumTestsContextManager.getThreadContext().getAppURL() :
                null);
    }
}
