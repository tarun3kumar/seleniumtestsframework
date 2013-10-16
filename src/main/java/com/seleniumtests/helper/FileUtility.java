package com.seleniumtests.helper;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.channels.FileLock;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class FileUtility {
	static Logger logger = Logger.getLogger(FileUtility.class);
	final static int  BUFFER = 2048;

	private static Map<Integer, FileLock> fileLockMap = new HashMap<Integer, FileLock>();

	public static void extractJar(String storeLocation,Class<?> clz) throws IOException{
		File firefoxProfile = new File(storeLocation);
		String location = clz.getProtectionDomain().getCodeSource().getLocation().getFile();
		
		JarFile jar = new JarFile(location);
		System.out.println("Extracting jar file::: "+location);
		firefoxProfile.mkdir();
		Enumeration<?> jarFiles = jar.entries();
		while (jarFiles.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) jarFiles.nextElement();
			String currentEntry = entry.getName();
			File destinationFile = new File(storeLocation, currentEntry);
			File destinationParent = destinationFile.getParentFile();
			// create the parent directory structure if required
			destinationParent.mkdirs();
			if (!entry.isDirectory()) {
				BufferedInputStream is = new BufferedInputStream(jar.getInputStream(entry));
				int currentByte;
				// buffer for writing file
				byte data[] = new byte[BUFFER];
				// write the current file to disk
				FileOutputStream fos = new FileOutputStream(destinationFile);
				BufferedOutputStream destination = new BufferedOutputStream(fos, BUFFER);
				// read and write till last byte
				while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
					destination.write(data, 0, currentByte);
				}
				destination.flush();
				destination.close();
				is.close();
			}
		}
		FileUtils.deleteDirectory(new File(storeLocation + "\\META-INF"));
		if(OSUtility.isWindows())
			new File(storeLocation + "\\"+ clz.getCanonicalName().replaceAll("\\.", "\\\\") + ".class").delete();
		else
			new File(storeLocation + "/"+ clz.getCanonicalName().replaceAll("\\.", "/") + ".class").delete();
	}
	
	public static void copyFile(File srcPath, File dstPath) throws IOException {

		if (srcPath.isDirectory()) {
			if (!dstPath.exists()) {
				dstPath.mkdir();
			}

			String files[] = srcPath.list();
            for (String file : files) {
                copyFile(new File(srcPath, file), new File(dstPath,
                        file));
            }
		} else {
			if (!srcPath.exists()) {
				throw new IOException("Directory Not Found ::: " + srcPath);
			} else {
				InputStream in = null;
				OutputStream out = null;
				try {
					if (!dstPath.getParentFile().exists())
						dstPath.getParentFile().mkdirs();

					in = new FileInputStream(srcPath);
					out = new FileOutputStream(dstPath);

					// Transfer bytes
					byte[] buf = new byte[1024];
					int len;

					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (Exception e) {
                            e.printStackTrace();
						}
					}
					if (out != null) {
						try {
							out.close();
						} catch (Exception e) {
                            e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public static void copyFile(String srcPath, String dstPath)
			throws IOException {
		copyFile(new File(srcPath), new File(dstPath));
	}

	/**
	 * Deletes Directory with Files
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
		}
		return path.delete();
	}
	
	public static boolean deleteDirectory(String dir){
		return deleteDirectory(new File(dir));
	}

    /**
	 * Read contents of a file
	 * 
	 * @param path
	 * @returns content
	 * @throws IOException
	 */
	public static String readFromFile(File path) throws IOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			return readFromFile(fis);
		} finally {
			if (fis != null)
				fis.close();
		}
	}

	/**
	 * Read contents From Stream
	 * 
	 * @param path
	 * @returns content
	 * @throws IOException
	 */
	public static String readFromFile(InputStream path) throws IOException {
		InputStreamReader fr = null;
		BufferedReader br = null;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			fr = new InputStreamReader(path);
			br = new BufferedReader(fr);

			String line;
			while ((line = br.readLine()) != null)
				stringBuilder.append(line).append("\n");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
                    e.printStackTrace();
				}
			}
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
                    e.printStackTrace();
				}
			}
		}

		return stringBuilder.toString();
	}

    /**
	 * Constructs ImageElement from bytes and stores it
	 * 
	 * @param path
	 */
	public static synchronized void writeImage(String path, byte[] byteArray) {
		if(byteArray.length==0)return;
		System.gc();

		InputStream in = null;
		FileOutputStream fos = null;
		try {
			File parentDir = new File(path).getParentFile();
			if(! parentDir.exists()) 
			   parentDir.mkdirs(); 
			byte[] decodeBuffer = Base64.decodeBase64(byteArray);
			in = new ByteArrayInputStream(decodeBuffer);
			BufferedImage img = ImageIO.read(in);
			fos = new FileOutputStream(path);
			ImageIO.write(img, "png", fos);
			img = null;
		} catch (Exception e) {
			logger.warn(e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
                    e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
                    e.printStackTrace();
				}
			}
		}
	}

    /**
	 * Saves HTML Source
	 * 
	 * @param path
	 * @throws Exception
	 */

	public static void writeToFile(String path, String content)
			throws IOException {
		
		System.gc();
		FileOutputStream fileOutputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedWriter bw = null;
		try {
			File parentDir = new File(path).getParentFile();
			if(! parentDir.exists()) 
			   parentDir.mkdirs(); 
			fileOutputStream = new FileOutputStream(path);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF8");
			bw = new BufferedWriter(outputStreamWriter);
			bw.write(content);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (Exception e) {
                    e.printStackTrace();
				}
			}
			if (outputStreamWriter != null) {
				try {
					outputStreamWriter.close();
				} catch (Exception e) {
                    e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (Exception e) {
                    e.printStackTrace();
				}
			}
		}
	}

	public static String getLatestFile(String folder){
		String file = null;
		File folderFile = new File(folder);
		if(folderFile.exists() && folderFile.isDirectory()){
			File[] files = folderFile.listFiles();
			long date = 0;
			
			for(int i=0;i<files.length;i++)
			{
				if(files[i].lastModified()>date){
					date = files[i].lastModified();
					file = files[i].getAbsolutePath();
				}
			}
		}
		return file;
	}
	
	public static String decodePath(String path) throws UnsupportedEncodingException{
		return URLDecoder.decode(path, "UTF-8");
	}
}
