package com.oneiron.login.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request) {
		return "views/login/login";
	}
}

