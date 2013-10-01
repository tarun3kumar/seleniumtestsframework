package com.seleniumtests.xmldog;

import org.w3c.dom.Node;



/**

 *

 * Value object that describes a difference between DOM Nodes using one of

 * the DifferenceConstants ID values and a XNode instance.

 * <br />Examples and more at <a href="http://xmlunit.sourceforge.net"/>xmlunit.sourceforge.net</a>

 * @see XNode

 */

public class Difference 

{

    /** Simple unique identifier */

    private int _id;

    /** Description of the difference */

    private final String _description;

    /** TRUE if the difference represents a similarity, FALSE otherwise */

    private boolean _recoverable;

    

    private XNode _xControlNode = null;

    private XNode _xTestNode = null;



    /**

     * Constructor for non-similar Difference instances

     * @param id

     * @param description

     */

    protected Difference(int id, String description) 

    {

        this(id, description, false);

    }



    /**

     * Constructor for similar Difference instances

     * @param id

     * @param description

     */

    protected Difference(int id, String description, boolean recoverable) 

    {

        _id = id;

        _description = description;

        _recoverable = recoverable;

    }

    



    /**

     * Copy constructor using prototype Difference and

     * encountered NodeDetails

     */

    protected Difference(Difference prototype, XNode xControlNode, XNode xTestNode) 

    {

        this(prototype.getId(), prototype.getDescription(), prototype.isRecoverable());

        _xControlNode = xControlNode;

        _xTestNode = xTestNode;

    }

    

    /**

     * @return the id

     */

    public int getId() 

    {

        return _id;

    }

    

    public void setId(int id)

    {

    	_id = id;

    }	



    /**

     * @return the description

     */

    public String getDescription() 

    {

        return _description;

    }



    /**

     * @return TRUE if the difference represents a similarity, FALSE otherwise

     */

    public boolean isRecoverable() 

    {

        return _recoverable;

    }

    

    /**

     * Allow the recoverable field value to be overridden.

     * Used when an override DifferenceListener is used in conjunction with

     * a DetailedDiff.

     */

    protected void setRecoverable(boolean overrideValue) 

    {

    	_recoverable = overrideValue;

    }

    

    /**

     * @return the XNode from the piece of XML used as the control 

     * at the Node where this difference was encountered

     */

    public XNode getControlNodeDetail() 

    {

    	return _xControlNode;

    }



	/**

	 * Sets control Node detail

	 */    

    public void setControlNodeDetail(XNode XNode)

    {

    	_xControlNode = XNode;

    }



    /**

     * @return the XNode from the piece of XML used as the test

     * at the Node where this difference was encountered

     */

    public XNode getTestNodeDetail() 

    {

    	return _xTestNode;

    }

    

    /**

     * Sets test Node detail

     */

    public void setTestNodeDetail(XNode XNode)

    {

    	_xTestNode = XNode;

    }

    

    /**

     * @return a basic representation of the object state and identity

     * and if <code>XNode</code> instances are populated append 

     * their details also

     */

    public String toString() 

    {

    	StringBuffer buf = new StringBuffer();

    	if (_xControlNode == null || _xTestNode == null) 

    	{

    		appendBasicRepresentation(buf);

    	} else 

    	{

    		appendDetailedRepresentation(buf);

    	}

        return buf.toString();

    } 

    

    private void appendBasicRepresentation(StringBuffer buf) 

    {

        buf.append("Difference (#").append(_id).append(") ").append(_description);

    }

    

    private void appendDetailedRepresentation(StringBuffer buf) 

    {    	

    	buf.append("Expected ").append(getDescription())

            .append(" '").append(_xControlNode.getValue())

            .append("' but was '").append(_xTestNode.getValue())

            .append("' - comparing ");

        NodeDescription.appendNodeDetail(buf, _xControlNode);

        buf.append(" to ");

        NodeDescription.appendNodeDetail(buf, _xTestNode);

    }

}