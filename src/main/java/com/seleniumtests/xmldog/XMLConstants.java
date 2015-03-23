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

/**
 * To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates. To
 * enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */

/**
 * A convenient place to hang constants relating to general XML usage.
 */

public interface XMLConstants {

    /**
     * &lt;?xml&greaterThan; declaration.
     */

    String XML_DECLARATION =

        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";

    /**
     * xmlns attribute prefix.
     */

    String XMLNS_PREFIX = "xmlns";

    /**
     * "&lt;"
     */

    String OPEN_START_NODE = "<";

    /**
     * "&lt;/"
     */

    String OPEN_END_NODE = "</";

    /**
     * "&greaterThan;"
     */

    String CLOSE_NODE = ">";

    /**
     * "![CDATA["
     */

    String START_CDATA = "![CDATA[";

    /**
     * "]]"
     */

    String END_CDATA = "]]";

    /**
     * "!--"
     */

    String START_COMMENT = "!--";

    /**
     * "--""
     */

    String END_COMMENT = "--";

    /**
     * "?"
     */

    String START_PROCESSING_INSTRUCTION = "?";

    /**
     * "?"
     */

    String END_PROCESSING_INSTRUCTION = "?";

    /**
     * "!DOCTYPE"
     */

    String START_DOCTYPE = "!DOCTYPE ";

    /**
     * "/"
     */

    String XPATH_SEPARATOR = "/";

    /**
     * "["
     */

    String XPATH_NODE_INDEX_START = "[";

    /**
     * "]"
     */

    String XPATH_NODE_INDEX_END = "]";

    /**
     * "comment()"
     */

    String XPATH_COMMENT_IDENTIFIER = "comment()";

    /**
     * "processing-instruction()"
     */

    String XPATH_PROCESSING_INSTRUCTION_IDENTIFIER = "processing-instruction()";

    /**
     * "text()"
     */

    String XPATH_CHARACTER_NODE_IDENTIFIER = "text()";

    /**
     * "&at;"
     */

    String XPATH_ATTRIBUTE_IDENTIFIER = "@";

    /**
     * Regular Expression startor for Exclusion List.
     */

    String XPATH_REGEX_BEGIN = "[";

    /**
     * Regular Expression terminator for Exclusion List.
     */

    String XPATH_REGEX_END = "]";

}
