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

import org.w3c.dom.Node;

/**
 * Convenience Node class containing the Node itself and corresponding XPath.
 *
 * <p/>expression.
 *
 * <p/>
 * <br>
 * This can be used to improve the performance where XPath of all the children
 *
 * <p/>needs to be computed, saves time not traversing to the ROOT multiple times.
 */

public class XNode implements Serializable {

    private Node _node = null;

    private String _xPath = null;

    private String _noIndexXPath = null;

    private int _position = 0; // Position of the Node under a parent

    private int _depth = 0; // Depth of the Node in the Document

    /**
     * Default Constructor.
     */

    public XNode() { }

    /**
     * Constructor.
     *
     * @param  node   the Node
     * @param  xPath  the XPath expression for the Node
     */

    public XNode(final Node node, final String xPath) {

        _node = node;

        _xPath = xPath;

    }

    /**
     * Constructor.
     *
     * @param  node          the Node
     * @param  xPath         the XPath expression for the Node
     * @param  noIndexXPath  the XPath expression without the indexes
     */

    public XNode(final Node node, final String xPath, final String noIndexXPath) {

        _node = node;

        _xPath = xPath;

        _noIndexXPath = noIndexXPath;

    }

    /**
     * Gets Node.
     */

    public Node getNode() {

        return _node;

    }

    /**
     * Sets Node.
     */

    public void setNode(final Node node) {

        _node = node;

    }

    /**
     * Gets the position of the Node under the parent.
     */

    public int getPosition() {

        return _position;

    }

    /**
     * Sets the position of the Node under the parent.
     */

    public void setPosition(final int position) {

        _position = position;

    }

    /**
     * Gets the depth of the Node in the Document.
     */

    public int getDepth() {

        return _depth;

    }

    /**
     * Sets the depth of the Node in the Document.
     */

    public void setDepth(final int depth) {

        _depth = depth;

    }

    /**
     * Gets the XPath expression.
     */

    public String getXPath() {

        return _xPath;

    }

    /**
     * Sets XPath expression.
     */

    public void setXPath(final String xPath) {

        _xPath = xPath;

    }

    /**
     * Gets XPath expression without the indexes.
     */

    public String getNoIndexXPath() {

        String xPath = getXPath();

        xPath = XMLUtil.getNoIndexXPath(xPath);

        setNoIndexXPath(xPath);

        return xPath;

    }

    /**
     * Sets XPath expression without the indexes.
     */

    public void setNoIndexXPath(final String noIndexXPath) {

        _noIndexXPath = noIndexXPath;

    }

    /**
     * Gets Node Value.
     */

    public String getValue() {

        if (_node == null) {

            return null;
        }

        return _node.getNodeValue();

    }

    /**
     * Gets Node Name.
     */

    public String getName() {

        if (_node == null) {

            return null;
        }

        return _node.getNodeName();

    }

    /**
     * Gets String representation of the XNode.
     */

    public String toString() {

        String eol = System.getProperty("line.separator");

        StringBuffer sb = new StringBuffer("XNode:[");

        sb.append("Node Name:" + getNode().getNodeName());

        sb.append(eol);

        sb.append("Node Value:" + getNode().getNodeValue());

        sb.append(eol);

        sb.append("Node Type:" + getNode().getNodeType());

        sb.append(eol);

        sb.append("Node XPath:" + getXPath());

        sb.append(eol);

        sb.append("]");

        return sb.toString();

    }

}
