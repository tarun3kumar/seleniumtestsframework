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
 *
 * import org.apache.oro.text.regex.Pattern;
 *
 * import org.apache.oro.text.regex.PatternCompiler;
 *
 * import org.apache.oro.text.regex.Perl5Compiler;
 *
 * import org.apache.oro.text.regex.Perl5Pattern;
 *
 * import org.apache.oro.text.regex.PatternMatcher;
 *
 * import org.apache.oro.text.regex.PatternMatcherInput;
 *
 * import org.apache.oro.text.regex.MalformedPatternException;
 *
 */

/**
 * Config class containing configuration information about the XMLDog Application including the XML parser.
 *
 * <p/>configuration
 *
 * <p/>
 * <br>
 * Attach Config when instantiating XMLDog to provide your own configuration
 *
 * <p/>
 * <br>
 * Attributes for the Element Node to be excluded during comparison and Only the Attributes
 *
 * <p/>to be used to compare the Element Nodes can be specified.
 *
 * <p/>
 * <br>
 * Elements to be ignored during the comparison can also be specified
 *
 * <p/>
 * <br>
 * Unique attributes for Element Nodes can be specified which will be used to identify matching
 *
 * <p/>Element Nodes
 */

public class Config implements XMLDogConstants {

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
     * Default Constructor.
     */

    public Config() { }

    /**
     * Sets Validating flag.
     */

    public void setValidating(final boolean flag) {

        _isValidating = flag;

    }

    /**
     * Gets Validating flag.
     */

    public boolean isValidating() {

        return _isValidating;

    }

    /**
     * Sets IgnoringWhitespace flag.
     */

    public void setIgnoringWhitespace(final boolean flag) {

        _isIgnoringWS = flag;

    }

    /**
     * Gets IgnoringWhitespace flag.
     */

    public boolean isIgnoringWhitespace() {

        return _isIgnoringWS;

    }

    /**
     * Sets IgnoringOrder flag.
     *
     * <p/>
     * <br>
     * Set this flag to true if the order in which the elements occur doesnt matter,
     *
     * <p/>for stricter comparison set it to false
     */

    public void setIgnoringOrder(final boolean flag) {

        _isIgnoringOrder = flag;

    }

    /**
     * Gets IgnoringOrder flag.
     *
     * <p/>
     * <br>
     * This flag is used to determine if order in which elements occur in Golden
     *
     * <p/>and current Document is ignored
     */

    public boolean isIgnoringOrder() {

        return _isIgnoringOrder;

    }

    /**
     * Sets Namespace aware flag.
     */

    public void setNamespaceAware(final boolean flag) {

        _isNamespaceAware = flag;

    }

    /**
     * Gets Namespace aware flag.
     */

    public boolean isNamespaceAware() {

        return _isNamespaceAware;

    }

    /**
     * Sets flag for ignoring XML Comments.
     */

    public void setIgnoringComments(final boolean flag) {

        _isIgnoringComments = flag;

    }

    /**
     * Checks if XMLDog is ignoring comments.
     */

    public boolean isIgnoringComments() {

        return _isIgnoringComments;

    }

    /**
     * Sets if XMLDog is expanding Entity regferences in the Documents.
     */

    public void setExpandingEntityRefs(final boolean flag) {

        _isExpandingEntityRefs = flag;

    }

    /**
     * Checks if XMLDog is expanding Entity References in the Documents.
     */

    public boolean isExpandingEntityRefs() {

        return _isExpandingEntityRefs;

    }

    /**
     * Sets if XMLDog is in the Detailed mode.
     *
     * <p/>
     * <br>
     * A <i>Detailed</i> mode forces XMLDog to continue finding the
     *
     * <p/>differences in the entire Document versus a <i>Non-Detailed</i> mode
     *
     * <p/>will stop processing the Document as soon as first difference is found.
     *
     * <p/>
     * <br>
     * Use this feature based on the for performance and Application requirements.
     */

    public void setDetailedMode(final boolean flag) {

        _isDetailedMode = flag;

    }

    /**
     * Checks if XMLDog is working in the detailed mode.
     */

    public boolean isDetailedMode() {

        return _isDetailedMode;

    }

    public void setIncludeNodeValuesInXPath(final boolean flag) {

        _includeNodeValuesInXPath = flag;

    }

    public boolean includesNodeValuesInXPath() {

        return _includeNodeValuesInXPath;

    }

    /**
     * Sets Custom difference flag.
     *
     * <p/>If Custom difference is set to true, Differences will be logged identical to
     *
     * <p/>XMLUnit<br>
     *
     * @see  DifferenceConstants
     */

    public void setCustomDifference(final boolean flag) {

        _isCustomDifference = flag;

    }

    /**
     * Checks if Custom Difference is turned on.
     *
     * <p/>If Custom difference is turned on, each Node difference as defined in DifferenceConstants
     *
     * <p/>will be logged
     *
     * @see  DifferenceConstants, NodeDetail
     */

