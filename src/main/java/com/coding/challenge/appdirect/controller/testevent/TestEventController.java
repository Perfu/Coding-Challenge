package com.coding.challenge.appdirect.controller.testevent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coding.challenge.appdirect.oauth.Keys;
import com.coding.challenge.appdirect.util.URLUtils;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;

/**
 * Controller to send dummyApdirect event to the app.
 * @author A. Zieba
 *
 */
@Controller
public class TestEventController {

	private static final Logger LOG = Logger.getLogger(TestEventController.class);

	@RequestMapping("/order")
	public @ResponseBody String order(HttpServletRequest request) {
		LOG.info("Sending order event...");
		return callEvent(request, "dummyOrder.xml");
	}

	@RequestMapping("/change")
	public @ResponseBody String change(HttpServletRequest request) {
		LOG.info("Sending change event...");
		return callEvent(request, "dummyChange.xml");
	}

	@RequestMapping("/cancel")
	public @ResponseBody String cancel(HttpServletRequest request) {
		LOG.info("Sending cancel event...");
		return callEvent(request, "dummyCancel.xml");
	}
	
	@RequestMapping("/notice")
	public @ResponseBody String notice(HttpServletRequest request) {
		LOG.info("Sending cancel event...");
		return callEvent(request, "dummyCancel.xml");
	}
	
	@RequestMapping("/assign")
	public @ResponseBody String assign(HttpServletRequest request) {
		LOG.info("Sending cancel event...");
		return callEvent(request, "dummyAssign.xml");
	}
	
	@RequestMapping("/unassign")
	public @ResponseBody String unassign(HttpServletRequest request) {
		LOG.info("Sending cancel event...");
		return callEvent(request, "dummyUnassign.xml");
	}

	private String callEvent(HttpServletRequest request, String event) {
		try {
			OAuthConsumer consumer = new DefaultOAuthConsumer(Keys.CONSUMER_KEY, Keys.SECRET_KEY);
			StringBuffer urlBuffer = new StringBuffer();

			urlBuffer.append(URLUtils.getBaseURL(request));

			String urlParam = URLEncoder.encode(urlBuffer.toString() + "/" + event, "UTF8");

			urlBuffer.append("/notification/event?url=").append(urlParam);

			URL urlConnect = new URL(urlBuffer.toString());
			HttpURLConnection requestConnection = (HttpURLConnection) urlConnect.openConnection();
			consumer.sign(requestConnection);
			requestConnection.connect();

			InputStream is = requestConnection.getInputStream();

			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();

			String line;
			try {

				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						LOG.error(e);
					}
				}
			}

			return sb.toString();

		} catch (Exception e) {
			LOG.error("ERROR during event for " + event, e);

		}

		return "ERROR during event for " + event;

	}

}