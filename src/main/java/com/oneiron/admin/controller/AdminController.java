package com.oneiron.admin.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oneiron.admin.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Resource(name="adminServiceImpl")
	AdminService adminServiceImpl;

	/**
	 * 
	 * @return GO TO ADMIN PAGE
	 */
	@RequestMapping("/home")
	public String admin() {
		return "views/admin/admin";
	}
	
	/**
	 * @return ADMIN 관리 페이지에서 관리자 정보들 가져오는곳.
	 */
	@RequestMapping("/getAdminInfo")
	public @ResponseBody Map<String, Object> getAdminInfo() {
		return adminServiceImpl.getAdminInfoByN1ql();
	}
	
	/**
	 * 
	 * @param doc
	 * @return 관리자 개인정보 저장함.
	 */
	@RequestMapping("/putAdminInfo")
	public @ResponseBody boolean putAdminInfo(@RequestBody Map<String, Object> map, HttpServletRequest reqest) {
		return adminServiceImpl.putAdminInfo(map);
	}
	
	/**
	 * 
	 * @param doc
	 * @return 관리자 플젝 정보 저장함
	 */
	@RequestMapping("/putAdminProjectInfo")
	public @ResponseBody boolean putAdminProjectInfo(@RequestBody Map<String, Object> map) {
		return adminServiceImpl.putAdminProjectInfo(map);
	}
	
}
