package com.seleniumtests.helper;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
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

public class FileHelper {
	static Logger logger = Logger.getLogger(FileHelper.class);
	final static int  BUFFER = 2048;

	private static Map<Integer, FileLock> fileLockMap = new HashMap<Integer, FileLock>();

	public static void extractJar(String storeLocation,Class<?> clz) throws IOException{
		File ffprofile = new File(storeLocation);
		String location = clz.getProtectionDomain().getCodeSource().getLocation().getFile();
		
		JarFile jar = new JarFile(location);
		System.out.println("extracting "+location);
		// make the new directory
		ffprofile.mkdir();
		//System.out.println("FirefoxProfile: " + profilePath + "\\CustomProfileDirCUSTFF");
		Enumeration<?> jarFiles = jar.entries();
		while (jarFiles.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) jarFiles.nextElement();
			String currentEntry = entry.getName();
			File destFile = new File(storeLocation, currentEntry);
			File destinationParent = destFile.getParentFile();

			// create the parent directory structure if needed
			destinationParent.mkdirs();
			if (!entry.isDirectory()) {
				BufferedInputStream is = new BufferedInputStream(jar.getInputStream(entry));
				int currentByte;
				// establish buffer for writing file
				byte data[] = new byte[BUFFER];

				// write the current file to disk
				FileOutputStream fos = new FileOutputStream(destFile);
				BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

				// read and write until last byte is encountered
				while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, currentByte);
				}
				dest.flush();
				dest.close();
				is.close();
			}
		}
		FileUtils.deleteDirectory(new File(storeLocation + "\\META-INF"));
		if(OSHelper.isWindows())
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
			for (int i = 0; i < files.length; i++) {
				copyFile(new File(srcPath, files[i]), new File(dstPath,
						files[i]));
			}
		} else {
			if (!srcPath.exists()) {
				throw new IOException("DIR_NOT_FOUND: " + srcPath);
			} else {
				InputStream in = null;
				OutputStream out = null;
				try {
					if (!dstPath.getParentFile().exists())
						dstPath.getParentFile().mkdirs();

					in = new FileInputStream(srcPath);
					out = new FileOutputStream(dstPath);

					// Transfer bytes from in to out
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
						}
					}
					if (out != null) {
						try {
							out.close();
						} catch (Exception e) {
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
	 * Deletes a Directory with Files
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return path.delete();
	}
	
	public static boolean deleteDirectory(String dir){
		return deleteDirectory(new File(dir));
	}

	/**
	 * Locks a profile to use
	 * 
	 * @param profilePath
	 * @param max
	 * @return
	 * @throws IOException
	 */
	public static int performFileLock(String profilePath, int max)
			throws IOException {

		FileLock lock = null;

		File dir = new File(profilePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		RandomAccessFile raf = null;
		FileChannel fileChannel = null;

		for (int i = 1; i <= max; i++) {
			try {
				logger.info("Trying to lock profile_" + i);

				raf = new RandomAccessFile(profilePath + "/" + i + ".lck", "rw");
				fileChannel = raf.getChannel();
				lock = fileChannel.tryLock();
				if (lock != null && lock.isValid() && !lock.isShared()) {
					File pdir = new File(profilePath + "/profile_" + i);
					if (!pdir.exists())
						pdir.mkdirs();

					fileLockMap.put(Integer.valueOf(i), lock);

					logger.info("Successfully locked profile_" + i);
					return i;
				} else {
					try {
						lock = null;
						fileChannel.close();
						raf.close();
					} catch (Exception e) {
					}// KEEPME
				}
			} catch (Exception ioe) {
				// ioe.printStackTrace();
				try {
					lock = null;
					fileChannel.close();
					raf.close();
				} catch (Exception e) {
				}
			}
		}
		throw new IOException("UNABLE_TO_LOCK_PROFILE");
	}

	/**
	 * Locking the port
	 * 
	 * @param startPort
	 * @param max
	 * @return
	 */
	public static int performPortLock(int startPort, int max) {

		for (int i = startPort; i <= startPort + max; i++) {
			try {
				// Create a server socket
				final ServerSocket ss = new ServerSocket(i);
				Runnable rble = new Runnable() {
					public void run() {
						try {
							ss.accept();
						} catch (Exception ex) {
						} finally {
							try {
								logger.info("Releasing lock on port "
										+ ss.getLocalPort());
								ss.close();
								logger.info("Done");
							} catch (Exception e) {// KEEPME
							}
						}
					}
				};
				Thread thr = new Thread(rble);
				thr.start();
				return i;
			} catch (Exception e) {
			}
		}
		return 0;
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
		StringBuffer sbContent = new StringBuffer();

		try {
			fr = new InputStreamReader(path);
			br = new BufferedReader(fr);

			String line = null;
			while ((line = br.readLine()) != null)
				sbContent.append(line).append("\n");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
				}
			}
		}

		return sbContent.toString();
	}

	/**
	 * Releases filelock given a profile number
	 * 
	 * @param i
	 */
	public static void releaseFileLock(int i) {
		FileLock lock = fileLockMap.get(Integer.valueOf(i));
		logger.info("Lock for profile_" + i + "-->" + lock);
		if (lock != null) {
			try {
				FileChannel channel = lock.channel();
				lock.release();
				channel.close();

				fileLockMap.remove(Integer.valueOf(i));
				logger.info("Successfully released profile_" + i);
			} catch (IOException e) {
				logger.error("Ex", e);
			}
		}
	}

	public static void releasePortLock(int port) {
		Socket socket = null;
		OutputStream os = null;
		try {
			socket = new Socket("127.0.0.1", port);
			os = socket.getOutputStream();
		} catch (Exception e) {
			logger.warn("Ex", e);
		} finally {
			try {
				os.close();
			} catch (Exception e) {
			}
			try {
				socket.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Creates Image from bytes and stores it
	 * 
	 * @param path
	 * @param screenShot
	 */
	public static synchronized void writeImage(String path, byte[] byteArray) {
		if(byteArray.length==0)return;
		System.gc(); // KEEPME

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
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	public static byte[] readBinaryFile(String aInputFileName)
	{
		  logger.info("Reading in binary file named : " + aInputFileName);
		    File file = new File(aInputFileName);
		    logger.info("File size: " + file.length());
		    byte[] result = new byte[(int)file.length()];
		    try {
		      InputStream input = null;
		      try {
		        int totalBytesRead = 0;
		        input = new BufferedInputStream(new FileInputStream(file));
		        while(totalBytesRead < result.length){
		          int bytesRemaining = result.length - totalBytesRead;
		          //input.read() returns -1, 0, or more :
		          int bytesRead = input.read(result, totalBytesRead, bytesRemaining); 
		          if (bytesRead > 0){
		            totalBytesRead = totalBytesRead + bytesRead;
		          }
		        }
		        /*
		         the above style is a bit tricky: it places bytes into the 'result' array; 
		         'result' is an output parameter;
		         the while loop usually has a single iteration only.
		        */
		        logger.info("Num bytes read: " + totalBytesRead);
		      }
		      finally {
		    	logger.info("Closing input stream.");
		        if(input!=null)
		        	input.close();
		      }
		    }
		    catch (FileNotFoundException ex) {
		    	logger.info("File not found.");
		    }
		    catch (IOException ex) {
		    	logger.info(ex);
		    }
		    return result;
	}

	/**
	 * Write to file
	 * 
	 * @param path
	 * @param is
	 * @throws IOException
	 */
	public static void writeToFile(String path, InputStream is)
			throws IOException {
		
		System.gc(); // KEEPME
		
		
		BufferedInputStream bis = new BufferedInputStream(is);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(path));
		try {
			File parentDir = new File(path).getParentFile();
			if(! parentDir.exists()) 
			   parentDir.mkdirs(); 
			byte[] bArr = new byte[4096];
			int nRead = 0;
			while ((nRead = bis.read(bArr)) != -1) {
				bos.write(bArr, 0, nRead);
			}
		} finally {
			try {
				bos.close();
			} catch (Exception e) {
			}
			;
			try {
				bis.close();
			} catch (Exception e) {
			}
			;
			try {
				is.close();
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * Saves HTML Source
	 * 
	 * @param path
	 * @param selenium
	 * @throws Exception
	 */

	public static void writeToFile(String path, String content)
			throws IOException {
		
		System.gc(); // KEEPME
		
		
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			File parentDir = new File(path).getParentFile();
			if(! parentDir.exists()) 
			   parentDir.mkdirs(); 
			fos = new FileOutputStream(path);
			osw = new OutputStreamWriter(fos, "UTF8");
			bw = new BufferedWriter(osw);
			bw.write(content);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (Exception e) {
				}// KEEPME
			}
			if (osw != null) {
				try {
					osw.close();
				} catch (Exception e) {
				}// KEEPME
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}// KEEPME
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
