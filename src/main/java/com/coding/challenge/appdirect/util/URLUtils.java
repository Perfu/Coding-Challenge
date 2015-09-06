package com.coding.challenge.appdirect.util;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class to help URL creation.
 * 
 * @author A. Zieba
 *
 */
public class URLUtils {
	
	public static final String HEADER_PROTO = "x-forwarded-proto";
	public static final String HEADER_PORT = "x-forwarded-port";
	

	private URLUtils() {
	}

	/**
	 * Method to create base URL from the request
	 * 
	 * @param request
	 *            request to use
	 * @return the url base extracted with this form
	 *         http(s)://host(:port)/contextpath/servletPath
	 */
	public static String getBaseURL(HttpServletRequest request) {

		StringBuilder urlBuffer = new StringBuilder();

		int port = request.getServerPort();
		String scheme = request.getScheme();
		String contextPath = request.getContextPath();

		// In case of proxy in front our app
		if (StringUtils.isNotBlank(request.getHeader(HEADER_PROTO))) {

			scheme = request.getHeader(HEADER_PROTO);
		}

		if (StringUtils.isNotBlank(request.getHeader(HEADER_PORT))) {

			port = Integer.parseInt(request.getHeader(HEADER_PORT));
		}

		urlBuffer.append(scheme).append("://").append(request.getServerName());

		if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
			urlBuffer.append(':').append(port);
		}

		urlBuffer.append(contextPath);
		
		return urlBuffer.toString();

	}

	/**
	 * Method to extract the full URL from the request
	 * 
	 * @param request
	 *            request to use
	 * @return the url including queryString
	 */
	public static String getFullURL(HttpServletRequest request) {

		StringBuilder urlBuffer = new StringBuilder();

		urlBuffer.append(getBaseURL(request));

		String pathInfo = request.getPathInfo();
		String queryString = request.getQueryString();
		String servletPath = request.getServletPath();

		urlBuffer.append(servletPath);

		if (pathInfo != null) {
			urlBuffer.append(pathInfo);
		}
		if (queryString != null) {
			urlBuffer.append("?").append(queryString);
		}
		return urlBuffer.toString();
	}

	/**
	 * Method to extract base URL from a url
	 * 
	 * @param url
	 *            the String to use
	 * @return the url base extracted with this form http(s)://host(:port)
	 */
	public static String getBaseURLFromString(String url) throws URISyntaxException {

		URI uri = new URI(url);
		StringBuilder urlBuffer = new StringBuilder();

		int port = uri.getPort();
		String scheme = uri.getScheme();
		String host = uri.getHost();

		urlBuffer.append(scheme).append("://").append(host);

		if (port > 0 && ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443))) {
			urlBuffer.append(':').append(port);
		}

		return urlBuffer.toString();
	}
}
