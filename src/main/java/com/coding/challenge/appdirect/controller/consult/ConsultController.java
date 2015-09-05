package com.coding.challenge.appdirect.controller.consult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.coding.challenge.appdirect.repositories.AccountRepository;
 
/**
 * 
 * @author A. Zieba
 *
 */
@Controller
public class ConsultController {
	
	@Autowired
	AccountRepository accountRepository;
 
	@RequestMapping("/users")
	public String getUsers(Model model) {
		model.addAttribute("accounts", accountRepository.findAllFetchUsers());
		return "users";
	}
}