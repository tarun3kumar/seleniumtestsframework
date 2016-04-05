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

import com.seleniumtests.helper.StringUtility;

public class ShortTestResult {

    private String name;
    private String id;

    private int totalMethod;

    private int instancesPassed;

    private int instancesFailed;

    private int instancesSkipped;

    public ShortTestResult(final String name) {
        this.name = name;
        this.id = StringUtility.md5(name);
    }

    public String getId() {
        return id;
    }

    public int getInstancesFailed() {
        return instancesFailed;
    }

    public int getInstancesPassed() {
        return instancesPassed;
    }

    public int getInstancesSkipped() {
        return instancesSkipped;

    }

    public String getName() {
        return name;
    }

    public int getTotalMethod() {
        return totalMethod;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setInstancesFailed(final int instancesFailed) {
        this.instancesFailed = instancesFailed;
    }

    public void setInstancesPassed(final int instancesPassed) {
        this.instancesPassed = instancesPassed;
    }

    public void setInstancesSkipped(final int instancesSkipped) {
        this.instancesSkipped = instancesSkipped;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setTotalMethod(final int totalMethod) {
        this.totalMethod = totalMethod;
    }
}
