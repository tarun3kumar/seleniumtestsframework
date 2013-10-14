package com.seleniumtests.xmldog;

/**


 *

 * To change this generated comment edit the template variable "typecomment":

 * Window>Preferences>Java>Templates.

 * To enable and disable the creation of type comments go to

 * Window>Preferences>Java>Code Generation.

 */



/**

 * A convenient place to hang constants relating to general XML usage

 */

public interface XMLConstants 

{



    /**

     * &lt;?xml&greaterThan; declaration

     */

    public static final String XML_DECLARATION =

        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";



    /**

     * xmlns attribute prefix

     */

    public static final String XMLNS_PREFIX = "xmlns";



    /**

     * "&lt;"

     */

    public static final String OPEN_START_NODE = "<";



    /**

     * "&lt;/"

     */

    public static final String OPEN_END_NODE = "</";



    /**

     * "&greaterThan;"

     */

    public static final String CLOSE_NODE = ">";



    /**

     * "![CDATA["

     */

    public static final String START_CDATA = "![CDATA[";

    /**

     * "]]"

     */

    public static final String END_CDATA = "]]";



    /**

     * "!--"

     */

    public static final String START_COMMENT = "!--";

    /**

     * "--""

     */

    public static final String END_COMMENT = "--";



    /**

     * "?"

     */

    public static final String START_PROCESSING_INSTRUCTION = "?";

    /**

     * "?"

     */

    public static final String END_PROCESSING_INSTRUCTION = "?";



    /**

     * "!DOCTYPE"

     */

    public static final String START_DOCTYPE = "!DOCTYPE ";

    

    /**

     * "/"

     */

    public static final String XPATH_SEPARATOR = "/";



    /**

     * "["

     */

    public static final String XPATH_NODE_INDEX_START = "[";



    /**

     * "]"

     */

    public static final String XPATH_NODE_INDEX_END = "]";

    

    /**

     * "comment()"

     */

    public static final String XPATH_COMMENT_IDENTIFIER = "comment()";

    

    /**

     * "processing-instruction()"

     */

    public static final String XPATH_PROCESSING_INSTRUCTION_IDENTIFIER = "processing-instruction()";

    

    /**

     * "text()"

     */

    public static final String XPATH_CHARACTER_NODE_IDENTIFIER = "text()";



    /**

     * "&at;"

     */

    public static final String XPATH_ATTRIBUTE_IDENTIFIER = "@";

    

    /**

     * Regular Expression startor for Exclusion List

     */

    public static final String XPATH_REGEX_BEGIN = "[";

    

    /**

     * Regular Expression terminator for Exclusion List

     */

    public static final String XPATH_REGEX_END = "]";



}

