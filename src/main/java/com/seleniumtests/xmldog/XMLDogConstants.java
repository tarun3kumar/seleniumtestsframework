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
