package com.coding.challenge.appdirect.controller.consult;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.openid4java.OpenIDException;
import org.openid4java.association.AssociationSessionType;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageException;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.coding.challenge.appdirect.bean.User;
import com.coding.challenge.appdirect.bean.appdirect.event.NoticeType;
import com.coding.challenge.appdirect.repositories.UserRepository;
import com.coding.challenge.appdirect.util.URLUtils;

/**
 * Controller for OpenID, only the ressource /sso/login is concern by openId
 * 
 * This class is based on the example available in openid4java.
 * 
 * @author A. Zieba
 *
 */
@Controller
public class OpenIDController {

	private static final String OPTIONAL_VALUE = "0";
	private static final String REQUIRED_VALUE = "1";
	private static final Logger LOG = Logger.getLogger(OpenIDController.class);

	private ConsumerManager manager;
	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	public void init() throws ServletException {

		this.manager = new ConsumerManager();
		manager.setAssociations(new InMemoryConsumerAssociationStore());
		manager.setNonceVerifier(new InMemoryNonceVerifier(5000));
		manager.setMinAssocSessEnc(AssociationSessionType.DH_SHA256);
	}


	/**
	 * {@inheritDoc}
	 */
	@RequestMapping("/login")
	public String processOpenID(HttpServletRequest req, HttpServletResponse resp) {
		String result = "unauthorized";
		
		LOG.info("OpenID URL : " + URLUtils.getFullURL(req));
		try{
		if ("true".equals(req.getParameter("is_return"))) {
			return processReturn(req, resp);
		} else {
			String identifier = req.getParameter("openid_identifier");
			LOG.info("OPENID :" + identifier);
			if (identifier != null) {
				result = this.authRequest(identifier, req, resp);
			}
		}

		} catch (Exception e){
			
			LOG.error("Error during OpenID Treatment : ", e);
		}
		return result;
	}

	private String processReturn(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Identifier identifier = this.verifyResponse(req);
		LOG.debug("identifier: " + identifier);
		if (identifier == null) {
			return "unauthorized";

		} else {
			
			User user = userRepository.findByOpenID( identifier.getIdentifier());
			
			req.setAttribute("user", user);
			
			
			if(user.getAccount().getStatus() != null && NoticeType.valueOf(user.getAccount().getStatus()) == NoticeType.DEACTIVATED) {
				
				return "unauthorize";
			}
			
			return "return";
		}
	}

	// --- placing the authentication request ---
	public String authRequest(String userSuppliedString, HttpServletRequest httpReq, HttpServletResponse httpResp)
			throws IOException, OpenIDException {

		// configure the return_to URL where your application will receive
		// the authentication responses from the OpenID provider
		// String returnToUrl = "http://example.com/openid";
		String returnToUrl = URLUtils.getBaseURL(httpReq) + "/login?is_return=true";

		LOG.info("Return URL : " + returnToUrl);
		// perform discovery on the user-supplied identifier
		List discoveries = manager.discover(userSuppliedString);

		// attempt to associate with the OpenID provider
		// and retrieve one service endpoint for authentication
		DiscoveryInformation discovered = manager.associate(discoveries);

		// store the discovery information in the user's session
		httpReq.getSession().setAttribute("openid-disc", discovered);
		
		// obtain a AuthRequest message to be sent to the OpenID provider
		AuthRequest authReq = manager.authenticate(discovered, returnToUrl);

		// Simple registration example
		addSimpleRegistrationToAuthRequest(httpReq, authReq);

		// Attribute exchange example
		//addAttributeExchangeToAuthRequest(httpReq, authReq);
		LOG.info("Destination URL : " + authReq.getDestinationUrl(true));
		return "redirect:" + authReq.getDestinationUrl(true);
	}

	/**
	 * Simple Registration Extension example.
	 * 
	 * @param httpReq
	 * @param authReq
	 * @throws MessageException
	 * @see <a href="http://code.google.com/p/openid4java/wiki/SRegHowTo">Simple
	 *      Registration HowTo</a>
	 * @see <a href=
	 *      "http://openid.net/specs/openid-simple-registration-extension-1_0.html">
	 *      OpenID Simple Registration Extension 1.0</a>
	 */
	private void addSimpleRegistrationToAuthRequest(HttpServletRequest httpReq, AuthRequest authReq)
			throws MessageException {
		// Attribute Exchange example: fetching the 'email' attribute
		// FetchRequest fetch = FetchRequest.createFetchRequest();
		SRegRequest sregReq = SRegRequest.createFetchRequest();

		String[] attributes = { "nickname", "email", "fullname" };
		for (int i = 0, l = attributes.length; i < l; i++) {
			String attribute = attributes[i];
			String value = httpReq.getParameter(attribute);
			if (OPTIONAL_VALUE.equals(value)) {
				sregReq.addAttribute(attribute, false);
			} else if (REQUIRED_VALUE.equals(value)) {
				sregReq.addAttribute(attribute, true);
			}
		}

		// attach the extension to the authentication request
		if (!sregReq.getAttributes().isEmpty()) {
			authReq.addExtension(sregReq);
		}
	}

