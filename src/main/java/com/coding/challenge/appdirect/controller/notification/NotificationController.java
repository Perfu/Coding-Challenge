package com.coding.challenge.appdirect.controller.notification;

import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coding.challenge.appdirect.bean.Account;
import com.coding.challenge.appdirect.bean.appdirect.event.Company;
import com.coding.challenge.appdirect.bean.appdirect.event.Event;
import com.coding.challenge.appdirect.bean.appdirect.event.Item;
import com.coding.challenge.appdirect.bean.appdirect.event.Order;
import com.coding.challenge.appdirect.bean.appdirect.event.User;
import com.coding.challenge.appdirect.bean.appdirect.result.ErrorCode;
import com.coding.challenge.appdirect.bean.appdirect.result.Result;
import com.coding.challenge.appdirect.oauth.Keys;
import com.coding.challenge.appdirect.repositories.AccountRepository;
import com.coding.challenge.appdirect.repositories.UserRepository;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;

@Controller
public class NotificationController {

	private static final Logger LOG = Logger.getLogger(NotificationController.class);

	@Autowired
	UserRepository userRepository;
	@Autowired
	AccountRepository accountRepository;
	
	@RequestMapping("/order")
	public @ResponseBody Result event(@RequestParam("url") String url, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.info("Order Event. Url :" + url);
		Result result = new Result();

		try {
			OAuthConsumer consumer = new DefaultOAuthConsumer(Keys.CONSUMER_KEY, Keys.SECRET_KEY);
			
			String urlSign = consumer.sign(url);
			
			LOG.info("new URL :" + urlSign);
			URL urlEvent = new URL(urlSign);
			
			InputStream is = urlEvent.openStream();
			
			JAXBContext context = JAXBContext.newInstance(Event.class);
	        Unmarshaller unmarshaller = context.createUnmarshaller();
	        Event event = (Event) unmarshaller.unmarshal(is);
	        
	        if ("STATELESS".equalsIgnoreCase(event.getFlag())) {
	        	
	    		result.setSuccess(true);
	    		return result;
	        }
	        
	        switch (event.getType()){
	        case SUBSCRIPTION_ORDER:
	        	result = processOrderEvent(event);
	        	break;
	        case SUBSCRIPTION_CANCEL:
	        	
	        	break;
	        case SUBSCRIPTION_CHANGE:
	        	
	        	break;
	        case SUBSCRIPTION_NOTICE:
	        	
	        	break;
	        case USER_ASSIGNMENT :
	        	
	        	break;
	        	
	        case USER_UNASSIGNMENT :
	        	
	        	break;
	        
	        }
	        

			
		} catch (Exception e) {
			LOG.error("Problem during the process of the event : ", e);
        	result.setSuccess(false);
        	result.setErrorCode(ErrorCode.UNKNOWN_ERROR);
		}

		// Get the Event
		// Create Account
		// Return Result
		return result;
	}

	private Result processOrderEvent(Event event) {
		
		Result result = new Result();
		
        Company company = event.getPayload().getCompany();
        Order order = event.getPayload().getOrder();
        
        if (accountRepository.findOne(company.getUuid()) != null) {
        	
        	result.setSuccess(false);
        	result.setErrorCode(ErrorCode.USER_ALREADY_EXISTS);
        	
        	return result;
        }
        
        Account account = loadAccount(company, order);
        
        User user = event.getCreator();
        
        com.coding.challenge.appdirect.bean.User dbUser = loadUser(account, user);
        
        accountRepository.save(account);
        userRepository.save(dbUser);
        
        result.setSuccess(true);
        result.setAccountIdentifier(account.getUuid());
        result.setMessage("Welcome");
        
        return result;
	}

	private com.coding.challenge.appdirect.bean.User loadUser(Account account, User user) {
		com.coding.challenge.appdirect.bean.User dbUser = new com.coding.challenge.appdirect.bean.User();
        
        dbUser.setAccount(account.getUuid());
        dbUser.setEmail(user.getEmail());
        dbUser.setFirstName(user.getFirstName());
        dbUser.setLanguage(user.getLanguage());
        dbUser.setLastName(user.getLastName());
        dbUser.setOpenId(user.getOpenId());
        dbUser.setUuid(user.getUuid());
		return dbUser;
	}

	private Account loadAccount(Company company, Order order) {
		Account account = new Account();
        account.setCountry(company.getCountry());
        account.setEdition(order.getEditionCode());
        account.setEmail(company.getEmail());
        
        for (Item item : order.getItem()) {
        
        	if ("USER".equals(item.getUnit())) {
        		
        		account.setMaxUsers(item.getQuantity());
        	}
        }
        
        account.setName(company.getName());
        account.setPhoneNumber(company.getPhoneNumber());
        account.setUuid(company.getUuid());
		return account;
	}
}
