package com.seleniumtests.xmldog;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import java.io.Serializable;
import java.util.Enumeration;

/**
 * Class representing Ordered List ADT
 * @author  trivedr
 * @version 1.0 02/16/2003 5:20 PM CST
 */
public class OrderedMap 
    implements Serializable
{
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
     * Constructor
     * @param type the type of the OrderedMap, possible values are
     * TYPE_SYNCHRONIZED, TYPE_UNSYNCHRONIZED, TYPE_UNSYNCHRONIZED_MOV
     */
    public OrderedMap(int type)
    {
        if (type == TYPE_SYNCHRONIZED)
        {
            _elementOrder = Collections.synchronizedList(new ArrayList());
            _elements = Collections.synchronizedMap(new HashMap());
        }
        else
        if (type == TYPE_UNSYNCHRONIZED)
        {
            _elementOrder = new ArrayList();
            _elements = new HashMap();
        }
        else
        if (type == TYPE_UNSYNCHRONIZED_MOV)
        {
            _elementOrder = new ArrayList();
            _elements = new MOVMap();
        }
        else
            throw new IllegalArgumentException("Unrecongnized OrderedMap type");
    }
    
    /**
     * Gets size of the ordered list
     * @return size of this list
     */
    public int size()
    {
        return _elementOrder.size();
    }
    
    /**
     * Clears the Ordered List
     */
    public void clear()
    {
        _elementOrder.clear();
        _elements.clear();
    }
    
    /**
     * Gets the type of the OrderedList
     */
    public int getType()
    {
    	return _type;
    }
    
    /**
     * Checks to see if the element is in the Ordered List
     * @param element Element to be checked 
     * @return true if element exists in the list, false otherwise
     */
    public boolean contains(Object element)
    {
        return _elements.containsValue(element);
    }
    
    /**
     * Checks to see if the element key exists
     * @param eKey Element key
     * @return true if element key exists, false otherwise
     */
    public boolean containsKey(Object eKey)
    {
        return _elementOrder.contains(eKey);
    }
    
    /**
     * Checks if elementkey exists in the Ordered List
     * @param eKey Element key
     * @return true if element key exists, false otherwise
     */
    public boolean containsElementKey(Object eKey)
    {
        return _elementOrder.contains(eKey);
    } 
           
    /**.
     * Gets Elements Order
     * <br> Returns synchronized or unsynchronized list depending on how the instance
     * was created
     * @return Vector containing elements key in the order which they are added
     * @see OrderdedList() Contructor
     */
    public List getElementOrder()
    {
        return _elementOrder;
    }        
    
    /**
     * Gets Element for the Element Key
     * @param elementKey the element key
     * @return Element whose key is elementKey
     */
    public Object getElement(Object elementKey)
    {
         if (elementKey != null)
            return _elements.get(elementKey);
        else
            return null;
    }
    
    /**
     * Gets Element at a given position
     * @param position the position of the element
     * @return Element at a given position
     */
    public Object getElement(int position)
    {
        if ((position > _elementOrder.size()) || (position < 0))
            return null;
        
        Object elementKey = _elementOrder.get(position);
         
        return getElement(elementKey);
    }
    
    /**
     * Gets all the elements in the Ordered List in the Order in which
     * they were entered
     * @return Array of elements
     */
    public Object[] elements()
    {
        Object[] objects = new Object[_elementOrder.size()];
        
        for(int i=0; i<_elementOrder.size(); i++)
        {
        	//System.out.println(" ----> Getting Elements at " + i);
            objects[i] = _elements.get(_elementOrder.get(i));
        }
        
        return objects;
    }
    
    /**
     * Gets Element keys
     * @return Enumeration of the element keys
     */
    public Iterator elementKeys()
    {
        return _elementOrder.iterator();
    }
    
    /**
     * Adds element with the key into the Ordered List
     */
    public void add(Object eKey, Object element)
    {
    	if ((!_elements.containsKey(eKey)) && (getType() != TYPE_UNSYNCHRONIZED_MOV))
        	_elementOrder.add(eKey);
        	
        _elements.put(eKey, element);
    }
    
    /**
     * Inserts element at a position
     * @param eKey Element Key
     * @param element Element to be inserted
     * @param position position at which element is to be inserted
     */
    public void insert(Object eKey, Object element, int position)
    {
        _elementOrder.add(position, eKey);
        _elements.put(eKey, element);
    }
    
    /**
     * Gets index of a given element with element key
     * @param eKey the element key
     * @return position of the element key in this ordered list
     */
    public int indexOf(Object eKey)
    {
        return _elementOrder.indexOf(eKey);
    }
}