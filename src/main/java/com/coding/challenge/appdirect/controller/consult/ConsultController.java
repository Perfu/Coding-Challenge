package com.coding.challenge.appdirect.controller.consult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.coding.challenge.appdirect.bean.User;
import com.coding.challenge.appdirect.oauth.Keys;
import com.coding.challenge.appdirect.repositories.AccountRepository;
import com.coding.challenge.appdirect.repositories.UserRepository;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
 
/*
 * author: Crunchify.com
 * 
 */
@Controller
public class ConsultController {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	AccountRepository accountRepository;
 
	@RequestMapping("/users")
	public String getUsers(Model model) {
		//repo.save(new Test("coucou"));
		for (User test : userRepository.findByAccount("account1")) {
            System.out.println(test);
        }
		model.addAttribute("accounts", accountRepository.findAll());
		model.addAttribute("users", userRepository.findAll());
		return "users";
	}
	
	@RequestMapping("test")
	public String test(){
		
		try {
			OAuthConsumer consumer = new DefaultOAuthConsumer(Keys.CONSUMER_KEY, Keys.SECRET_KEY);
			String urlSign = consumer.sign("http://localhost:8080/challenge/notification/order?url=http%3A%2F%2Flocalhost%3A8080%2Fchallenge%2FdummyOrder.xml");
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

			System.out.println("Content Secured" + sb.toString());
			
			
		} catch (Exception e) {
			e.printStackTrace();

		}
		
		
		return "users";
	}
}