    public boolean isCustomDifference() {

        return _isCustomDifference;

    }

    /**
     * Sets the flag indicating whether EList entries should be applied to the siblings of the.
     *
     * <p/>same type or not<br>
     */

    public void setApplyEListToSiblings(final boolean flag) {

        _elistToSiblings = flag;

    }

    /**
     * Checks if EList entries apply to the siblings or not.
     *
     * @return  true if it does, false otherwise
     */

    public boolean applyEListToSiblings() {

        return _elistToSiblings;

    }

    /**
     * Adds Attribute to be included in the Element comparison.
     */

    public void addIncludedAttribute(final String elementName, final String attrName) {

        if ((elementName == null) || (elementName.trim().equals(""))) {

            return;
        }

        if ((attrName == null) || (attrName.trim().equals(""))) {

            return;
        }

        List attrNames = null;

        if ((attrNames = (List) _includedElementAttrsMap.get(elementName)) == null) {

            attrNames = new ArrayList();
        }

        attrNames.add(attrName);

        _includedElementAttrsMap.put(elementName, attrNames);

    }

    /**
     * Adds Attributes to be included in the Element comparison.
     */

    public void addIncludedAttributes(final String elementName, final List attrNames) {

        List attrNamesList = null;

        if ((attrNamesList = (List) _includedElementAttrsMap.get(elementName)) == null) {

            attrNamesList = new ArrayList();
        }

        attrNamesList.addAll(attrNames);

        _includedElementAttrsMap.put(elementName, attrNamesList);

    }

    /**
     * Adds Attribute to be excluded in the Element comparison.
     */

    public void addExcludedAttribute(final String elementName, final String attrName) {

        if ((elementName == null) || (elementName.trim().equals(""))) {

            return;
        }

        if ((attrName == null) || (attrName.trim().equals(""))) {

            return;
        }

        List attrNames = null;

        if ((attrNames = (List) _excludedElementAttrsMap.get(elementName)) == null) {

            attrNames = new ArrayList();
        }

        attrNames.add(attrName);

        _excludedElementAttrsMap.put(elementName, attrNames);

    }

    /**
     * Adds Attributes to be excluded in the Element comparison.
     */

    public void addExcludedAttributes(final String elementName, final List attrNames) {

        if ((elementName == null) || (elementName.trim().equals(""))) {

            return;
        }

        List attrNamesList = null;

        if ((attrNamesList = (List) _excludedElementAttrsMap.get(elementName)) == null) {

            attrNamesList = new ArrayList();
        }

        attrNamesList.addAll(attrNames);

        _excludedElementAttrsMap.put(elementName, attrNamesList);

    }

    /**
     * Add Unique Attribute to the Element which will force Elements to be identical.
     */

    public void addUniqueAttribute(final String elementName, final String attrName) {

        if ((elementName != null) && (attrName != null)) {

            _uniqueElementAttrMap.put(elementName, attrName);
        }

    }

    /**
     * Adds Element name to the excluded Elements Set.
     */

    public void addExcludedElement(final String elementName) {

        if ((elementName == null) || (elementName.trim().equals(""))) {

            return;
        }

        _excludedElementsSet.add(elementName);

    }

    /**
     * Gets excluded Attributes Map.
     */

    public Map getExcludedAttributesMap() {

        return _excludedElementAttrsMap;

    }

    /**
     * Gets included Attributes Map.
     */

    public Map getIncludedAttributesMap() {

        return _includedElementAttrsMap;

    }

    /**
     * Gets unique Attributes Map.
     */

    public Map getUniqueAttributeMap() {

        return _uniqueElementAttrMap;

    }

    /**
     * Gets excluded Elements Map.
     */

    public Set getExcludedElementsSet() {

        return _excludedElementsSet;

    }

    /**
     * Enables - disables XPath elist.
     *
     * @param  flag  the boolean flag
     */

    public void setXPathEListEnabled(final boolean flag) {

        _elistEnabled = flag;

    }

    /**
     * Checks if XPath elist is enabled.
     *
     * @return  true if elist enabled, false otherwise
     */

    public boolean isXPathEListEnabled() {

        return _elistEnabled;

    }

    /**
     * Loads XPath EList.
     *
     * <p/>
     * <br>
     * Elist is a list of XPath expressions to exclude Nodes represented by the XPath expression
     *
     * <p/>from the comparison
     *
     * @return  the Map containing all the XPath exclusion entries
     */

