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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Multiple Object Values Map<br>
 * Class is useful when multiple Objects are put in to the Map with the same Key value. This implementation will save
 * Mutiple Object Values for the Key as a List, instead of overwriting the value (default behaviour of the Map).<br>
 * IMPORTANT!! Current implementation has a limitation where List type objects shouldnot be put in the MOVMap<br>
 * FIXME FIXME FIXME<br>
 * Support for List type Object addition
 *
 * @author   Ritesht
 * @version  1.0
 */
public final class MOVMap extends HashMap implements XMLDogConstants {

    /**
     * Default Constructor.
     */
    public MOVMap() {
        super();
    }

    /**
     * Constructor.
     *
     * @param  initialCapacity  the initialCapacity of the Map
     */
    public MOVMap(final int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructor.
     *
     * @param  initialCapacity  the initialCapacity of the Map
     * @param  loadFactory      the load factory for the Map
     */
    public MOVMap(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Constructor.
     *
     * @param  t  the Map to be copied in the new Map instance
     */
    public MOVMap(final Map t) {
        putAll(t);
    }

    /**
     * Puts the Object into the Map with a given Key.
     *
     * @param   key    the key for the given Object
     * @param   value  the Object value for a given key
     *
     * @throws  IllegalArgumentException  If Null or List type Objects are passed
     */
    public Object put(final Object key, final Object value) {
        if ((value instanceof List) && (value == null)) {
            throw new IllegalArgumentException("Null and List type Objects cannot be added to MOVMap");
        }

        Object oldValue = null;
        List valueList = null;

        if ((oldValue = get(key)) != null) {
            if (oldValue instanceof List) {
                log("Value is already a list");
                ((List) oldValue).add(value);
                super.put(key, oldValue);
            } else {
                log("Adding value which is not a list, so creating list");
                valueList = new ArrayList();
                valueList.add(oldValue);
                valueList.add(value);
                super.put(key, valueList);
            }
        } else {
            log("Adding value for the first time, so its NOT list");
            super.put(key, value);
        }

        return value;
    }

    /**
     * Puts all the elements of the given Map in to this instance.
     *
     * @param  t  the Map whose elements are to be copied
     */
    public void putAll(final Map t) {
        Iterator iterator = t.keySet().iterator();
        Object key = null;

        while (iterator.hasNext()) {
            key = iterator.next();
            put(key, t.get(key));
        }
    }

    /**
     * Prints msg to System.out.
     */
    public static void log(final String msg) {
        if (DEBUG) {
            System.out.println("MOVMap:" + msg);
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

}
