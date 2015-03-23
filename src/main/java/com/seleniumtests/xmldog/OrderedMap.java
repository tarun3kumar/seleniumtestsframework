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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class representing Ordered List ADT.
 *
 * @author   trivedr
 * @version  1.0 02/16/2003 5:20 PM CST
 */
public class OrderedMap implements Serializable {

    // Thread safe OrderedMap
    public static final int TYPE_SYNCHRONIZED = 1;

    // Unsynchronized OrderedMap
    public static final int TYPE_UNSYNCHRONIZED = 2;

    // Unsychronized and allows Mutiple Object Values
    public static final int TYPE_UNSYNCHRONIZED_MOV = 3;

    private List _elementOrder = null;
    private Map _elements = null;
    private int _type = -1;

    /**
     * Constructor.
     *
     * @param  type  the type of the OrderedMap, possible values are TYPE_SYNCHRONIZED, TYPE_UNSYNCHRONIZED,
     *               TYPE_UNSYNCHRONIZED_MOV
     */
    public OrderedMap(final int type) {
        if (type == TYPE_SYNCHRONIZED) {
            _elementOrder = Collections.synchronizedList(new ArrayList());
            _elements = Collections.synchronizedMap(new HashMap());
        } else if (type == TYPE_UNSYNCHRONIZED) {
            _elementOrder = new ArrayList();
            _elements = new HashMap();
        } else if (type == TYPE_UNSYNCHRONIZED_MOV) {
            _elementOrder = new ArrayList();
            _elements = new MOVMap();
        } else {
            throw new IllegalArgumentException("Unrecongnized OrderedMap type");
        }
    }

    /**
     * Gets size of the ordered list.
     *
     * @return  size of this list
     */
    public int size() {
        return _elementOrder.size();
    }

    /**
     * Clears the Ordered List.
     */
    public void clear() {
        _elementOrder.clear();
        _elements.clear();
    }

    /**
     * Gets the type of the OrderedList.
     */
    public int getType() {
        return _type;
    }

    /**
     * Checks to see if the element is in the Ordered List.
     *
     * @param   element  Element to be checked
     *
     * @return  true if element exists in the list, false otherwise
     */
    public boolean contains(final Object element) {
        return _elements.containsValue(element);
    }

    /**
     * Checks to see if the element key exists.
     *
     * @param   eKey  Element key
     *
     * @return  true if element key exists, false otherwise
     */
    public boolean containsKey(final Object eKey) {
        return _elementOrder.contains(eKey);
    }

    /**
     * Checks if elementkey exists in the Ordered List.
     *
     * @param   eKey  Element key
     *
     * @return  true if element key exists, false otherwise
     */
    public boolean containsElementKey(final Object eKey) {
        return _elementOrder.contains(eKey);
    }

    /**
     * . Gets Elements Order<br>
     * Returns synchronized or unsynchronized list depending on how the instance was created
     *
     * @return  Vector containing elements key in the order which they are added
     *
     * @see     OrderdedList() Contructor
     */
    public List getElementOrder() {
        return _elementOrder;
    }

    /**
     * Gets Element for the Element Key.
     *
     * @param   elementKey  the element key
     *
     * @return  Element whose key is elementKey
     */
    public Object getElement(final Object elementKey) {
        if (elementKey != null) {
            return _elements.get(elementKey);
        } else {
            return null;
        }
    }

    /**
     * Gets Element at a given position.
     *
     * @param   position  the position of the element
     *
     * @return  Element at a given position
     */
    public Object getElement(final int position) {
        if ((position > _elementOrder.size()) || (position < 0)) {
            return null;
        }

        Object elementKey = _elementOrder.get(position);

        return getElement(elementKey);
    }

    /**
     * Gets all the elements in the Ordered List in the Order in which they were entered.
     *
     * @return  Array of elements
     */
    public Object[] elements() {
        Object[] objects = new Object[_elementOrder.size()];

        for (int i = 0; i < _elementOrder.size(); i++) {

            // System.out.println(" ----> Getting Elements at " + i);
            objects[i] = _elements.get(_elementOrder.get(i));
        }

        return objects;
    }

    /**
     * Gets Element keys.
     *
     * @return  Enumeration of the element keys
     */
    public Iterator elementKeys() {
        return _elementOrder.iterator();
    }

    /**
     * Adds element with the key into the Ordered List.
     */
    public void add(final Object eKey, final Object element) {
        if ((!_elements.containsKey(eKey)) && (getType() != TYPE_UNSYNCHRONIZED_MOV)) {
            _elementOrder.add(eKey);
        }

        _elements.put(eKey, element);
    }

    /**
     * Inserts element at a position.
     *
     * @param  eKey      Element Key
     * @param  element   Element to be inserted
     * @param  position  position at which element is to be inserted
     */
    public void insert(final Object eKey, final Object element, final int position) {
        _elementOrder.add(position, eKey);
        _elements.put(eKey, element);
    }

    /**
     * Gets index of a given element with element key.
     *
     * @param   eKey  the element key
     *
     * @return  position of the element key in this ordered list
     */
    public int indexOf(final Object eKey) {
        return _elementOrder.indexOf(eKey);
    }
}
