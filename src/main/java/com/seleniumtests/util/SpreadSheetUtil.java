package com.seleniumtests.util;

import java.io.FileInputStream;
import java.io.InputStream;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;

import com.seleniumtests.controller.ContextManager;
import com.seleniumtests.controller.EasyFilter;
import com.seleniumtests.exception.SeleniumTestsException;
import com.seleniumtests.util.internal.entity.TestObject;

public class SpreadSheetUtil {

    private static Logger logger = Logger.getLogger(SpreadSheetUtil.class);
    /** Primitive type name -> class map. */

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_MAP = new HashMap<Class<?>, Class<?>>();

    /** Setup the primitives map. */
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Object _readFieldValueObject(Class<?> fieldClz, Type type,
                                                Map<String, Object> dataMap, String combinedFieldName)
            throws Exception {
        Object fieldValue = null;

        if (fieldClz.isArray()) {// Take care of Arrays
            int size = getArraySize(dataMap, combinedFieldName);
            if (size > 0) {
                fieldValue = Array.newInstance(fieldClz.getComponentType(),
                        size);
                for (int j = 0; j < size; j++) {
                    Array.set(
                            fieldValue,
                            j,
                            readFieldValue(fieldClz.getComponentType(),
                                    combinedFieldName + "." + j, dataMap));
                }
            }
        } else if (fieldClz.isAssignableFrom(java.util.List.class)) {// Take
            // care
            // of
            // Collections
            java.util.ArrayList list = java.util.ArrayList.class.newInstance();
            int size = getArraySize(dataMap, combinedFieldName);
            if (size > 0) {
                fieldValue = list;
                Class<?> itemClz = getListItemType(type);
                for (int j = 0; j < size; j++) {
                    list.add(readFieldValue(itemClz, combinedFieldName + "."
                            + j, dataMap));
                }
            }
        } else if (fieldClz.isAssignableFrom(java.util.Set.class)) {// Take care of Set
            java.util.Set list = java.util.LinkedHashSet.class.newInstance();
            int size = getArraySize(dataMap, combinedFieldName);
            if (size > 0) {
                fieldValue = list;
                Class<?> itemClz = getListItemType(type);
                for (int j = 0; j < size; j++) {
                    list.add(readFieldValue(itemClz, combinedFieldName + "."
                            + j, dataMap));
                }
            }
        }else {
            fieldValue = readFieldValue(fieldClz, combinedFieldName, dataMap);
        }

        return fieldValue;
    }

    protected static void formatDPTags(Map<String, Object> rowDataMap) {
        if (rowDataMap.get(TestObject.TEST_DP_TAGS) != null) {
            String dpTags = rowDataMap.get(TestObject.TEST_DP_TAGS).toString();
            if (dpTags.trim().length() > 0) {
                String[] dpTagArray = dpTags.split(",");
                String tempDPTags = "";
                for (int idx = 0; dpTagArray.length > 0
                        && idx < dpTagArray.length; idx++) {
                    tempDPTags = tempDPTags.concat("[" + dpTagArray[idx].trim()
                            + "]");
                    if (idx != dpTagArray.length - 1) {
                        tempDPTags = tempDPTags.concat(",");
                    }
                }
                rowDataMap.put(TestObject.TEST_DP_TAGS, tempDPTags);
            }
        }
    }

    public static int getArraySize(Map<String, Object> map, String key) {
        int count = 0;
        boolean valueFound = false;
        key = key.toLowerCase();
        for (Entry<String, Object> entry : map.entrySet()) {
            String key2 = entry.getKey();
            String value2 = (String) entry.getValue();
            if (value2 == null || value2.length() == 0)
                continue;

            key2 = key2.toLowerCase();
            if (key2.startsWith(key + ".")) {
                valueFound = true;
                String subst = key2.substring((key + ".").length());
                String[] ss = subst.split("\\.");
                try {
                    int value = Integer.parseInt(ss[0]);
                    count = (value > count ? value : count);
                } catch (Exception e) {
                }
            }
        }

        return (valueFound ? count + 1 : count);
    }

