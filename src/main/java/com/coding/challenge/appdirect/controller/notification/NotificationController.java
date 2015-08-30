package com.coding.challenge.appdirect.controller.notification;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coding.challenge.appdirect.bean.appdirect.result.Result;

@Controller
public class NotificationController {

	private static final Logger LOG = Logger.getLogger(NotificationController.class);


	@RequestMapping("/order")
	public @ResponseBody Result order(@RequestParam("url") String url, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.info("Order Event. Url :" + url);

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
