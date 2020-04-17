package com.oneiron.intro.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.oneiron.intro.service.IntroService;

/**
 * 
 * @author Interface
 *
 * 첫시작부터 사용할곳임
 */
@Controller
@RequestMapping("/intro")
public class IntroController {
	
	@Resource(name = "introServiceImpl")
	IntroService introServiceImpl;
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/home")
	public ModelAndView intro(HttpServletRequest request) {
		return new ModelAndView("views/intro/intro");
	}
	
	/**
	 * 
	 * @param map
	 * @return DESK 를 생성합니다
	 */
	@RequestMapping("/createDesk")
	public @ResponseBody boolean createDesk(@RequestBody Map<String, Object> map) {
		return introServiceImpl.createDesk(map);
	}
	
	/**
	 * 
	 * @return DESK LIST 가져옵니다
	 */
	@RequestMapping("/getDeskList")
	public @ResponseBody List<Map<String, Object>> getDeskList() {
		return introServiceImpl.getDeskList();
	}
}
