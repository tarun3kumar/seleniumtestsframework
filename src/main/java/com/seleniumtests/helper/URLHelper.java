package com.seleniumtests.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.log4j.Logger;

import com.seleniumtests.controller.Context;
import com.seleniumtests.controller.ContextManager;
import com.seleniumtests.driver.api.EasySSLProtocolSocketFactory;
import com.seleniumtests.exception.SeleniumTestsException;

public class URLHelper {

	private static Logger logger = Logger.getLogger(URLHelper.class);
	private static final String HTTPS_PROTOCOL = "https";

	// private static final int HTTPS_PORT = 443;

//	public static String convert(String url) {
//		String urlConvertClass = ContextManager.getThreadContext()
//				.getUrlConvertClass();
//		if (urlConvertClass != null) {
//			try {
//				Class<?> converterClass = Class.forName(urlConvertClass);
//				Object converter = converterClass.newInstance();
//				Method convert = converterClass.getMethod("convertURL",
//						String.class);
//				return (String) convert.invoke(converter, url);
//			} catch (Exception e) {
//				logger.warn("Convert URL failed", e);
//			}
//
//		}
//		return url;
//	}

	public static String getRandomHashCode(String seed) {
		String signature;
		if (ContextManager.getThreadContext() != null)
			signature = ContextManager.getThreadContext()
					.getTestMethodSignature();
		else
			signature = "";
		byte[] data = (signature + UUID.randomUUID().getLeastSignificantBits() + seed)
				.getBytes();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			return new BigInteger(1, digest.digest(data)).toString(16);
		} catch (Exception e2) {
			// OK, we won't digest
		}
		return new String(data);
	}

	public static void main(String[] args) throws Exception {
	}
	
   public static String encode(String urlString) throws MalformedURLException, UnsupportedEncodingException{
	   URL url = new URL(urlString);
		if(url.getQuery()==null)return urlString;
		String[] params = url.getQuery().split("&");
		String encodedQueryString = "";
		for(int i=0;i<params.length;i++)
		{
			if(params[i].contains("="))
				{
					String key = params[i].split("=")[0];
					
					String value = params[i].substring(key.length()+1,params[i].length());
					encodedQueryString = encodedQueryString + URLEncoder.encode(key,"UTF-8")+"="+URLEncoder.encode(value,"UTF-8") +"&";
				}else
					encodedQueryString = encodedQueryString + URLEncoder.encode(params[i],"UTF-8") +"&";
		}
		if(encodedQueryString.endsWith("&"))encodedQueryString=encodedQueryString.substring(0,encodedQueryString.length()-1);
		String encodedUrl = urlString.replace(url.getQuery(), encodedQueryString);
		
		//System.out.println(encodedUrl);
		return encodedUrl;
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
	 * @param useProxy : true - will use webProxyAddress in Context, default:true
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

			is = openAsStream(url, validate, timeout,useProxy);
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
		return openAsStream(url, false, 30 * 1000,true);
	}

	/**
	 * Open the URL, remember close the input stream after usage
	 * 
	 * @param url
	 * @param convert
	 *            false-won't convert the url with urlconverter
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream openAsStream(String url, boolean convert, boolean useProxy)
			throws Exception {
		return openAsStream(url, convert, 30 * 1000, useProxy);
	}

	private static InputStream openAsStream(String url, boolean convert,
			int timeout,boolean useProxy) throws Exception {
		// validate URL
		if (convert)
//			convert(url);

		if (url.startsWith(HTTPS_PROTOCOL))
			return openHttpsUrlAsStream(url,useProxy);

		logger.info("Opening URL: " + url);
		InputStream is = null;
		URLConnection connection = null;
		try {
			Context context = ContextManager.getThreadContext();
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

	@SuppressWarnings("deprecation")
	public static String openHttpsUrl(String url) throws HttpException,
			IOException {
		URL newUrl = new URL(url);
		Protocol myhttps = new Protocol("https",
				new EasySSLProtocolSocketFactory(), newUrl.getPort());
		Protocol.registerProtocol("https", myhttps);

		HttpClient httpclient = new HttpClient();
		String response = null;
		GetMethod httpget = new GetMethod(url);
		try {

			// httpclient.getHostConfiguration().setHost(newUrl.getHost(),
			// newUrl.getPort(), new Protocol("https", new
			// EasySSLProtocolSocketFactory(), newUrl.getPort()));
			httpclient.executeMethod(httpget);
			response = httpget.getResponseBodyAsString();
		} finally {
			httpget.releaseConnection();
		}
		return response;
	}

	@SuppressWarnings("deprecation")
	public static InputStream openHttpsUrlAsStream(String url,boolean useProxy)
			throws HttpException, IOException {
		URL newUrl = new URL(url);
		Protocol myhttps = new Protocol("https",
				new EasySSLProtocolSocketFactory(), newUrl.getDefaultPort());
		Protocol.registerProtocol("https", myhttps);
		HttpClient httpclient = new HttpClient();
		Context context = ContextManager.getThreadContext();
		if (useProxy && context.getWebProxyAddress()!= null) {
			String host = context.getWebProxyAddress().split(":")[0];
			int port = Integer.parseInt(context.getWebProxyAddress().split(":")[1]);
			httpclient.getHostConfiguration().setProxy(host, port);
		}
		InputStream response = null;
		GetMethod httpget = new GetMethod(url);

		httpclient.executeMethod(httpget);
		response = httpget.getResponseBodyAsStream();

		return response;
	}

}