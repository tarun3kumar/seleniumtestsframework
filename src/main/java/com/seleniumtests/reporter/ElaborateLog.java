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

package com.seleniumtests.reporter;

public class ElaborateLog {

    private String type;
    private String msg;
    private String screen;
    private String src;
    private String location;
    private String href;

    private String root;

    public ElaborateLog(final String s, final String root) {
        this.root = root;
        if (s == null) {
            return;
        }

        String[] parts = s.split("\\|\\|");
        for (String part : parts) {
            parse(part);
        }
    }

    public String getHref() {
        return href;
    }

    public String getLocation() {
        return location;
    }

    public String getMsg() {
        return msg;
    }

    private void parse(final String part) {
        if (part.startsWith("TYPE=")) {
            type = part.replace("TYPE=", "");
        } else if (part.startsWith("MSG=")) {
            msg = part.replace("MSG=", "");
        } else if (part.startsWith("SCREEN=")) {
            screen = part.replace("SCREEN=", "");
        } else if (part.startsWith("SRC=")) {
            src = part.replace("SRC=", "");
        } else if (part.startsWith("LOCATION=")) {
            location = part.replace("LOCATION=", "");
        } else if (part.startsWith("HREF=")) {
            href = part.replace("HREF=", "");
        } else {
            msg = part;
        }

    }

    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append("TYPE=");
        if (type != null) {
            buff.append(type);
        }

        buff.append("||MSG=");
        if (msg != null) {
            buff.append(msg);
        }

        buff.append("||SCREEN=");
        if (screen != null) {
            buff.append(screen);
        }

        buff.append("||SRC=");
        if (src != null) {
            buff.append(src);
        }

        buff.append("||LOCATION=");
        if (location != null) {
            buff.append(location);
        }

        buff.append("||HREF=");
        if (href != null) {
            buff.append(href);
        }

        return buff.toString();
    }
}
