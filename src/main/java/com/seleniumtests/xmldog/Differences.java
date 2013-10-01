package com.seleniumtests.xmldog;

import java.util.ArrayList;

import org.w3c.dom.Node;


/**

 * Differences class used to store all the Differences between the Nodes

 * being compared
 */

public class Differences extends ArrayList

	implements DifferenceListener

{		

	/**

	 * Default Constructor

	 */

	public Differences()

	{

		super();

	}

	

	/**

	 * Copy Constructor

	 */

	public Differences(Differences differences)

	{

		addAll(differences);

	}

	

	/**

	 * Adds Differences from the input NodeResult

	 * @see NodeResult

	 */

	public boolean add(NodeResult nr)

	{

		if ((nr != null) && (nr.getDifferences() != null))

		{

			addAll(nr.getDifferences());

			return true;

		}

		

		return false;

	}

	

	/**

	 * Adds Differences from the input Differences	 

	 */

	public boolean add(Differences diffs)

	{

		return addAll(diffs);		

	}

	

	public boolean add(Object obj)

	{

		if (obj instanceof NodeResult)

			return add((NodeResult)obj);

		else

		if (obj instanceof Differences)

			return add((Differences)obj);

		else

			return super.add(obj.toString());

	}

	

	/**

	 * Notifies DifferenceListener that similar node has been found in 

	 * the test Document<br>

	 * Implementation method from DifferenceListener interface

	 */

	public void similarNodeFound(Node controlNode, Node testNode, String msg)

	{

		add(msg);

	}

	

	/**

	 * Notifies DifferenceListener that identical node has been found in 

	 * the test Document<br>

	 * Implementation method from DifferenceListener interface

	 */

	public void identicalNodeFound(Node controlNode, Node testNode, String msg)

	{

		add(msg);

	}

	

	/**

	 * Notifies DifferenceListener that corresponding node has NOT been found in 

	 * the test Document<br>

	 * Implementation method from DifferenceListener interface

	 */

	public void nodeNotFound(Node controlNode, Node testNode, String msg)

	{

		add(msg);

	}

	

	

	/**

	 * Gets difference count

	 */	

	public int getDiffCount()

	{

		return size();

	}

	

	/**

	 * Gets the String representation of the object

	 */

	public String toString()

	{

		StringBuffer sb = new StringBuffer();

		sb.append("Differences:[ size: " + size());

		sb.append(StringUtil.getNewlineStr()); 

		

		if (size() > 0)

		{

			for(int i=0; i<size(); i++)

			{

				//System.out.println(" Element in the differences list is of type " + get(i).getClass());

				sb.append(get(i).toString());				

				sb.append(StringUtil.getNewlineStr());

			}

		}

		else

			sb.append("XML Nodes are identical, No differences found");

		

		sb.append(StringUtil.getNewlineStr());

		sb.append("]");

			

		return sb.toString();

	}

	

	/**

	 * Gets Simple HTML string form of the Differences, with all the individual differences

	 * seperated by line break <br> HTML tag

	 */

	public String getHTML()

	{

		StringBuffer sb = new StringBuffer();		

		sb.append("<UL>");

		

		if (size() > 0)

		{

			for(int i=0; i<size(); i++)

			{

				//System.out.println(" Element in the differences list is of type " + get(i).getClass());

				sb.append("<LI>");

				sb.append(get(i).toString());

				sb.append("</LI>");

			}

		}

		else

			sb.append("<B>Nodes are identical, No differences found</B>");

		

		sb.append("</UL>");		

			

		return sb.toString();

	}

}
