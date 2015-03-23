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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

public class OSUtility {

    public static String getOSName() {
        return System.getProperty("os.name");
    }

    public static boolean isMac() {
        return getOSName().startsWith("Mac");
    }

    public static boolean isWindows() {
        return getOSName().startsWith("Win");
    }

    public static String getOSBits() {
        return System.getProperty("os.arch");
    }

    public static boolean is32() {
        return getOSBits().equals("x86");
    }

    public static boolean is64() {
        if (isWindows()) {
            return (System.getenv("ProgramW6432") != null);
        } else {
            return !getOSBits().equals("x86");
        }
    }

    private static List<String> executeCommand(final String cmd) {
        List<String> output = new ArrayList<String>();
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
        } catch (IOException e1) {
            return output;
        }

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()), 8 * 1024);
        String s = null;
        try {
            while ((s = stdInput.readLine()) != null) {
                output.add(s);
            }
        } catch (IOException e) {
            return output;
        }

        return output;
    }

    public static int getIEVersion() {
        List<String> output;
        output = executeCommand("reg query \"HKLM\\Software\\Microsoft\\Internet Explorer\" /v svcVersion");
        if (output.size() < 3) {
            output = executeCommand("reg query \"HKLM\\Software\\Microsoft\\Internet Explorer\" /v Version");
        }

        String internet_explorer_value = (output.get(2));
        String version = internet_explorer_value.trim().split("   ")[2];
        version = version.trim().split("\\.")[0];
        return Integer.parseInt(version);
    }

    public static String getSlash() {
        if (isWindows()) {
            return "\\";
        } else {
            return "/";
        }
    }

    public static void main(final String[] args) {

        System.out.println(getIEVersion());

    }

}
