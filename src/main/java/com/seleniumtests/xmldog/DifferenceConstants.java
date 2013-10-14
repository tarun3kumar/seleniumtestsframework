package com.seleniumtests.xmldog;

/**

 * Constants for describing differences between DOM Nodes.

 * <br />Examples and more at <a href="http://xmlunit.sourceforge.net"/>xmlunit.sourceforge.net</a>

 */

public interface DifferenceConstants 

{

	/** Comparing an implied attribute value against an explicit value */

	int ATTR_VALUE_EXPLICITLY_SPECIFIED_ID = 1;

	/** Comparing 2 elements and one has an attribute the other does not */

	int ATTR_NAME_NOT_FOUND_ID = 2;

	/** Comparing 2 attributes with the same name but different values */

	int ATTR_VALUE_ID = 3;

	/** Comparing 2 attribute lists with the same attributes in different sequence */

	int ATTR_SEQUENCE_ID = 4;

	/** Comparing 2 CDATA sections with different values */

	int CDATA_VALUE_ID = 5;

	/** Comparing 2 comments with different values */

	int COMMENT_VALUE_ID = 6;

	/** Comparing 2 document types with different names */

	int DOCTYPE_NAME_ID = 7;

	/** Comparing 2 document types with different public identifiers */

	int DOCTYPE_PUBLIC_ID_ID = 8;

	/** Comparing 2 document types with different system identifiers */

	int DOCTYPE_SYSTEM_ID_ID = 9;

	/** Comparing 2 elements with different tag names */

	int ELEMENT_TAG_NAME_ID = 10;

	/** Comparing 2 elements with different number of attributes */

	int ELEMENT_NUM_ATTRIBUTES_ID = 11;

	/** Comparing 2 processing instructions with different targets */

	int PROCESSING_INSTRUCTION_TARGET_ID = 12;

	/** Comparing 2 processing instructions with different instructions */

	int PROCESSING_INSTRUCTION_DATA_ID = 13;

	/** Comparing 2 different text values */

	int TEXT_VALUE_ID = 14;

	/** Comparing 2 nodes with different namespace prefixes */

	int NAMESPACE_PREFIX_ID = 15;

	/** Comparing 2 nodes with different namespace URIs */

	int NAMESPACE_URI_ID = 16;

	/** Comparing 2 nodes with different node types */

	int NODE_TYPE_ID = 17;

	/** Comparing 2 nodes but only one has any children*/

	int HAS_CHILD_NODES_ID = 18;

	/** Comparing 2 nodes with different numbers of children */

	int CHILD_NODELIST_LENGTH_ID = 19;

	/** Comparing 2 nodes with children whose nodes are in different sequence*/

	int CHILD_NODELIST_SEQUENCE_ID = 20;

	/** Comparing 2 Documents only one of which has a doctype */

	int HAS_DOCTYPE_DECLARATION_ID = 21;

	

	/**

	 * Entirely new Document

	 */

	int DOCUMENT_NEW_ID = 22;

	

	/**

	 * Empty Document

	 */

	int DOCUMENT_EMPTY_ID = 23;

	

	/**

	 * Node added/removed

	 */

	int NODE_NOT_FOUND_ID = 24;

	

	/**

	 * Multiple Node Matches/Node added

	 */

	int MULTIPLE_MATCHES_ADDED_NODE_ID = 25;

	

	/**

	 * Node added ID

	 */

	int ADDED_NODE_ID = 26;

	

	/**

	 * Position Mismatch

	 */

	int MISMATCHED_POSITION = 27;

	

    /** Comparing an implied attribute value against an explicit value */

	public static final Difference ATTR_VALUE_EXPLICITLY_SPECIFIED =

        new Difference(ATTR_VALUE_EXPLICITLY_SPECIFIED_ID, 

			"attribute value explicitly specified", true);



    /** Comparing 2 elements and one has an attribute the other does not */

    public static final Difference ATTR_NAME_NOT_FOUND =

        new Difference(ATTR_NAME_NOT_FOUND_ID, "attribute name");



    /** Comparing 2 attributes with the same name but different values */

    public static final Difference ATTR_VALUE =

        new Difference(ATTR_VALUE_ID, "attribute value");



    /** Comparing 2 attribute lists with the same attributes in different sequence */

    public static final Difference ATTR_SEQUENCE =

        new Difference(ATTR_SEQUENCE_ID, "sequence of attributes", true);



    /** Comparing 2 CDATA sections with different values */