    public Map loadXPathEList(final String filename) {

        BufferedReader br = null;

        try {

            if (!(new File(filename)).exists()) {

                System.out.println("Elist (" + filename + ") doesn't exist -- exclude list turned off");

            }

            br = new BufferedReader(new FileReader(filename));

            String line;

            String regEx = null;

            while ((line = br.readLine()) != null) {

                /*
                 *
                 * line = line.trim();
                 *
                 * // Regular Expression for Attribute found
                 *
                 *
                 *
                 *      if (line.endsWith(XMLConstants.XPATH_REGEX_END))
                 *
                 *      {
                 *
                 *              regEx = parseRegEx(line);
                 *
                 *
                 *
                 *              if ((regEx != null) && (isValidRegEx(regEx)))
                 *
                 *                      _xpathEList.put(line, regEx);
                 *
                 *      }
                 *
                 *      else
                 *
                 */

                _xpathEList.put(line.trim(), null);

            }

        } catch (IOException ex) {

            ex.printStackTrace();

        } finally {

            if (br != null) {

                try {

                    br.close();

                } catch (Exception ex) { }

                br = null;

            }

        }

        return _xpathEList;

    }

    /**
     * Loads XPath RList<br>
     * Elist is a list of XPath expressions containing Regular Expressions to compare the Nodes with a given Regular
     * Expression.
     *
     * @return  the Map containing all the XPath Node Regular Expression entries
     */

    /*
     *
     * public Map loadXPathRList(String filename)
     *
     * {
     *
     *      BufferedReader br = null;
     *
     *
     *
     *      try
     *
     *      {
     *
     *           if (!(new File(filename)).exists())
     *
     *           {
     *
     *                      System.out.println("Rlist ("+filename+") doesn't exist -- regular expression list turned
     * off");
     *
     *           }
     *
     *
     *
     *           br = new BufferedReader(new FileReader(filename));
     *
     *           String line;
     *
     *           String regEx = null;
     *
     *
     *
     *           while ((line = br.readLine()) != null)
     *
     *           {
     *
     *              line = line.trim();
     *
     *              // Regular Expression for Attribute found
     *
     *                      if (line.endsWith(XMLConstants.XPATH_REGEX_END))
     *
     *                      {
     *
     *                              regEx = parseRegEx(line);
     *
     *
     *
     *                              if ((regEx != null) && (isValidRegEx(regEx)))
     *
     *                                      _xpathRList.put(line, regEx);
     *
     *                      }
     *
     *           }
     *
     *       }
     *
     *       catch (IOException ex)
     *
     *       {
     *
     *           ex.printStackTrace();
     *
     *       }
     *
     *       finally
     *
     *       {
     *
     *              if (br != null)
     *
     *              {
     *
     *                      try
     *
     *                      {
     *
     *                              br.close();
     *
     *                      } catch (Exception ex) {}
     *
     *                      br = null;
     *
     *              }
     *
     *       }
     *
     *
     *
     *      return _xpathRList;
     *
     * }
     *
     */

    /**
     * Checks to see if the Input line has a valid regualr expression<br>
     * Currently Perl 5 Regular Expressions are supported<br>
     * see Apache ORO documentation.
     */

    /*
     *
     * public static boolean isValidRegEx(String regEx)
     *
     * {
     *
     *      PatternCompiler compiler = new Perl5Compiler();
     *
     *
     *
     *      try
     *
     *      {
     *
     *              compiler.compile(regEx);
     *
     *      }
     *
     *      catch (MalformedPatternException mex)
     *
     *      {
     *
     *              mex.printStackTrace();
     *
     *              return false;
     *
     *      }
     *
     *
     *
     *      return true;
     *
     * }
     *
     */

    /**
     * Parses input string for the Regular Expression.
     *
     * <p/>
     * <br>
     * e.g. Line containing Regular Expression for Attribute value
     *
     * <p/>/emp/[@name="value"]
     */

    public static String parseRegEx(String line) {

        if ((line == null) || (line.trim().length() == 0)) {

            return null;
        }

        // Incorrect format if [ is absent

        if (line.indexOf(XMLConstants.XPATH_REGEX_BEGIN) < 0) {

            return null;
        }

        line = line.trim();

        int regExBegin, regExEnd = 0;

        // Incorrect format if multiple ] occurs

        if (((line.indexOf(']')) != (regExEnd = line.lastIndexOf(']')))
                ||

                (regExEnd <= 0)) {

            return null;
        }

        if ((regExBegin = line.lastIndexOf('=')) < 0) {

            return null;
        }

        return line.substring(regExBegin + 2, regExEnd - 1);

    }

    /**
     * Gets Elist containing XPath.
     *
     * @return  the Set containing XPath Elist
     */

    public Map getXPathEList() {

        return _xpathEList;

    }

    /**
     * Prints msg to System.out.
     */

    public static void log(final String msg) {

        if (DEBUG) {

            System.out.println("Config:" + msg);
        }

    }

    /**
     * Prints msg and Exception to System.out.
     */

    public static void log(final String msg, final Throwable t) {

        if (DEBUG) {

            log(msg);

            t.printStackTrace(System.out);

        }

    }

    public static void main(final String[] args) {

        String str = "test";

        System.out.println("index of e " + str.indexOf("e"));

        System.out.println(Config.parseRegEx("/emp/[@name='whatever']"));

    }

}
