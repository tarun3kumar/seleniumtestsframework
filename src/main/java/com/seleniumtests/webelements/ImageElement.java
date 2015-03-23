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

public class ImageElement extends HtmlElement {

    public ImageElement(final String label, final By by) {
        super(label, by);
    }

    public int getHeight() {
        return super.getSize().getHeight();
    }

    public int getWidth() {
        return super.getSize().getWidth();
    }

    public String getUrl() {
        return super.getAttribute("src");
    }
}