    /**
     * add for backward compatability
     * @param clazz
     * @param filename
     * @param sheetNumber
     * @param columnNames
     * @param filter
     * @return
     */
    public static synchronized Iterator<Object[]> getDataFromSpreadsheet(Class<?> clazz, String filename, int sheetNumber,
                                                                         String[] columnNames, EasyFilter filter){
        return SpreadSheetUtil.getDataFromSpreadsheet(clazz, filename, null, sheetNumber, columnNames, filter, false);
    }

    /**
     * Reads data from spreadsheet. If sheetName and sheetNumber both are
     * supplied the sheetName takes precedence. Put the excel sheet in the same
     * folder as the test case and specify clazz as <code>this.getClass()</code>
     * .
     */
    public static synchronized Iterator<Object[]> getDataFromSpreadsheet(
            Class<?> clazz, String filename, String sheetName, int sheetNumber,
            String[] fields, EasyFilter filter, boolean readHeaders) {
        return getDataFromSpreadsheet(clazz, filename, sheetName, sheetNumber,
                fields, filter, readHeaders, true);
    }

    /**
     * Reads data from spreadsheet. If sheetName and sheetNumber both are
     * supplied the sheetName takes precedence. Put the excel sheet in the same
     * folder as the test case and specify clazz as <code>this.getClass()</code>
     * .
     *
     * @param clazz
     * @param filename
     * @param sheetName
     * @param sheetNumber
     * @param fields
     * @param filter
     * @param readHeaders
     * @return
     * @throws Exception
     */

