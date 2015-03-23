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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class XMLUtil

        implements XMLDogConstants

{

    private static DocumentBuilder _docBuilder = null;


    static

    {

        try

        {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            /*

            factory.setExpandEntityReferences(false);

            factory.setValidating(false);

             */

            factory.setNamespaceAware(true);

            _docBuilder = factory.newDocumentBuilder();


            //System.out.println("Is factory name space aware " + factory.isNamespaceAware());

            //_docBuilder.setEntityResolver(new MyEntityResolver());

        } catch (Exception ex)

        {

            ex.printStackTrace();

        }

    }


    /**
     * Default Constructor
     */

    public XMLUtil()

    {

    }


    /**
     * Gets Nodes identical to a given test Node for a given parent Node
     */

    public static List getSimilarChildNodes(Node parent, Node testNode, boolean ignoreWhitespace)

    {

        ArrayList nodes = new ArrayList();


        if (!parent.hasChildNodes())

            return nodes;


        NodeList childNodes = parent.getChildNodes();


        for (int i = 0; i < childNodes.getLength(); i++)

        {

            if (nodesSimilar(childNodes.item(i), testNode, ignoreWhitespace))

                nodes.add(childNodes.item(i));

        }


        return nodes;

    }


    /**
     * Gets XNodes identical to a given test Node for a given parent Node
     * <p/>
     * <br> XNode in this case is used to store the position of the Node under the parent
     */

    public static List getSimilarChildXNodes(Node parent, Node testNode, boolean ignoreWhitespace)

    {

        ArrayList nodes = new ArrayList();


        if (!parent.hasChildNodes())

            return nodes;


        NodeList childNodes = parent.getChildNodes();

        XNode xNode = null;

        for (int i = 0; i < childNodes.getLength(); i++)

        {

            if (nodesSimilar(childNodes.item(i), testNode, ignoreWhitespace))

            {

                xNode = new XNode(childNodes.item(i), null);

                xNode.setPosition(i);

                nodes.add(xNode);

            }

        }


        return nodes;

    }


    /**
     * Replaces Elements subtree with the text value in the XML document<br>
     * <p/>
     * IMPORTANT!! This method removes entire subtree of the Element and puts the text as a child node.
     * <p/>
     * Use this method when only one Element tag needs to be changed in the XML file.
     *
     * @param xmlFile   the fully qualified name of the XML file
     * @param tagName   the element tag whose text needs to be changed
     * @param value     the new text value for the Element
     * @param all       true if all the matching Elements values needs to be changed, false otherwise
     * @param overwrite true if File needs to be overwritten with the changes, false otherwise
     * @return the String representation of the new XML document with elements replaced
     * <p/>
     * if successful, null otherwise
     */

    public static String replaceElementText(String xmlFile, String tagName, String value, boolean all, boolean overwrite)

            throws DOMException

    {

        Map elements = new HashMap();

        elements.put(tagName, value);

        return replaceElementText(xmlFile, elements, all, overwrite);

    }


    /**
     * Gets XML Document for a given document path
     *
     * @param docPath the path to the XML file
     * @return the XML Document object
     * @see org.w3c.dom.Document
     */

    public static Document getDocument(String docPath)

            throws SAXException, IOException

    {

        return _docBuilder.parse(docPath);

    }


    /**
     * Replaces Elements subtree with the text value in the XML document<br>
     * <p/>
     * IMPORTANT!! This method removes entire subtree of all the Elements and puts the text as a child node.
     * <p/>
     * Use this method when multiple Element tags needs to be changed in the XML file.
     *
     * @param xmlFile   the fully qualified name of the XML file
     * @param elements  the Map containing Element tag names and corresponding Text values
     * @param all       true if all the matching Elements values needs to be changed, false otherwise
     * @param overwrite true if File needs to be overwritten with the changes, false otherwise
     * @return the String representation of the new XML document with all the Elements replaced
     */

    public static String replaceElementText(String xmlFile, Map elements, boolean all, boolean overwrite)

            throws DOMException

    {

        if ((xmlFile == null) ||

                (elements == null))

            return null;


        File file = null;

        FileWriter fw = null;

        Document doc = null;


        try

        {

            doc = _docBuilder.parse(xmlFile);


            String xmlStr = print(doc, false, false);


            //System.out.println("********* Document is " + xmlStr);

            Set allElements = elements.keySet();


            if (allElements.size() > 0)

            {

                Iterator iterator = allElements.iterator();

                while (iterator.hasNext())

                {

                    String tagName = (String) iterator.next();

                    String value = (String) elements.get(tagName);

                    NodeList nodes = doc.getDocumentElement().getElementsByTagName(tagName);


                    if (value != null)

                    {

                        for (int i = 0; i < nodes.getLength(); i++)

                        {

                            if ((!all) && (i > 0))

                                break;


                            Node element = nodes.item(i);

                            String eText = getText(element);

                            //System.out.println("Element : " + ((Element)element).getTagName() + ":Value:" + eText);

                            element = removeChildren(element);

                            Text textNode = doc.createTextNode(value);

                            element.appendChild(textNode);

                        }

                    }

                }


                String newXMLStr = print(doc, false, false);

                //System.out.println("***New document is " + newXMLStr);


                if (overwrite)

                {

                    file = new File(xmlFile);

                    file.renameTo(new File(xmlFile + ".bak"));


                    fw = new FileWriter(file);

                    fw.write(newXMLStr);

                    fw.flush();

                }


                return newXMLStr;

            }

        } catch (Exception ex)

        {

            ex.printStackTrace(System.out);

            throw new DOMException(DOMException.INVALID_MODIFICATION_ERR, "Error replacing Element text " + ex.toString());

        } finally

        {

            try

            {

                if (fw != null)

                    fw.close();

            } catch (IOException ex)

            {

                // do nothing

            }

            file = null;

            fw = null;

            doc = null;

        }


        return null;

    }


    /**
     * Replaces Elements subtree with the text value in the XML document<br>
     * <p/>
     * IMPORTANT!! This method removes entire subtree of all the Elements and puts the text as a child node.
     * <p/>
     * Use this method when multiple OCCRENCES of the Element tags needs to be changed with one value each
     * <p/>
     * from the values list in that order in the XML file.
     *
     * @param xmlFile    the fully qualified name of the XML file
     * @param elementTag the Element tag whose value is to be changed
     * @param values     the List containing values for each occerence of the Element tag
     * @param overwrite  true if File needs to be overwritten with the changes, false otherwise
     * @return the String representation of the new XML document with all the Elements replaced
     */

    public static String replaceElementText(String xmlFile, String elementTag, List values, boolean overwrite)

            throws DOMException

    {

        File file = null;

        FileWriter fw = null;

        Document doc = null;


        if ((xmlFile == null) ||

                (elementTag == null) || (elementTag.trim().equals("")) ||

                (values == null))

            return null;


        try

        {

            doc = _docBuilder.parse(xmlFile);

            String xmlStr = print(doc, false, false);


            //System.out.println("********* Document is " + xmlStr);


            NodeList nodes = doc.getDocumentElement().getElementsByTagName(elementTag);


            int limit = nodes.getLength() < values.size() ? nodes.getLength() : values.size();

            for (int i = 0; i < limit; i++)

            {

                Node element = nodes.item(i);

                element = removeChildren(element);

                Text textNode = doc.createTextNode((String) values.get(i));

                element.appendChild(textNode);

            }


            // If more values than the elements found

            // add more elements

            if (values.size() > nodes.getLength())

            {

                Element parent = (Element) nodes.item(0).getParentNode();


                for (int i = nodes.getLength(); i < values.size(); i++)

                {

                    if ((values.get(i) != null) && (!((String) values.get(i)).trim().equals("")))

                    {

                        //System.out.println("********** XML value to be put is NOT null");

                        Element newElement = doc.createElement(elementTag);

                        Text textNode = doc.createTextNode((String) values.get(i));

                        newElement.appendChild(textNode);

                        parent.appendChild(newElement);

                    }

                }

            }


            String newXMLStr = print(doc, false, false);

            //System.out.println("***New document is " + newXMLStr);


            if (overwrite)

            {

                file = new File(xmlFile);

                file.renameTo(new File(xmlFile + ".bak"));


                fw = new FileWriter(file);

                fw.write(newXMLStr);

                fw.flush();

            }


            return newXMLStr;


        } catch (Exception ex)

        {

            ex.printStackTrace();

            throw new DOMException(DOMException.INVALID_MODIFICATION_ERR, "Error replacing Element text " + ex.toString());

        } finally

        {

            try

            {

                if (fw != null)

                    fw.close();

            } catch (IOException ex)

            {

                // do nothing

            }

            file = null;

            fw = null;

            doc = null;

        }

    }


    /**
     * Removes all the children of a given Node
     *
     * @param node the Node whose children is to be removed
     * @return Node after the children removed
     */

    public static Node removeChildren(Node node)

    {

        /*

        System.out.println(" Node whose children are to be removed:" + node.getNodeName() + 

                                    ":Type:" + node.getNodeType() + ":value:" + node.getNodeValue());

         */

        // Currently remove children of only Element Nodes

        if (node.getNodeType() == Node.ELEMENT_NODE)

        {

            NodeList children = node.getChildNodes();

            //System.out.println("***** XMLUTIL....removeChildren....# of children are " + children.getLength());


            for (int i = 0; i < children.getLength(); i++)

            {

                Node child = children.item(i);

                /*

                System.out.println("Child Name:" + child.getNodeName() + 

                                        ":Type:" + child.getNodeType() + " value:" + child.getNodeValue() + ":i= " + i);

                 */


                Node temp = node.removeChild(child);

                /*

                System.out.println("Removed Name:" + temp.getNodeName() + 

                                    ":Type:" + temp.getNodeType() + " value:" + temp.getNodeValue() + ":i= " + i);

                 */

                i--;

            }

        }

        return node;

    }


    /**
     * � * Return the text that a node contains. This routine:<ul>
     * <p/>
     * � * <li>Ignores comments and processing instructions.
     * <p/>
     * � * <li>Concatenates TEXT nodes, CDATA nodes, and the results of
     * <p/>
     * � *     recursively processing EntityRef nodes.
     * <p/>
     * � * <li>Ignores any element nodes in the sublist.
     * <p/>
     * � *     (Other possible options are to recurse into element sublists
     * <p/>
     * � *      or throw an exception.)
     * <p/>
     * � * </ul>
     * <p/>
     * � * @param    node  a  DOM node
     * <p/>
     * � * @return   a String representing its contents
     * <p/>
     * �
     */

    public static String getText(Node node)

    {

        StringBuffer result = new StringBuffer();

        if (!node.hasChildNodes())

            return "";


        NodeList list = node.getChildNodes();

        for (int i = 0; i < list.getLength(); i++)

        {

            Node subnode = list.item(i);

            if (subnode.getNodeType() == Node.TEXT_NODE)

            {

                result.append(subnode.getNodeValue());

            } else if (subnode.getNodeType() == Node.CDATA_SECTION_NODE)

            {

                result.append(subnode.getNodeValue());

            } else if (subnode.getNodeType() == Node.ENTITY_REFERENCE_NODE)

            {

                // Recurse into the subtree for text

                // (and ignore comments)

                result.append(getText(subnode));

            }

        }

        return result.toString();

    }


    /**
     * Gets String representation of a given Node
     *
     * @param node      the Node to be converted to string
     * @param canonical the flag to indicate if conversion is canonical or not
     * @param normalize true if normalized document is to be returned, false otherwise
     *                  <p/>
     *                  <br>Normalized document means all the special characters in the document are replaced with their
     *                  <p/>
     *                  corresponding Entity References
     * @return the String representation of Node
     */

    public static String print(Node node, boolean canonical, boolean normalize)

    {

        StringBuffer sb = new StringBuffer();


        // is there anything to do?

        if (node == null)

        {

            return null;

        }


        int type = node.getNodeType();

        //System.out.println("IN the print function Node type " + type);


        switch (type)

        {

            // print document

            case Node.DOCUMENT_NODE:

            {

                if (!canonical)

                {

                    String Encoding = "UTF-8";

                    sb.append("<?xml version=\"1.0\" encoding=\"" + Encoding + "\"?>");

                    sb.append(StringUtil.getNewlineStr());

                }

                print(((Document) node).getDocumentElement(), canonical, normalize);


                NodeList children = node.getChildNodes();

                for (int iChild = 0; iChild < children.getLength(); iChild++)

                {

                    sb.append(print(children.item(iChild), canonical, normalize));

                }

                break;

            }


            // print element with attributes

            case Node.ELEMENT_NODE:

            {

                sb.append('<');

                sb.append(node.getNodeName());

                Attr attrs[] = sortAttributes(node.getAttributes());

                for (int i = 0; i < attrs.length; i++)

                {

                    Attr attr = attrs[i];

                    sb.append(' ');

                    sb.append(attr.getNodeName());

                    sb.append("=\"");

                    if (normalize)

                        sb.append(normalize(attr.getNodeValue(), canonical));

                    else

                        sb.append(attr.getNodeValue());


                    sb.append('"');

                }

                sb.append('>');

                NodeList children = node.getChildNodes();

                if (children != null)

                {

                    int len = children.getLength();

                    for (int i = 0; i < len; i++)

                    {

                        sb.append(print(children.item(i), canonical, normalize));

                    }

                }

                break;

            }


            // handle entity reference nodes

            case Node.ENTITY_REFERENCE_NODE:

            {

                if (canonical)

                {

                    NodeList children = node.getChildNodes();

                    if (children != null)

                    {

                        int len = children.getLength();

                        for (int i = 0; i < len; i++)

                        {

                            sb.append(print(children.item(i), canonical, normalize));

                        }

                    }

                } else

                {

                    sb.append('&');

                    System.out.println("Entity node name " + node.getNodeName());

                    sb.append(node.getNodeName());

                    sb.append(';');

                }

                break;

            }


            // print cdata sections

            case Node.CDATA_SECTION_NODE:

            {

                if (canonical)

                {

                    if (normalize)

                        sb.append(normalize(node.getNodeValue(), canonical));

                    else

                        sb.append(node.getNodeValue());

                } else

                {

                    sb.append("<![CDATA[");

                    sb.append(node.getNodeValue());

                    sb.append("]]>");

                }

                break;

            }


            // print text

            case Node.TEXT_NODE:

            {

                // Normalize the element text no matter what

                //  Ritesh

                //if (normalize)                    

                sb.append(normalize(node.getNodeValue(), canonical));

                /*else

                    sb.append(node.getNodeValue());

                 */

                break;

            }


            // print processing instruction

            case Node.PROCESSING_INSTRUCTION_NODE:

            {

                sb.append("<?");

                sb.append(node.getNodeName());

                String data = node.getNodeValue();

                if (data != null && data.length() > 0)

                {

                    sb.append(' ');

                    sb.append(data);

                }

                sb.append("?>");

                break;

            }


            case Node.DOCUMENT_TYPE_NODE:

            {

                //System.out.println("Doc Type name:" + node.getNodeName());

                //System.out.println("Doc type value:" + node.getNodeValue());

                sb.append("<!DOCTYPE ");

                sb.append(node.getNodeName());

                sb.append(" PUBLIC ");

                sb.append("\"" + ((DocumentType) node).getPublicId() + "\" ");

                sb.append(" \"" + ((DocumentType) node).getSystemId() + "\"");

                sb.append(">");

                sb.append(StringUtil.getNewlineStr());

                //System.out.println("System id " + ((DocumentType)node).getSystemId());

                //System.out.println("public id " + ((DocumentType)node).getPublicId());

                break;

            }

        }


        if (type == Node.ELEMENT_NODE)

        {

            sb.append("</");

            sb.append(node.getNodeName());

            sb.append('>');

        }


        return sb.toString();


    } // print(Node)


    /**
     * Normalizes a given string
     *
     * @param s the String to be normalized
     */

    protected static String normalize(String s, boolean canonical)

    {

        StringBuffer str = new StringBuffer();


        int len = (s != null) ? s.length() : 0;

        for (int i = 0; i < len; i++)

        {

            char ch = s.charAt(i);

            switch (ch)

            {

                case '<':

                {

                    str.append("&lt;");

                    break;

                }

                case '>':

                {

                    str.append("&gt;");

                    break;

                }

                case '&':

                {

                    str.append("&amp;");

                    break;

                }

                case '"':

                {

                    str.append("&quot;");

                    break;

                }

                case '\'':

                {

                    str.append("&apos;");

                    break;

                }

                case '\r':

                case '\n':

                {

                    if (canonical)

                    {

                        str.append("&#");

                        str.append(Integer.toString(ch));

                        str.append(';');

                        break;

                    }

                    // else, default append char

                }

                default:

                {

                    str.append(ch);

                }

            }

        }


        return (str.toString());


    } // normalize(String):String


    /**
     * Sorts Attributes of a given Node
     *
     * @param attrs the NamedNodeMap containing Node Attributes
     * @return Array containing sorted Attributes
     */

    protected static Attr[] sortAttributes(NamedNodeMap attrs)

    {

        int len = (attrs != null) ? attrs.getLength() : 0;

        Attr array[] = new Attr[len];

        for (int i = 0; i < len; i++)

        {

            array[i] = (Attr) attrs.item(i);

        }


        for (int i = 0; i < len - 1; i++)

        {

            String name = array[i].getNodeName();

            int index = i;

            for (int j = i + 1; j < len; j++)

            {

                String curName = array[j].getNodeName();

                if (curName.compareTo(name) < 0)

                {

                    name = curName;

                    index = j;

                }

            }

            if (index != i)

            {

                Attr temp = array[i];

                array[i] = array[index];

                array[index] = temp;

            }

        }


        return (array);

    } // sortAttributes(NamedNodeMap):Attr[]


    /**
     * Gets parent XPath from the child XPath
     * <p/>
     * <br> XPath string is obtained by stripping all the characters from the last
     * <p/>
     * "/" character
     *
     * @param childXPath the child XPath
     */

    public static String generateXPath(String childXPath)

    {

        return childXPath.substring(0, childXPath.lastIndexOf("/"));

    }


    /**
     * Generates XPath expression for the given node, relative to the Root of the Document
     */

    public static String generateXPath(Node node, boolean ignoreWhitespace)

    {

        return generateXPath(node, ignoreWhitespace, false);

    }


    /**
     * Generates XPath expression with the option of the Node values and Node indexes are included
     *
     * @param node             the Node whose XPath is to be found
     * @param ignoreWhitespace the flag to indicate if Whitespace will be ignored
     * @param noValues         the flag to indicate if Node values will be included
     * @return the XPath string representation of the Node
     */

    public static String generateXPath(Node node, boolean ignoreWhitespace, boolean noValues)

    {

        return generateXPath(node, ignoreWhitespace, noValues, false);

    }


    /**
     * Generates XPath expression with the option of the Node values appended
     *
     * @param node             the Node whose XPath is to be found
     * @param ignoreWhitespace the flag to indicate if Whitespace will be ignored
     * @param includeValues    the flag to indicate if Node values will be included
     * @param noIndex          the flag to indicate if Node indexes are included
     * @return the XPath string representation of the Node
     */

    public static String generateXPath(Node node, boolean ignoreWhitespace, boolean includeValues, boolean noIndex)

    {

        boolean noValues = !includeValues;

        if (node == null)

            return "";


        Node parent = node.getParentNode();

        int index = noIndex ? 0 : getXPathNodeIndex(node, ignoreWhitespace);

        String indexStr = "";


        if (index > 0)

            indexStr = "[" + Integer.toString(index) + "]";


        //printNode(node);

        //printNode(parent);


        if (node.getNodeType() == Node.DOCUMENT_NODE)

        {

            // return only the blank String, since all the other types are preceded with /

            return "";

        } else if (node.getNodeType() == Node.TEXT_NODE)

        {

            return generateXPath(parent, ignoreWhitespace, noValues, noIndex) +

                    (noValues ? node.getNodeValue() + indexStr : "/TEXT(" + node.getNodeValue() + ")" + indexStr);

        } else if (node.getNodeType() == Node.ELEMENT_NODE)

        {

            return generateXPath(parent, ignoreWhitespace, noValues, noIndex) +

                    "/" + node.getNodeName() + indexStr;

        } else if (node.getNodeType() == Node.COMMENT_NODE)

        {

            return generateXPath(parent, ignoreWhitespace, noValues, noIndex) +

                    (noValues ? node.getNodeValue() + indexStr : "/COMMENT(" + node.getNodeValue() + ")" + indexStr);

        } else if (node.getNodeType() == Node.ENTITY_REFERENCE_NODE)

        {

            return generateXPath(parent, ignoreWhitespace, noValues, noIndex) +

                    (noValues ? node.getNodeValue() + indexStr : "/EntityReference(" + node.getNodeValue() + ")" + indexStr);

        } else if (node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE)

        {

            return generateXPath(parent, ignoreWhitespace, noValues, noIndex) +

                    (noValues ? node.getNodeValue() + indexStr : "/PI(" + node.getNodeValue() + ")" + indexStr);

        } else if (node.getNodeType() == Node.ATTRIBUTE_NODE)

        {

            return generateXPath(((Attr) node).getOwnerElement(), ignoreWhitespace, noValues, noIndex) +

                    "/'@" + node.getNodeName() +

                    (noValues ? "" : "=" + node.getNodeValue()) + "]";

        } else if (node.getNodeType() == Node.DOCUMENT_TYPE_NODE)

        {

            return generateXPath(parent, ignoreWhitespace, noValues, noIndex) +

                    (noValues ? node.getNodeValue() : "/DOCTYPE(" + node.getNodeName() + ")");

        } else if (node.getNodeType() == Node.CDATA_SECTION_NODE)

        {

            return generateXPath(parent, ignoreWhitespace, noValues, noIndex) +

                    (noValues ? node.getNodeValue() : "/CDATA(" + node.getNodeName() + ")");

        }


        // Wont reach this far but just in case

        return "";

    }


    /**
     * Generates XPath expression with the option of the Node values appended
     *
     * @param node             the Node whose XPath is to be found
     * @param parentXPath      the XPath of the parent Node
     * @param ignoreWhitespace the flag to indicate if Whitespace will be ignored
     * @param includeValues    the flag to indicate if Node values will be included
     * @param noIndex          the flag to indicate if Node indexes are included
     * @return the XPath string representation of the Node
     */

    public static String generateXPath(Node node, String parentXPath, boolean ignoreWhitespace, boolean includeValues, boolean noIndex)

    {

        boolean noValues = !includeValues;

        if (node == null)

            return "";


        Node parent = node.getParentNode();

        int index = noIndex ? 0 : getXPathNodeIndex(node, ignoreWhitespace);

        String indexStr = "";


        if (index > 0)

            indexStr = "[" + Integer.toString(index) + "]";


        if (node.getNodeType() == Node.DOCUMENT_NODE)

        {

            // return only the blank String, since all the other types are preceded with /

            return parentXPath + "";

        } else if (node.getNodeType() == Node.TEXT_NODE)

        {

            return parentXPath +

                    (noValues ? "/" + node.getNodeValue() + indexStr : "/TEXT(" + node.getNodeValue() + ")" + indexStr);

        } else if (node.getNodeType() == Node.ELEMENT_NODE)

        {

            return parentXPath +

                    "/" + node.getNodeName() + indexStr;

        } else if (node.getNodeType() == Node.COMMENT_NODE)

        {

            return parentXPath +

                    (noValues ? "/" + node.getNodeValue() + indexStr : "/COMMENT(" + node.getNodeValue() + ")" + indexStr);

        } else if (node.getNodeType() == Node.ENTITY_REFERENCE_NODE)

        {

            return parentXPath +

                    (noValues ? "/" + node.getNodeValue() + indexStr : "/EntityReference(" + node.getNodeValue() + ")" + indexStr);

        } else if (node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE)

        {

            return parentXPath +

                    (noValues ? "/" + node.getNodeValue() + indexStr : "/PI(" + node.getNodeValue() + ")" + indexStr);

        } else if (node.getNodeType() == Node.ATTRIBUTE_NODE)

        {

            return parentXPath + "/[@" + node.getNodeName() +

                    (noValues ? "" : "=" + node.getNodeValue()) + "]";

        } else if (node.getNodeType() == Node.DOCUMENT_TYPE_NODE)

        {

            return parentXPath +

                    (noValues ? "/" + node.getNodeValue() : "/DOCTYPE(" + node.getNodeName() + ")");

        } else if (node.getNodeType() == Node.CDATA_SECTION_NODE)

        {

            return parentXPath +

                    (noValues ? "/" + node.getNodeValue() : "/CDATA(" + node.getNodeName() + ")");

        }


        // Wont reach this far but just in case

        return "";

    }


    /**
     * Gets XPath expression without the indexes
     *
     * @param xPath the XPath expression with indexes
     */

    public static String getNoIndexXPath(String xPath)

    {

        if (xPath == null)

            return null;


        boolean open = false;

        boolean closed = false;

        StringBuffer sb = new StringBuffer();


        for (int i = 0; i < xPath.length(); i++)

        {

            if (xPath.charAt(i) == '[')

            {

                open = true;

                closed = false;

            } else if ((xPath.charAt(i) == ']') && (open))

            {

                open = false;

                closed = true;

            } else if ((open) && (!closed))

                continue;

            else

                sb.append(xPath.charAt(i));

        }


        return (sb.toString());

    }


    /**
     * Gets Node Index value for the XPath expression<br>
     * <p/>
     * e.g. <root><a><b>ritesh</b><b>trivedi</b></a></root> calling
     * <p/>
     * getXPathNodeIndex for Node with value
     * <p/>
     * trivedi would return 2
     */

    public static int getXPathNodeIndex(Node node, boolean ignoreWhitespace)

    {

        int nodeIndex = 0;


        if (node == null)

        {

            return -1;

            //throw new IllegalArgumentException("Node argument for getXPathNodeIndex cannot be null");

        }


        Node prevNode = node;


        //log("getXPathNodeIndex info next few lines");            

        //log("Current node:");

        //printNode(node);


        while ((prevNode = prevNode.getPreviousSibling()) != null)

        {

            //log("previous node");

            //printNode(prevNode);

            if (nodesEqual(node, prevNode, ignoreWhitespace))

                nodeIndex++;

        }


        // If similar children are found, ONLY then increase

        // the nodeIndex by 1 since XPath exprn starts at 1 and not 0

        if (nodeIndex > 0)

            nodeIndex++;


        if (nodeIndex == 0)

        {

            Node nextNode = node;

            boolean found = false;

            while (((nextNode = nextNode.getNextSibling()) != null) && (!found))

            {

                //log("Next node");

                //printNode(nextNode);

                if (nodesEqual(node, nextNode, ignoreWhitespace))

                {

                    nodeIndex++;

                    found = true;

                }

                //node = prevNode;

            }

        }


        return nodeIndex;

    }


    /**
     * Checks if two Nodes are equal<br>
     * <p/>
     * Compares Nodes just by their Name, Type, Value and Namespace generically
     */

    public static boolean nodesEqual(Node node1, Node node2, boolean ignoreWhitespace)

    {

        if ((node1 == null) || (node2 == null))

            return false;

    	

    	/*	

    	if (node1.getNodeType() == node2.getNodeType() &&

           	(areNullorEqual(node1.getNodeValue(), node2.getNodeValue(), ignoreWhitespace, false)) &&

           	(areNullorEqual(node1.getLocalName(), node2.getLocalName(), ignoreWhitespace, false)) &&

           	(areNullorEqual(node1.getNamespaceURI(), node2.getNamespaceURI(), ignoreWhitespace, false)) &&

           	(areNullorEqual(node1.getNodeName(), node2.getNodeName(), ignoreWhitespace, false)))

            	return true;

         */


        if (areNonNullAndEqual(node1.getNamespaceURI(), node2.getNamespaceURI()))

        {

            if (node1.getNodeType() == node2.getNodeType() &&

                    (areNullorEqual(node1.getNodeValue(), node2.getNodeValue(), ignoreWhitespace, false)) &&

                    (areNullorEqual(node1.getLocalName(), node2.getLocalName(), ignoreWhitespace, false)))

                return true;

        } else if ((node1.getNamespaceURI() == null) && (node2.getNamespaceURI() == null))

        {

            //System.out.println("===> Both Namespace URIs are null");

            if ((node1.getNodeType() == node2.getNodeType()) &&

                    (areNullorEqual(node1.getNodeValue(), node2.getNodeValue(), ignoreWhitespace, false)) &&

                    (areNullorEqual(node1.getNodeName(), node2.getNodeName(), ignoreWhitespace, false)))

                return true;

        }


        return false;

    }


    /**
     * Checks if two Nodes are Similar<br>
     * <p/>
     * Compares Nodes just by their Name, Type, local name and Namespace generically
     */

    public static boolean nodesSimilar(Node node1, Node node2, boolean ignoreWhitespace)

    {

        if ((node1 == null) || (node2 == null))

            return false;

    	

    	/*	

    	if (node1.getNodeType() == node2.getNodeType() &&           	

           	(areNullorEqual(node1.getLocalName(), node2.getLocalName(), ignoreWhitespace, false)) &&

           	(areNullorEqual(node1.getNamespaceURI(), node2.getNamespaceURI(), ignoreWhitespace, false)) &&

           	(areNullorEqual(node1.getNodeName(), node2.getNodeName(), ignoreWhitespace, false)))

            	return true;

        */

        if (areNonNullAndEqual(node1.getNamespaceURI(), node2.getNamespaceURI()))

        {

            if (node1.getNodeType() == node2.getNodeType() &&

                    (areNullorEqual(node1.getLocalName(), node2.getLocalName(), ignoreWhitespace, false)))

                return true;

        } else if ((node1.getNamespaceURI() == null) && (node2.getNamespaceURI() == null))

        {

            if (node1.getNodeType() == node2.getNodeType() &&

                    (areNullorEqual(node1.getNodeName(), node2.getNodeName(), ignoreWhitespace, false)))

                return true;

        }


        return false;

    }


    /**
     * Prints a given Node<br>
     * <p/>
     * For debugging purpose only
     */

    public static void printNodeBasics(Node node)

    {

        if (node == null)

        {

            System.out.println(" Null node");

            return;

        }


        System.out.println(" Node[Namespace URI=" + node.getNamespaceURI() + " localname=" +

                node.getLocalName() + " name=" +

                node.getNodeName() + " type=" +

                getNodeTypeStr(node.getNodeType()) + " Value=" + node.getNodeValue() +

                "]");

    }


    /**
     * Gets Nodes basic information such as Node name, type, value, namespace
     * <p/>
     * and local name
     */

    public static String getNodeBasics(Node node)

    {

        StringBuffer sb = new StringBuffer();


        if (node == null)

        {

            sb.append(" Null node");

            return sb.toString();

        }


        sb.append(" Node[Namespace URI=" + node.getNamespaceURI() + " localname=" +

                node.getLocalName() + " name=" +

                node.getNodeName() + " type=" +

                getNodeTypeStr(node.getNodeType()) + " Value=" + node.getNodeValue() +

                "]");


        return sb.toString();

    }


    /**
     * Removes the Leaf Node
     */

    public static void removeLeafNode(Node node)

    {

        if (!node.hasChildNodes())

            node.getParentNode().removeChild(node);

    }


    /**
     * Gets Node type String for a given node type constant
     */

    public static String getNodeTypeStr(int nodeType)

    {

        switch (nodeType)

        {

            case Node.ATTRIBUTE_NODE:

                return "ATTRIBUTE_NODE ";


            case Node.CDATA_SECTION_NODE:

                return "CDATA_SECTION_NODE";


            case Node.COMMENT_NODE:

                return "COMMENT_NODE";


            case Node.DOCUMENT_FRAGMENT_NODE:

                return "DOCUMENT_FRAGMENT_NODE";


            case Node.DOCUMENT_TYPE_NODE:

                return "DOCUMENT_TYPE_NODE";


            case Node.ELEMENT_NODE:

                return "ELEMENT_NODE";


            case Node.ENTITY_NODE:

                return "ENTITY_NODE";


            case Node.ENTITY_REFERENCE_NODE:

                return "ENTITY_REFERENCE_NODE";


            case Node.NOTATION_NODE:

                return "NOTATION_NODE";


            case Node.PROCESSING_INSTRUCTION_NODE:

                return "PROCESSING_INSTRUCTION_NODE";


            case Node.TEXT_NODE:

                return "TEXT_NODE";


            case Node.DOCUMENT_NODE:

                return "DOCUMENT_NODE";


            default:

                return "UN-INDENTIFIED NODE";

        }

    }


    /**
     * Checks if the Node is Text Node and contains Whitespace only
     */

    public static boolean isWhitespaceTextNode(Node node)

    {

        if (node == null)

            return false;


        if (node.getNodeType() == Node.TEXT_NODE)

            return StringUtil.isWhitespaceStr(node.getNodeValue());


        return false;

    }


    /**
     * Checks if the given Node is Comment Node
     */

    public static boolean isCommentNode(Node node)

    {

        if (node == null)

            return false;


        return (node.getNodeType() == Node.COMMENT_NODE);

    }


    /**
     * Checks if Element Node is same as a Element name String
     */

    public static boolean isStrElementNode(String elementName, Node elementNode, boolean ignoreCase)

    {

        if ((elementNode == null) || (elementName == null) ||

                (elementName.trim().equals("")) || (elementNode.getNodeType() != Node.ELEMENT_NODE))

            return false;


        StringTokenizer tokenizer = new StringTokenizer(":");

        int numTokens = tokenizer.countTokens();


        if (numTokens == 1)

        {

            String name = (String) tokenizer.nextElement();

            Element element = (Element) elementNode;

            if (element.getNamespaceURI() != null)

                return false;


            if (ignoreCase)

                return element.getNodeName().trim().equalsIgnoreCase(elementName);


            return element.getNodeName().trim().equals(elementName);

        } else if (numTokens == 2)

        {

            String namespace = (String) tokenizer.nextElement();

            String localName = (String) tokenizer.nextElement();

            Element element = (Element) elementNode;


            if (element.getNamespaceURI() == null)

                return false;


            if (ignoreCase)

                return ((element.getLocalName().trim().equalsIgnoreCase(localName)) &&

                        (element.getNamespaceURI().equalsIgnoreCase(namespace.trim())));


            return ((element.getLocalName().trim().equals(localName)) &&

                    (element.getNamespaceURI().equals(namespace.trim())));

        } else

            return false;

    }


    /**
     * Checks if both the Object arguments are equal (including if they are null)
     */

    public static boolean areNullorEqual(Object obj1, Object obj2, boolean ignoreWhitespace, boolean ignoreCase)

    {

        // if both are null, they are equal

        if ((obj1 == null) && (obj2 == null))

            return true;


        // if either one of them is null, they are not equal

        if ((obj1 == null) || (obj2 == null))

            return false;


        // if they are String type

        if ((obj1 instanceof String) && (obj2 instanceof String))

        {

            if (ignoreWhitespace)

            {

                if (ignoreCase)

                    return ((String) obj1).trim().equalsIgnoreCase(((String) obj2).trim());

                else

                    return ((String) obj1).trim().equals(((String) obj2).trim());

            } else

            {

                if (ignoreCase)

                    return ((String) obj1).equalsIgnoreCase((String) obj2);

                else

                    return obj1.equals(obj2);

            }

        }


        return (obj1.equals(obj2));

    }


    /**
     * Checks if the input Objects are Non NULL and Equal
     */

    public static boolean areNonNullAndEqual(Object obj1, Object obj2)

    {

        if ((obj1 == null) || (obj2 == null))

            return false;


        return obj1.equals(obj2);

    }


    /**
     * Prints msg to System.out
     */

    public static void log(String msg)

    {

        if (DEBUG)

            System.out.println("XMLUtil:" + msg);

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


    /**
     * Main method used for debugging purpose only
     */

    public static void main(String[] args)

    {

        Map map = new HashMap();

        /*

        map.put("dbType", "new Whats UP");

        map.put("password", "DONTCARE");

         */

        map.put("env-entry-type", "DONTCARE");

        List values = new ArrayList(3);

        values.add("111111");

        values.add("222222");

        System.out.println("Entity Ref node type " + Node.ENTITY_REFERENCE_NODE);

        try

        {


            Document doc = XMLUtil.getDocument("d:\\try\\xml\\0095_a.xml");

            NodeList ens = doc.getElementsByTagName("Environment");


            Element env = (Element) ens.item(0);

            Attr expireAttr = env.getAttributeNode("expires");

            System.out.println("# of environment elements " + ens.getLength());

            System.out.println(" Expire attrs XPath " + XMLUtil.generateXPath(expireAttr, true, true));

            /*

            if (doc.hasChildNodes())

            {

            	System.out.println("Document has " + doc.getChildNodes().getLength() + " child nodes which are ");

            	for(int i=0; i<doc.getChildNodes().getLength(); i++)

            		XMLUtil.printNode(doc.getChildNodes().item(i));

            }

            else

            {

            	System.out.println("Document doesnt have any child nodes");

            }

            */

            

            /*

            NodeList nodes = doc.getElementsByTagName("Family");

            System.out.println("# of Family nodes " + nodes.getLength());

            

            Element familyNode = (Element)nodes.item(0);

            NodeList children = familyNode.getChildNodes();

            

            for(int i=0; i<children.getLength(); i++)

            {

            	Node node = children.item(i);

            	System.out.println(" Node type " + node.getNodeType() + 

            						" local name " + node.getLocalName() + " namespace " + node.getNamespaceURI() +

            						" Node name " + node.getNodeName() );

            }

            */

            

            /*

            Attr attrNode1 = (Attr)((Element)nodes.item(0)).getAttributes().item(0);

            Attr attrNode2 = (Attr)((Element)nodes.item(1)).getAttributes().item(0);

            System.out.println("XPath expression " + XMLUtil.generateXPath(attrNode2));

            map.put("attr1", attrNode2);

            System.out.println("Attr1 " + attrNode1);

            System.out.println("Attr2 " + attrNode2);

            System.out.println("Are attribute nodes equal " + attrNode1.equals(attrNode1));

            System.out.println("Map contains attribute " + map.containsValue(attrNode1));

            */

            

            /*

            NodeList nodes = doc.getElementsByTagName("Species");

            System.out.println("# of species nodes " + nodes.getLength());

            System.out.println("Printing element node " + ((Element)nodes.item(0)).toString());

            System.out.println(" if 2 nodes are equal " + ((Element)nodes.item(0)).equals((Element)nodes.item(1)));

            System.out.println(" Node text " + XMLUtil.getText(nodes.item(0)));

            System.out.println(" Node 2 text " + XMLUtil.getText(nodes.item(1)));

             */

            

            /*

            String str = XMLUtil.print(doc, false, false);

            System.out.println("Doc\n" + str);

            */

            

            /* 

            XMLUtil.replaceElementText("d:/temp/config.xml", "alias", values, true);

             */

        } catch (Exception ex)

        {

            ex.printStackTrace();

        }



        /*

        System.out.println("*******SECOND TEST ***********");

        XMLUtil.replaceElementText("/Users/trivedr/test/config.xml", "db", "whatsup", false);

         */

    }


    /**
     * EntityResolver class
     */

    private static class MyEntityResolver

            implements EntityResolver

    {

        public org.xml.sax.InputSource resolveEntity(String publicID, String systemID)

                throws org.xml.sax.SAXException, java.io.IOException

        {

            System.out.println("System id is " + systemID);

            if (systemID.equals("http://java.sun.com/j2ee/dtds/web-app_2_2.dtd"))

                return (new InputSource("web-app_2_2.dtd"));

            else if (systemID.equals("http://java.sun.com/dtd/web-app_2_3.dtd"))

                return (new InputSource("web-app_2_3.dtd"));

            else

                //By returning null, the parser will use its default behaviour. 

                return null;

        }

    }

}