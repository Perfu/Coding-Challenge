package com.coding.challenge.appdirect.controller.notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coding.challenge.appdirect.bean.appdirect.result.Result;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;

@Controller
public class NotificationController {

	private static final Logger LOG = Logger.getLogger(NotificationController.class);

	@RequestMapping("/order")
	public @ResponseBody Result order(@RequestParam("url") String url, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.info("Order Event. Url :" + url);

		try {
			OAuthConsumer consumer = new DefaultOAuthConsumer("Dummy", "secret");
			String urlSign = consumer.sign(url);
			LOG.info("new URL :" + urlSign);
			URL urlEvent = new URL(urlSign);
			
			InputStream is = urlEvent.openStream();
			
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
						e.printStackTrace();
					}
				}
			}

			LOG.info("Content" + sb.toString());
			
			
		} catch (Exception e) {
			LOG.error("Problem during the call to get the event : ", e);

		}

		Result result = new Result();

		// Get the Event
		// Create Account
		// Return Result

		result.setSuccess(true);
		result.setMessage("Welcome");
		result.setAccountIdentifier("identifier");
		return result;
	}
}