    public static synchronized Iterator<Object[]> getDataFromSpreadsheet(
            Class<?> clazz, String filename, String sheetName, int sheetNumber,
            String[] fields, EasyFilter filter, boolean readHeaders,
            boolean supportDPFilter) {

        System.gc(); // KEEPME

        // Let CSVUtil handle CSV Files
        if (filename.toLowerCase().endsWith(".csv")) {
            return CSVUtil.getDataFromCSVFile(clazz, filename, fields, filter,
                    readHeaders, supportDPFilter);
        }

        Workbook w = null;
        InputStream is = null;
        try {
            if (clazz != null) {
                is = clazz.getResourceAsStream(filename);
            } else {
                is = new FileInputStream(filename);
            }

            if (is == null) {
                return new ArrayList<Object[]>().iterator();
            }

            w = Workbook.getWorkbook(is);

            if (w.getSheetNames().length <= sheetNumber) {
                throw new SeleniumTestsException("Sheet # " + sheetNumber + " for "
                        + filename + " not found.");
            }

            if (sheetName != null) {
                for (int i = 0; i < w.getSheetNames().length; i++) {
                    if (sheetName.equals(w.getSheetNames()[i])) {
                        sheetNumber = i;
                    }
                }
            }

            // Get the sheet
            Sheet sheet = w.getSheet(sheetNumber);

            // ignore blank columns
            int columnCount = sheet.getColumns();
            for (int j = 0; j < sheet.getColumns(); j++) {
                String content = sheet.getCell(j, 0).getContents();
                if (content == null || content.trim().length() == 0) {
                    //columnCount = j + 1;
                    columnCount = j;//Change by Jojo, for the 7th column, it's "", so the column count should be 7
                    break;
                }
            }

            List<Object[]> sheetData = new ArrayList<Object[]>();
            if (readHeaders) {
                List<Object> rowData = new ArrayList<Object>();
                if (fields == null) {
                    for (int j = 0; j < columnCount; j++) {
                        rowData.add(sheet.getCell(j, 0).getContents());
                    }
                } else {
                    for (int i = 0; i < fields.length; i++) {
                        rowData.add(fields[i]);
                    }
                }
                sheetData.add(rowData.toArray(new Object[rowData.size()]));
            }

            int testTitleColumnIndex = -1;
            int testSiteColumnIndex = -1;
            // Search for Title & Site column
            for (int i = 0; i < columnCount; i++) {
                if (testTitleColumnIndex == -1
                        && TestObject.TEST_TITLE.equalsIgnoreCase(sheet
                        .getCell(i, 0).getContents())) {
                    testTitleColumnIndex = i;
                } else if (testSiteColumnIndex == -1
                        && TestObject.TEST_SITE.equalsIgnoreCase(sheet.getCell(
                        i, 0).getContents())) {
                    testSiteColumnIndex = i;
                }

                if (testTitleColumnIndex != -1 && testSiteColumnIndex != -1) {
                    break;
                }
            }

            // Let's check for blank rows first
            // The first row is the header
            StringBuffer sbBlank = new StringBuffer();
            for (int i = 1; i < sheet.getRows(); i++) {
                if (testTitleColumnIndex != -1
                        && testSiteColumnIndex != -1
                        && ((sheet.getCell(testTitleColumnIndex, i)
                        .getContents() == null || sheet
                        .getCell(testTitleColumnIndex, i).getContents()
                        .trim().length() == 0) || (sheet.getCell(
                        testSiteColumnIndex, i).getContents() == null || sheet
                        .getCell(testSiteColumnIndex, i).getContents()
                        .trim().length() == 0))) {
                    sbBlank.append(i + 1).append(',');
                }
            }
            if (sbBlank.length() > 0) {
                sbBlank.deleteCharAt(sbBlank.length() - 1);
                throw new SeleniumTestsException(
                        "Blank TestTitle and/or Site value(s) found on Row(s) "
                                + sbBlank.toString() + ".");
            }

            Set<String> uniqueDataSet = new TreeSet<String>();

            // Jerry: Add DataProviderTags filter
            /**
             * Modified by Gary to support include tags and exclude tags
             */
            if (supportDPFilter) {
                EasyFilter dpFilter = getDPFilter();

                if (dpFilter != null) {
                    if (filter == null) {
                        filter = dpFilter;
                    } else {
                        filter = EasyFilter.and(filter, dpFilter);
                    }
                }
            }
            // End
            // The first row is the header
            for (int i = 1; i < sheet.getRows(); i++) {
                // Check for duplicate Title & Site
                if (testTitleColumnIndex != -1 && testSiteColumnIndex != -1) {
                    String uniqueString = sheet
                            .getCell(testTitleColumnIndex, i).getContents()
                            + "$$$$####$$$$"
                            + sheet.getCell(testSiteColumnIndex, i)
                            .getContents();
                    if (uniqueDataSet.contains(uniqueString))
                        throw new SeleniumTestsException(
                                "Duplicate TestTitle and Site combination found in the spreadsheet "
                                        + "with TestTitle = {"
                                        + sheet.getCell(testTitleColumnIndex, i)
                                        .getContents()
                                        + "} "
                                        + "and Site = {"
                                        + sheet.getCell(testSiteColumnIndex, i)
                                        .getContents() + "}");

                    uniqueDataSet.add(uniqueString);
                }

                Map<String, Object> rowDataMap = new HashMap<String, Object>();
                List<Object> rowData = new ArrayList<Object>();

                // Create the mapping between headers and column data
                for (int j = 0; j < columnCount; j++) {
                    rowDataMap.put(sheet.getCell(j, 0).getContents(), sheet
                            .getCell(j, i).getContents());
                }

                if (fields == null) {
                    for (int j = 0; j < columnCount; j++) {
                        rowData.add(sheet.getCell(j, i).getContents());
                    }
                } else {
                    for (int k = 0; k < fields.length; k++) {
                        rowData.add(getValue(rowDataMap, fields[k]));
                    }
                }

                // Jerry: Add DataProviderTags filter
                /**
                 * Modified by Gary to support include tags and exclude tags
                 */
                if (supportDPFilter) {
                    SpreadSheetUtil.formatDPTags(rowDataMap);
                }
                // End
                if (filter == null || filter.match(rowDataMap)) {
                    sheetData.add(rowData.toArray(new Object[rowData.size()]));
                }
            }

            sheet = null;

            if ((!readHeaders && sheetData.isEmpty())
                    || (readHeaders && sheetData.size() <= 1))
                logger.warn("No matching data found on spreadsheet: "
                        + filename + " with filter criteria: "
                        + filter.toString());

            return sheetData.iterator();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {

            if (w != null) {
                w.close();
                w = null;
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }// KEEPME
            }
        }
    }

