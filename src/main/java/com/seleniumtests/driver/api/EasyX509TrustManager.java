package com.seleniumtests.driver.api;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * <p>
 * EasyX509TrustManager unlike default {@link X509TrustManager} accepts self-signed certificates.
 * </p>
 * <p>
 * This trust manager SHOULD NOT be used for productive systems due to security reasons, unless it is a concious decision and you are perfectly aware of security implications of accepting self-signed certificates
 * </p>
 * 
 * @author <a href="mailto:adrian.sutton@ephox.com">Adrian Sutton</a>
 * @author <a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>
 * 
 *         <p>
 *         DISCLAIMER: HttpClient developers DO NOT actively support this component. The component is provided as a reference material, which may be inappropriate for use without additional customization.
 *         </p>
 */
public class EasyX509TrustManager implements X509TrustManager {
	private X509TrustManager standardTrustManager = null;

	/**
	 * Constructor for EasyX509TrustManager.
	 */
	public EasyX509TrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
		super();

		TrustManagerFactory factory = TrustManagerFactory.getInstance(System.getProperty("java.vendor").toUpperCase().indexOf("IBM") != -1 ? "IbmX509" : "SunX509");// SunX509
		factory.init(keystore);
		TrustManager[] trustmanagers = factory.getTrustManagers();
		if (trustmanagers.length == 0) {
			throw new NoSuchAlgorithmException("SunX509 trust manager not supported");
		}
		this.standardTrustManager = (X509TrustManager) trustmanagers[0];
	}

	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		//System.out.println("checkClientTrusted called");
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		System.out.println("checkServerTrusted called");
	}

	/**
	 * @see com.sun.net.ssl.X509TrustManager#getAcceptedIssuers()
	 */
	public X509Certificate[] getAcceptedIssuers() {
		System.out.println("getAcceptedIssuers called");
		return this.standardTrustManager.getAcceptedIssuers();
	}
}