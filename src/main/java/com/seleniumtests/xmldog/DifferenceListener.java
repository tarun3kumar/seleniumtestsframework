package com.seleniumtests.xmldog;

import org.w3c.dom.Node;



public interface DifferenceListener 

{

	public void similarNodeFound(Node controlNode, Node testNode, String msg);

	public void identicalNodeFound(Node controlNode, Node testNode, String msg);

	public void nodeNotFound(Node controlNode, Node testNode, String msg);

}