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

package com.seleniumtests.helper;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtility {

    public static String constructMethodSignature(final Method method, final Object[] parameters) {
        return method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "("
                + constructParameterString(parameters) + ")";
    }

    public static String constructParameterString(final Object[] parameters) {
        StringBuffer sbParam = new StringBuffer();

        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] == null) {
                    sbParam.append("null, ");
                } else if (parameters[i] instanceof java.lang.String) {
                    sbParam.append("\"").append(parameters[i]).append("\", ");
                } else {
                    sbParam.append(parameters[i]).append(", ");
                }
            }
        }

        if (sbParam.length() > 0) {
            sbParam.delete(sbParam.length() - 2, sbParam.length() - 1);
        }

        return sbParam.toString();
    }

    public static String md5(final String str) {

        if (str == null) {
            return null;
        }

        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return str;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }

        byte[] byteArray = messageDigest.digest();

        return toHexString(byteArray);
    }

    public static String toHexString(byte[] byteArray) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                builder.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                builder.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return builder.toString();
    }
}