	/**
	 * Attribute exchange example.
	 * 
	 * @param httpReq
	 * @param authReq
	 * @throws MessageException
	 * @see <a href=
	 *      "http://code.google.com/p/openid4java/wiki/AttributeExchangeHowTo">
	 *      Attribute Exchange HowTo</a>
	 * @see <a href=
	 *      "http://openid.net/specs/openid-attribute-exchange-1_0.html">OpenID
	 *      Attribute Exchange 1.0 - Final</a>
	 */
	private void addAttributeExchangeToAuthRequest(HttpServletRequest httpReq, AuthRequest authReq)
			throws MessageException {
		String[] aliases = httpReq.getParameterValues("alias");
		String[] typeUris = httpReq.getParameterValues("typeUri");
		String[] counts = httpReq.getParameterValues("count");
		FetchRequest fetch = FetchRequest.createFetchRequest();
		for (int i = 0, l = typeUris == null ? 0 : typeUris.length; i < l; i++) {
			String typeUri = typeUris[i];
			if (StringUtils.isNotBlank(typeUri)) {
				String alias = aliases[i];
				boolean required = httpReq.getParameter("required" + i) != null;
				int count = NumberUtils.toInt(counts[i], 1);
				fetch.addAttribute(alias, typeUri, required, count);
			}
		}
		authReq.addExtension(fetch);
	}

	// --- processing the authentication response ---
	public Identifier verifyResponse(HttpServletRequest httpReq) throws ServletException {
		try {
			// extract the parameters from the authentication response
			// (which comes in as a HTTP request from the OpenID provider)
			ParameterList response = new ParameterList(httpReq.getParameterMap());

			// retrieve the previously stored discovery information
			DiscoveryInformation discovered = (DiscoveryInformation) httpReq.getSession().getAttribute("openid-disc");

			// verify the response; ConsumerManager needs to be the same
			// (static) instance used to place the authentication request
			VerificationResult verification = manager.verify(URLUtils.getFullURL(httpReq), response, discovered);

			// examine the verification result and extract the verified
			// identifier
			Identifier verified = verification.getVerifiedId();
			if (verified != null) {
				AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();

				receiveSimpleRegistration(httpReq, authSuccess);

				receiveAttributeExchange(httpReq, authSuccess);

				return verified; // success
			}
		} catch (OpenIDException e) {
			// present error to the user
			throw new ServletException(e);
		}

		return null;
	}

	/**
	 * @param httpReq
	 * @param authSuccess
	 * @throws MessageException
	 */
	private void receiveSimpleRegistration(HttpServletRequest httpReq, AuthSuccess authSuccess)
			throws MessageException {
		if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG)) {
			MessageExtension ext = authSuccess.getExtension(SRegMessage.OPENID_NS_SREG);
			if (ext instanceof SRegResponse) {
				SRegResponse sregResp = (SRegResponse) ext;
				for (Iterator iter = sregResp.getAttributeNames().iterator(); iter.hasNext();) {
					String name = (String) iter.next();
					String value = sregResp.getParameterValue(name);
					httpReq.setAttribute(name, value);
				}
			}
		}
	}

	/**
	 * @param httpReq
	 * @param authSuccess
	 * @throws MessageException
	 */
	private void receiveAttributeExchange(HttpServletRequest httpReq, AuthSuccess authSuccess) throws MessageException {
		if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
			FetchResponse fetchResp = (FetchResponse) authSuccess.getExtension(AxMessage.OPENID_NS_AX);

			// List emails = fetchResp.getAttributeValues("email");
			// String email = (String) emails.get(0);

			List aliases = fetchResp.getAttributeAliases();
			Map attributes = new LinkedHashMap();
			for (Iterator iter = aliases.iterator(); iter.hasNext();) {
				String alias = (String) iter.next();
				List values = fetchResp.getAttributeValues(alias);
				if (values.size() > 0) {
					String[] arr = new String[values.size()];
					values.toArray(arr);
					attributes.put(alias, StringUtils.join(arr));
				}
			}
			httpReq.setAttribute("attributes", attributes);
		}
	}

}