    public static List<Object[]> getDataList(List<Object[]> table,
                                             EasyFilter filter) {

        List<Object[]> sheetData = new ArrayList<Object[]>();

        // The first row is the header
        String[] fields = (String[]) table.get(0);

        for (int i = 1; i < table.size(); i++) {
            Map<String, Object> rowDataMap = new HashMap<String, Object>();
            Object[] rowData = table.get(i);
            // Create the mapping between headers and column data
            for (int j = 0; j < rowData.length; j++) {
                rowDataMap.put(fields[j], rowData[j]);
            }
            if (filter == null || filter.match(rowDataMap)) {
                sheetData.add(rowData);
            }
        }

        sheetData.add(0, fields);

        return sheetData;
    }

    protected static EasyFilter getDPFilter() {
        String includedTags = ContextManager.getGlobalContext()
                .getDPTagsInclude();
        String excludedTags = ContextManager.getGlobalContext()
                .getDPTagsExclude();

        EasyFilter dpFilter = null;
        if (includedTags != null && includedTags.trim().length() > 0) {
            String[] includeTagsArray = includedTags.split(",");
            for (int idx = 0; includeTagsArray.length > 0
                    && idx < includeTagsArray.length; idx++) {
                if (dpFilter == null) {
                    dpFilter = EasyFilter.containsIgnoreCase(
                            TestObject.TEST_DP_TAGS,
                            "[" + includeTagsArray[0].trim() + "]");
                } else {
                    dpFilter = EasyFilter.or(dpFilter, EasyFilter
                            .containsIgnoreCase(TestObject.TEST_DP_TAGS, "["
                                    + includeTagsArray[idx].trim() + "]"));
                }
            }
        }
        if (excludedTags != null && excludedTags.trim().length() > 0) {
            String[] excludeTagsArray = excludedTags.split(",");
            for (int idx = 0; excludeTagsArray.length > 0
                    && idx < excludeTagsArray.length; idx++) {
                if (dpFilter == null) {
                    dpFilter = EasyFilter.not(EasyFilter.containsIgnoreCase(
                            TestObject.TEST_DP_TAGS, "["
                            + excludeTagsArray[idx].trim() + "]"));
                } else {
                    dpFilter = EasyFilter.and(dpFilter, EasyFilter
                            .not(EasyFilter.containsIgnoreCase(
                                    TestObject.TEST_DP_TAGS, "["
                                    + excludeTagsArray[idx].trim()
                                    + "]")));
                }
            }
        }
        return dpFilter;
    }

    /**
     * Create Entity Objects based on data in spreadsheet.
     *
     * This method is only for Data Provider. Because it also filer the data
     * based on the dpTagsInclude/dpTagsExclude which is defined in testng
     * configuration file
     */
    public static Iterator<Object[]> getEntitiesFromSpreadsheet(Class<?> clazz,
                                                                LinkedHashMap<String, Class<?>> entityClazzMap, String filename,
                                                                int sheetNumber, String[] fields, EasyFilter filter)
            throws Exception {
        return SpreadSheetUtil.getEntitiesFromSpreadsheet(clazz,
                entityClazzMap, filename, null, sheetNumber, fields, filter);
    }

