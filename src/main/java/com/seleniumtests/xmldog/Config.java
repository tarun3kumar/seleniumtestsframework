package com.seleniumtests.xmldog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*import org.apache.oro.text.perl.MalformedPerl5PatternException;

import org.apache.oro.text.regex.Pattern;

import org.apache.oro.text.regex.PatternCompiler;

import org.apache.oro.text.regex.Perl5Compiler;

import org.apache.oro.text.regex.Perl5Pattern;

import org.apache.oro.text.regex.PatternMatcher;

import org.apache.oro.text.regex.PatternMatcherInput;

import org.apache.oro.text.regex.MalformedPatternException;

*/





/**

 * Config class containing configuration information about the XMLDog Application including the XML parser

 * configuration 

 * <br> Attach Config when instantiating XMLDog to provide your own configuration

 * <br> Attributes for the Element Node to be excluded during comparison and Only the Attributes

 * to be used to compare the Element Nodes can be specified.

 * <br> Elements to be ignored during the comparison can also be specified

 * <br> Unique attributes for Element Nodes can be specified which will be used to identify matching

 * Element Nodes

 */

public class Config

	implements XMLDogConstants

{

	private boolean _isValidating = false;

	private boolean _isIgnoringWS = true;

	private boolean _isNamespaceAware = false;

	private boolean _isIgnoringComments = true;

	private boolean _isExpandingEntityRefs = true;

	private boolean _isDetailedMode = true;

	private boolean _isCustomDifference = false;

	private boolean _elistEnabled = false;

	private boolean _elistToSiblings = false;

	private boolean _isIgnoringOrder = true;

	private boolean _includeNodeValuesInXPath = true;

	

	// HashMap containing Element names as Keys and List of Attributes

	// as Objects

	private HashMap _includedElementAttrsMap = new HashMap();

	private HashMap _excludedElementAttrsMap = new HashMap();

	private HashSet _excludedElementsSet = new HashSet();

	private HashMap _xpathEList = new HashMap();

	private HashMap _xpathRList = new HashMap();

	

	// HashMap containing Element names as Keys and unique attribute

	// names as objects

	private HashMap _uniqueElementAttrMap = new HashMap();

	

	/**

	 *  Default Constructor

	 */

	public Config()

	{

	}



	/**

	 * Sets Validating flag

	 */	

	public void setValidating(boolean flag)

	{

		_isValidating = flag;		

	}

	

	/**

	 *  Gets Validating flag

	 */

	public boolean isValidating()

	{

		return _isValidating;

	}

	

	/**

	 *  Sets IgnoringWhitespace flag

	 */

	public void setIgnoringWhitespace(boolean flag)

	{

		_isIgnoringWS = flag;

	}

	

	/**

	 *  Gets IgnoringWhitespace flag

	 */

	public boolean isIgnoringWhitespace()

	{

		return _isIgnoringWS;

	}

	

	/**

	 * Sets IgnoringOrder flag

	 * <br>Set this flag to true if the order in which the elements occur doesnt matter, 

	 * for stricter comparison set it to false

	 */

	public void setIgnoringOrder(boolean flag)

	{

		_isIgnoringOrder = flag;

	}

	

	/**

	 *  Gets IgnoringOrder flag

	 * <br> This flag is used to determine if order in which elements occur in Golden

	 * and current Document is ignored

	 */

	public boolean isIgnoringOrder()

	{

		return _isIgnoringOrder;

	}



	/**

	 *  Sets Namespace aware flag

	 */	

	public void setNamespaceAware(boolean flag)

	{

		_isNamespaceAware = flag;		

	}

	

	/**

	 *  Gets Namespace aware flag

	 */

	public boolean isNamespaceAware()

	{

		return _isNamespaceAware;

	}



	/**

	 * Sets flag for ignoring XML Comments

	 */	

	public void setIgnoringComments(boolean flag)

	{

		_isIgnoringComments = flag;

	}



	/**

	 * Checks if XMLDog is ignoring comments

	 */	

	public boolean isIgnoringComments()

	{

		return _isIgnoringComments;

	}

	

	/**

	 * Sets if XMLDog is expanding Entity regferences in the Documents

	 */

	public void setExpandingEntityRefs(boolean flag)

	{

		_isExpandingEntityRefs = flag;

	}

	

	/**

	 * Checks if XMLDog is expanding Entity References in the Documents

	 */

	public boolean isExpandingEntityRefs()

	{

		return _isExpandingEntityRefs;

	}

	

	/**

	 * Sets if XMLDog is in the Detailed mode.

	 * <br> A <i>Detailed</i> mode forces XMLDog to continue finding the 

	 * differences in the entire Document versus a <i>Non-Detailed</i> mode

	 * will stop processing the Document as soon as first difference is found.

	 * <br> Use this feature based on the for performance and Application requirements.

	 */

	public void setDetailedMode(boolean flag)

	{

		_isDetailedMode = flag;

	}



	/**

	 * Checks if XMLDog is working in the detailed mode

	 */	

	public boolean isDetailedMode()

	{

		return _isDetailedMode;

	}

	

	public void setIncludeNodeValuesInXPath(boolean flag)

	{

		_includeNodeValuesInXPath = flag;

	}

	

	public boolean includesNodeValuesInXPath()

	{

		return _includeNodeValuesInXPath;

	}

	

	/**

	 * Sets Custom difference flag

	 * If Custom difference is set to true, Differences will be logged identical to 

	 * XMLUnit<br>

	 * @see DifferenceConstants

	 */

	public void setCustomDifference(boolean flag)

	{

		_isCustomDifference = flag;

	}

	

	/**

	 * Checks if Custom Difference is turned on

	 * If Custom difference is turned on, each Node difference as defined in DifferenceConstants

	 * will be logged 

	 * @see DifferenceConstants, NodeDetail

	 */

	public boolean isCustomDifference()

	{

		return _isCustomDifference;

	}

	

	/**

	 * Sets the flag indicating whether EList entries should be applied to the siblings of the

	 * same type or not<br>

	 */

	public void setApplyEListToSiblings(boolean flag)

	{

		_elistToSiblings = flag;

	}

	

	/**

	 * Checks if EList entries apply to the siblings or not

	 * @return true if it does, false otherwise

	 */

	public boolean applyEListToSiblings()

	{

		return _elistToSiblings;

	}

	

	/**

	 * Adds Attribute to be included in the Element comparison

	 */

	public void addIncludedAttribute(String elementName, String attrName)

	{

		if ((elementName == null) || (elementName.trim().equals("")))

			return;

		

		if ((attrName == null) || (attrName.trim().equals("")))

			return;

			

		List attrNames = null;

		

		if((attrNames = (List)_includedElementAttrsMap.get(elementName)) == null)		

			attrNames = new ArrayList();

		

		attrNames.add(attrName)	;

		

		_includedElementAttrsMap.put(elementName, attrNames);

		

	}

	

	/**

	 * Adds Attributes to be included in the Element comparison

	 */

	public void addIncludedAttributes(String elementName, List attrNames)

	{

		List attrNamesList = null;

		

		if((attrNamesList = (List)_includedElementAttrsMap.get(elementName)) == null)		

			attrNamesList = new ArrayList();

		

		attrNamesList.addAll(attrNames)	;

		

		_includedElementAttrsMap.put(elementName, attrNamesList);

	}

	

	/**

	 * Adds Attribute to be excluded in the Element comparison

	 */

	public void addExcludedAttribute(String elementName, String attrName)

	{

		if ((elementName == null) || (elementName.trim().equals("")))

			return;

		

		if ((attrName == null) || (attrName.trim().equals("")))

			return;

			

		List attrNames = null;

		

		if((attrNames = (List)_excludedElementAttrsMap.get(elementName)) == null)		

			attrNames = new ArrayList();

		

		attrNames.add(attrName)	;

		

		_excludedElementAttrsMap.put(elementName, attrNames);

	}

	

	/**

	 * Adds Attributes to be excluded in the Element comparison

	 */

	public void addExcludedAttributes(String elementName, List attrNames)

	{

		if ((elementName == null) || (elementName.trim().equals("")))

			return;



		List attrNamesList = null;

		

		if((attrNamesList = (List)_excludedElementAttrsMap.get(elementName)) == null)		

			attrNamesList = new ArrayList();

		

		attrNamesList.addAll(attrNames)	;

		

		_excludedElementAttrsMap.put(elementName, attrNamesList);

	}

	

	/**

	 * Add Unique Attribute to the Element which will force Elements to be identical

	 */

	public void addUniqueAttribute(String elementName, String attrName)

	{

		if ((elementName != null) && (attrName != null))

			_uniqueElementAttrMap.put(elementName, attrName);

	}

	

	/**

	 * Adds Element name to the excluded Elements Set

	 */

	public void addExcludedElement(String elementName)

	{

		if ((elementName == null) || (elementName.trim().equals("")))

			return;

			

		_excludedElementsSet.add(elementName);

	}

	

	/**

	 * Gets excluded Attributes Map

	 */

	public Map getExcludedAttributesMap()

	{

		return _excludedElementAttrsMap;

	}

		

	/**

	 * Gets included Attributes Map

	 */

	public Map getIncludedAttributesMap()

	{

		return _includedElementAttrsMap;

	}

	

	/**

	 * Gets unique Attributes Map

	 */

	public Map getUniqueAttributeMap()

	{

		return _uniqueElementAttrMap;

	}

	

	/**

	 * Gets excluded Elements Map

	 */

	public Set getExcludedElementsSet()

	{

		return _excludedElementsSet;

	}

	

	/**

	 * Enables - disables XPath elist 

	 * @param flag the boolean flag

	 */

	public void setXPathEListEnabled(boolean flag)

	{

		_elistEnabled = flag;			

	}

	

	/**

	 * Checks if XPath elist is enabled

	 * @return true if elist enabled, false otherwise

	 */

	public boolean isXPathEListEnabled()

	{

		return _elistEnabled;

	}

	

	

	/**

	 * Loads XPath EList

	 * <br>Elist is a list of XPath expressions to exclude Nodes represented by the XPath expression

	 * from the comparison

	 * @return the Map containing all the XPath exclusion entries

	 */

	public Map loadXPathEList(String filename)

	{		

		BufferedReader br = null;

					

		try

		{

		     if (!(new File(filename)).exists()) 

		     {						 	

			 	System.out.println("Elist ("+filename+") doesn't exist -- exclude list turned off");						 	

		     }

			    

		     br = new BufferedReader(new FileReader(filename));

		     String line;		     

		     String regEx = null;

		     

		     while ((line = br.readLine()) != null)

		     {

		     	/*

		     	line = line.trim();

		     	// Regular Expression for Attribute found

		     	

			 	if (line.endsWith(XMLConstants.XPATH_REGEX_END))

			 	{

			 		regEx = parseRegEx(line);

			 		

			 		if ((regEx != null) && (isValidRegEx(regEx)))

			 			_xpathEList.put(line, regEx);

			 	}

			 	else

			 	*/

			 	_xpathEList.put(line.trim(), null);

		     }

		 }

		 catch (IOException ex)

		 {					     

		     ex.printStackTrace();

		 }

		 finally

		 {

	 		if (br != null)

	 		{

	 			try

	 			{

	 				br.close();

	 			} catch (Exception ex) {}

	 			br = null;

	 		}

		 }

					 

		return _xpathEList;

	}

	

	/**

	 * Loads XPath RList

	 * <br>Elist is a list of XPath expressions containing Regular Expressions to 

	 * compare the Nodes with a given Regular Expression

	 * @return the Map containing all the XPath Node Regular Expression entries

	 */

	/*

	public Map loadXPathRList(String filename)

	{		

		BufferedReader br = null;

					

		try

		{

		     if (!(new File(filename)).exists()) 

		     {						 	

			 	System.out.println("Rlist ("+filename+") doesn't exist -- regular expression list turned off");						 	

		     }

			    

		     br = new BufferedReader(new FileReader(filename));

		     String line;		     

		     String regEx = null;

		     

		     while ((line = br.readLine()) != null)

		     {

		     	line = line.trim();

		     	// Regular Expression for Attribute found

			 	if (line.endsWith(XMLConstants.XPATH_REGEX_END))

			 	{

			 		regEx = parseRegEx(line);

			 		

			 		if ((regEx != null) && (isValidRegEx(regEx)))

			 			_xpathRList.put(line, regEx);

			 	}

		     }					     					     

		 }

		 catch (IOException ex)

		 {					     

		     ex.printStackTrace();

		 }

		 finally

		 {

	 		if (br != null)

	 		{

	 			try

	 			{

	 				br.close();

	 			} catch (Exception ex) {}

	 			br = null;

	 		}

		 }

					 

		return _xpathRList;

	} 

	*/

	

	/**

	 * Checks to see if the Input line has a valid regualr expression

	 * <br>Currently Perl 5 Regular Expressions are supported

	 * <br> see Apache ORO documentation

	 */

	/*

	public static boolean isValidRegEx(String regEx)

	{

		PatternCompiler compiler = new Perl5Compiler();

		

		try

		{

			compiler.compile(regEx);

		}

		catch (MalformedPatternException mex)

		{

			mex.printStackTrace();

			return false;

		}

		

		return true;

	}

	*/

	

	/**

	 * Parses input string for the Regular Expression

	 * <br>e.g. Line containing Regular Expression for Attribute value 

	 * /emp/[@name="value"]

	 */

	public static String parseRegEx(String line)

	{

		if ((line == null) || (line.trim().length() == 0))

			return null;

		

		// Incorrect format if [ is absent

		if (line.indexOf(XMLConstants.XPATH_REGEX_BEGIN) < 0)

			return null;

				

		line = line.trim();		

		int regExBegin, regExEnd = 0;

		

		// Incorrect format if multiple ] occurs

		if (((line.indexOf(']')) != ((regExEnd = line.lastIndexOf(']')))) ||

			(regExEnd <= 0))

			return null;

			

		if((regExBegin = line.lastIndexOf('=')) < 0)

			return null;

		

		

		return line.substring(regExBegin+2, regExEnd-1);

	}

	

	/**

	 * Gets Elist containing XPath

	 * @return the Set containing XPath Elist

	 */

	public Map getXPathEList()

	{

		return _xpathEList;

	}

		

	

	/**

	 * Prints msg to System.out

	 */

	public static void log(String msg)

	{

		if (DEBUG)

			System.out.println("Config:" + msg);

	}

	

	/**

	 * Prints msg and Exception to System.out

	 */

	public static void log(String msg, Throwable t)

	{

		if (DEBUG)

		{		

			log(msg);

			t.printStackTrace(System.out);

		}

	}

	

	public static void main(String[] args)

	{

		String str = "test";

		

		System.out.println("index of e " + str.indexOf("e"));

		

		System.out.println(Config.parseRegEx("/emp/[@name='whatever']"));

	}

}