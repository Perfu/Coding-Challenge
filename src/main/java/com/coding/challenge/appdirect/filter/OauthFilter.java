package com.coding.challenge.appdirect.filter;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.coding.challenge.appdirect.oauth.HttpServletOauth;
import com.coding.challenge.appdirect.oauth.Keys;

import oauth.signpost.OAuth;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;

public class OauthFilter implements Filter {

	private static final Logger LOG = Logger.getLogger(OauthFilter.class);

	@Override
	public void destroy() {
		// ...
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		LOG.info("Validating the signature of the request(Oauth).");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		try {
			HttpStatus status = oauthValidation(httpRequest);

			if (status != HttpStatus.OK) {

				LOG.warn("Signature not Valid");
				httpResponse.setStatus(status.value());
				return;
			}

			chain.doFilter(request, response);
		} catch (Exception ex) {
			LOG.error("Problem occured during validation of signature", ex);
			httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return;
		}

	}

	private HttpStatus oauthValidation(HttpServletRequest request) {

		LOG.debug("------Request information-----");
		for (String name : Collections.list(request.getHeaderNames())) {

			LOG.debug("HEADER : " + name + " = " + request.getHeader(name));
		}
		LOG.debug("URL : " + request.getRequestURL());
		LOG.debug("Query String : " + request.getQueryString());
		LOG.debug("------------------------------");

		HttpServletOauth oauthRequest = new HttpServletOauth(request);

		HttpParameters headerParams = OAuth.oauthHeaderToParamsMap(request.getHeader(OAuth.HTTP_AUTHORIZATION_HEADER));

		String requestSignature = OAuth.percentDecode(headerParams.getFirst(OAuth.OAUTH_SIGNATURE));

		if (requestSignature == null) {
			requestSignature = request.getParameter(OAuth.OAUTH_SIGNATURE);
		}

		LOG.debug("Request signature : " + requestSignature);

		DefaultOAuthConsumer consumer = new DefaultOAuthConsumer(Keys.CONSUMER_KEY, Keys.SECRET_KEY);

		try {
			consumer.sign(oauthRequest);
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException e) {

			LOG.error(e);
			return HttpStatus.UNAUTHORIZED;
		}

		String realSignature = OAuth
				.percentDecode(OAuth.oauthHeaderToParamsMap(oauthRequest.getHeader(OAuth.HTTP_AUTHORIZATION_HEADER))
						.getFirst(OAuth.OAUTH_SIGNATURE));

		LOG.debug("Real signature : " + realSignature);

		if (realSignature.equals(requestSignature)) {
			LOG.debug("Signature is valid.");
			return HttpStatus.OK;

		} else {
			return HttpStatus.UNAUTHORIZED;
		}

	}
}
