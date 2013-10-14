package com.seleniumtests.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.UUID;

import com.seleniumtests.controller.SeleniumTestsContext;
import com.seleniumtests.controller.SeleniumTestsContextManager;
import org.apache.log4j.Logger;

import com.seleniumtests.exception.SeleniumTestsException;

public class URLAssistant {

	private static Logger logger = Logger.getLogger(URLAssistant.class);
	private static final String HTTPS_PROTOCOL = "https";

	public static String getRandomHashCode(String seed) {
		String signature;
		if (SeleniumTestsContextManager.getThreadContext() != null)
			signature = SeleniumTestsContextManager.getThreadContext()
					.getTestMethodSignature();
		else
			signature = "";
		byte[] data = (signature + UUID.randomUUID().getLeastSignificantBits() + seed)
				.getBytes();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			return new BigInteger(1, digest.digest(data)).toString(16);
		} catch (Exception e2) {
		}
		return new String(data);
	}

	public static void main(String[] args) throws Exception {
	}

	/**
	 * Open URL using URLConnection
	 * 
	 * @param url
	 * @return content of the page
	 * @throws Exception
	 */
	public static String open(String url) throws Exception {

		return open(url, false);
	}

	public static String open(String url, boolean validate) throws Exception {
		return open(url, validate, 30 * 1000,true);
	}
	
	/**
	 * 
	 * @param url
	 * @param validate : default: false
	 * @param useProxy : true - will use webProxyAddress in SeleniumTestsContext, default:true
	 * @return
	 * @throws Exception
	 */
	public static String open(String url, boolean validate, boolean useProxy) throws Exception {
		return open(url, validate, 30 * 1000,useProxy);
	}

	/**
	 * Open URL using URLConnection
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String open(String url, boolean validate, int timeout,boolean useProxy)
			throws Exception {

		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;

		try {

			is = openAsStream(url, timeout,useProxy);
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line).append('\n');
			}

			return sb.toString();
		} catch (Throwable e) {
			throw new SeleniumTestsException(e.getMessage(), e);
		} finally {

			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Open the URL, remember close the input stream after usage
	 * 
	 * @param url
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream openAsStream(String url) throws Exception {
		return openAsStream(url, 30 * 1000,true);
	}

	private static InputStream openAsStream(String url,
			int timeout,boolean useProxy) throws Exception {
		/*if (url.startsWith(HTTPS_PROTOCOL))
			return openHttpsUrlAsStream(url,useProxy);
*/
		logger.info("Opening URL: " + url);
		InputStream is = null;
		URLConnection connection = null;
		try {
			SeleniumTestsContext context = SeleniumTestsContextManager.getThreadContext();
			if (useProxy && context.getWebProxyAddress()!= null) {
				String host = context.getWebProxyAddress().split(":")[0];
				int port = Integer.parseInt(context.getWebProxyAddress().split(":")[1]);
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
				connection = new URL(url).openConnection(proxy);
			}
			else {
				connection = new URL(url).openConnection();
			}
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			is = connection.getInputStream();
			return is;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new SeleniumTestsException(e.getMessage(), e);
		}
	}
}