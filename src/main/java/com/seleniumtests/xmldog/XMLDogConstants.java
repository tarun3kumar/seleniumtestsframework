package com.seleniumtests.xmldog;

/**
 * XMLDogConstants.java. To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation. $Id$
 */

/**
 * XMLDogConstants Interface containing all the XMLDog Application constants.
 */

public interface XMLDogConstants {

    // Application name

    String APP_NAME = "XMLDog";

    // DEBUG flag

    boolean DEBUG = false;

    // Event types sent to the DifferenceListeners

    int EVENT_NODE_IDENTICAL = 0;

    int EVENT_NODE_SIMILAR = 1;

    int EVENT_NODE_MISMATCH = 2;

}
