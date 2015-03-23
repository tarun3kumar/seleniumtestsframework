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

import java.io.Serializable;

/**
 * NodeResult class used to store the Control Node, Test Node and Differences between them.
 *
 * <p/>
 * <br>
 * NodeResult is used to pass results between classes in the XMLDog Application
 */

public class NodeResult implements Serializable {

    private XNode _testNode = null;

    private XNode _controlNode = null;

    private Differences _differences = null;

    private boolean _uniqueAttrMatch = false;

    private boolean _isExactMatch = false;

    /**
     * Default Constructor.
     */

    public NodeResult() { }

    /**
     * Constructor.
     *
     * @param  controlNode  the Control Node
     * @param  testNode     the test Node
     * @param  diff         the Differences
     *
     * @see    Differences
     */

    public NodeResult(final XNode controlNode, final XNode testNode, final Differences diff) {

        _controlNode = controlNode;

        _testNode = testNode;

        _differences = diff;

    }

    /**
     * Gets Test Node.
     */

    public XNode getTestNode() {

        return _testNode;

    }

    /**
     * Sets Test Node.
     */

    public void setTestNode(final XNode node) {

        _testNode = node;

    }

    /**
     * Gets control Node.
     */

    public XNode getControlNode() {

        return _controlNode;

    }

    /**
     * Sets Control Node.
     */

    public void setControlNode(final XNode node) {

        _controlNode = node;

    }

    /**
     * Get Differences.
     *
     * @see  Differences
     */

    public Differences getDifferences() {

        return _differences;

    }

    /**
     * Sets Differences.
     *
     * @see  Differences
     */

    public void setDifferences(final Differences diff) {

        _differences = diff;

    }

    /**
     * Checks if unique Attribute matches.
     */

    public boolean isUniqueAttrMatch() {

        return _uniqueAttrMatch;

    }

    /**
     * Sets Unique Attribute match flag.
     */

    public void setUniqueAttrMatch(final boolean flag) {

        _uniqueAttrMatch = flag;

    }

    /**
     * Checks if its an exact match.
     */

    public boolean isExactMatch() {

        return _isExactMatch;

    }

    /**
     * Sets the flag if its an exact match.
     */

    public void setIfExactMatch(final boolean flag) {

        _isExactMatch = flag;

    }

    /**
     * Checks if the NodeResult is a match, either by unique attribute.
     *
     * <p/>or an exact match
     *
     * <p/>
     * <br>
     * Convenience method, since sometimes it is JUST useful to
     *
     * <p/>check if its match, immaterial of the type of match
     */

    public boolean isMatch() {

        return (isExactMatch() || isUniqueAttrMatch());

    }

    /**
     * Gets number of Differences for given Nodes.
     */

    public int getNumDifferences() {

        if (_differences == null) {

            return 0;
        }

        return _differences.size();

    }

    /**
     * Gets String representation of the Instance.
     */

    public String toString() {

        StringBuffer sb = new StringBuffer();

        sb.append("NodeResult[");

        sb.append(StringUtil.getNewlineStr());

        sb.append("Golden");

        sb.append(XMLUtil.getNodeBasics(getControlNode().getNode()));

        sb.append(StringUtil.getNewlineStr());

        sb.append("Current");

        sb.append(StringUtil.getNewlineStr());

        sb.append(XMLUtil.getNodeBasics(getTestNode().getNode()));

        sb.append(StringUtil.getNewlineStr());

        // sb.append("Differences");

        sb.append(getDifferences());

        sb.append(StringUtil.getNewlineStr() + "]");

        return sb.toString();

    }

    /**
     * Main method for debugging purpose only.
     */

    public static void main(final String[] args) { }

}
