package com.oneiron.about.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/about")
public class AboutController {

	@RequestMapping("/profile")
	public String profile() {
		return "views/about/profile";
	}
	
	@RequestMapping("/projects")
	public String projects() {
		return "views/about/projects";
	}
}
