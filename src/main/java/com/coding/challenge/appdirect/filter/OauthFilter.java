package com.coding.challenge.appdirect.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.scribe.model.Verb;
import org.springframework.http.HttpStatus;

import com.coding.challenge.appdirect.oauth.Keys;
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		try {
		HttpStatus status = doOAuthHMACValidation(httpRequest);

		if (status != HttpStatus.OK) {

			httpResponse.setStatus(status.value());
			return;
		}


			chain.doFilter(request, response);
		} catch (Exception ex) {
			request.setAttribute("errorMessage", ex);
			request.getRequestDispatcher("/WEB-INF/views/jsp/error.jsp").forward(request, response);
		}

	}

	private HttpStatus doOAuthHMACValidation(HttpServletRequest request) throws Exception {

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
		boolean isValid = oauthValidator.validate(Verb.valueOf(request.getMethod()), request.getRequestURL().toString(),
				extractQueryParametersExceptOauth(request), request.getParameter(TIMESTAMP_QUERY_PARAM),
				request.getParameter(NONCE_QUERY_PARAM), request.getParameter(SIGNATURE_METHOD_QUERY_PARAM),
				request.getParameter(VERSION_QUERY_PARAM), request.getParameter(CONSUMER_KEY_QUERY_PARAM), Keys.SECRET_KEY,
				request.getParameter(SIGNATURE_QUERY_PARAM));

		if (!isValid) {
			return HttpStatus.FORBIDDEN;
		}

		return HttpStatus.OK;
	}

	/**
	 * Extract Parameters that are not include in Oauthparameters ton validate the signature correctly.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private static Map<String, String[]> extractQueryParametersExceptOauth(HttpServletRequest request) throws Exception {
		String[] oauthParameters = { TIMESTAMP_QUERY_PARAM, NONCE_QUERY_PARAM, SIGNATURE_METHOD_QUERY_PARAM,
				VERSION_QUERY_PARAM, CONSUMER_KEY_QUERY_PARAM, SIGNATURE_QUERY_PARAM };

		List<String> oauthParametersList = Arrays.asList(oauthParameters);
		Map<String, String[]> queryParameters = new HashMap<String, String[]>();
		String queryString = request.getQueryString();

		if (StringUtils.isEmpty(queryString)) {
			return queryParameters;
		}

		String[] parameters = queryString.split("&");

		for (String parameter : parameters) {
			String[] keyValuePair = parameter.split("=");

			if (oauthParametersList.contains(keyValuePair[0])) {
				continue;
			}
			String[] values = queryParameters.get(keyValuePair[0]);
			if (keyValuePair.length == 2) {

				String valueDecoded = java.net.URLDecoder.decode(keyValuePair[1], "UTF-8");
				values = ArrayUtils.add(values, valueDecoded);
				queryParameters.put(keyValuePair[0], values);
			}
		}
		return queryParameters;
	}
}
