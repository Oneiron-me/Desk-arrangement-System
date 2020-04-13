package com.oneiron.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oneiron.doc.OneironDoc;
import com.oneiron.doc.TravelDoc;
import com.oneiron.service.RepositoryService;
import com.oneiron.util.SuperHashMap;
import com.oneiron.ws.SessionInfo;

@Controller
public class NewController {
	
	/*@Resource(name="repositoryServiceImpl")
	RepositoryService repositoryServiceImpl;*/
	
	protected static final Logger logger = LoggerFactory.getLogger(NewController.class);

	@Resource(name="repositoryServiceImpl")
	RepositoryService repositoryServiceImpl;
	
	@Autowired
	SessionInfo sessionInfo;
	
	@RequestMapping("/")
	public String intro() {
		//repository.count();
		
		return "index";
	}
	
	@RequestMapping("/nope/index")
	public String nope() {
		return "/views/index";
	}
	
	@RequestMapping("/nope/websocket")
	public String nope2(Model model) {
		return "/views/websocket";
	}
	
	@RequestMapping("/getSessionCount")
	public @ResponseBody int sessionCount() {
		return sessionInfo.getSessionCount();
	}
	
	@RequestMapping("/getSessionList")
	public @ResponseBody List<String> sessionList() {
		return sessionInfo.getUserList();
	}
	
	@RequestMapping("/getSessionMap")
	public @ResponseBody Map<String, String> sessionMap() {
		return sessionInfo.getUserMap();
	}
	
	
	
	@RequestMapping("/first")
	public @ResponseBody List<SuperHashMap> findByKey(@RequestBody TravelDoc map) throws Exception {
		logger.info("test : " + map);
		return repositoryServiceImpl.findByKey(map);
	}
	
	@RequestMapping("/createId")
	public @ResponseBody int genId(@RequestBody OneironDoc doc) throws Exception {
		return repositoryServiceImpl.createId(doc);
	}
	
	@RequestMapping("/findUserInfo")
	public @ResponseBody List<SuperHashMap> findUserInfo(@RequestBody OneironDoc map) throws Exception {
		logger.info("test : " + map);
		return repositoryServiceImpl.findUserInfo(map);
	}
	
	
}
