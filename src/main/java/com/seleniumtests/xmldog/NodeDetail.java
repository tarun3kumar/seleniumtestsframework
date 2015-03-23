package com.seleniumtests.xmldog;

import org.w3c.dom.Node;

/**
 * Class that mimics XMLUnit <i>NodeDetail</i> class. Primarily used
 *
 * <p/>to store the details about the Node being compared
 */

public class NodeDetail {

    private Node _node = null;

    private String _xpath = null;

    private String _value = null;

    /**
     * Constructor.
     */

    public NodeDetail() { }

    /**
     * Constructor.
     *
     * @param  node  the Document Node
     *
     * @paran  xpath the XPath location of the Node
     */

    public NodeDetail(final Node node, final String xpath) {

        _node = node;

        _xpath = xpath;

    }

    /**
     * Constructor.
     *
     * @param  value  the value of the Node
     * @param  node   the Document Node
     *
     * @paran  xpath the XPath location of the Node
     */

    public NodeDetail(final String value, final Node node, final String xpath) {

        _value = value;

        _node = node;

        _xpath = xpath;

    }

    /**
     * Gets the Node.
     *
     * @return  the Document Node
     */

    public Node getNode() {

        return _node;

    }

    /**
     * Sets the Node.
     *
     * @param  node  the Document Node
     */

    public void setNode(final Node node) {

        _node = node;

    }

    /**
     * Gets XPath expression for the Node.
     *
     * @return  the XPath expression for the Node
     */

    public String getXPathLocation() {

        return _xpath;

    }

    /**
     * Sets XPath expression for the Node.
     *
     * @param  xpath  the XPath expression for the Node
     */

    public void setXPathLocation(final String xpath) {

        _xpath = xpath;

    }

    /**
     * Gets the value of the Node.
     *
     * @return  the Node value
     */

    public String getValue() {

        if (_value == null) {

            return getNode().getNodeValue();
        }

        return _value;

    }

}
