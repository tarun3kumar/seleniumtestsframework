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

package com.seleniumtests.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.math.BigDecimal;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.seleniumtests.core.Filter;
import com.seleniumtests.core.SeleniumTestsContextManager;

import com.seleniumtests.util.internal.entity.TestEntity;

public class SpreadSheetHelper {

    private static Logger logger = Logger.getLogger(SpreadSheetHelper.class);

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_MAP = new HashMap<Class<?>, Class<?>>();

    // Setup primitives map
    static {
        PRIMITIVE_TYPE_MAP.put(Boolean.TYPE, Boolean.class);
        PRIMITIVE_TYPE_MAP.put(Byte.TYPE, Byte.class);
        PRIMITIVE_TYPE_MAP.put(Character.TYPE, Character.class);
        PRIMITIVE_TYPE_MAP.put(Short.TYPE, Short.class);
        PRIMITIVE_TYPE_MAP.put(Integer.TYPE, Integer.class);
        PRIMITIVE_TYPE_MAP.put(Long.TYPE, Long.class);
        PRIMITIVE_TYPE_MAP.put(Float.TYPE, Float.class);
        PRIMITIVE_TYPE_MAP.put(Double.TYPE, Double.class);
    }

    private static Object _readFieldValueObject(final Class<?> fieldClz, final Type type,
            final Map<String, Object> dataMap, final String combinedFieldName) throws Exception {
        Object fieldValue = null;

        if (fieldClz.isArray()) {
            int size = getArraySize(dataMap, combinedFieldName);
            if (size > 0) {
                fieldValue = Array.newInstance(fieldClz.getComponentType(), size);
                for (int j = 0; j < size; j++) {
                    Array.set(fieldValue, j,
                        readFieldValue(fieldClz.getComponentType(), combinedFieldName + "." + j, dataMap));
                }
            }
        } else if (fieldClz.isAssignableFrom(java.util.List.class)) {
            java.util.ArrayList list = java.util.ArrayList.class.newInstance();
            int size = getArraySize(dataMap, combinedFieldName);
            if (size > 0) {
                fieldValue = list;

                Class<?> itemClz = getListItemType(type);
                for (int j = 0; j < size; j++) {
                    list.add(readFieldValue(itemClz, combinedFieldName + "." + j, dataMap));
                }
            }
        } else if (fieldClz.isAssignableFrom(java.util.Set.class)) {
            java.util.Set list = java.util.LinkedHashSet.class.newInstance();
            int size = getArraySize(dataMap, combinedFieldName);
            if (size > 0) {
                fieldValue = list;

                Class<?> itemClz = getListItemType(type);
                for (int j = 0; j < size; j++) {
                    list.add(readFieldValue(itemClz, combinedFieldName + "." + j, dataMap));
                }
            }
        } else {
            fieldValue = readFieldValue(fieldClz, combinedFieldName, dataMap);
        }

        return fieldValue;
    }

    protected static void formatDPTags(final Map<String, Object> rowDataMap) {
        if (rowDataMap.get(TestEntity.TEST_DP_TAGS) != null) {
            String dpTags = rowDataMap.get(TestEntity.TEST_DP_TAGS).toString();
            if (dpTags.trim().length() > 0) {
                String[] dpTagArray = dpTags.split(",");
                String tempDPTags = "";
                for (int idx = 0; dpTagArray.length > 0 && idx < dpTagArray.length; idx++) {
                    tempDPTags = tempDPTags.concat("[" + dpTagArray[idx].trim() + "]");
                    if (idx != dpTagArray.length - 1) {
                        tempDPTags = tempDPTags.concat(",");
                    }
                }

                rowDataMap.put(TestEntity.TEST_DP_TAGS, tempDPTags);
            }
        }
    }

