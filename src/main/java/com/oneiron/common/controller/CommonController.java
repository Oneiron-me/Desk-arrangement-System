package com.oneiron.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommonController {

	/**
	 * 
	 * @return Access Denied Page
	 */
	@RequestMapping("/denied")
	public String denied() {
		return "views/common/denied";
	}
}
