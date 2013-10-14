package com.seleniumtests.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.seleniumtests.controller.Filter;
import com.seleniumtests.util.internal.entity.TestEntity;
import org.apache.log4j.Logger;

import com.seleniumtests.controller.TestLogging;
import com.seleniumtests.exception.SeleniumTestsException;

public class CSVUtil {
    private static Logger logger = TestLogging.getLogger(CSVUtil.class);

    public static final String DOUBLE_QUOTE = "\"";
    public static final String DELIM_CHAR = ",";
    public static final String TAB_CHAR = "	";

    /**
     * Reads data from csv formatted file. Put the excel sheet in the same folder as the test case and specify clazz as <code>this.getClass()</code>.
     *
     * @param clazz
     * @param filename
     * @param fields
     * @param filter
     * @param readHeaders
     * @return
     * @throws Exception
     */
    public static Iterator<Object[]> getDataFromCSVFile(Class<?> clazz, String filename, String[] fields, Filter filter, boolean readHeaders, boolean supportDPFilter) {
        return getDataFromCSVFile(clazz, filename, fields, filter, readHeaders, null, supportDPFilter);
    }

    public static Iterator<Object[]> getDataFromCSVFile(Class<?> clazz, String filename, String[] fields, Filter filter, boolean readHeaders, String delimiter, boolean supportDPFilter) {

        InputStream is = null;
        try {
            if (clazz != null)
                is = clazz.getResourceAsStream(filename);
            else
                is = new FileInputStream(filename);

            if (is == null)
                return new ArrayList<Object[]>().iterator();

            // Get the sheet
            String[][] csvData = read(is, delimiter);

            List<Object[]> sheetData = new ArrayList<Object[]>();
            if (readHeaders) {
                List<Object> rowData = new ArrayList<Object>();
                if (fields == null) {
                    for (int j = 0; j < csvData[0].length; j++) {
                        rowData.add(csvData[0][j]);
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
            // Search for Title
            for (int i = 0; i < csvData[0].length; i++) {
                if (testTitleColumnIndex == -1 && TestEntity.TEST_TITLE.equalsIgnoreCase(csvData[0][i])) {
                    testTitleColumnIndex = i;
                }
                if (testTitleColumnIndex != -1 && testSiteColumnIndex != -1)
                    break;
            }

            // Let's check for blank rows first
            // The first row is the header
            StringBuffer sbBlank = new StringBuffer();
            for (int i = 1; i < csvData.length; i++) {
                if (testTitleColumnIndex != -1 && testSiteColumnIndex != -1 && (csvData[i][testTitleColumnIndex].trim().length() == 0 || csvData[i][testSiteColumnIndex].trim().length() == 0)) {
                    sbBlank.append(i + 1).append(',');
                }
            }
            if (sbBlank.length() > 0) {
                sbBlank.deleteCharAt(sbBlank.length() - 1);
                throw new SeleniumTestsException("Blank TestTitle found on Row(s) " + sbBlank.toString() + ".");
            }

            Set<String> uniqueDataSet = new TreeSet<String>();

            // Jerry: Add DataProviderTags filter
            /**
             * Modified by Gary to support include tags and exclude tags
             */
            if (supportDPFilter) {
                Filter dpFilter = SpreadSheetUtil.getDPFilter();

                if (dpFilter != null) {
                    if (filter == null) {
                        filter = dpFilter;
                    } else {
                        filter = Filter.and(filter, dpFilter);
                    }
                }
            }
            // End
            // The first row is the header
            for (int i = 1; i < csvData.length; i++) {

                // Check for duplicate Title
                if (testTitleColumnIndex != -1 && testSiteColumnIndex != -1) {
                    String uniqueString = csvData[i][testTitleColumnIndex] + "$$$$####$$$$" + csvData[i][testSiteColumnIndex];
                    if (uniqueDataSet.contains(uniqueString))
                        throw new SeleniumTestsException("Duplicate TestTitle found in the spreadsheet " + "with TestTitle = {" + csvData[i][testTitleColumnIndex] + "} " + "and Site = {" + csvData[i][testSiteColumnIndex] + "}");

                    uniqueDataSet.add(uniqueString);
                }

                Map<String, Object> rowDataMap = new HashMap<String, Object>();
                List<Object> rowData = new ArrayList<Object>();

                // Create the mapping between headers and column data
                for (int j = 0; j < csvData[i].length; j++) {
                    rowDataMap.put(csvData[0][j], csvData[i][j]);
                }

                if (fields == null) {
                    for (int j = 0; j < csvData[0].length; j++) {
                        // Fix for null values not getting created when number of columns in a row is less than expected.
                        if (csvData[i].length > j)
                            rowData.add(csvData[i][j]);
                        else
                            rowData.add(null);
                    }
                } else {
                    for (int k = 0; k < fields.length; k++) {
                        rowData.add(SpreadSheetUtil.getValue(rowDataMap, fields[k]));
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

            if ((!readHeaders && sheetData.isEmpty()) || (readHeaders && sheetData.size() <= 1))
                logger.warn("No matching data found on csv file: " + filename + " with filter criteria: " + filter.toString());

            return sheetData.iterator();
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }// KEEPME
            }
        }
    }

    /**
     * Get headers from a csv file
     * @param clazz	- null means use the absolute file path, otherwise use relative path under the class
     * @param filename
     * @param delimiter - null means ","
     * @return
     */
    public static ArrayList<String> getHeaderFromCSVFile(Class<?> clazz, String filename,String delimiter)
    {
        if(delimiter==null)delimiter=",";

        InputStream is = null;
        try {
            if (clazz != null)
                is = clazz.getResourceAsStream(filename);
            else
                is = new FileInputStream(filename);

            if (is == null)
                return null;

            // Get the sheet
            String[][] csvData = read(is, delimiter);

            ArrayList<String> rowData = new ArrayList<String>();

            for (int j = 0; j < csvData[0].length; j++) {
                rowData.add(csvData[0][j]);
            }
            return rowData;
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }// KEEPME
            }
        }

    }

    public static void main(String[] s) {
        String line = "142843,\"testOneTimeCCMultiple,AccountPending\"," + "	Step 8. Verify user can make 1xCC payment for pending and active accounts " + "(for users with multiple \"accounts),US,,ecaf_seller_cc_pmt0_us,password,,,991,,";
        String[] s2 = parseLine(line, DELIM_CHAR);
        System.out.print(s2);
    }

    /**
     * Parse a line
     *
     * @param line
     * @param delim
     * @return
     */
    public static String[] parseLine(String line, String delim) {
        if (line == null || line.trim().length() == 0) {
            return null;
        }

        List<String> tokenList = new ArrayList<String>();
        String[] result = null;

        String[] tokens = line.split(delim);
        int count = 0;
        while (count < tokens.length) {
            if (tokens[count] == null || tokens[count].length() == 0) {
                tokenList.add("");
                count++;
                continue;
            }

            if (tokens[count].startsWith(DOUBLE_QUOTE)) {
                StringBuffer sbToken = new StringBuffer(tokens[count].substring(1));
                while (count < tokens.length && !tokens[count].endsWith(DOUBLE_QUOTE)) {
                    count++;
                    sbToken.append(DELIM_CHAR).append(tokens[count]);
                }
                sbToken.deleteCharAt(sbToken.length() - 1);
                tokenList.add(sbToken.toString());
            } else {
                tokenList.add(tokens[count]);
            }
            count++;
        }

        if (tokenList.size() > 0) {
            result = new String[tokenList.size()];
            tokenList.toArray(result);
        }
        return result;

    }

    /**
     * Parses an file and returns a String[][] object
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String[][] read(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        return read(fis);
    }

    public static String[][] read(InputStream is) throws IOException {
        return read(is, null);
    }

    /**
     * Parses an input stream and returns a String[][] object
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String[][] read(InputStream is, String delim) throws IOException {

        String[][] result = null;
        List<String[]> list = new ArrayList<String[]>();
        String inputLine; // String that holds current file line

        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8")); // KEEPME
        while ((inputLine = reader.readLine()) != null) {
            try {
                String[] item = null;
                if (delim == null)
                    item = parseLine(inputLine, DELIM_CHAR);
                else
                    item = parseLine(inputLine, delim);
                if (item != null)
                    list.add(item);
            } catch (Exception e) {// KEEPME
            }
        }
        reader.close();

        if (list.size() > 0) {
            result = new String[list.size()][];
            list.toArray(result);
        }
        return result;
    }

    /**
     * Parses an URL and returns a String[][] object
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String[][] read(URL url) throws IOException {
        URLConnection con = url.openConnection();
        return read(con.getInputStream());
    }
}
