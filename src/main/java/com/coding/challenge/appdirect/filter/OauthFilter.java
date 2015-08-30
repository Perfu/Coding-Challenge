package com.coding.challenge.appdirect.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scribe.model.Verb;
import org.springframework.http.HttpStatus;

import com.coding.challenge.appdirect.oauth.OAuthRequestValidator;

public class OauthFilter implements Filter {
	

	static final String TIMESTAMP_QUERY_PARAM = "oauth_timestamp";
	static final String NONCE_QUERY_PARAM = "oauth_nonce";
	static final String SIGNATURE_METHOD_QUERY_PARAM = "oauth_signature_method";
	static final String VERSION_QUERY_PARAM = "oauth_version";
	static final String CONSUMER_KEY_QUERY_PARAM = "oauth_consumer_key";
	static final String SIGNATURE_QUERY_PARAM = "oauth_signature";

	@Override
	public void destroy() {
		// ...
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//
	}

	@Override
	public void doFilter(ServletRequest request, 
               ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest httpRequest  = (HttpServletRequest) request;
		HttpServletResponse httpResponse  = (HttpServletResponse) response;
		
		HttpStatus status = doOAuthHMACValidation(httpRequest);

		if (status != HttpStatus.OK) {

			httpResponse.setStatus(status.value());
			return;
		}
		
		try {
			chain.doFilter(request, response);
		} catch (Exception ex) {
			request.setAttribute("errorMessage", ex);
			request.getRequestDispatcher("/WEB-INF/views/jsp/error.jsp")
                               .forward(request, response);
		}

	}
	
	private HttpStatus doOAuthHMACValidation(HttpServletRequest request) {

		// Check that all the needed parameters are contained in the request
		Map<String, String[]> queryValues = request.getParameterMap();
		if (!queryValues.containsKey(TIMESTAMP_QUERY_PARAM) || !queryValues.containsKey(NONCE_QUERY_PARAM)
				|| !queryValues.containsKey(SIGNATURE_METHOD_QUERY_PARAM)
				|| !queryValues.containsKey(VERSION_QUERY_PARAM) || !queryValues.containsKey(CONSUMER_KEY_QUERY_PARAM)
				|| !queryValues.containsKey(SIGNATURE_QUERY_PARAM)) {

			return HttpStatus.BAD_REQUEST;
		}

		OAuthRequestValidator oauthValidator = OAuthRequestValidator.getInstance();
		// Validate the request
		//TODO mettre la clé
		boolean isValid = oauthValidator.validate(Verb.valueOf(request.getMethod()), request.getRequestURI(),
				request.getParameter(TIMESTAMP_QUERY_PARAM), request.getParameter(NONCE_QUERY_PARAM),
				request.getParameter(SIGNATURE_METHOD_QUERY_PARAM), request.getParameter(VERSION_QUERY_PARAM),
				request.getParameter(CONSUMER_KEY_QUERY_PARAM), "key", request.getParameter(SIGNATURE_QUERY_PARAM));

		if (!isValid) {
			return HttpStatus.FORBIDDEN;
		}

		return HttpStatus.OK;
	}


}
