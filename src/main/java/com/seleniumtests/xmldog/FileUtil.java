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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import java.util.List;
import java.util.StringTokenizer;

/**
 * FileUtil class containing utility functions related to Files.
 *
 * @author   Ritesh Trivedi
 * @version  1.0 07/03/2003 2:49 PM PST
 */

public class FileUtil {

    /**
     * Writes String representation of the elements in the list in to the file.
     *
     * @param   file  the File in which write
     * @param   list  the list containing the elements to be written
     *
     * @throws  IOException  If anything wrong with File operations
     */

    public static void writeListAsStr(final String file, final List list) throws IOException {

        if ((list == null) || (list.size() == 0)
                ||

                (file == null) || (file.trim().length() == 0)) {

            return;
        }

        BufferedWriter bw = null;

        try {

            bw = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < list.size(); i++) {

                bw.write(list.get(i).toString());

                bw.write(StringUtil.getNewlineStr());

            }

        } catch (IOException ex) {

            throw ex;

        } finally {

            try {

                bw.close();

                bw = null;

            } catch (Exception ex) {

                // do nothing

            }

        }

    }

    /**
     * Gets prefix for the filename.
     *
     * <p/>
     * <br>
     * Anything till the last "." in the filename is considered Prefix
     *
     * @param   filename  the Name of the file
     *
     * @return  the prefix for the filename
     */

    public static String getPrefix(final String filename) {

        StringTokenizer st = new StringTokenizer(filename, ".");

        StringBuffer sb = new StringBuffer();

        // System.out.println("# of tokens " + st.countTokens());

        int index = 0;

        int numTokens = st.countTokens();

        if (numTokens == 1) {

            return st.nextToken();
        }

        String token = null;

        while (st.hasMoreTokens()) {

            // System.out.println("token # " + index);

            token = st.nextToken();

            if (index < numTokens - 1) {

                sb.append(token + ".");
            }

            index++;

        }

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();

    }

    /**
     * Creates temporary file in a temporary directory under user.home dir.
     *
     * @param   filename  the Name of the temp file to be created
     * @param   is        the InputStream which is read to put the file content
     *
     * @return  full path of the temporary file created
     *
     * @throws  IOException  If temporary file cannot be created for any reason
     */

    public static String createTempFile(final String filename, final InputStream is) throws IOException {

        if (is == null) {

            throw new IOException("InputStream is null");
        }

        FileOutputStream fos = null;

        File tempFile = null;

        try {

            String userHome = System.getProperty("user.home");

            File f = new File(userHome + File.separator + "temp");

            f.mkdirs();

            if ((filename == null) || (filename.trim().equals(""))) {

                tempFile = f.createTempFile("file1", ".tmp");
            } else {

                tempFile = f.createTempFile(filename, "");
            }

            fos = new FileOutputStream(tempFile);

            byte[] buffer = new byte[8192];

            int bytesRead = 0;

            while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {

                fos.write(buffer, 0, bytesRead);
            }

        } finally {

            if (fos != null) {

                fos.close();

                fos = null;

            }

        }

        return tempFile.getCanonicalPath();

    }

    /**
     * Main method for debugging purpose only.
     */

    public static void main(final String[] args) {

        /*
         *
         * Tests writing list of String to the File
         *
         * List list = new ArrayList();
         *
         *
         *
         * list.add("Ritesh");
         *
         * list.add("Trivedi");
         *
         * list.add("EOF");
         *
         *
         *
         * try
         *
         * {
         *
         *      FileUtil.writeListAsStr("tempfile.txt", list);
         *
         * } catch (Exception ex)
         *
         * {
         *
         *      ex.printStackTrace();
         *
         * }
         *
         */

        /*
         *
         * Tests the Prefix
         *
         */

        System.out.println(FileUtil.getPrefix("test3.txt.out"));

    }

}
