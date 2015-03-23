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

package com.seleniumtests.xmldog;

/**
 * StringUtil.java. To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation. $Id$
 */

/**
 * StringUtil class containing utility functions related to Strings.
 */

public class StringUtil {

    /**
     * Checks if the input String is Whitespace only.
     */

    public static boolean isWhitespaceStr(String str) {

        if (str == null) {

            return false;
        }

        str = str.trim();

        for (int i = 0; i < str.length(); i++) {

            if (!Character.isWhitespace(str.charAt(i))) {

                return false;
            }

        }

        return true;

    }

    /**
     * Gets Platform independent line separator (new line character(s).
     */

    public static String getNewlineStr() {

        return System.getProperty("line.separator");

    }

    /**
     * Main method for debugging purpose only.
     */

    public static void main(final String[] args) {

        /*
         *
         * NULL value checking for instanceof operator
         *
         */

        /*
         *
         * StringUtil su = null;
         *
         * if (su instanceof StringUtil)
         *
         *  System.out.println(" null can be instance of anything");
         *
         * else
         *
         *  System.out.println(" null cannot be instance of anything");
         *
         */

    }

}
