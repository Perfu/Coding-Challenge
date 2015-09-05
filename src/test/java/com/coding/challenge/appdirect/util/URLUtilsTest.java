package com.coding.challenge.appdirect.util;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class URLUtilsTest {
	

	@Test
	public void getBaseURLFromStringOK() throws Exception {
		
		Assert.assertEquals("https://www.appdirect.com", URLUtils.getBaseURLFromString("https://www.appdirect.com/openid/id/fe197487-18c7-498e-9f42-5bd246cb89bf"));
	}
	
	@Test
	public void getBaseURLFromStringWithPort() throws Exception {
		
		Assert.assertEquals("https://www.appdirect.com:8443", URLUtils.getBaseURLFromString("https://www.appdirect.com:8443/openid/id/fe197487-18c7-498e-9f42-5bd246cb89bf"));
	}
	
	@Test(expected=URISyntaxException.class)
	public void getBaseURLFromStringException() throws Exception {
		
		URLUtils.getBaseURLFromString("htps:.appdirect.com/openid/id/^^fe197487-18c7-498e-9f42-5bd246cb89bf");
	}
	
	@Test
	public void getFullURLOK() {
		
		String url = "http://appdirect-challenge.herokuapp.com/notification/event/ok?url=https%3A%2F%2Fwww.appdirect.com%2Fapi%2Fintegration%2Fv1%2Fevents%2Ff7fc61ee-a4fd-4fba-9901-fdccc37ca098";
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setQueryString("url=https%3A%2F%2Fwww.appdirect.com%2Fapi%2Fintegration%2Fv1%2Fevents%2Ff7fc61ee-a4fd-4fba-9901-fdccc37ca098");
		request.setScheme("http");
		request.setServerName("appdirect-challenge.herokuapp.com");
		request.setServerPort(80);
		request.setServletPath("/notification/event");
		request.setPathInfo("/ok");
		Assert.assertEquals(url, URLUtils.getFullURL(request));
	}
	
	@Test
	public void getFullURLWithPort() {
		
		String url = "http://appdirect-challenge.herokuapp.com:8080/notification/event?url=https%3A%2F%2Fwww.appdirect.com%2Fapi%2Fintegration%2Fv1%2Fevents%2Ff7fc61ee-a4fd-4fba-9901-fdccc37ca098";
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setQueryString("url=https%3A%2F%2Fwww.appdirect.com%2Fapi%2Fintegration%2Fv1%2Fevents%2Ff7fc61ee-a4fd-4fba-9901-fdccc37ca098");
		request.setScheme("http");
		request.setServerName("appdirect-challenge.herokuapp.com");
		request.setServerPort(8080);
		request.setServletPath("/notification/event");
		
		Assert.assertEquals(url, URLUtils.getFullURL(request));
	}
	
	@Test
	public void getFullURLWithProxy() {
		
		String url = "https://appdirect-challenge.herokuapp.com/notification/event?url=https%3A%2F%2Fwww.appdirect.com%2Fapi%2Fintegration%2Fv1%2Fevents%2Ff7fc61ee-a4fd-4fba-9901-fdccc37ca098";
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setQueryString("url=https%3A%2F%2Fwww.appdirect.com%2Fapi%2Fintegration%2Fv1%2Fevents%2Ff7fc61ee-a4fd-4fba-9901-fdccc37ca098");
		request.setScheme("http");
		request.setServerName("appdirect-challenge.herokuapp.com");
		request.setServerPort(8080);
		request.setServletPath("/notification/event");
		request.addHeader(URLUtils.HEADER_PROTO, "https");
		request.addHeader(URLUtils.HEADER_PORT, "443");
		
		Assert.assertEquals(url, URLUtils.getFullURL(request));
	}
}