    /**
     * Create Entity Objects based on data in spreadsheet.
     *
     * This method is only for Data Provider. Because it also filer the data
     * based on the dpTagsInclude/dpTagsExclude which is defined in testng
     * configuration file
     *
     * @param clazz
     * @param entityClazzMap
     * @param filename
     * @param sheetName
     * @param sheetNumber
     * @param fields
     * @param filter
     * @param readHeaders
     * @param supportDPFilter
     * @return
     * @throws Exception
     */
    public static Iterator<Object[]> getEntitiesFromSpreadsheet(Class<?> clazz,
                                                                LinkedHashMap<String, Class<?>> entityClazzMap, String filename,
                                                                String sheetName, int sheetNumber, String[] fields,
                                                                EasyFilter filter, boolean readHeaders, boolean supportDPFilter) throws Exception {

        Iterator<Object[]> dataIterator = getDataFromSpreadsheet(clazz,
                filename, sheetName, sheetNumber, fields, filter, readHeaders, supportDPFilter);

        List<Object[]> list = getEntityData(dataIterator,entityClazzMap);

        return list.iterator();
    }

    /**
     * Create Entity Objects based on data in spreadsheet.
     *
     * This method is only for Data Provider. Because it also filer the data
     * based on the dpTagsInclude/dpTagsExclude which is defined in testng
     * configuration file
     *
     * @param clazz
     * @param entityClazzMap
     * @param filename
     * @param sheetName
     * @param sheetNumber
     * @param fields
     * @param filter
     * @return
     * @throws Exception
     */
    public static Iterator<Object[]> getEntitiesFromSpreadsheet(Class<?> clazz,
                                                                LinkedHashMap<String, Class<?>> entityClazzMap, String filename,
                                                                String sheetName, int sheetNumber, String[] fields,
                                                                EasyFilter filter) throws Exception {

        Iterator<Object[]> dataIterator = getDataFromSpreadsheet(clazz,
                filename, sheetName, sheetNumber, fields, filter, true);



        List<Object[]> list = getEntityData(dataIterator,entityClazzMap);

        return list.iterator();
    }
    /**
     * Create Entity Objects based on data in spreadsheet.
     *
     * This method is only for Data Provider. Because it also filer the data
     * based on the dpTagsInclude/dpTagsExclude which is defined in testng
     * configuration file
     */
    public static List<Object[]> getEntitiesListFromSpreadsheet(Class<?> clazz,
                                                                LinkedHashMap<String, Class<?>> entityClazzMap, String filename,
                                                                int sheetNumber, String[] fields, EasyFilter filter)
            throws Exception {
        return SpreadSheetUtil.getEntitiesListFromSpreadsheet(clazz,
                entityClazzMap, filename, null, sheetNumber, fields, filter);
    }

    /**
     * Create Entity Objects based on data in spreadsheet.
     *
     * This method is only for Data Provider. Because it also filer the data
     * based on the dpTagsInclude/dpTagsExclude which is defined in testng
     * configuration file
     *
     * @param clazz
     * @param entityClazzMap
     * @param filename
     * @param sheetName
     * @param sheetNumber
     * @param fields
     * @param filter
     * @return List<Object[]>
     * @throws Exception
     */
    public static List<Object[]> getEntitiesListFromSpreadsheet(Class<?> clazz,
                                                                LinkedHashMap<String, Class<?>> entityClazzMap, String filename,
                                                                String sheetName, int sheetNumber, String[] fields,
                                                                EasyFilter filter) throws Exception {

        Iterator<Object[]> dataIterator = getDataFromSpreadsheet(clazz,
                filename, sheetName, sheetNumber, fields, filter, true);

        List<Object[]> list = getEntityData(dataIterator,entityClazzMap);

        return list;
    }

