package com.seleniumtests.xmldog;

import org.w3c.dom.Node;

public interface DifferenceListener {

    void similarNodeFound(Node controlNode, Node testNode, String msg);

    void identicalNodeFound(Node controlNode, Node testNode, String msg);

    void nodeNotFound(Node controlNode, Node testNode, String msg);

}
