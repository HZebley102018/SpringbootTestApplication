package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController
{
	
	private static final Logger logger = LoggerFactory.getLogger(PageController.class);
	
	@GetMapping("/register")
	public String showRegisterPage()
	{
		logger.info("Entering and exiting showRegisterPage()");
		return "register";
	}
	
	@GetMapping("/superuser")
	public String showSuperuserDashboard()
	{
		logger.info("Entering and exiting showSuperuserDashboard()");
		return "superuser";
	}
	
	@GetMapping("/")
	public String showLoginPage()
	{
		logger.info("Entering and exiting showLoginPage()");
		return "index";
	}
	
	@GetMapping("/user")
	public String showUserPage()
	{
		logger.info("Entering and exiting showUserPage()");
		return "user";
	}

}
