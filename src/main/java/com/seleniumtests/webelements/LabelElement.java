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

import org.openqa.selenium.By;

import com.seleniumtests.core.CustomAssertion;
import com.seleniumtests.core.TestLogging;

public class LabelElement extends HtmlElement {
    public LabelElement(final String label, final By by) {
        super(label, by);
    }

    @Override
    public String getText() {
        TestLogging.logWebStep("get text from " + toHTML(), false);
        return super.getText();
    }

    public boolean isTextPresent(final String pattern) {
        String text = getText();
        return (text != null && (text.contains(pattern) || text.matches(pattern)));
    }

    @Deprecated
    public String getExpectedText() {
        CustomAssertion.assertTrue(false, "NOT supported!");
        return null;
    }
}