    public static int getArraySize(final Map<String, Object> map, String key) {
        int count = 0;
        boolean valueFound = false;
        key = key.toLowerCase();
        for (Entry<String, Object> entry : map.entrySet()) {
            String key2 = entry.getKey();
            String value2 = (String) entry.getValue();
            if (value2 == null || value2.length() == 0) {
                continue;
            }

            key2 = key2.toLowerCase();
            if (key2.startsWith(key + ".")) {
                valueFound = true;

                String subst = key2.substring((key + ".").length());
                String[] ss = subst.split("\\.");
                try {
                    int value = Integer.parseInt(ss[0]);
                    count = (value > count ? value : count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return (valueFound ? count + 1 : count);
    }

    /**
     * Reads data from spreadsheet. If sheetName and sheetNumber both are supplied the sheetName takes precedence. Put
     * the excel sheet in the same folder as the test case and specify clazz as <code>this.getClass()</code> .
     */
    public static synchronized Iterator<Object[]> getDataFromSpreadsheet(final Class<?> clazz, final String filename,
            final Filter filter, final boolean readHeaders) throws Exception {
        return getDataFromSpreadsheet(clazz, filename, filter, readHeaders, true);
    }

    /**
     * Reads data from spreadsheet. If sheetName and sheetNumber both are supplied the sheetName takes precedence. Put
     * the excel sheet in the same folder as the test case and specify clazz as <code>this.getClass()</code> .
     *
     * @param   clazz
     * @param   filename
     * @param   filter
     * @param   readHeaders
     *
     * @return
     *
     * @throws  Exception
     */
    public static synchronized Iterator<Object[]> getDataFromSpreadsheet(final Class<?> clazz, final String filename,
            final Filter filter, final boolean readHeaders, final boolean supportDPFilter) throws Exception {

        System.gc();

        // CSVHelper handle CSV Files
        if (filename.toLowerCase().endsWith(".csv")) {
            return CSVHelper.getDataFromCSVFile(clazz, filename, filter, readHeaders, supportDPFilter);
        } else {
            throw new Exception("illegal file format, only csv files are supported for now");
        }
    }

    protected static Filter getDPFilter() {
        String includedTags = SeleniumTestsContextManager.getGlobalContext().getDPTagsInclude();
        String excludedTags = SeleniumTestsContextManager.getGlobalContext().getDPTagsExclude();

        Filter dpFilter = null;
        if (includedTags != null && includedTags.trim().length() > 0) {
            String[] includeTagsArray = includedTags.split(",");
            for (int idx = 0; includeTagsArray.length > 0 && idx < includeTagsArray.length; idx++) {
                if (dpFilter == null) {
                    dpFilter = Filter.containsIgnoreCase(TestEntity.TEST_DP_TAGS,
                            "[" + includeTagsArray[0].trim() + "]");
                } else {
                    dpFilter = Filter.or(dpFilter,
                            Filter.containsIgnoreCase(TestEntity.TEST_DP_TAGS,
                                "[" + includeTagsArray[idx].trim() + "]"));
                }
            }
        }

        if (excludedTags != null && excludedTags.trim().length() > 0) {
            String[] excludeTagsArray = excludedTags.split(",");
            for (int idx = 0; excludeTagsArray.length > 0 && idx < excludeTagsArray.length; idx++) {
                if (dpFilter == null) {
                    dpFilter = Filter.not(Filter.containsIgnoreCase(TestEntity.TEST_DP_TAGS,
                                "[" + excludeTagsArray[idx].trim() + "]"));
                } else {
                    dpFilter = Filter.and(dpFilter,
                            Filter.not(
                                Filter.containsIgnoreCase(TestEntity.TEST_DP_TAGS,
                                    "[" + excludeTagsArray[idx].trim() + "]")));
                }
            }
        }

        return dpFilter;
    }

    /**
     * Create Entity Objects based on data in spreadsheet.
     *
     * <p/>This method is only for Data Provider. Because it also filer the data based on the
     * dpTagsInclude/dpTagsExclude which is defined in testng configuration file
     *
     * @param   clazz
     * @param   entityClazzMap
     * @param   filename
     * @param   filter
     *
     * @return
     *
     * @throws  Exception
     */
    public static Iterator<Object[]> getEntitiesFromSpreadsheet(final Class<?> clazz,
            final LinkedHashMap<String, Class<?>> entityClazzMap, final String filename, final Filter filter)
        throws Exception {

        Iterator<Object[]> dataIterator = getDataFromSpreadsheet(clazz, filename, filter, true);

        List<Object[]> list = getEntityData(dataIterator, entityClazzMap);

        return list.iterator();
    }

    private static List<Object[]> getEntityData(final Iterator<Object[]> dataIterator,
            final LinkedHashMap<String, Class<?>> entityClazzMap) throws Exception {
        List<Object[]> list = new ArrayList<Object[]>();

        // Get the headers
        Object[] headerArray = null;
        if (dataIterator.hasNext()) {
            headerArray = dataIterator.next();
        }

        while (dataIterator.hasNext()) {

            Object[] rowDataArray = dataIterator.next();
            Map<String, Object> map = new LinkedHashMap<String, Object>();

            List<Object> rowData = new ArrayList<Object>();
            for (int j = 0; j < headerArray.length; j++) {
                String header = (String) headerArray[j];
                map.put(header, rowDataArray[j]);
            }

            Map<String, Boolean> temp = new HashMap<String, Boolean>();
            if (entityClazzMap != null) {
                for (Entry<String, Class<?>> entry : entityClazzMap.entrySet()) {
                    temp.put(entry.getKey(), Boolean.TRUE);
                    rowData.add(readObject(entry.getValue(), entry.getKey(), map));
                }
            }

            for (int i = rowDataArray.length - 1; i >= 0; i--) {
                int docIdx = ((String) headerArray[i]).indexOf(".");
                if (docIdx < 0) {
                    rowData.add(0, rowDataArray[i]);
                } else if (temp.get(((String) headerArray[i]).substring(0, docIdx)) == null) {
                    rowData.add(0, rowDataArray[i]);
                }
            }

            list.add(rowData.toArray(new Object[] {rowData.size()}));
        }

        return list;
    }

    public static Map<String, Object> getFieldsDataNeedToBeSet(final Map<String, Object> map, final String key) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        for (String key2 : map.keySet()) {
            if (key2.equalsIgnoreCase(key)) {
                if (map.get(key2) != null) {
                    result.put(key2, map.get(key2).toString());
                }
            }

            if (key2.toLowerCase().startsWith(key.toLowerCase() + ".")) {
                if (map.get(key2) != null) {
                    result.put(key2.substring(key.length() + 1), map.get(key2).toString());
                }
            }
        }

        return result;
    }

    public static Map<String, Object> getFieldsNeedToBeSet(final Map<String, Object> map, final String key) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        String lastKey = "";
        for (String key2 : map.keySet()) {
            if (key2.equalsIgnoreCase(key)) {
                result.put(key2, map.get(key2));
            }

            if (key2.toLowerCase().startsWith(key.toLowerCase() + ".")) {
                String newkey = key2.substring(key.length() + 1);
                if (newkey.contains(".")) {
                    newkey = newkey.substring(0, newkey.indexOf("."));
                }

                if (!newkey.equalsIgnoreCase(lastKey)) {
                    result.put(newkey, map.get(key2));
                }
            }
        }

        return result;
    }

    private static Class<?> getListItemType(final Type type) throws ClassNotFoundException {

        Class<?> itemClz = null;

        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            itemClz = Class.forName(pt.getActualTypeArguments()[0].toString().substring("class ".length()));
        }

        return itemClz;
    }

    public static Object getValue(final Map<String, Object> map, final String key) {
        for (Entry<String, Object> entry : map.entrySet()) {
            if ((entry.getKey() == null && key == null)
                    || (entry.getKey() != null && entry.getKey().equalsIgnoreCase(key))) {
                return entry.getValue();
            }
        }

        return null;
    }

    private static boolean isPrimitive(final Class<?> clz) {
        return clz.isPrimitive() || clz.getCanonicalName().equals("java.lang." + clz.getSimpleName());
    }

    private static Object readFieldValue(final Class<?> fieldClz, final String fieldName,
            final Map<String, Object> dataMap) throws Exception {
        Object fieldValue = null;
        String tempValue = (String) getValue(dataMap, fieldName);

        // Return null when field is atomic and value is null or blank
        if ((tempValue == null || tempValue.length() == 0)
                && (fieldClz.isEnum() || fieldClz.getName().equals("java.util.Calendar")
                    || fieldClz.getName().equals("java.math.BigDecimal") || isPrimitive(fieldClz))) {
            return null;
        }

        if (fieldClz.isEnum()) {
            try {
                fieldValue = fieldClz.getMethod("valueOf", String.class).invoke(fieldClz, tempValue);
            } catch (Exception e) {
                logger.warn("Ex", e);
            }
        } else if (fieldClz.getName().equals("java.util.Calendar")) {
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(new SimpleDateFormat("MM/dd/yyyy").parse(tempValue));
            } catch (ParseException ex) {
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(tempValue));
                } catch (ParseException ex2) {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz").parse(tempValue));

                }

            }

