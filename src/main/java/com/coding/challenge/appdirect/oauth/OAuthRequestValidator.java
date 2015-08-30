package com.coding.challenge.appdirect.oauth;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
 
public class OAuthRequestValidator {
 
	static final int TIMESTAMP_VALIDITY_IN_SECS = 120;
 
	private static final OAuthRequestValidator instance = new OAuthRequestValidator();
 
	private static final Logger LOG = Logger.getLogger(OAuthRequestValidator.class);
 
	private OAuthRequestValidator() {}
	
	public static final OAuthRequestValidator getInstance() {
		return instance;
	}
 
	public boolean validate(Verb verb, String url, Map<String, String[]> queryParameters, String timestamp, String nonce, String signatureMethod,
							String version, String publicKey, String secretKey, String requestSignature) {
 
		DefaultApi10a api = new Oauth10aAPI();
 
		// Check if the request is too old
		long seconds = (System.currentTimeMillis() / 1000L) - Long.valueOf(timestamp); 
		if (seconds < 0 || seconds > TIMESTAMP_VALIDITY_IN_SECS) {
			LOG.warn("Received old request:\n" + 
					 formatRequest(verb, url, timestamp, nonce, signatureMethod, version, publicKey, requestSignature));
			return false;
		}
 
		OAuthRequest request = new OAuthRequest(verb, url);
		// add the necessary parameters
		
			for(Entry<String, String[]> entry : queryParameters.entrySet()) {
				
				for (String value : entry.getValue()) {
					
					request.addQuerystringParameter(entry.getKey(), value);
				}
			}
			
	        request.addOAuthParameter(OAuthConstants.TIMESTAMP, timestamp);
	        request.addOAuthParameter(OAuthConstants.NONCE, nonce);
	        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, publicKey);
	        request.addOAuthParameter(OAuthConstants.SIGN_METHOD, signatureMethod);
	        request.addOAuthParameter(OAuthConstants.VERSION, version);
 
		String baseString = api.getBaseStringExtractor().extract(request);
		// Passing an empty token, as the 2-legged OAuth doesn't require it
		String realSignature = api.getSignatureService().getSignature(baseString, secretKey, ""); 
 
		return (requestSignature.compareTo(realSignature) == 0);
	}
 
	private String formatRequest(Verb verb, String url, String timestamp, String nonce, String signatureMethod,
			String version, String publicKey, String requestSignature) {
 
		return new StringBuffer()
			.append(verb.name()).append(" ").append(url)
			.append("\n\ttimestamp: ").append(timestamp)
			.append("\n\tnonce: ").append(nonce)
			.append("\n\tsignature method: ").append(signatureMethod)
			.append("\n\tversion: ").append(version)
			.append("\n\tpublic key: ").append(publicKey)
			.append("\n\trequest signature: ").append(requestSignature)
			.toString();	
	}
}