    private static List<Object[]> getEntityData(Iterator<Object[]> dataIterator,LinkedHashMap<String, Class<?>> entityClazzMap) throws Exception
    {
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
                    rowData.add(readObject(entry.getValue(), entry.getKey(),
                            map));
                }
            }

            for (int i = rowDataArray.length - 1; i >= 0; i--) {
                int docIdx = ((String) headerArray[i]).indexOf(".");
                if (docIdx < 0) {
                    rowData.add(0, rowDataArray[i]);
                } else if (temp.get(((String) headerArray[i]).substring(0,
                        docIdx)) == null) {
                    rowData.add(0, rowDataArray[i]);
                }
            }

            list.add(rowData.toArray(new Object[] { rowData.size() }));
        }
        return list;
    }

    public static Map<String, Object> getFieldsDataNeedToBeSet(
            Map<String, Object> map, String key) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        for (String key2 : map.keySet()) {
            if (key2.equalsIgnoreCase(key)) {
                if(map.get(key2) != null)
                    result.put(key2, map.get(key2).toString());
            }
            if (key2.toLowerCase().startsWith(key.toLowerCase() + ".")) {
                if(map.get(key2) != null)
                    result.put(key2.substring(key.length() + 1), map.get(key2)
                            .toString());
            }
        }
        return result;
    }

    public static Map<String, Object> getFieldsNeedToBeSet(
            Map<String, Object> map, String key) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        String lastKey = "";
        for (String key2 : map.keySet()) {
            if (key2.equalsIgnoreCase(key))
                result.put(key2, map.get(key2));
            if (key2.toLowerCase().startsWith(key.toLowerCase() + ".")) {
                String newkey = key2.substring(key.length() + 1);
                if (newkey.contains("."))
                    newkey = newkey.substring(0, newkey.indexOf("."));
                if (!newkey.equalsIgnoreCase(lastKey))
                    result.put(newkey, map.get(key2));
            }
        }
        return result;
    }

    private static Class<?> getListItemType(Type type)
            throws ClassNotFoundException {

        Class<?> itemClz = null;

        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            itemClz = Class.forName(pt.getActualTypeArguments()[0].toString()
                    .substring("class ".length()));
        }
        return itemClz;
    }

    public static Object getValue(Map<String, Object> map, String key) {
        for (Entry<String, Object> entry : map.entrySet()) {
            if ((entry.getKey() == null && key == null)
                    || (entry.getKey() != null && entry.getKey()
                    .equalsIgnoreCase(key)))
                return entry.getValue();
        }
        return null;
    }

    private static boolean isPrimitive(Class<?> clz) {
        if (clz.isPrimitive())
            return true;
        else if (clz.getCanonicalName().equals(
                "java.lang." + clz.getSimpleName()))
            return true;
        else
            return false;
    }

    public static void main(String[] args) throws ParseException
    {
        Date d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse("2012-07-13T21:05:39.000Z");
        System.out.println(d);
        Date d2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz").parse("2012-05-11T12:01:12.123PDT");
        System.out.println(d2);
    }

    private static Object readFieldValue(Class<?> fieldClz, String fieldName,
                                         Map<String, Object> dataMap) throws Exception {
        Object fieldValue = null;
        String tempValue = (String) getValue(dataMap, fieldName);

        // Return null when field is atomic and value is null or blank
        if ((tempValue == null || tempValue.length() == 0)
                && (fieldClz.isEnum()
                || fieldClz.getName().equals("java.util.Calendar")
                || fieldClz.getName().equals("java.math.BigDecimal") || isPrimitive(fieldClz)))
            return null;

        if (fieldClz.isEnum()) {
            try {
                fieldValue = fieldClz.getMethod("valueOf", String.class)
                        .invoke(fieldClz, tempValue);
            } catch (Exception e) {
                logger.warn("Ex", e);
            }
        } else if (fieldClz.getName().equals("java.util.Calendar")) {// Take
            // care
            // of
            // Date
            Calendar calendar = Calendar.getInstance();
            try{
                calendar.setTime(new SimpleDateFormat("MM/dd/yyyy").parse(tempValue));
            }
            catch(ParseException ex)
            {
                try{
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(tempValue));
                }catch(ParseException ex2)
                {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz").parse(tempValue));

                }

            }
            fieldValue = calendar;
        } else if (fieldClz.getName().equals("java.math.BigDecimal")) {// Take
            // care
            // of
            // BigDecimal
            fieldValue = new BigDecimal(tempValue);
        } else if (isPrimitive(fieldClz)) {// Take care of primitives
            Constructor<?> constructor;
            try {
                if (fieldClz.getName().equals("java.lang.String")) {
                    fieldValue = tempValue;
                } else {
                    if (PRIMITIVE_TYPE_MAP.containsKey(fieldClz))
                        constructor = PRIMITIVE_TYPE_MAP.get(fieldClz)
                                .getConstructor(String.class);
                    else
                        constructor = fieldClz.getConstructor(String.class);

                    fieldValue = constructor.newInstance(tempValue);
                }
            } catch (Exception e) {
                logger.warn("Ex", e);
            }
        } else {// Non-atomic or Object field
            fieldValue = readObject(fieldClz, fieldName, dataMap);
        }

        return fieldValue;
    }

    public static Object readObject(Class<?> clz, String objectName,
                                    Map<String, Object> dataMap) throws Exception {
        Object object = null;
        if (clz == null)
            return null;
        if (objectName == null)
            objectName = clz.getSimpleName();
        Map<String, Object> fieldMap = getFieldsNeedToBeSet(dataMap, objectName);
        Map<String, Object> datamap = getFieldsDataNeedToBeSet(dataMap,
                objectName);

        for (String fieldName : fieldMap.keySet()) {
            String first = "" + fieldName.charAt(0);
            String realfieldName = fieldName.replaceFirst(first,
                    first.toLowerCase());
            Object fieldValue = null;
            Class<?> type = null;
            try {
                Class<?>[] parameterTypes = new Class<?>[] {};
                Method method = clz
                        .getMethod("get" + fieldName, parameterTypes);
                type = method.getReturnType();
                fieldValue = _readFieldValueObject(type,
                        method.getGenericReturnType(), datamap, fieldName);
            } catch (NoSuchMethodException ex) {
                try {
                    Class<?>[] parameterTypes = new Class<?>[] {};
                    Method method = clz.getMethod("is" + fieldName,
                            parameterTypes);
                    type = method.getReturnType();
                    fieldValue = _readFieldValueObject(type,
                            method.getGenericReturnType(), datamap, fieldName);
                } catch (NoSuchMethodException ex2) {
                    try {
                        Field field = clz.getDeclaredField(realfieldName);
                        fieldValue = _readFieldValueObject(field.getType(), field.getGenericType(), datamap, fieldName);
                    }catch (NoSuchFieldException ex3) {
                        //Can't find field, get**,is*** method, set it to String.class
                        try{
                            fieldValue = _readFieldValueObject(String.class, String.class, datamap, fieldName);
                            type = String.class;
                        }
                        catch(Exception e)
                        {
                            logger.warn("Ex:"+clz.getName(), e);
                        }
                    }
                }
            }

            // execute the Setter Method
            try {
                if(fieldValue != null){

                    if(object == null)
                    {
                        try{
                            object = clz.newInstance();
                        }catch(InstantiationException e)
                        {
                            //handle no null parameter constructor
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

                    if (object == null)
                        object = clz.newInstance();
                    try {
                        Class<?>[] parameterTypes = new Class<?>[1];
                        parameterTypes[0] = type;
                        Method method = object.getClass().getMethod(
                                "set" + fieldName, parameterTypes);
                        method.invoke(object, fieldValue);
                    } catch (Exception ex) {
                        Field field2 = object.getClass().getDeclaredField(
                                realfieldName);
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
