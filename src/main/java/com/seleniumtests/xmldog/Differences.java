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

import java.util.ArrayList;

import org.w3c.dom.Node;

/**
 * Differences class used to store all the Differences between the Nodes.
 *
 * <p/>being compared
 */

public class Differences extends ArrayList implements DifferenceListener {

    /**
     * Default Constructor.
     */

    public Differences() {

        super();

    }

    /**
     * Copy Constructor.
     */

    public Differences(final Differences differences) {

        addAll(differences);

    }

    /**
     * Adds Differences from the input NodeResult.
     *
     * @see  NodeResult
     */

    public boolean add(final NodeResult nr) {

        if ((nr != null) && (nr.getDifferences() != null)) {

            addAll(nr.getDifferences());

            return true;

        }

        return false;

    }

    /**
     * Adds Differences from the input Differences.
     */

    public boolean add(final Differences diffs) {

        return addAll(diffs);

    }

    public boolean add(final Object obj) {

        if (obj instanceof NodeResult) {

            return add((NodeResult) obj);
        } else if (obj instanceof Differences) {

            return add((Differences) obj);
        } else {

            return super.add(obj.toString());
        }

    }

    /**
     * Notifies DifferenceListener that similar node has been found in.
     *
     * <p/>the test Document<br>
     *
     * <p/>Implementation method from DifferenceListener interface
     */

    public void similarNodeFound(final Node controlNode, final Node testNode, final String msg) {

        add(msg);

    }

    /**
     * Notifies DifferenceListener that identical node has been found in.
     *
     * <p/>the test Document<br>
     *
     * <p/>Implementation method from DifferenceListener interface
     */

    public void identicalNodeFound(final Node controlNode, final Node testNode, final String msg) {

        add(msg);

    }

    /**
     * Notifies DifferenceListener that corresponding node has NOT been found in.
     *
     * <p/>the test Document<br>
     *
     * <p/>Implementation method from DifferenceListener interface
     */

    public void nodeNotFound(final Node controlNode, final Node testNode, final String msg) {

        add(msg);

    }

    /**
     * Gets difference count.
     */

    public int getDiffCount() {

        return size();

    }

    /**
     * Gets the String representation of the object.
     */

    public String toString() {

        StringBuffer sb = new StringBuffer();

        sb.append("Differences:[ size: " + size());

        sb.append(StringUtil.getNewlineStr());

        if (size() > 0) {

            for (int i = 0; i < size(); i++) {

                // System.out.println(" Element in the differences list is of type " + get(i).getClass());

                sb.append(get(i).toString());

                sb.append(StringUtil.getNewlineStr());

            }

        } else {

            sb.append("XML Nodes are identical, No differences found");
        }

        sb.append(StringUtil.getNewlineStr());

        sb.append("]");

        return sb.toString();

    }

    /**
     * Gets Simple HTML string form of the Differences, with all the individual differences.
     *
     * <p/>seperated by line break<br>
     * HTML tag
     */

    public String getHTML() {

        StringBuffer sb = new StringBuffer();

        sb.append("<UL>");

        if (size() > 0) {

            for (int i = 0; i < size(); i++) {

                // System.out.println(" Element in the differences list is of type " + get(i).getClass());

                sb.append("<LI>");

                sb.append(get(i).toString());

                sb.append("</LI>");

            }

        } else {

            sb.append("<B>Nodes are identical, No differences found</B>");
        }

        sb.append("</UL>");

        return sb.toString();

    }

}
