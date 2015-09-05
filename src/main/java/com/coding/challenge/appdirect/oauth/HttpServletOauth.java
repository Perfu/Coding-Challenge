package com.coding.challenge.appdirect.oauth;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.coding.challenge.appdirect.util.URLUtils;

import oauth.signpost.OAuth;
import oauth.signpost.http.HttpRequest;

public class HttpServletOauth implements HttpRequest {

	private HttpServletRequest request;
	private Map<String, String> headers;

	public HttpServletOauth(HttpServletRequest request) {
		super();
		this.request = request;

		headers = new HashMap<>();
		if (StringUtils.isNotBlank(request.getHeader(OAuth.HTTP_AUTHORIZATION_HEADER))) {
			headers.put(OAuth.HTTP_AUTHORIZATION_HEADER, request.getHeader(OAuth.HTTP_AUTHORIZATION_HEADER));
		}
	}

	@Override
	public String getMethod() {

		return request.getMethod();
	}

	@Override
	public String getRequestUrl() {

		return URLUtils.getFullURL(request);
	}

	

	@Override
	public void setRequestUrl(String url) {

		throw new RuntimeException(new UnsupportedOperationException());
	}

	@Override
	public void setHeader(String name, String value) {
		headers.put(name, value);
	}

	@Override
	public String getHeader(String name) {

		return headers.get(name);
	}

	@Override
	public Map<String, String> getAllHeaders() {

		return headers;
	}

	@Override
	public InputStream getMessagePayload() throws IOException {

		return request.getInputStream();
	}

	@Override
	public String getContentType() {

		return request.getContentType();
	}

	@Override
	public Object unwrap() {

		throw new RuntimeException(new UnsupportedOperationException());
	}

}
