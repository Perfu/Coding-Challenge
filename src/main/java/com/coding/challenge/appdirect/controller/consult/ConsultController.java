package com.coding.challenge.appdirect.controller.consult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.coding.challenge.appdirect.bean.User;
import com.coding.challenge.appdirect.repositories.UserRepository;
 
/*
 * author: Crunchify.com
 * 
 */
@Controller
public class ConsultController {
	
	@Autowired
	UserRepository repo;
 
	@RequestMapping("/users")
	public String helloWorld(Model model) {
		//repo.save(new Test("coucou"));
		for (User test : repo.findByAccount("account1")) {
            System.out.println(test);
        }
		model.addAttribute("users", repo.findAll());
		return "users";
	}
}