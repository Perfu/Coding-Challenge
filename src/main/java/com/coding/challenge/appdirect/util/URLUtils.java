package com.coding.challenge.appdirect.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class URLUtils {

	private URLUtils(){	}
	
	public static String getBaseURL(HttpServletRequest request) {
		
		StringBuilder urlBuffer = new StringBuilder();
		
		int port = request.getServerPort();
		String scheme = request.getScheme();
		String contextPath = request.getContextPath();
	    String servletPath = request.getServletPath();

		//In case of proxy in front our app
		if (StringUtils.isNotBlank(request.getHeader("x-forwarded-proto"))) {

			scheme = request.getHeader("x-forwarded-proto");
		}

		if (StringUtils.isNotBlank(request.getHeader("x-forwarded-port"))) {

			port = Integer.parseInt(request.getHeader("x-forwarded-port"));
		}

		urlBuffer.append(scheme).append("://").append(request.getServerName());

		if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
			urlBuffer.append(':').append(port);
		}

		urlBuffer.append(contextPath).append(servletPath);

		return urlBuffer.toString();
		
	}
	
	public static String getFullURL(HttpServletRequest request) {
		
		StringBuilder urlBuffer = new StringBuilder();
		
		urlBuffer.append(getBaseURL(request));
		
		String pathInfo = request.getPathInfo();
		String queryString = request.getQueryString();

	    if (pathInfo != null) {
	    	urlBuffer.append(pathInfo);
	    }
	    if (queryString != null) {
	    	urlBuffer.append("?").append(queryString);
	    }
	    return urlBuffer.toString();
	}
}