    public static final Difference CDATA_VALUE =

        new Difference(CDATA_VALUE_ID, "CDATA section value");



    /** Comparing 2 comments with different values */

    public static final Difference COMMENT_VALUE =

        new Difference(COMMENT_VALUE_ID, "comment value");



    /** Comparing 2 document types with different names */

    public static final Difference DOCTYPE_NAME =

        new Difference(DOCTYPE_NAME_ID, "doctype name");



    /** Comparing 2 document types with different public identifiers */

    public static final Difference DOCTYPE_PUBLIC_ID =

        new Difference(DOCTYPE_PUBLIC_ID_ID, "doctype public identifier");



    /** Comparing 2 document types with different system identifiers */

    public static final Difference DOCTYPE_SYSTEM_ID =

        new Difference(DOCTYPE_SYSTEM_ID_ID, "doctype system identifier", true);



    /** Comparing 2 elements with different tag names */

    public static final Difference ELEMENT_TAG_NAME =

        new Difference(ELEMENT_TAG_NAME_ID, "element tag name");



    /** Comparing 2 elements with different number of attributes */

    public static final Difference ELEMENT_NUM_ATTRIBUTES =

        new Difference(ELEMENT_NUM_ATTRIBUTES_ID, "number of element attributes");



    /** Comparing 2 processing instructions with different targets */

    public static final Difference PROCESSING_INSTRUCTION_TARGET =

        new Difference(PROCESSING_INSTRUCTION_TARGET_ID, 

			"processing instruction target");



    /** Comparing 2 processing instructions with different instructions */

    public static final Difference PROCESSING_INSTRUCTION_DATA =

        new Difference(PROCESSING_INSTRUCTION_DATA_ID, 

			"processing instruction data");



    /** Comparing 2 different text values */

    public static final Difference TEXT_VALUE =

        new Difference(TEXT_VALUE_ID, "text value");



    /** Comparing 2 nodes with different namespace prefixes */

    public static final Difference NAMESPACE_PREFIX =

        new Difference(NAMESPACE_PREFIX_ID, "namespace prefix", true);



    /** Comparing 2 nodes with different namespace URIs */

    public static final Difference NAMESPACE_URI =

        new Difference(NAMESPACE_URI_ID, "namespace URI");



    /** Comparing 2 nodes with different node types */

    public static final Difference NODE_TYPE =

        new Difference(NODE_TYPE_ID, "node type");



    /** Comparing 2 nodes but only one has any children*/

    public static final Difference HAS_CHILD_NODES =

        new Difference(HAS_CHILD_NODES_ID, "presence of child nodes to be");



    /** Comparing 2 nodes with different numbers of children */

    public static final Difference CHILD_NODELIST_LENGTH =

        new Difference(CHILD_NODELIST_LENGTH_ID, "number of child nodes");



    /** Comparing 2 nodes with children whose nodes are in different sequence*/

    public static final Difference CHILD_NODELIST_SEQUENCE =

        new Difference(CHILD_NODELIST_SEQUENCE_ID, 

			"sequence of child nodes", true);

    

    /** Comparing 2 Documents only one of which has a doctype */

    public static final Difference HAS_DOCTYPE_DECLARATION = 

        new Difference(HAS_DOCTYPE_DECLARATION_ID, 

			"presence of doctype declaration", true);

						

	/**

	 * Entirely new Document

	 */

	public static final Difference NEW_DOCUMENT =

		new Difference(DOCUMENT_NEW_ID, "Entirely new Document", false);

		

	/**

	 * Empty Document

	 */

	public static final Difference EMPTY_DOCUMENT =

		new Difference(DOCUMENT_EMPTY_ID, "Empty Document", false);

		

	/**

	 * Node Added

	 */

	public static final Difference NODE_NOT_FOUND =

		new Difference(NODE_NOT_FOUND_ID, "Node NOT Found", false);

		

	/**

	 * Multiple Node Matches/Added Node

	 */

	public static final Difference MULTIPLE_MATCHES_ADDED_NODE =

		new Difference(MULTIPLE_MATCHES_ADDED_NODE_ID, "Node NOT Found", false);

	

	/**

	 * Added Node

	 */

	public static final Difference ADDED_NODE =

		new Difference(ADDED_NODE_ID, "Node Added", false);

		

	public static final Difference POSITION_MISMATCH 

		= new Difference(MISMATCHED_POSITION, "Position Mismatch", false);

}