            fieldValue = calendar;
        } else if (fieldClz.getName().equals("java.math.BigDecimal")) {
            fieldValue = new BigDecimal(tempValue);
        } else if (isPrimitive(fieldClz)) {
            Constructor<?> constructor;
            try {
                if (fieldClz.getName().equals("java.lang.String")) {
                    fieldValue = tempValue;
                } else {
                    if (PRIMITIVE_TYPE_MAP.containsKey(fieldClz)) {
                        constructor = PRIMITIVE_TYPE_MAP.get(fieldClz).getConstructor(String.class);
                    } else {
                        constructor = fieldClz.getConstructor(String.class);
                    }

                    fieldValue = constructor.newInstance(tempValue);
                }
            } catch (Exception e) {
                logger.warn("Ex", e);
            }
        } else { // Non-atomic or Object field
            fieldValue = readObject(fieldClz, fieldName, dataMap);
        }

        return fieldValue;
    }

    public static Object readObject(final Class<?> clz, String objectName, final Map<String, Object> dataMap)
        throws Exception {
        Object object = null;
        if (clz == null) {
            return null;
        }

        if (objectName == null) {
            objectName = clz.getSimpleName();
        }

        Map<String, Object> fieldMap = getFieldsNeedToBeSet(dataMap, objectName);
        Map<String, Object> datamap = getFieldsDataNeedToBeSet(dataMap, objectName);

        for (String fieldName : fieldMap.keySet()) {
            String first = "" + fieldName.charAt(0);
            String realfieldName = fieldName.replaceFirst(first, first.toLowerCase());
            Object fieldValue = null;
            Class<?> type = null;
            try {
                Class<?>[] parameterTypes = new Class<?>[] {};
                Method method = clz.getMethod("get" + fieldName, parameterTypes);
                type = method.getReturnType();
                fieldValue = _readFieldValueObject(type, method.getGenericReturnType(), datamap, fieldName);
            } catch (NoSuchMethodException ex) {
                try {
                    Class<?>[] parameterTypes = new Class<?>[] {};
                    Method method = clz.getMethod("is" + fieldName, parameterTypes);
                    type = method.getReturnType();
                    fieldValue = _readFieldValueObject(type, method.getGenericReturnType(), datamap, fieldName);
                } catch (NoSuchMethodException ex2) {
                    try {
                        Field field = clz.getDeclaredField(realfieldName);
                        fieldValue = _readFieldValueObject(field.getType(), field.getGenericType(), datamap, fieldName);
                    } catch (NoSuchFieldException ex3) {
                        try {
                            fieldValue = _readFieldValueObject(String.class, String.class, datamap, fieldName);
                            type = String.class;
                        } catch (Exception e) {
                            logger.warn("Ex:" + clz.getName(), e);
                        }
                    }
                }
            }

            // execute the Setter Method
            try {
                if (fieldValue != null) {

                    if (object == null) {
                        try {
                            object = clz.newInstance();
                        } catch (InstantiationException e) {

                            // handle no null parameter constructor
                            Class<?>[] parameterTypes = new Class<?>[1];
                            parameterTypes[0] = fieldValue.getClass();

                            Constructor<?> constructor = clz.getDeclaredConstructor(parameterTypes);
                            constructor.setAccessible(true);
                            object = constructor.newInstance(fieldValue);
                            return object;
                        }
                    }
                }

                if (fieldValue != null) {

                    if (object == null) {
                        object = clz.newInstance();
                    }

                    try {
                        Class<?>[] parameterTypes = new Class<?>[1];
                        parameterTypes[0] = type;

                        Method method = object.getClass().getMethod("set" + fieldName, parameterTypes);
                        method.invoke(object, fieldValue);
                    } catch (Exception ex) {
                        Field field2 = object.getClass().getDeclaredField(realfieldName);
                        field2.setAccessible(true);
                        field2.set(object, fieldValue);
                    }
                }
            } catch (Exception e) {
                logger.warn("Ex", e);
            }
        }

        return object;
    }
}
