package com.seleniumtests.xmldog;

/**

 * StringUtil.java

 * 

 * To change this generated comment edit the template variable "typecomment":

 * Window>Preferences>Java>Templates.

 * To enable and disable the creation of type comments go to

 * Window>Preferences>Java>Code Generation.

 * 

 * $Id$

 */



/**

 * StringUtil class containing utility functions related to Strings

 */

public class StringUtil 

{



	/**

	 * Checks if the input String is Whitespace only

	 */

	public static boolean isWhitespaceStr(String str)

	{				

		if (str == null)

			return false;

		

		str = str.trim();

		

		for(int i=0; i<str.length(); i++)

		{

			if (!Character.isWhitespace(str.charAt(i)))

				return false;

		}

		

		

		return true;

	}

    

    /**

     * Gets Platform independent line separator (new line character(s)

     */

    public static String getNewlineStr()

    {

    	return System.getProperty("line.separator");

    }

    

    /**

     * Main method for debugging purpose only

     */

    public static void main(String[] args)

    {

    	/*

    	 * NULL value checking for instanceof operator

    	 */

    	/*

        StringUtil su = null;

        if (su instanceof StringUtil)

            System.out.println(" null can be instance of anything");

        else

            System.out.println(" null cannot be instance of anything");

        */

    }

}