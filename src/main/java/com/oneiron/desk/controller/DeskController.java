package com.oneiron.desk.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.oneiron.desk.service.DeskService;

@Controller
@RequestMapping("/desk")
public class DeskController {
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
//	@Autowired
//	SessionInfo sessionInfo;
	
	@Resource(name = "deskServiceImpl")
	DeskService deskServiceImpl;
	
	@RequestMapping("/home")
	public String deskHome(Model model, HttpSession session) {
		
		if(session.getAttribute("lastDeskId") != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("deskId", session.getAttribute("lastDeskId"));
			model.addAttribute("noteList", deskServiceImpl.getNoteList(map).get("msgList"));
		}
		
		logger.info("test!! {}", session.getAttribute("lastDeskId"));
		return "views/desk/deskHome";
	}
	
	@RequestMapping("/set")
	public String setDeskId(@RequestParam("deskId") String deskId, HttpSession session) {
		session.setAttribute("lastDeskId", deskId);
		
		return "redirect:/desk/home";
	}
	
	@RequestMapping("/getNoteList")
	public @ResponseBody Map<String, Object> getNoteList(@RequestBody Map<String, Object> map) {
		return deskServiceImpl.getNoteList(map);
	}
	
//	@RequestMapping("/getSessionCount")
//	public @ResponseBody int sessionCount(@RequestBody Map<String, Object> map) {
//		if(map != null && map.get("status").toString().equals("disconnected")) {
//			sessionInfo.remove(map.get("uId").toString());
//		}
//		return sessionInfo.getSessionCount();
//	}
//	
//	@RequestMapping("/getSessionList")
//	public @ResponseBody List<String> sessionList(@RequestBody Map<String, Object> map) {
//		if(map != null && map.get("status").toString().equals("disconnected")) {
//			sessionInfo.remove(map.get("uId").toString());
//		}
//		return sessionInfo.getUserList();
//	}
//	
//	@RequestMapping("/getSessionMap")
//	public @ResponseBody Map<String, String> sessionMap() {
//		return sessionInfo.getUserMap();
//	}
}
