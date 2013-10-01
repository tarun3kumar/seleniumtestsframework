package com.seleniumtests.xmldog;

import java.io.Serializable;


/** 

 * NodeResult class used to store the Control Node, Test Node and Differences between them

 * <br>NodeResult is used to pass results between classes in the XMLDog Application

 */

public class NodeResult

	implements Serializable 

{

	private XNode _testNode = null;

	private XNode _controlNode = null;

	private Differences _differences = null;

	private boolean _uniqueAttrMatch = false;

	private boolean _isExactMatch = false;

	

	/**

	 * Default Constructor

	 */

	public NodeResult()

	{

	}

	

	/**

	 * Constructor

	 * @param controlNode the Control Node

	 * @param testNode the test Node

	 * @param diff the Differences

	 * @see Differences

	 */

	public NodeResult(XNode controlNode, XNode testNode, Differences diff)

	{

		_controlNode = controlNode;

		_testNode = testNode;

		_differences = diff;

	}

	

	/**

	 * Gets Test Node

	 */

	public XNode getTestNode()

	{

		return _testNode;

	}

	

	/**

	 * Sets Test Node

	 */

	public void setTestNode(XNode node)

	{

		_testNode = node;

	}

	

	/**

	 * Gets control Node

	 */

	public XNode getControlNode()

	{

		return _controlNode;

	}

	

	/**

	 * Sets Control Node

	 */

	public void setControlNode(XNode node)

	{

		_controlNode = node;

	}

	

	/**

	 * Get Differences

	 * @see Differences

	 */

	public Differences getDifferences()

	{

		return _differences;

	}

	

	/**

	 * Sets Differences

	 * @see Differences

	 */

	public void setDifferences(Differences diff)

	{

		_differences = diff;

	}

	

	/**

	 * Checks if unique Attribute matches

	 */

	public boolean isUniqueAttrMatch()

	{

		return _uniqueAttrMatch;

	}

	

	/**

	 * Sets Unique Attribute match flag

	 */

	public void setUniqueAttrMatch(boolean flag)

	{

		_uniqueAttrMatch = flag;

	}

	

	/**

	 * Checks if its an exact match

	 */

	public boolean isExactMatch()

	{

		return _isExactMatch;

	}

	

	/**

	 * Sets the flag if its an exact match

	 */

	public void setIfExactMatch(boolean flag)

	{

		_isExactMatch = flag;

	}

	

    /**

     * Checks if the NodeResult is a match, either by unique attribute

     * or an exact match

     * <br> Convenience method, since sometimes it is JUST useful to

     * check if its match, immaterial of the type of match

     */

    public boolean isMatch()

    {

        return (isExactMatch() || isUniqueAttrMatch());

    }

    

    /**

     * Gets number of Differences for given Nodes

     */

    public int getNumDifferences()

    {

    	if (_differences == null)

    		return 0;

    	

    	return _differences.size();

    }

    

    /**

     * Gets String representation of the Instance

     */

    public String toString()

    {

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

    	//sb.append("Differences");

    	sb.append(getDifferences());

    	sb.append(StringUtil.getNewlineStr() + "]");

    	

    	return sb.toString();

    }

    

	/**

	 * Main method for debugging purpose only

	 */

	public static void main(String[] args)

	{

	}

}