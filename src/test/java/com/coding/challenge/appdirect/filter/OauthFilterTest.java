package com.coding.challenge.appdirect.filter;

import javax.servlet.FilterChain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import oauth.signpost.OAuth;

public class OauthFilterTest {

	MockHttpServletRequest request;
	MockHttpServletResponse response;
	OauthFilter filter;
	
	@Mock
	FilterChain chain;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		System.setProperty("debug", "1");
		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		request.setQueryString("url=https%3A%2F%2Fwww.appdirect.com%2Fapi%2Fintegration%2Fv1%2Fevents%2Ff7fc61ee-a4fd-4fba-9901-fdccc37ca098");
		request.setScheme("https");
		request.setServerName("appdirect-challenge.herokuapp.com");
		request.setServerPort(443);
		request.setServletPath("/notification/event");
		request.setMethod("GET");
		
		request.addHeader(OAuth.HTTP_AUTHORIZATION_HEADER, "OAuth oauth_consumer_key=\"test-38747\", oauth_nonce=\"4206273937599682240\", oauth_signature=\"5KBx%2Fmmmn58nFSVxH8Fw8CIm3gU%3D\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"1441453733\", oauth_version=\"1.0\"");

		filter = new OauthFilter();
	}
	
	@Test
	public void filterOK() throws Exception {
		
		
		
		filter.doFilter(request, response, chain);
		
		Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	public void filterKO() throws Exception {
		
		request.setMethod("POST");
		filter.doFilter(request, response, chain);
		
		Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
	}
}
