package com.seleniumtests.driver.api;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ControllerThreadSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

public class EasySSLProtocolSocketFactory implements SecureProtocolSocketFactory {

	private static SSLContext createEasySSLContext() {
		try {
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, new TrustManager[] { new EasyX509TrustManager(null) }, null);
			return context;
		} catch (Exception e) {
			throw new HttpClientError(e.toString());
		}
	}

	private SSLContext sslcontext = null;

	/**
	 * Constructor for EasySSLProtocolSocketFactory.
	 */
	public EasySSLProtocolSocketFactory() {
		super();
	}

	/**
	 * @see SecureProtocolSocketFactory#createSocket(java.net.Socket,java.lang.String,int,boolean)
	 */
	public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
	}

	/**
	 * @see SecureProtocolSocketFactory#createSocket(java.lang.String,int)
	 */
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(host, port);
	}

	/**
	 * @see SecureProtocolSocketFactory#createSocket(java.lang.String,int,java.net.InetAddress,int)
	 */
	public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {

		return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
	}

	/**
	 * Attempts to get a new socket connection to the given host within the given time limit.
	 * <p>
	 * To circumvent the limitations of older JREs that do not support connect timeout a controller thread is executed. The controller thread attempts to create a new socket within the given limit of time. If socket constructor does not return until the
	 * timeout expires, the controller terminates and throws an {@link ConnectTimeoutException}
	 * </p>
	 * 
	 * @param host
	 *            the host name/IP
	 * @param port
	 *            the port on the host
	 * @param clientHost
	 *            the local host name/IP to bind the socket to
	 * @param clientPort
	 *            the port on the local machine
	 * @param params
	 *            {@link HttpConnectionParams Http connection parameters}
	 * 
	 * @return Socket a new socket
	 * 
	 * @throws IOException
	 *             if an I/O error occurs while creating the socket
	 * @throws UnknownHostException
	 *             if the IP address of the host cannot be determined
	 */
	public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort, final HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
		if (params == null) {
			throw new IllegalArgumentException("Parameters may not be null");
		}
		int timeout = params.getConnectionTimeout();
		if (timeout == 0) {
			return createSocket(host, port, localAddress, localPort);
		} else {
			// To be eventually deprecated when migrated to Java 1.4 or above
			return ControllerThreadSocketFactory.createSocket(this, host, port, localAddress, localPort, timeout);
		}
	}

	public boolean equals(Object obj) {
		return ((obj != null) && obj.getClass().equals(EasySSLProtocolSocketFactory.class));
	}

	private SSLContext getSSLContext() {
		if (this.sslcontext == null) {
			this.sslcontext = createEasySSLContext();
		}
		return this.sslcontext;
	}

	public int hashCode() {
		return EasySSLProtocolSocketFactory.class.hashCode();
	}

}